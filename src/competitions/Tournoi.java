package competitions;

import java.util.ArrayList;
import java.util.Collections;

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
 *         Cette classe permet de repésenter un tournoi de football, elle hérite
 *         de competition
 *         </p>
 */
public class Tournoi extends Competition {

	private static final long serialVersionUID = 5327763971912454828L;

	/**
	 * Le tour courant du tournoi
	 */
	private Tour tourCourant;

	/**
	 * La liste des clubs participants dans le tournoi
	 */
	private ArrayList<Club> listeClubs;

	/**
	 * Constructeur de Tournoi à 5 parametres
	 * 
	 * @param nom            : le nom du Tournoi
	 * @param recompense     : la recompense du Tournoi
	 * @param gainExperience : le gain d'experience du Tournoi
	 * @param arbitres       : la liste des arbitres
	 * @param listeClubs     : la liste des clubs
	 */
	public Tournoi(String nom, double recompense, int gainExperience, ArrayList<Club> listeClubs,
			ArrayList<Arbitre> arbitres) throws NonAutoriseException, NullPointerException {

		super(nom, recompense, gainExperience, arbitres, listeClubs);

		/*
		 * Faut que la taille de la liste soit une puissance de 2, on reccupere la
		 * representation binaire de la taille de la liste et il faut qu'elle soit de
		 * cette forme 10....0 Exemple : 2^4 = 16 est une puissance de 2, il s'ecrit
		 * 10000 Mais 7 n'est pas une puissance de 2 donc il s'ecrit 111
		 */
		String str = Integer.toBinaryString(listeClubs.size());
		int length = str.length();
		int i;
		for (i = 1; i < length; i++)
			if (str.charAt(i) != '0')
				break;

		if (str.charAt(0) != '1' || i != length)
			throw new NonAutoriseException("Le nombre de clubs doit une puissance de deux");

		Collections.shuffle(listeClubs);

		this.listeClubs = listeClubs;

		// On construit le premier tour
		tourCourant = new Tour("Tour " + ++numeroTour, construireListeMatchs(listeClubs, arbitres));
	}

	/**
	 * Permet de construire la liste des matchs d'un tour
	 * 
	 * @param listeClubs : la liste des clubs
	 * @param arbitres   : la liste des arbitres
	 * @return la liste des matchs
	 */
	private static ArrayList<Match> construireListeMatchs(ArrayList<Club> listeClubs, ArrayList<Arbitre> arbitres)
			throws NullPointerException, NonAutoriseException {

		// On mélange les arbitres
		Collections.shuffle(arbitres);

		ArrayList<Match> matchs = new ArrayList<>();

		// On creer les matchs avec les 2 premieres equipes et un arbitre et on avance
		// de 2
		for (int i = 0; i < listeClubs.size(); i += 2)
			matchs.add(new Match(listeClubs.get(i), listeClubs.get(i + 1), arbitres.get(i / 2)));

		return matchs;
	}

	@Override
	public void jouerTour() throws NullPointerException, NonAutoriseException {
		// Si le tournoi est fini on fait rien
		if (estFini())
			return;

		// On joue les tour courant, avec la contrainte que les match ne peuvent pas
		// etre nul
		tourCourant.jouer(false, nom);

		// On reccupere les clubs qualifies (les gangants des matchs)
		ArrayList<Club> clubsQualifies = new ArrayList<>();
		for (Match match : tourCourant.getListeMatchs())
			clubsQualifies.add(match.getVainqueur());

		// On garde le tour joue
		tourPrecedent = tourCourant;

		// Il reste qu'un seul club dans le tournoi, donc c'est le gangant du tournoi
		if (clubsQualifies.size() == 1) {
			tourCourant = null;
			return;
		}

		// On construit le tour prochain
		tourCourant = new Tour("Tour " + ++numeroTour, construireListeMatchs(clubsQualifies, arbitres));
	}

	@Override
	public boolean estFini() {
		return tourCourant == null;
	}

	@Override
	public Tour getTourCourant() {
		return tourCourant;
	}

	@Override
	public int getExperienceGagnee(Club club) {
		if (getVainqueur() != null && getVainqueur().equals(club))
			return gainExperience;
		return 0;
	}

	@Override
	public ArrayList<Joueur> getJoueursCompetition() {
		ArrayList<Joueur> joueurs = new ArrayList<>();
		// On reccupere les joueurs de chaque clubs
		for (Club club : listeClubs)
			joueurs.addAll(club.getListeJoueurs());

		return joueurs;
	}

	@Override
	public Club getVainqueur() {
		if (estFini())
			return tourPrecedent.getListeMatchs().get(0).getVainqueur();

		return null;
	}

	@Override
	public void updateParticipation() {
		for (Club club : listeClubs)
			if (club.estChoisis()) {
				participe = true;
				return;
			}

		participe = false;
	}

	@Override
	public String toString() {
		return tourCourant.toString();
	}

	@Override
	public ArrayList<Club> getListeClubs() {
		return listeClubs;
	}
}