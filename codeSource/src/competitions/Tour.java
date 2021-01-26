package competitions;

import java.util.ArrayList;

import outils.NonAutoriseException;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de representer un tour d'une competiton
 *         </p>
 */
public class Tour implements outils.Sauvegardable {

	private static final long serialVersionUID = 957215548503418071L;

	/**
	 * Le nom du tour
	 */
	public final String nom;

	/**
	 * La liste des matchs du tour
	 */
	private ArrayList<Match> listeMatchs;

	/**
	 * Constructeur de Tour à 2 parametres
	 * 
	 * @param nom         : le nom du tour
	 * @param listeMatchs : la liste des matchs du tour
	 */
	public Tour(String nom, ArrayList<Match> listeMatchs) throws NullPointerException, NonAutoriseException {
		this.nom = nom == null ? "" : nom;

		if (listeMatchs == null)
			throw new NullPointerException("La liste des matchs n'existe pas");

		if (listeMatchs.size() == 0)
			throw new NonAutoriseException("La liste des matchs est vide");

		this.listeMatchs = listeMatchs;
	}

	/**
	 * Permet de faire jouer le tour
	 */
	public void jouer(boolean matchNul, String competition) throws NullPointerException, NonAutoriseException {
		for (Match match : listeMatchs)
			match.jouer(matchNul, competition);
	}

	/**
	 * Accesseur à la liste des matchs
	 * 
	 * @return la liste des matchs du tour
	 */
	public ArrayList<Match> getListeMatchs() {
		return listeMatchs;
	}

	/**
	 * Permet d'inverser la liste des tours
	 * 
	 * @param tours : la liste à inverser
	 * @param nom   : le prefixe du nom (Tour, Journne ...)
	 * @return la liste inversé
	 * @throws NullPointerException : Si un des elements n'existe pas
	 * @throws NonAutoriseException : Si y a eu probleme dans l'inversement
	 */
	public static ArrayList<Tour> inverser(ArrayList<Tour> tours, String nom)
			throws NullPointerException, NonAutoriseException {

		if (tours == null)
			throw new NullPointerException("La liste n'existe pas");

		if (tours.size() == 0)
			throw new NonAutoriseException("La liste est vide");

		ArrayList<Tour> toursInverse = new ArrayList<>();

		int cpt = tours.size();
		// On parcours les tours
		for (Tour tour : tours) {
			ArrayList<Match> matchs = new ArrayList<>();

			// On créer les matchs retours (les inverses des matchs aller)
			for (int i = 0; i < tour.getListeMatchs().size(); i++)
				matchs.add(Match.inverser(tour.getListeMatchs().get(i)));

			// On ajoute le nouveau tour inverse
			toursInverse.add(new Tour(nom + " " + (++cpt), matchs));
		}
		return toursInverse;
	}

	@Override
	public String toString() {
		String rep = "";

		for (Match match : listeMatchs)
			rep += "\n\t" + match;

		return rep;
	}

	/**
	 * Permet de reccuperer les resultat du tour
	 * 
	 * @return les resultat du tour
	 */
	public String getResultat() {
		String rep = "";

		for (Match match : listeMatchs)
			rep += "\n\t" + match.getScore();

		return rep;
	}

	/**
	 * Permet de savoir si le club choisis pas l'utilisateur joue le tour courant
	 * 
	 * @return true si il participe, false sinon
	 */
	public boolean estParticipe() {
		for (Match match : listeMatchs)
			if (match.estParticipe())
				return true;

		return false;
	}
}