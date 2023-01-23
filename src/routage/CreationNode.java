package routage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

/**
 * @author ADOU Michel
 *
 */
@SuppressWarnings("serial")
public class CreationNode extends JDialog{

	public JRadioButton ordi;//bouton radio permettant de créer un ordinateur
	public JRadioButton routeur;//bouton radio permettant de créer un routeur
    public JTextField id;//champs de text pour saisir l'id du composant
    public GestionNode gn;//Permet de modifier l'interface maitre
    public JButton creer;//bouton permettant de terminer la creation d'un composant
    public DefaultGraph greseau;// Graphe qui supporte le reseau
    

    /**
     * Constructeur de noeud
     * @param gn objet de l'IHM parent, utile pour la modalite de la fenetre
     * @param reseau graph qui soutient le reseau
     */
	
	public CreationNode(GestionNode gn,DefaultGraph reseau) {
		super(gn,"Creation de composant",true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		this.greseau=reseau;
		this.gn=gn;
		/**************** Centre ******************/
		// type | id 
		JPanel pan=new JPanel();
		
		pan.add(new JLabel("Type de composant:"));
	    ButtonGroup bg = new ButtonGroup();
	    
	    ordi=new JRadioButton("Ordinateur");
	    ordi.setSelected(true);
	    pan.add(ordi);
	    bg.add(ordi);
	    
	    routeur=new JRadioButton("Routeur");
	    pan.add(routeur);
	    bg.add(routeur);
	    
	    pan.add(new JLabel("Id: "));
	    id=new JTextField(3);
	    pan.add(id);
	    
	    add(pan);
	    
	    /****************** BAS ******************/
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
	 * Class permettant de géré la création d'un composant 
	 * @author ADOU Michel
	 *.addAttribute("xy", 1, 1)
	 */
	private class EcouteurBouton implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			//on recupere l'id et on supprime les espaces si il y en a
			String idT=id.getText().replaceAll(" ","");
			
			//verification de la unicité de l'id
			if(greseau.getNode(idT)==null){
				
				//l'id n'est pas deja present dans le reseau
				greseau.addNode(idT).addAttribute("label", idT);
				
				//on regarde le type de composant 
				// on ajoute en consequence les attributs
				if(ordi.isSelected()){
					
					greseau.getNode(idT).addAttribute("type", "ordi");
					greseau.addAttribute("ui.quality");
			        greseau.addAttribute("ui.antialias");
			        greseau.addAttribute("ui.stylesheet",
			            "edge { fill-color: grey; }","node { size: 32px; fill-mode: image-scaled; fill-image: url('./images/network.png'); }");
				}
				else{
					greseau.getNode(idT).addAttribute("type", "routeur");
					greseau.getNode(idT).addAttribute("ui.style", "text-size:20px;");
				}
				
				
				//on ajoute a la liste le composant creer
				gn.listNode.removeAll();
				DefaultListModel listModel = new DefaultListModel();
				String tmp;
				for(Node n:greseau.getEachNode()){
					tmp="id: "+n.getId()+" Type: "+n.getLabel("type");
					listModel.addElement(tmp);
				}
				
				gn.listNode.setModel(listModel);
				
				//fermeture de la fenetre
				CreationNode.this.dispose();
				
			}
			else{
				//l'id est deja present dans le reseau
				JOptionPane.showMessageDialog(null, "l'id déjà present dans le reseau", "Help", JOptionPane.ERROR_MESSAGE);
				id.setText("");
			}
			
		}
	}

}
