package routage;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Class gérant la fenetre affichant les tables de toutage 
 * @author ADOU Michel
 *
 */
@SuppressWarnings("serial")
public class AfficheTableRoutage extends JDialog {
	
	public AfficheTableRoutage(Visu visu,ArrayList<String> resultat){
		super(visu,"RoutingSimulator | ESATIC M1 Info | Tables de routage du réseau",true);
		
		JPanel p=new JPanel(new GridLayout(1, resultat.size(),10,10));
		
		for(String s: resultat)
			p.add(new JLabel(s));
		
		JScrollPane jsp=new JScrollPane(p);
		add(jsp);
		
		pack();
		setResizable(false);
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}
