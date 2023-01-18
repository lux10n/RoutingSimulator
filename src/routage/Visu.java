package routage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.Dijkstra.Element;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.GraphParseException;

/**
 * Class Permettant de visualiser le reseau
 * @author ADOU Michel
 *
 */
@SuppressWarnings("serial")
public class Visu extends JFrame {
	
	public JButton calcul;//Bouton permettant de calculer les tables de routage
	public JButton raz;//Bouton permettant de retourner a la permiere interface (choix par fichier ou par IHM )
	public DefaultGraph reseau;//Graph representant le reseau
	private String tableRoutage;//String contenant le resultat des tables de routage
	
	
	
	/**
	 * Constructeur de la visualisation depuis un fichier
	 * @param fichier contenient le fichier de description du reseau
	 */
	public Visu(File fichier){
		super("RoutingSimulator | ESATIC M1 Info | ADOU Michel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//creation du graph vide
		reseau=new DefaultGraph("reseau");
		// System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");//Bibliothèque graphstream 1.1.1 permet d'afficher tout ce qui est icone des noeuds...
		reseau.addAttribute("ui.quality");
        reseau.addAttribute("ui.antialias");
        reseau.addAttribute("ui.stylesheet",
          "edge { fill-color: grey; } node { size: 32px; fill-mode: image-scaled; fill-image: url('./images/network.png'); }");
		//recupération des données concernant le reseau
		try {
			reseau.read(fichier.getPath());
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GraphParseException e) {
			System.err.println("Le format du fichier dgs est incorrect !");
			e.printStackTrace();
			new SaisieFichier();
			this.dispose();
		}
		
		//Construction interface graphique
		constructionIHM();
	}
	
	/**
	 * Constructeur de visualisation depuis un graph
	 * @param reseau le graph qui represente le reseau
	 */
	public Visu(DefaultGraph reseau){
		super("Routage Calculator | ADOU Michel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//recuperation du graph reseau
		this.reseau=reseau;
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		//creation de l'interface
		constructionIHM();
		
	}
	
	/**
	 * Methode permettant de creer l'interface graphique
	 */
	private void constructionIHM(){
		tableRoutage=new String();
		
		/***************** CENTRE *****************/
		
		PanelReseau pt=new PanelReseau(reseau);
		add(pt);		
		
		/***************** BAS ********************/
		
		JPanel panBas=new JPanel();
		
		calcul=new JButton("Calculer les tables de routage",new ImageIcon("./images/table.png"));
		calcul.addActionListener(new EcouteurBouton());
		panBas.add(calcul);
		
		raz=new JButton("Retour a l'état initial",new ImageIcon("./images/back.png"));
		raz.addActionListener(new EcouteurBouton());
		panBas.add(raz);
		
		add(panBas,BorderLayout.SOUTH);
		
		/*******************************************/
		
		setSize(800,700);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		setVisible(true);
	}
	
	/**
	 * Fonction qui va calculer les
	 * tables de routage
	 */
	private void calculRoutage(){
		tableRoutage="";
		
		ArrayList<Node> ordi=new ArrayList<Node>();
		ArrayList<Node> routeur=new ArrayList<Node>();
		
		//trie des composant en fonction de leur type
		for(Node n : reseau.getNodeSet()){
			if(n.getLabel("type").equals("routeur"))
				routeur.add(n);
			else
				ordi.add(n);
		}
		
		//variable qui va contenir les tables de routage
		ArrayList<String> resultat=new ArrayList<String>();
		
		
		//boucle de parcour de chaque routeur
		for(Node n: routeur){
			
			tableRoutage="<html><body>";

			//on compte le nombre de voisin routeur (utile pour le colspan)
			int nbVoisin=0;
			Iterator<Node> it=n.getNeighborNodeIterator();
			while(it.hasNext())
				if(it.next().getLabel("type").equals("routeur"))
					nbVoisin++;
			
			tableRoutage+="<table style='border-collapse: collapse;' ><tr align='center' border=1><th style='color:vert;' border=1 colspan="+(nbVoisin+1)+">"+n.getId()+"</th></tr>";
			//pour chaque pc a rejoindre
			for(Node nOrdi:ordi){
				tableRoutage+="<tr>";
				tableRoutage+="<th style='color:green;' border=1>"+nOrdi+"</th>";
				

				// calcul de la ligne
				calculRoutage(n,nOrdi);
				tableRoutage+="</tr>";

			}
			tableRoutage+="</table></body></html>";

			resultat.add(tableRoutage);
		}
		
		
		new AfficheTableRoutage(this, resultat);
		
		
	}
	
	/**
	 * Methode permettant de calculer une ligne d'une table de routage
	 * @param routeur le routeur qui va contenir la ligne de la table de routage
	 * @param ordi la ligne a calculer dans la table de routage
	 */
	private void calculRoutage(Node routeur,Node ordi){
		
		//preparation de l'algorithme de dijkstra
		Dijkstra dij=new Dijkstra(Element.edge, "poids");
		dij.init(reseau);
		
		
		//recupération des voisins (trie entre routeur et ordi)
		ArrayList<Node> voisinRouteur = new ArrayList<Node>();
		ArrayList<Node> voisinOrdi = new ArrayList<Node>();

		Iterator<Node> it=routeur.getNeighborNodeIterator();
		while(it.hasNext()){
			Node n=it.next();
			if(n.getLabel("type").equals("routeur"))
				voisinRouteur.add(n);
			else
				voisinOrdi.add(n);
		}
		
		
		//recuperation du poids de chaque route
		ArrayList<Couple> resultat =new ArrayList<Couple>();
		for(Node n : voisinRouteur){
			
			boolean contains=false;
			for(Node n1:voisinOrdi){
				if(n1.getId().equals(ordi.getId())&&!contains){
					contains=true;
				}
			}
			
			if(!contains){
			
				//fixe le point de depart de l'ago
				dij.setSource(n.getId());
				
				//ajout du poids entre le routeur et le voisin teste
				double poidsTmp=routeur.getEdgeBetween(n.getId()).getNumber("poids");
				
				//on fixe un poids max afin de forcer l'algo a ne pas passé par cette edge
				routeur.getEdgeBetween(n.getId()).addAttribute("poids", Integer.MAX_VALUE);
				
				//recuperation du poids du chemin le plus court
				double valeur=poidsTmp;
				dij.compute();
				valeur+=dij.getShortestPathValue(ordi);
				
				//on replace au point de depart
				routeur.getEdgeBetween(n.getId()).addAttribute("poids", poidsTmp);
								
				resultat.add(new Couple(n.getId(), valeur));
			}
			else
				resultat.add(new Couple(routeur.getId(),0));
			 
		}
		
		//trie des resulats en fonction des poids
		Collections.sort(resultat);
		
		//stockage de la ligne pour affichage utérieur
		for(Couple c: resultat){
			tableRoutage+="<td border=1>"+c.getIdNode()+"</td>";
		}
		
	}
	
	
	
	/**
	 * Class permettant de géré l'appui sur les boutons calcul et raz
	 * @author ADOU Michel
	 *
	 */
	private class EcouteurBouton implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==calcul){
				//ici on va lancer le calcul des tables de routage et ensuite 
				// faire la mise a jour de l'affichage
				calculRoutage();
			}
			else{
				/*CONDITIONS GÉNÉRALES

				 * ici on quitte l'interface courante
				 * et on relance l'interface de depart
				 * 
				 */
				Visu.this.dispose();
				new HomePage();
			}
		}
	}
}
