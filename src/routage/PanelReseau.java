package routage;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

/**
 * class permettant d'inclure la visualisation du graph au sein d'un JPanel
 * @author ADOU Michel 
 *
 */
@SuppressWarnings("serial")
public class PanelReseau extends JPanel{
	
	/**
	 * Constructeur du JPanel contenant la visualisation du reseau
	 * @param reseau le graph du reseau a visualiser
	 */
	public PanelReseau(Graph reseau){
		
		
		Viewer v=new Viewer(reseau, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		v.enableAutoLayout();
		
		View vue=new DefaultView(v, "la vue", Viewer.newGraphRenderer());
		vue.openInAFrame(false);
		v.addView(vue);
		
		setLayout(new BorderLayout());
		add(vue, BorderLayout.CENTER);
	}
}
