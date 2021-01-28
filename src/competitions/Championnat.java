package competitions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import equipes.Club;
import outils.NonAutoriseException;
import personnes.Arbitre;
import personnes.Joueur;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de repésenter un championnat de football, elle
 *         hérite de compétition
 *         </p>
 */
public class Championnat extends Competition {

	private static final long serialVersionUID = -8191749195739234139L;

	/**
	 * Dictionnaire des clubes et leurs points
	 */
	private HashMap<Club, Integer> clubsPoints;

	/**
	 * La liste des journees du championnat
	 */
	private ArrayList<Tour> listeJournee;

	/**
	 * Constructeur de Championnat à 5 parametres
	 * 
	 * @param nom            : le nom du Championnat
	 * @param recompense     : la recompense du Championnat
	 * @param gainExperience : le gain d'experience du Championnat
	 * @param arbitres       : la liste des arbitres
	 * @param listeClubs     : la liste des clubs
	 */
	public Championnat(String nom, double recompense, int gainExperience, ArrayList<Club> listeClubs,
			ArrayList<Arbitre> arbitres) throws NonAutoriseException, NullPointerException {

		super(nom, recompense, gainExperience, arbitres, listeClubs);

		clubsPoints = new HashMap<>();
		// On crée le hashmap des clubs, avec des valeurs initiales de 0
		for (Club c : listeClubs)
			clubsPoints.put(c, 0);

		// On construit la liste des journees
		listeJournee = construireListeJournees(listeClubs, arbitres);
	}

	/**
	 * Permet de construire la liste des journees du championnat
	 * 
	 * @param listeClubs : La liste des clubs qui y participent
	 * @param arbitres   : La liste des arbitres
	 * @return la liste des journées du championnat
	 */
	private static ArrayList<Tour> construireListeJournees(ArrayList<Club> listeClubs, ArrayList<Arbitre> arbitres)
			throws NullPointerException, NonAutoriseException {

		// On melange la liste des clubs
		Collections.shuffle(listeClubs);

		ArrayList<Club> changement = new ArrayList<>(listeClubs);
		ArrayList<Tour> tours = new ArrayList<>();
		ArrayList<Match> matchs = null;

		int cptArbitre;
		int cpt = 0;
		int size = changement.size();

		do {
			// On mélange les arbitres
			Collections.shuffle(arbitres);
			cptArbitre = 0;
			matchs = new ArrayList<>();

			// On crée le 1er match
			matchs.add(new Match(changement.get(0), changement.get(1), arbitres.get(cptArbitre++)));

			// On fixe le premier club
			for (int i = 2; i <= size / 2; i++)
				matchs.add(new Match(changement.get(i), changement.get(size - i + 1), arbitres.get(cptArbitre++)));

			// On ajoute le tour
			tours.add(new Tour("Journee " + ++cpt, matchs));

			/*
			 * et on fait tourner les autres club d'un cran dans le sens des aiguilles d'une
			 * montre
			 */
			changement.add(1, changement.remove(size - 1));

			// Tantque on a pas atteint l'etat initial on boucle
		} while (!changement.equals(listeClubs));

		// On ajoute les matchs retours
		tours.addAll(Tour.inverser(tours, "Journee"));
		return tours;
	}

	@Override
	public void jouerTour() throws NullPointerException, NonAutoriseException {
		// Si le championnat est fini on fait rien
		if (estFini())
			return;

		// On fait jouer la journee courante du championnat
		listeJournee.get(numeroTour).jouer(true, nom);

		// On ajoute les points pour les clubs
		for (Match match : listeJournee.get(numeroTour).getListeMatchs()) {
			Club vainqueur = match.getVainqueur();
			// Si match nul
			if (vainqueur == null) {
				clubsPoints.replace(match.getClubDomicile(), clubsPoints.get(match.getClubDomicile()) + 1);
				clubsPoints.replace(match.getClubExterieur(), clubsPoints.get(match.getClubExterieur()) + 1);
				continue;
			}
			clubsPoints.replace(vainqueur, clubsPoints.get(vainqueur) + 3);
		}

		// On garde le tour joue
		tourPrecedent = listeJournee.get(numeroTour);

		// On passe au tour suivant
		numeroTour++;
	}

	@Override
	public boolean estFini() {
		return numeroTour == listeJournee.size();
	}

	@Override
	public Tour getTourCourant() {
		if (!estFini())
			return listeJournee.get(numeroTour);
		return null;
	}

	@Override
	public int getExperienceGagnee(Club club) {
		if (estFini())
			return gainExperience / getPosition(club);
		return 0;
	}

	@Override
	public ArrayList<Joueur> getJoueursCompetition() {
		ArrayList<Joueur> joueurs = new ArrayList<>();
		// On reccupere les joueurs de chaque club
		for (Club club : clubsPoints.keySet())
			joueurs.addAll(club.getListeJoueurs());

		return joueurs;
	}

	@Override
	public Club getVainqueur() {
		if (!estFini())
			return null;

		ArrayList<Map.Entry<Club, Integer>> liste = trierClubs();

		// On retorune le premier club qui est le gangant du championnat
		return liste.get(0).getKey();
	}

	@Override
	public void updateParticipation() {

		for (Map.Entry<Club, Integer> clP : clubsPoints.entrySet())
			if (clP.getKey().estChoisis()) {
				participe = true;
				return;
			}

		participe = false;
	}

	@Override
	public ArrayList<Club> getListeClubs() {
		return new ArrayList<Club>(clubsPoints.keySet());
	}

	/**
	 * Permet de trier les clubs du championnat
	 * 
	 * @return La liste trier des clubs avec leurs points
	 */
	public ArrayList<Map.Entry<Club, Integer>> trierClubs() {

		// On transforme le dictionnaire en une liste de couple
		ArrayList<Map.Entry<Club, Integer>> liste = new ArrayList<>(clubsPoints.entrySet());

		/*
		 * On trie la liste selon les points des clubs (les valeurs des clés) (trie
		 * decroissant)
		 */
		Collections.sort(liste, (kv1, kv2) -> {
			return kv2.getValue() - kv1.getValue();
		});

		return liste;
	}

	/**
	 * Accesseur au dictionnaire de points
	 * 
	 * @return les clubs et leurs points
	 */
	public HashMap<Club, Integer> getClubsPoints() {
		return clubsPoints;
	}

	/**
	 * Accesseur à la liste des journees
	 * 
	 * @return la liste des journees
	 */
	public ArrayList<Tour> getListeJournee() {
		return listeJournee;
	}

	/**
	 * Permet de reccupere le classement des clubs dans le championnat
	 * 
	 * @return le classement des clubs
	 */
	public String getClassement() {
		String rep = "";
		ArrayList<Map.Entry<Club, Integer>> liste = trierClubs();

		rep += "\n\t |" + String.format("%-30s", "   " + "Clubs") + " | " + String.format("%-20s", "    Points\n");

		rep += " ______________________________________________\n";

		for (Map.Entry<Club, Integer> clP : liste)
			rep += "\n\t |" + String.format("%-30s", "   " + clP.getKey().nom) + " | "
					+ String.format("%-20s", "   " + clP.getValue() + " pts");

		return rep + "\n";
	}

	/**
	 * Permet de remetre à zero le championnat
	 */
	public void clear() throws NullPointerException, NonAutoriseException {
		participe = false;
		for (Club club : clubsPoints.keySet()) {
			club.clear();
			participe |= club.estChoisis();
			clubsPoints.replace(club, 0);
		}

		dejaAffichee = false;
		numeroTour = 0;
		listeJournee = construireListeJournees(getListeClubs(), arbitres);
	}

	@Override
	public String toString() {
		String rep = "";
		int i = 0;
		int max = clubsPoints.size();
		for (Club club : clubsPoints.keySet())
			rep += "\t" + i++ + " - " + club + ((i == max) ? "" : "\n");

		return rep;
	}

	/**
	 * Permet de retourner la position du club dans le classement
	 * 
	 * @param club : le club en question
	 * @return : la position du club, -1 si il existe pas
	 */
	private int getPosition(Club club) {
		if (club == null)
			return -1;

		ArrayList<Map.Entry<Club, Integer>> liste = trierClubs();
		for (int i = 0; i < liste.size(); i++)
			if (liste.get(i).getKey().equals(club))
				return i + 1;

		return -1;
	}
}