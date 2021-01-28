package competitions;

import java.util.ArrayList;

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
 *         Cette classe permet de repésenter un match de football
 *         </p>
 */
public class Match implements outils.Sauvegardable {

	private static final long serialVersionUID = 7582254689490680638L;

	/**
	 * Le club qui joue à domicile
	 */
	private Club clubDomicile;

	/**
	 * Le club qui joue à l'exterieur
	 */
	private Club clubExterieur;

	/**
	 * L'arbitre du match
	 */
	private Arbitre arbitre;

	/**
	 * Les buts du match
	 */
	private String representationButs;

	/**
	 * Les blessures du match
	 */
	private String representationBlessures;

	/**
	 * Les joueurs suspendus durant le match
	 */
	private String representationSuspendus;

	/**
	 * Le nombre de but que le club à domicile a marqué
	 */
	private int nombreButsClubDomicile;

	/**
	 * Le nombre de but que le club à l'exterieur a marqué
	 */
	private int nombreButsClubExterieur;

	/**
	 * Constructeur de match à 3 parametres
	 * 
	 * @param clubDomicile  : le club à domicile
	 * @param clubExterieur : le club à l'exterieur
	 * @param arbitre       : l'arbitre du match
	 */
	public Match(Club clubDomicile, Club clubExterieur, Arbitre arbitre)
			throws NullPointerException, NonAutoriseException {

		if (clubDomicile == null || clubExterieur == null || arbitre == null)
			throw new NullPointerException("Un des arguments n'existe pas");

		if (clubDomicile.equals(clubExterieur))
			throw new NonAutoriseException("Les clubs sont les mêmes");

		this.clubDomicile = clubDomicile;
		this.clubExterieur = clubExterieur;
		this.arbitre = arbitre;
		representationButs = "";
		representationBlessures = "";
		representationSuspendus = "";
		nombreButsClubDomicile = nombreButsClubExterieur = 0;
	}

	/**
	 * Permet de jouer un match
	 * 
	 * @param matchNul    : Si la competition tolere les match nul
	 * @param competition : La competition dans laquelle le match se joue
	 */
	public void jouer(boolean matchNul, String competition) throws NullPointerException, NonAutoriseException {

		// On mets à jour les etats des joueurs
		clubDomicile.updateEtatJoueurs(competition);
		clubExterieur.updateEtatJoueurs(competition);

		// Si le club exterieur ne peut pas jouer le match, il déclare forfait
		if (!clubExterieur.peutJouerLeMatch(competition)) {
			nombreButsClubDomicile = 3;
			representationButs = "\t\t\tForfait ! " + clubExterieur.nom
					+ " n'a pas assez de joueurs pour jouer le match";
			return;
		}

		// De meme pour le club domicile
		if (!clubDomicile.peutJouerLeMatch(competition)) {
			nombreButsClubExterieur = 3;
			representationButs = "\t\t\tForfait ! " + clubDomicile.nom
					+ " n'a pas assez de joueurs pour jouer le match";
			return;
		}

		// Pour alterner entre les clubs (un attaque et l'autre defends)
		int nombreButsAttaquant = 0;
		Club clubAttaquant = clubDomicile;

		int nombreButsDefendant = 0;
		Club clubDefendant = clubExterieur;

		int fautesArbitrage = 0;
		int nombreInterventionsLegals = 0;

		// On divise le match en 10 phases
		for (int i = 0; i < 10; i++) {

			// Si l'attaque est reussite
			if (clubAttaquant.attaque(clubDefendant, competition)) {

				// On reccupere la liste des attaquants du club attaquant
				ArrayList<Joueur> att = clubAttaquant
						.reccupererListJoueurs(clubAttaquant.getFormation().getListeAttaquants());

				// On designe un joueur aléatoire pour tirer parmis les attaquants
				Joueur tireur = att.get((int) (Math.random() * att.size()));

				// On reccupere le gardien du club defendant
				Joueur gardien = clubDefendant.getJoueur(clubDefendant.getFormation().getGardien());

				// les points d'attaque du tireur sont > au niveau du gardien et le tireur est
				// d'humeur donc il marque un but
				if (tireur.getNombrePointsAttaque() > gardien.getNiveauGardien()
						&& Math.random() < tireur.getHumeur().getRandHumeur()) {

					// Le tireur marque
					tireur.marquerBut(competition);

					/*
					 * Si le gardien est bien un gardien il encaisse un but (L'utilisateur à le
					 * choix de mettre un autre joueur comme gardien, et cela n'affecte pas leur
					 * stats)
					 */
					if (gardien instanceof Gardien)
						((Gardien) gardien).encaisserBut(competition);

					// On ajoute à la representation
					representationButs += "\t\t\t - " + clubAttaquant.nom + ", but de " + tireur.nom + "\n";

					// On ajoute un but au score
					nombreButsAttaquant++;

					// On update les points du tireur et du gardien
					tireur.ajouterPoints(competition, (int) (Math.random() * 3));
					gardien.supprimerPoints(competition, (int) (Math.random() * 2));

				} else { // Le tireur a raté son tir
					tireur.supprimerPoints(competition, (int) (Math.random() * 2));
					gardien.ajouterPoints(competition, (int) (Math.random() * 3));
				}
			}

			// fautes
			ArrayList<Joueur> joueurs = clubDefendant
					.reccupererListJoueurs(clubDefendant.getFormation().getJoueursTitulaires());

			Joueur gardien = clubDefendant.getJoueur(clubDefendant.getFormation().getGardien());

			// Pour tous les joueurs du club defendant
			for (Joueur joueur : joueurs) {

				// Une proba de commettre une faute
				if (Math.random() > (1. / 5.) * (joueur.caractere + joueur.getHumeur().getRandHumeur()))
					continue;

				// Blessures : Proba pour que une blessure arrive
				if (Math.random() < 0.01 * joueur.caractere) {
					ArrayList<Joueur> liste = clubAttaquant
							.reccupererListJoueurs(clubAttaquant.getFormation().getJoueursTitulaires());

					// On selectionne un blesse aléatoire
					Joueur blesse = liste.get((int) (Math.random() * liste.size()));

					// On genere la gravite de la blessure
					blesse.seBlesse(1 + (int) (Math.random() * 4));

					// On ajoute la blessure à la representation
					representationBlessures += "\t\t\t - " + clubAttaquant.nom + ", blessure de " + blesse.nom + "\n";
				}

				// Si l'arbitre ne siffle pas la faute, donc c'est une faute d'arbitrage
				if (Math.random() > arbitre.getEfficacite()) {
					fautesArbitrage++;
					continue;
				}

				// Sinon c'est une bonne intervention
				nombreInterventionsLegals++;

				// Si l'arbitre sanctionne le joueur fautif
				if (Math.random() < 0.01 * (arbitre.caractere + arbitre.getHumeur().getRandHumeur())) {
					// on ajoute le nombre de matchs suspendus
					int nombreSusspension = 1 + (int) (Math.random() * 3);
					joueur.ajouterMatchSuspendu(competition, nombreSusspension);
					// Si un joueur est suspendus on lui retire des points
					joueur.supprimerPoints(competition, (int) (Math.random() * 3));

					// On ajoute à la representation des suspensions
					representationSuspendus += "\t\t\t - " + clubDefendant.nom + " : " + joueur.nom + " suspendu : "
							+ nombreSusspension + " matchs\n";
				}

				/*
				 * Penalty : si le fautif est un gardien y aura plus de chance q'un penalty
				 * arrive que si le fautif est un autre joueur
				 */
				if (joueur.equals(gardien) && Math.random() < 0.7 || Math.random() < 0.15) {
					// On choisi le tireur du penalty
					ArrayList<Joueur> pen = clubAttaquant
							.reccupererListJoueurs(clubAttaquant.getFormation().getJoueursTitulaires());

					Joueur tireur = pen.get((int) (Math.random() * pen.size()));

					// les memes traitements que lors d'un tir
					if (tireur.getNombrePointsAttaque() > joueur.getNiveauGardien()
							&& Math.random() < tireur.getHumeur().getRandHumeur()) {

						tireur.marquerBut(competition);
						tireur.ajouterPoints(competition, (int) (Math.random() * 3));

						joueur.supprimerPoints(competition, (int) (Math.random() * 2));

						if (joueur instanceof Gardien)
							((Gardien) joueur).encaisserBut(competition);

						representationButs += "\t\t\t - " + clubAttaquant.nom + ", but de " + tireur.nom + "\n";
						nombreButsAttaquant++;
						continue;
					}

					tireur.supprimerPoints(competition, (int) (Math.random() * 2));
					joueur.ajouterPoints(competition, (int) (Math.random() * 3));
				}
			}
			// On alterne les clubs
			Club clubTmp = clubAttaquant;
			int butsTmp = nombreButsAttaquant;

			clubAttaquant = clubDefendant;
			nombreButsAttaquant = nombreButsDefendant;

			clubDefendant = clubTmp;
			nombreButsDefendant = butsTmp;
		}

		// On met à jour l'efficacite de l'arbitre
		arbitre.updateEfficacite(fautesArbitrage, nombreInterventionsLegals);

		// On affecte les scores des clubs
		nombreButsClubDomicile = (clubAttaquant.equals(clubDomicile) ? nombreButsAttaquant : nombreButsDefendant);
		nombreButsClubExterieur = (clubAttaquant.equals(clubExterieur) ? nombreButsAttaquant : nombreButsDefendant);

		// Si la competition ne tolere pas les matchs nul
		if (!matchNul) {
			ArrayList<Joueur> joueurs = null;
			Joueur tir;
			// Si le match est nul
			if (nombreButsClubDomicile == nombreButsClubExterieur) {
				// On selectionne l'equipe la plus forte et on la fait gagnee
				if (clubDomicile.getPuissance() >= clubExterieur.getPuissance()) {
					nombreButsClubDomicile++;
					joueurs = clubDomicile.reccupererListJoueurs(clubDomicile.getFormation().getJoueursTitulaires());
				} else {
					nombreButsClubExterieur++;
					joueurs = clubExterieur.reccupererListJoueurs(clubExterieur.getFormation().getJoueursTitulaires());
				}
				// On selectionne le buteur
				tir = joueurs.get((int) (Math.random() * joueurs.size()));
				tir.marquerBut(competition);
				representationButs += "\t\t\t - " + clubDomicile.nom + ", but de " + tir.nom + "\n";
			}
		}
	}

	/**
	 * Permet de reccuperer le score du match
	 * 
	 * @return le socre du match
	 */
	public String getScore() {
		return String.format("%35s", clubDomicile.nom + " " + nombreButsClubDomicile) + " - "
				+ String.format("%-35s", nombreButsClubExterieur + " " + clubExterieur.nom);
	}

	@Override
	public String toString() {
		return String.format("%30s", clubDomicile.nom) + " - " + String.format("%-30s", clubExterieur.nom)
				+ "Arbitré par : " + arbitre;
	}

	/**
	 * Permet de savoir quel club a ganger le match
	 * 
	 * @return le club vainqueur, null si le match est null
	 */
	public Club getVainqueur() {
		if (nombreButsClubDomicile == nombreButsClubExterieur)
			return null;
		if (nombreButsClubDomicile > nombreButsClubExterieur)
			return clubDomicile;
		return clubExterieur;
	}

	/**
	 * Permet d'obtenir le club à domicile
	 * 
	 * @return le club à domicile
	 */
	public Club getClubDomicile() {
		return clubDomicile;
	}

	/**
	 * Permet d'obtenir le club à l'exterieur
	 * 
	 * @return le club à l'exterieur
	 */
	public Club getClubExterieur() {
		return clubExterieur;
	}

	/**
	 * Permet d'obtenir l'arbitre du match
	 * 
	 * @return l'arbitre du match
	 */
	public Arbitre getArbitre() {
		return arbitre;
	}

	/**
	 * Permet d'obtenir la representation du match
	 * 
	 * @return la representation du match
	 */
	public String getRepresentation() {

		return (representationButs.equals("") ? "" : "\n\t\t - Les buts : \n\n" + representationButs + "\n")
				+ (representationBlessures.equals("") ? ""
						: ("\n\t\t - Les blessures : \n\n" + representationBlessures))
				+ (representationSuspendus.equals("") ? ""
						: ("\n\t\t - Les suspensions : \n\n" + representationSuspendus));
	}

	/**
	 * Permet d'obtenir le nombre de buts du club à domicile
	 * 
	 * @return nombre buts du club à domicile
	 */
	public int getNombreButsClubDomicile() {
		return nombreButsClubDomicile;
	}

	/**
	 * Permet d'obtenit le nombre de buts du club à l'exterieur
	 * 
	 * @return nombre buts du club à l'exterieur
	 */
	public int getNombreButsClubExterieur() {
		return nombreButsClubExterieur;
	}

	/**
	 * Permet de generer le match retour
	 * 
	 * @param match : Le match à inverser
	 * @return le match retour
	 */
	public static Match inverser(Match match) throws NullPointerException, NonAutoriseException {
		return new Match(match.clubExterieur, match.clubDomicile, match.arbitre);
	}

	/**
	 * Permet de savoir si le club choisis pas l'utilisateur joue le match courant
	 * 
	 * @return true si il participe, false sinon
	 */
	public boolean estParticipe() {
		return clubDomicile.estChoisis() || clubExterieur.estChoisis();
	}
}