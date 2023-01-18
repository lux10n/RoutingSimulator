package routage;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import routage.SaisieFichier;

/**
 * Class gerant le mode de création du réseau ( par saisie graphique ou a partir d'un fichier .dgs)
 * 
 * @author ADOU Michel
 *
 */

@SuppressWarnings("serial")
public class HomePage extends JFrame{
	
	
	private JButton file;//Bouton permettant de deriger l'utilisateur vers une construction du reseau a partir d'un fichier
	private JButton graphique;//Bouton permettant de deriger l'utilisateur vers une construction du reseau a partir d'une saisie graphique
	
	private JMenuItem aideItem;
	private JMenuItem quitItem;
	
	

  	class ImagePanel extends JPanel {

		  private Image img;
		  public ImagePanel(String img) {
		    this(new ImageIcon(img).getImage());
		  }
		  public ImagePanel(Image img) {
		    this.img = img;
		    setLayout(null);
		  }
		  public void paintComponent(Graphics g) {
		    g.drawImage(img, 0, 0, null);
		  }

		}
	
	
	
	/**
	 * IHM d'HomePage proposant deux types de construction du réseau.
	 */
	public HomePage(){
		super("RoutingSimulator | ESATIC M1 Info | ADOU Michel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setBackground(Color.WHITE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./images/route2.png"));
		
		
				
		/**************** CENTER : message et ImagePanel **********************/
		
		
		ImagePanel pan = new ImagePanel(new ImageIcon("./images/Computer-Netw.jpg").getImage());
		JLabel lab=new JLabel("Veuillez charger la configuration de votre réseau : ", JLabel.CENTER);
		lab.setFont(new Font("Dialog", 1, 14));
		lab.setForeground(Color.BLACK);
		pan.setBackground(Color.WHITE);
		pan.setLayout(new BorderLayout());
		pan.add(lab,BorderLayout.CENTER);
		add(pan, BorderLayout.CENTER);
		
				
		/******************* SOUTH-EAST : Button création à partir d'un fichier ****************/
		JPanel pan1=new JPanel();
			
		file=new JButton("Charger le fichier *.dgs ", new ImageIcon("./images/fileopen.png"));
		file.addActionListener(new EcouteurBouton());
		pan1.setLayout(new BorderLayout());
		pan1.setBackground(Color.WHITE);
        pan1.add(file, BorderLayout.SOUTH);
        add(pan1, BorderLayout.EAST);
			
		
/*****************************Partie Menu******************************/
        
     // 01 Création des menus Items et de leurs raccourcis...
        
        aideItem = new JMenuItem("À Propos",KeyEvent.VK_A);
        aideItem.setIcon(new ImageIcon("./images/info.png"));
        aideItem.setMnemonic('A');
        aideItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
       
        quitItem = new JMenuItem("Quitter");
        quitItem.setIcon(new ImageIcon("./images/exit.png"));
            quitItem.setMnemonic('Q');
            quitItem.setAccelerator(KeyStroke.getKeyStroke("control Q"));

        // (2) Build  menubar, menus, and add menuitems.
        JMenuBar menubar = new JMenuBar();  // Create new menu bar
            JMenu fileMenu = new JMenu("Fichier"); // Create new menu
                fileMenu.setMnemonic('F');
                menubar.add(fileMenu);      // Add menu to the menubar
                fileMenu.add(aideItem);     // Add menu item to the menu
                fileMenu.addSeparator();    // Add separator line to menu
                fileMenu.add(quitItem);
           

        // (3) Add listeners to menu items
                
        aideItem.addActionListener(new EcouteurBouton());
        quitItem.addActionListener(new EcouteurBouton());
        
        setJMenuBar(menubar);       
		pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		setVisible(true);
	}
	    
		/**
		 * Class permettant de géré l'appui sur les boutons création à partir d'un fichier et création graphique
		 * @author ADOU Michel
		 *
		 */
		private class EcouteurBouton implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// Choix de l'interface a lancer
				if(e.getSource()==file)
					new SaisieFichier();
				else if (e.getSource()==aideItem){ 
					String string="<html><body> TP Réseaux-Routage : \n\n" +
							"Implémentation en JAVA permettant de donner le chemin le plus  court dansune topologie réseau donnée.\n" +
							"Le réseau est constitué de machines et de commutateurs avec plusieurs interfaces réseaux \n\n" +
							"1. Le programme permet de saisir le graphe correspondant à la topologie réseau (à partir d'un fichier *.dgs).\n" +
							"\n"+
							"2. Le programme permet aussi de calculer les tables de routages de chauqe noeuds.Constr";
					JOptionPane.showMessageDialog(null,string, "À propos",JOptionPane.INFORMATION_MESSAGE);
						
					
				}
				else if (e.getSource()==quitItem) 
					//HomePage.this.dispose();
					 System.exit(0);
				
				else
					new GestionNode();
				
				
				
			}
			
		}
	    
	    
	    
	
	
	/**
	 * Methode d'entre du programme
	 */
	public static void main(String[] args) {
		
		new HomePage();
		
	}

}



