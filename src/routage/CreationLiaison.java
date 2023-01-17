package routage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;



/**
 * Class permettant la creation de liaison dans 
 * le reseau entre les differents composants du reseaux
 * @author ADOU Michel
 *
 */ 
@SuppressWarnings("serial")
public class CreationLiaison extends JDialog{
	
	public GestionLiaison gl;//Objet de l'interface de gestion de liaison
	public DefaultGraph greseau;//Graph soutenant le reseau
	public JButton creer;//Bouton permettant de lancer la creation de la liaison
	public JComboBox nodeIn;//JComboBox contenant les composants du reseau"1ere partie de la liaison"
	public JComboBox nodeOut;//JComboBox contenant les composants du reseau"2eme partie de la liaison"
	public JTextField poids;//Champs de texte permettant de definir le poids d'une arrete
	
	
	
	/**
	 * Constructeur de l'interface de creation de liaison
	 * @param gl objet de l'ihm de gestion qui va permettre de mettre a jour celle-ci
	 * @param reseau graph contenant le reseau
	 */
	public CreationLiaison(GestionLiaison gl,DefaultGraph reseau){
		super(gl,"Creation de liaison",true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		this.greseau=reseau;
		this.gl=gl;
		
		/**************** Centre ******************/
		JPanel panC=new JPanel();
		
		
		nodeIn=new JComboBox();
		nodeOut=new JComboBox();
		
		for(Node n:reseau.getEachNode()){
			nodeIn.addItem(n.getId());
			nodeOut.addItem(n.getId());
		}
		panC.add(new JLabel("Composant 1: "));
		panC.add(nodeIn);
		
		panC.add(new JLabel("Composant 2:"));
		panC.add(nodeOut);
		
		panC.add(new JLabel("Poids de la liaison: "));
		poids=new JTextField(3);
		poids.setToolTipText("Utile pour les liaisons entre routeur");
		
		panC.add(poids);
		
		add(panC);
		
		/**************** BAS ***********************/
		
	    JPanel panB=new JPanel();
	    creer=new JButton("Creer");
	    creer.addActionListener(new EcouteurBouton());
	    panB.add(creer);
	    add(panB,BorderLayout.SOUTH);
	    
		/****************************************/
		pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		setVisible(true);
		
		
	}
	
	/**
	 * Class permettant de géré la création d'une liaison 
	 * @author ADOU Michel
	 *
	 */
	private class EcouteurBouton implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			//recuperation des points de connexion
			Node n1=greseau.getNode(nodeIn.getSelectedItem().toString());
			Node n2=greseau.getNode(nodeOut.getSelectedItem().toString());

			//on verifie si l'arrete existe deja
			if(greseau.getEdge(n1.getId()+n2.getId())==null && greseau.getEdge(n2.getId()+n1.getId())==null){
				
				//si la connexion est entre routeur il faut mettre un poids a l'arrete
				if(n1.getLabel("type").equals("routeur") && n2.getLabel("type").equals("routeur")){
					
					//on regarde si le poids est bien precisez, qu'il ne contient pas d'espace et qu'il est decimal
					if(!poids.getText().isEmpty()&&!poids.getText().matches(" *")&&poids.getText().matches("\\d*"))
					{
						greseau.addAttribute("ui.quality");
				        greseau.addAttribute("ui.antialias");
				        greseau.addAttribute("ui.stylesheet",
				            "edge { fill-color: grey; }","node { size: 32px; fill-mode: image-scaled; fill-image: url('./doc/image/network.png'); }");	
						Edge e1=greseau.addEdge(n1.getId()+n2.getId(), n1.getId(), n2.getId());
						e1.addAttribute("label", poids.getText());
						e1.addAttribute("poids", Integer.parseInt(poids.getText()));
						e1.addAttribute("ui.stylesheet", "edge { fill-color: grey; } node { size: 32px; fill-mode: image-scaled; fill-image: url('network.png'); }");
										
						
						
						//ajout a la liste
						gl.listEdge.removeAll();
						DefaultListModel listModel = new DefaultListModel();
						String tmp;
						for(Edge e2:greseau.getEachEdge()){
							tmp="id: "+e2.getId()+" Composant: ( "+e2.getNode0()+" , "+e2.getNode1()+" ) poids: "+e2.getNumber("poids");
							listModel.addElement(tmp);
						}
						
						gl.listEdge.setModel(listModel);
						
						//fermeture de la fenetre
						CreationLiaison.this.dispose();
					}
					else{
						String msg="<html><body>Vous devez obligatoirement saisir un poids pour une liaison entre routeurs<br/>" +
								"La valeur doit etre entiere et sans espace</body></html>";
						JOptionPane.showMessageDialog(null, msg, "Help", JOptionPane.ERROR_MESSAGE);
						poids.setText("");
					}	
				}
				else{
					//ici connexion entre pc et routeur donc pas de poids sur l'arrete
					Edge e1=greseau.addEdge(n1.getId()+n2.getId(), n1.getId(), n2.getId());
					e1.addAttribute("poids", 0);
					//e1.setDirected(on);
					
					
					//ajout a la liste
					gl.listEdge.removeAll();
					DefaultListModel listModel = new DefaultListModel();
					String tmp;
					for(Edge e2:greseau.getEachEdge()){
						tmp="id: "+e2.getId()+" Composant: ( "+e2.getNode0()+" , "+e2.getNode1()+" ) poids: "+e2.getNumber("poids");
						listModel.addElement(tmp);
					}
					
					gl.listEdge.setModel(listModel);
					
					CreationLiaison.this.dispose();
				}
			}
			else{
				//popup d'erreur en cas d'arret deja existante
				String msg="<html><body>L'arrete est déjà créée !</body></html>";
				JOptionPane.showMessageDialog(null, msg, "Help", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
