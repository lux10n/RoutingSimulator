package routage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


//


/**
 * Class permettant de specifier le chemin et choisir le fichier .dgs contenant le reseau
 * @author ADOU Michel
 *
 */
@SuppressWarnings("serial")
public class SaisieFichier extends JFrame {
	
	
	public JFileChooser fc; //Selection d'un fichier ".dgs" 
	public JLabel chemin;//JLabel contenant le chemin vers le fichier choisie	
	public JButton select;//Bouton permettant de choisir la construction à partir d'un fichier.	
	public JButton visualisation;//Bouton permettant de lancer la visualisation du reseau	
	public JButton home;// Bouton permettant de revenir à la fenetre "JFrame" d'HomePage	
	public File file;//Fichier .dgs contenant le reseau a construire
		
	/**
	 * Class permettant la gestion du choix du fichier d'entree
	 */
	public SaisieFichier(){
		super("RoutingSimulator | ESATIC M1 Info | ADOU Michel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setBackground(new Color(255,255,255));
		setIconImage(Toolkit.getDefaultToolkit()
				  .getImage("./doc/image/route2.png"));
	
				
		/**************** HAUT **********************/
		
		JLabel lab=new JLabel("Selectionnez votre fichier .dgs");
		lab.setForeground(new Color(100,200,3));
		lab.setBackground(new Color(255,255,255));
		lab.setHorizontalAlignment(JLabel.CENTER);
		add(lab,BorderLayout.NORTH);
		
		/******************* CENTRE ****************/
		
		JPanel pan=new JPanel();
		TitledBorder titleBorder = new TitledBorder(new LineBorder(new Color(0, 153, 0),
				1), "Choix du fichier");
		pan.setBorder(titleBorder);
		pan.setBackground(new Color(255,255,255));
		
		pan.add(new JLabel("Fichier du reseau: "));
		
		chemin=new JLabel("Aucun fichier choisi !! ");
		pan.add(chemin);
		
		//selecteur de fichier
		fc = new JFileChooser();
		//selection que des fichiers
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		//creation du filtre de fichier
		Fichier dgs = new Fichier("Fichiers Reseau .dgs",".dgs");

		//ajout du filtre
		fc.addChoosableFileFilter(dgs);
		fc.setFileFilter(dgs);

		add(pan);
		
		/******************** BAS *********************/
		JPanel panBas=new JPanel();
		
		select=new JButton("Selectionner un fichier",new ImageIcon("./doc/image/fileopen2.png"));
		select.addActionListener(new EcouteurBouton());
		panBas.add(select);
		
		
		visualisation=new JButton("Visualiser",new ImageIcon("./doc/image/visu1.png"));
		visualisation.setEnabled(false);
		visualisation.addActionListener(new EcouteurBouton());
		panBas.add(visualisation);
		
		home=new JButton("Retour à la fenetre d'HomePage",new ImageIcon("./doc/image/back.png"));
		home.addActionListener(new EcouteurBouton());
		panBas.add(home);
		
		add(panBas,BorderLayout.SOUTH);
		
		/*******************************************/
		pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		setVisible(true);
	}
	
	/**
	 * Class permettant de géré l'appui sur les boutons choisir et visu
	 *
	 */
	private class EcouteurBouton implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			//choix suivant le bouton presse
			if(e.getSource()==select){
				
				//on verifie qu'un fichier a été choisi
				int returnVal = fc.showOpenDialog(SaisieFichier.this);
				
				//si un fichier a bien ete choisi
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					
					//on recupere le fichier
					file = fc.getSelectedFile();
					
					//on affiche le chemin
					chemin.setText(file.getAbsolutePath());
					
					//on debloque le bouton calcul
					visualisation.setEnabled(true);
					
					//on recalcule la taille de la fenetre
					pack();
				}
			}
			else if (e.getSource()==home) {
				SaisieFichier.this.dispose();
				new HomePage();
				
			}
			else{
				
				//lancement de la visualisation
				new Visu(file);
				
				SaisieFichier.this.dispose();
			}
		}
		
	}
}
