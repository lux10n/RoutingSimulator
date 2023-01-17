package routage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

/**
 * Class de gestion de creation des noeuds
 * @author ADOU Michel
 *
 */
@SuppressWarnings("serial")
public class GestionNode extends JFrame{

	public JList listNode;//JList contenant les composant creer
	public JButton creer;//JButton permettant de creer un composant
	public JButton supp;//JButton permettant de supprimer un composant selectionner
	public JButton suivant;//JButton permettant de passer a l'étape suivante
	public DefaultGraph reseau;//Graph qui represente le reseau
	
	/**
	 * Constructeur de l'interface de gestion des nodes
	 */
	public GestionNode(){
		super("RoutingSimulator | ADOU Michel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		/***************** HAUT *******************/
		
		JLabel labH=new JLabel("Creation des composants du réseau");
		labH.setHorizontalAlignment(JLabel.CENTER);
		add(labH,BorderLayout.NORTH);
		
		/***************** CENTRE *****************/
		listNode=new JList();
		listNode.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listNode.setVisibleRowCount(5);
		JScrollPane scrollPane = new JScrollPane(listNode);		
		
		add(scrollPane);
		
		/***************** BAS ***********************/
		JPanel panB=new JPanel();
		
		creer=new JButton("Creer composant");
		creer.addActionListener(new EcouteurBouton());
		panB.add(creer);
		
		supp=new JButton("Supprimer composant");
		supp.addActionListener(new EcouteurBouton());
		panB.add(supp);
		
		suivant=new JButton("Suivant");
		suivant.addActionListener(new EcouteurBouton());
		panB.add(suivant);
		
		add(panB,BorderLayout.SOUTH);
		
		/******************************************/
		
		reseau=new DefaultGraph("reseau");
		reseau.addAttribute("ui.antialias");
		
		pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		setVisible(true);
	}
	
	/**
	 * Class permettant de géré l'appui sur les boutons 
	 * d'ajout de suppression et de passage a l'etape suivante
	 * @author ADOU Michel
	 *
	 */
	private class EcouteurBouton implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource()==creer){
				//ouverture de l'interface de creation de composant
				new CreationNode(GestionNode.this,reseau);
			}
			else if(e.getSource()==supp){
				
				//on test si il y a bien un composant de selectionner
				if(!listNode.isSelectionEmpty()){
				
					//recupération de l'id du composant a supprime
					String tmp=(String) listNode.getSelectedValue();
					String tab[]=tmp.split("id: ");
					tab=tab[1].split(" ");
					
					//suppression au sein du reseau
					reseau.removeNode(tab[0]);
					
					//suppression au sein de la liste
					listNode.removeAll();
					DefaultListModel listModel = new DefaultListModel();
					for(Node n:reseau.getEachNode()){
						tmp="id: "+n.getId()+" Type: "+n.getLabel("type");
						listModel.addElement(tmp);
					}
					
					listNode.setModel(listModel);
				}
				else
					JOptionPane.showMessageDialog(null, "Suppression impossible, aucun composant selectionné", "Help", JOptionPane.ERROR_MESSAGE);

				
			}
			else{
				//ici on passe a la suite
				new GestionLiaison(reseau);
				GestionNode.this.dispose();
			}
			
		}
	}
}
