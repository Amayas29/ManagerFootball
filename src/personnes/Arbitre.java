package personnes;

/**
 * @author Amayas SADI
 * @author Hamid KOLLI
 * 
 *         <p>
 *         Classe qui gere les arbitres durant le jeu
 *         </p>
 * 
 */
public class Arbitre extends Personne {

	private static final long serialVersionUID = -7320343774770889110L;

	/**
	 * l'efficacite de l'arbitre durant les matchs
	 */
	private double efficacite;

	/**
	 * Constructeur qui cree un arbitre selon son nom et sa personnalite son
	 * efficacite est initialisé a 0 et elle change au fur et a mesure des matchs
	 * 
	 * @param nom        : le nom de la personne
	 * @param caractere  : le caractere de l'arbitre
	 * @param efficacite : l'efficacite de l'arbitre
	 */
	public Arbitre(String nom, double caractere, double efficacite) {
		super(nom, caractere);
		this.efficacite = efficacite;
	}

	/**
	 * Permet de calculer l'efficacite de l'arbitre (entre 0 et 1 pour les randoms)
	 * 
	 * @return l'efficacite entre 0 et 1
	 */
	public double getEfficacite() {
		if (efficacite < 0)
			return 0;

		// Fonction monotone de R vers [0, 1[, pour avoir l'unicité et la monotonité des
		// valeurs
		return Math.sqrt(efficacite) / (Math.sqrt(efficacite) + 1);
	}

	/**
	 * met à jour l'efficacite de l'arbitre selon les arguments
	 * 
	 * @param fautesArbitrage           : le nombre de fautes d'arbitrage comises
	 *                                  durant le match
	 * @param nombreInterventionsLegals : le nombre d'interventions legals durant le
	 *                                  match
	 */
	public void updateEfficacite(int fautesArbitrage, int nombreInterventionsLegals) {
		efficacite += -fautesArbitrage + 1.5 * nombreInterventionsLegals;
	}

	@Override
	public Arbitre clone() {
		return new Arbitre(nom, caractere, efficacite);
	}
}