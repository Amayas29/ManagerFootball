package competitions;

import java.util.ArrayList;
import java.util.Collections;

import equipes.Club;
import outils.NonAutoriseException;
import personnes.Arbitre;
import personnes.Gardien;
import personnes.Joueur;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de repésenter une competition de football en
 *         générale
 *         </p>
 */
public abstract class Competition implements outils.Sauvegardable {

	private static final long serialVersionUID = -8792067758742911620L;

	/**
	 * La recompense par defaut d'une competiton
	 */
	public final static double DEFAULT_RECOMPENSE = 10;

	/**
	 * Le nom de la competition
	 */
	public final String nom;

	/**
	 * La recompense du gagneur de la competition
	 */
	public final double recompense;

	/**
	 * L'experience gagnée par l'utilisateur si il gagne la competition
	 */
	public final int gainExperience;

	/**
	 * Le tour qui est terminé pour reccupérer ces résultats
	 */
	protected Tour tourPrecedent;

	/**
	 * Le numero du tour courant
	 */
	protected int numeroTour;

	/**
	 * Pour savoir si le club choisis par l'utilisateur participe à la competition
	 */
	protected boolean participe;

	/**
	 * Permet de savoir si une competition est deja affichee (dans le cas si on a
	 * plusieurs competitions et la competition courante est deja affichee on a pas
	 * a la reafficher)
	 */
	protected boolean dejaAffichee;

	/**
	 * La liste des arbitres assignés à la competition
	 */
	protected ArrayList<Arbitre> arbitres;

	/**
	 * Constructeur de Competition à 5 arguments
	 * 
	 * @param nom            : le nom de la Competition
	 * @param recompense     : la recompense de la Competition
	 * @param gainExperience : le gain d'experience de la competition
	 * @param arbitres       : la liste des arbitres
	 * @param listeClubs     : la liste des clubs
	 */
	public Competition(String nom, double recompense, int gainExperience, ArrayList<Arbitre> arbitres,
			ArrayList<Club> listeClubs) throws NullPointerException, NonAutoriseException {

		if (listeClubs == null)
			throw new NullPointerException("La liste des clubs n'existe pas");

		if (listeClubs.size() == 0)
			throw new NonAutoriseException("La liste des clubs est vide");

		if (listeClubs.size() % 2 == 1)
			throw new NonAutoriseException("Une competition peut pas se derouler avec un nombre impair de clubs");

		participe = false;
		for (Club club : listeClubs) {
			// Si le club choisis par l'utilisateur participe au tournoi
			participe |= club.estChoisis();

			if (club.getListeJoueurs().size() < Club.MIN_JOUEURS)
				throw new NonAutoriseException("Un club doit avoir au minimum " + Club.MIN_JOUEURS);
		}

		if (arbitres.size() < listeClubs.size() / 2)
			throw new NonAutoriseException("Le nombre d'arbitre est insuffisant");

		this.nom = nom == null ? "" : nom;
		this.recompense = recompense <= 0 ? DEFAULT_RECOMPENSE : recompense;
		this.gainExperience = gainExperience <= 0 ? 1 + (int) (Math.random() * 100) : gainExperience;
		this.arbitres = arbitres;

		tourPrecedent = null;
		numeroTour = 0;
		dejaAffichee = false;
	}

	/**
	 * Permet d'obtenir le meilleur joueur de la Competition
	 * 
	 * @return le meilleur joueur de la Competition
	 */
	public Joueur getMeilleurJoueur() {
		ArrayList<Joueur> joueurs = getJoueursCompetition();

		// On tri les joueurs selon leurs nombre de points (tri décroissant)
		Collections.sort(joueurs, (j1, j2) -> {
			return j2.getPoints(Competition.this.nom) - j1.getPoints(Competition.this.nom);
		});

		// On reccupere le premier
		return joueurs.get(0);
	}

	/**
	 * Permet d'obtenir le meilleur gardien de la Competition
	 * 
	 * @return le meilleur gardien de la Competition
	 */
	public Gardien getMeilleurGardien() {

		// On reccupere les Gardiens du club
		ArrayList<Gardien> gardiens = new ArrayList<>();
		for (Joueur joueur : getJoueursCompetition())
			if (joueur instanceof Gardien)
				gardiens.add((Gardien) joueur);

		// On tri les gardien selon le quotien nombre buts encaisses / nombre matchs
		// joues (tri croissant)
		Collections.sort(gardiens, (g1, g2) -> {
			return (int) (1000 * ((g1.getNombreMatchsJoues(Competition.this.nom) <= 0 ? Integer.MAX_VALUE
					: (1.0 * g1.getNombreButEncaisses(Competition.this.nom))
							/ g1.getNombreMatchsJoues(Competition.this.nom))
					- (g2.getNombreMatchsJoues(Competition.this.nom) <= 0 ? Integer.MAX_VALUE
							: (1.0 * g2.getNombreButEncaisses(Competition.this.nom))
									/ g2.getNombreMatchsJoues(Competition.this.nom))));
		});

		// On reccupere le premier
		return gardiens.get(0);
	}

	/**
	 * Permet d'obtenir le meilleur buteur de la Competition
	 * 
	 * @return le meilleur buteur de la Competition
	 */
	public Joueur getMeilleurButeur() {
		ArrayList<Joueur> joueurs = getJoueursCompetition();

		// On tri les joueurs selon leurs nombre de buts marques (tri decroissant)
		Collections.sort(joueurs, (o1, o2) -> {
			return o2.getNombreButsMarques(Competition.this.nom) - o1.getNombreButsMarques(Competition.this.nom);
		});

		// On reccupere le premier
		return joueurs.get(0);
	}

	/**
	 * Permet d'afficher le match de l'utilisateur (le score + les details)
	 */
	public void afficherMatchUtilisateur() {
		if (!participe || tourPrecedent == null)
			return;

		for (Match match : tourPrecedent.getListeMatchs())
			if (match.estParticipe()) {
				System.out.println("\n" + match.getScore() + " \n " + match.getRepresentation());
				return;
			}
	}

	/**
	 * Faire jouer un tour dans la Competition
	 */
	public abstract void jouerTour() throws NullPointerException, NonAutoriseException;

	/**
	 * Permet de savoir si la Competition est fini ou pas
	 * 
	 * @return true si la Competition est terminée false sinon
	 */
	public abstract boolean estFini();

	/**
	 * Retourne le tour courant de la Competition
	 * 
	 * @return le tour courant
	 */
	public abstract Tour getTourCourant();

	/**
	 * Permet de calculer le nombre de gain d'experience d'un club selon son
	 * parcours dans la competition
	 * 
	 * @param club : le club en question
	 * @return le gain d'experience
	 */
	public abstract int getExperienceGagnee(Club club);

	/**
	 * Permet de reccupere la liste de tous les joueurs qui participent dans la
	 * competition
	 * 
	 * @return la liste des joueurs de la competition
	 */
	public abstract ArrayList<Joueur> getJoueursCompetition();

	/**
	 * Permet de savoir le vainqueur de la competition
	 * 
	 * @return le club vainqueur de la compeition null si la competition est pas
	 *         encore terminé
	 */
	public abstract Club getVainqueur();

	/**
	 * Permet de mettre à jour la participation du club à la competition
	 */
	public abstract void updateParticipation();

	/**
	 * Accesseur à la liste des clubs participants
	 * 
	 * @return la liste des clubs
	 */
	public abstract ArrayList<Club> getListeClubs();

	/**
	 * Permet de savoir si le club choisi pas l'utilisateur participe dans la
	 * competition
	 * 
	 * @return true le club choisi pas l'utilisateur participe dans la competition,
	 *         false sinon
	 */
	public boolean estParticipe() {
		return participe;
	}

	/**
	 * Accesseur vers le tour fini de la Competition
	 * 
	 * @return le tour precedent
	 */
	public Tour getTourPrecedent() {
		return tourPrecedent;
	}

	/**
	 * Accesseur au numero du tour
	 * 
	 * @return le numero du tour
	 */
	public int getNumeroTour() {
		return numeroTour;
	}

	/**
	 * Accesseur aux arbitres de la competition
	 * 
	 * @return la liste des arbitres
	 */
	public ArrayList<Arbitre> getArbitres() {
		return arbitres;
	}

	/**
	 * Permet de reccupere le resultat du tour precedent
	 * 
	 * @return les resultats du tour precedent
	 */
	public String getResultat() {
		if (tourPrecedent == null)
			return null;

		String rep = "";
		if (estFini()) {
			Joueur best = getMeilleurJoueur();
			Joueur buteur = getMeilleurButeur();

			rep = "\n\t\tLa competition " + nom + " est fini.\n\n\t\t   - Le vainqueur est : " + getVainqueur().nom;
			rep += "\n\n\t\tLes titres individuelles : \n\n\t\t\t - Le meilleur joueur : " + best.nom + " : "
					+ best.getPoints(nom) + " points" + "\n\t\t\t - Le meilleur gardien : " + getMeilleurGardien().nom
					+ "\n\t\t\t - Le meilleur buteur : " + buteur.nom + " : " + buteur.getNombreButsMarques(nom)
					+ " buts";
		}

		return tourPrecedent.getResultat() + "\n\t" + rep + "\n";
	}

	/**
	 * Permet de savoir si la competition est deja affichee, dans le cas non il met
	 * son etat à deja affichee
	 * 
	 * @return true si elle déja affichee, false sinon
	 */
	public boolean dejaAffichee() {
		boolean deja = dejaAffichee;
		dejaAffichee = true;
		return deja;
	}
}