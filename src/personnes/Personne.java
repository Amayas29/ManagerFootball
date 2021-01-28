package personnes;

import outils.Humeur;

/**
 * @author Hamid KOLLI
 * @author Amayas SADI
 * 
 *         <p>
 *         Cette classe gere les personnages du jeu (joueurs, arbitres)
 *         </p>
 */
public abstract class Personne implements outils.Sauvegardable {

	private static final long serialVersionUID = 866974122878450003L;

	/**
	 * le nom de la personne
	 */
	public final String nom;

	/**
	 * L'humeur de la personne
	 */
	protected Humeur humeur;

	/**
	 * Le caractere de la personne (0 caractere faible ... 1 caractere fort)
	 */
	public final double caractere;

	/**
	 * Constructeur de Personne à 2 parametres
	 * 
	 * @param nom       : le nom de la personne
	 * @param caractere : le caractere de la personne
	 */
	public Personne(String nom, double caractere) {
		this.nom = (nom == null) ? "" : nom;
		this.caractere = (caractere < 0 || caractere > 1) ? Math.random() : caractere;
		// On genere le niveau d'humeur de la personne
		updateHumeur();
	}

	/**
	 * Permet de mettre à jour le niveau d'humeur de la personne
	 */
	public void updateHumeur() {
		int rand = (int) Math.random() * 3;
		humeur = Humeur.getHumeur(rand);
	}

	@Override
	public String toString() {
		return nom;
	}

	/**
	 * Accesseur de l'humeur de la personne
	 * 
	 * @return Humeur
	 */
	public Humeur getHumeur() {
		return humeur;
	}
}