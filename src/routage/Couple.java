package routage;

/**
 * Class permettant de stocker un couple de valeurs
 * id d'un composant reseau, et le poids associer au chemin passant par ce composant.
 * Cette class permet surtout de pouvoir trier ces valeurs
 * @author ADOU Michel
 *
 */
public class Couple implements Comparable<Couple> {

	private String idNode;//Variable contenant le l'id du node
	private double poids;//Variable contenant le poids du chemin passant par ce node
	
	
	/**
	 * Constructeur permettant de generer le couple
	 * @param id id du node
	 * @param poids poids du chemin passant par ce node
	 */
	public Couple(String id,double poids){
		idNode=id;
		this.poids=poids;
	}

	
	//Getters
	/**
	 * Methode permettant de recuperer l'id du node
	 * @return id du node
	 */
	public String getIdNode() {
		return idNode;
	}

	/**
	 * Methode permettant de recupere le poids du chemin
	 * @return le point du chemin
	 */
	public double getPoids() {
		return poids;
	}

	/**
	 * Methode permettant de gerer le trie de couple
	 * @param c Couple a comparer
	 */
	public int compareTo(Couple c) {
		
		if(this.poids==c.poids)
			return 0;
		else if(this.poids>c.poids)
			return 1;
		else
			return -1;
		
	}

}
