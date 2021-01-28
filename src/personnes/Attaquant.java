package personnes;

import java.util.HashMap;

/**
 * @author Amayas SADI
 * @author Hamid KOLLI
 * 
 *         <p>
 *         Classe qui gere les Attaquants
 *         </p>
 */
public class Attaquant extends Joueur {

	private static final long serialVersionUID = 2001458072491645364L;

	/**
	 * Constructeur d'Attaquant à 6 parametres
	 * 
	 * @param nom                 : le nom de l'attaquant
	 * @param nombrePointsAttaque : nombre points d'attaque
	 * @param nombrePointsDefense : nombre points defense
	 * @param niveauGardien       : nombre points gardien
	 * @param caractere           : le caractere de l'attaquant
	 * @param numero              : numero de l'attaquant
	 */
	public Attaquant(String nom, double nombrePointsAttaque, double nombrePointsDefense, double niveauGardien,
			double caractere, int numero) {
		super(nom, nombrePointsAttaque, nombrePointsDefense, niveauGardien, caractere, numero);
	}

	@Override
	public double getPuissance() {
		// On favorise le nombre points d'attaque
		return (2 * nombrePointsDefense + 4 * nombrePointsAttaque + niveauGardien) / 7.;
	}

	@Override
	public Joueur clone() {
		Attaquant attaquant = new Attaquant(nom, nombrePointsAttaque, nombrePointsDefense, niveauGardien, caractere,
				numero);
		attaquant.nombreButs = new HashMap<>(nombreButs);
		attaquant.nombreMatchsSuspondus = new HashMap<>(nombreMatchsSuspondus);
		attaquant.nombrePoints = new HashMap<>(nombrePoints);
		return attaquant;
	}
}