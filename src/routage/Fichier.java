package routage;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Class permettant de filtre les fichiers visible dans
 * un JFileChooser
 * @author ADOU Michel
 *
 */
public class Fichier extends FileFilter{

	private String description;//String contenant la description du fitre
	private String extension;//String contenant l'extension du filtre
   
	
	/**
	 * Constructeur du filtre
	 * @param description description du filtre
	 * @param extension extension du fichier a accepter
	 */
	public Fichier(String description, String extension){
		
		// on declenche une exception si la description est null
		if(description == null)
			throw new NullPointerException("La description ne peut être null.");
		
		// on declenche une exception si l'extension est null
		if(extension ==null)
			throw new NullPointerException("L' extension ne peut être null.");
      
		this.description = description;
		this.extension = extension;
	}
	
	/**
	 * Methode permettant de dire si un fichier est acceptable ou non
	 * @param file Fichier a verifier
	 * @return true si le fichier est acceptable
	 */
	public boolean accept(File file){
      if(file.isDirectory()) { 
         return true; 
      } 
      String nomFichier = file.getName().toLowerCase(); 

      return nomFichier.endsWith(extension);
   }
	
	/**
	 * Methode permettant de retourné la description du fitre
	 * @return retourne la description du filtre
	 */
	public String getDescription(){
		return description;
	}
}
