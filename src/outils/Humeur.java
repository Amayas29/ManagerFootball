package outils;

/**
 *
 * @author KOLLI Hamid
 * @author SADI Amayas
 * 
 *         <p>
 *         Enumerateur d'humeur
 *         </p>
 */
public enum Humeur {

	Faible(0), Normal(1), Elevee(2);

	/**
	 * Le niveau de humeur
	 */
	private int niveauHumeur;

	/**
	 * Constructeur d'humeur
	 * 
	 * @param niveauHumeur : entier qui représente l'humeur (0,1,2)
	 */
	private Humeur(int niveauHumeur) {
		this.niveauHumeur = niveauHumeur;
	}

	/**
	 * Accesseur au niveau d'humeur
	 * 
	 * @return le niveau d'humeur
	 */
	public int getNiveauHumeur() {
		return niveauHumeur;
	}

	/**
	 * Permet de retourner les chances de la personne selon son humeur
	 * 
	 * @return les chances de bien joué le match
	 */
	public double getRandHumeur() {
		if (this == Faible)
			return 0.25;
		if (this == Normal)
			return 0.5;
		return 0.75;
	}

	/**
	 * Permet d'obtenir l'humeur correspondant à un niveau d'humeur
	 * 
	 * @param niveauHumeur : l'entier qui represente le niveau d'humeur
	 * @return l'humeur si il existe sinon null
	 */
	public static Humeur getHumeur(int niveauHumeur) {
		for (Humeur humeur : values()) {
			if (humeur.niveauHumeur == niveauHumeur)
				return humeur;
		}
		return null;
	}
}