package outils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette interface donne la posibilité à un objet de se sauvegarder et
 *         de se recharger depuis un stream elle hérite de Serializable
 *         </p>
 */
public interface Sauvegardable extends Serializable {

	/**
	 * Permet de sauvegarder un objet de type Sauvegardable dans un fichier
	 * 
	 * @param fileName : le nom du fichier
	 * @param objet    : l'objet a sauvegarder
	 * @throws IOException : Si ya eu une erreur durant la sauvegarde
	 */
	public static void save(String fileName, Sauvegardable objet) throws IOException {
		// On crée les stream de sortie
		FileOutputStream fileStream = new FileOutputStream(fileName);

		ObjectOutputStream objectStream = null;
		try {
			objectStream = new ObjectOutputStream(fileStream);
			// On ecrit l'objet (en binaire)
			objectStream.writeObject(objet);
		} finally {
			// On ferme de stream
			if (objectStream != null)
				objectStream.close();
		}
	}

	/**
	 * Permet de charger un objet de type Sauvegardable depuis un fichier
	 * 
	 * @param fileName : le nom du fichier
	 * @return : l'objet chargé
	 * @throws ClassNotFoundException : Si l'objet n'est pas de type Sauvegardable
	 * @throws IOException            : Si ya eu une erreur durant le chargement
	 */
	public static Sauvegardable charge(String fileName) throws ClassNotFoundException, IOException {
		// On crée les stream d'entrées
		FileInputStream fileStream = new FileInputStream(fileName);

		ObjectInputStream objectStream = null;
		try {
			objectStream = new ObjectInputStream(fileStream);
			// On lit l'objet
			return (Sauvegardable) (objectStream.readObject());
		} finally {
			// On ferme de stream
			if (objectStream != null)
				objectStream.close();
		}
	}

	/**
	 * Permet de savoir si un fichier existe
	 * 
	 * @param fileName : Le nom du fichier
	 * @return true si il existe, false sinon
	 */
	public static boolean existe(String fileName) {
		File f = new File(fileName);
		return f.exists();
	}
}