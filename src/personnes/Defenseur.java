
package personnes;

import java.util.HashMap;

/**
 * @author Amayas SADI
 * @author Hamid KOLLI
 * 
 *         <p>
 *         Classe qui gere les defenseurs
 *         </p>
 */
public class Defenseur extends Joueur {

	private static final long serialVersionUID = 5227725577977872553L;

	/**
	 * Constructeur de Defenseur à 6 parametres
	 *
	 * @param nom                 : le nom du defenseur
	 * @param nombrePointsAttaque : nombre points d'attaque
	 * @param nombrePointsDefense : nombre points defense
	 * @param niveauGardien       : nombre points gardien
	 * @param caractere           : le caractere du defenseur
	 * @param numero              : numero du defenseur
	 */
	public Defenseur(String nom, double nombrePointsAttaque, double nombrePointsDefense, double niveauGardien,
			double caractere, int numero) {
		super(nom, nombrePointsAttaque, nombrePointsDefense, niveauGardien, caractere, numero);
	}

	@Override
	public double getPuissance() {
		// On favorise le nombre points de defense
		return (4 * nombrePointsDefense + 1 * nombrePointsAttaque + 2 * niveauGardien) / 7.;
	}

	@Override
	public Joueur clone() {
		Defenseur defenseur = new Defenseur(nom, nombrePointsAttaque, nombrePointsDefense, niveauGardien, caractere,
				numero);

		defenseur.nombreButs = new HashMap<>(nombreButs);
		defenseur.nombreMatchsSuspondus = new HashMap<>(nombreMatchsSuspondus);
		defenseur.nombrePoints = new HashMap<>(nombrePoints);

		return defenseur;
	}
}