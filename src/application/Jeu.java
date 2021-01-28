package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

import competitions.Championnat;
import competitions.Competition;
import competitions.Tour;
import competitions.Tournoi;
import equipes.Club;
import equipes.Transfert;
import outils.Clavier;
import outils.FormationNonValideException;
import outils.NonAutoriseException;
import outils.Sauvegardable;
import outils.TransfertImpossibleException;
import personnes.Arbitre;
import personnes.Attaquant;
import personnes.Defenseur;
import personnes.Gardien;
import personnes.Joueur;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de repésenter la simulation du jeu, c'est un
 *         singleton
 *         </p>
 */
public class Jeu implements outils.Sauvegardable {

	private static final long serialVersionUID = -3179389604118648666L;

	/**
	 * Le fichier init qui contient les championnats et leurs clubs (il sera charge
	 * qu'au debut du jeu)
	 */
	public static final String FILE_NAME_DATA_BASE = "ressources/jeu.db";

	/**
	 * Le fichier de sauvegarde qui sera mis à jour au fur et à mesure de la partie
	 */
	public static final String FILE_SAVE = "ressources/jeu.ser";

	/**
	 * Pour altérner entre les differentes compétitions (Il est static pour le
	 * fichier init, pour cette premiere version on a choisi de simplifier les
	 * choses et de le mettre le nombre de clubs et l'alternance static)
	 */
	public static final int QUANTUM_JEU = 6;

	/**
	 * Le jeu
	 */
	private static Jeu JEU = null;

	/**
	 * Le pseudo du joueur
	 */
	private String pseudoPlayer;

	/**
	 * l'experience du joueur
	 */
	private int experience;

	/**
	 * L'iteration courante dans la saison
	 */
	private int iterationSaison;

	/**
	 * Le club choisis par l'utilisateur
	 */
	private Club clubChoisis;

	/**
	 * La liste des championnat
	 */
	private ArrayList<Championnat> championnats;

	/**
	 * La liste des tournois
	 */
	private ArrayList<Tournoi> tournois;

	/**
	 * La liste des coupes
	 */
	private ArrayList<Tournoi> coupes;

	/**
	 * Tous les clubs du jeu
	 */
	private ArrayList<Club> tousLesClubs;

	/**
	 * Tous les arbitres du jeu
	 */
	private ArrayList<Arbitre> tousLesArbitres;

	/**
	 * La competition precedente jouee du jeu
	 */
	private Competition competitionPrecedente;

	/**
	 * Le constructeur du jeu
	 */
	private Jeu() {
		pseudoPlayer = null;
		experience = iterationSaison = 0;
		clubChoisis = null;
		championnats = new ArrayList<>();
		coupes = new ArrayList<>();
		tournois = new ArrayList<>();
		tousLesClubs = new ArrayList<>();
		tousLesArbitres = new ArrayList<>();
		competitionPrecedente = null;
	}

	/**
	 * Permet de retourner le jeu
	 * 
	 * @return le jeu
	 */
	public static Jeu getJeu()
			throws NullPointerException, NonAutoriseException, IOException, FormationNonValideException {
		// Si le singelton n'existe pas on le charge
		if (JEU == null)
			JEU = charger();

		return JEU;
	}

	/**
	 * Permet de charger un jeu depuis le fichier de sauvegarde si il existe, sinon
	 * le creer depuis le fichier init
	 * 
	 * @return Le jeu charge/cree
	 */
	private static Jeu charger()
			throws NullPointerException, NonAutoriseException, IOException, FormationNonValideException {

		try {
			// On le charge depuis le fichier de sauvegarde si il existe
			return (Jeu) Sauvegardable.charge(FILE_SAVE);

		} catch (ClassNotFoundException e) {
			// Si il y a eu un probleme de classe
			System.out.println(e.getMessage());
			return null;
		} catch (FileNotFoundException e) {
			// Si le fichier de sauvegarde n'existe pas on creer le jeu depuis le fichier
			// init
			return creer();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Permet de creer un jeu depuis le fichier init
	 * 
	 * @throws NullPointerException        : Si un des elements n'existe pas
	 * @throws NonAutoriseException        : Si on a eu des problemes d'autorisation
	 * @throws IOException                 : Si le fichier init existe pas
	 * @throws FormationNonValideException : Si on a eu des problemes dans les
	 *                                     formations
	 */
	private static Jeu creer()
			throws NullPointerException, NonAutoriseException, IOException, FormationNonValideException {

		Jeu jeu = new Jeu();

		// On lit depuis le fichier init
		Scanner sc = new Scanner(new BufferedReader(new FileReader(FILE_NAME_DATA_BASE)));

		// On lit le nombre de championnat existant dans le fichier
		int nombreChampionnat = sc.nextInt();
		sc.nextLine();

		// On parcours les championnat
		for (int i = 0; i < nombreChampionnat; i++) {

			// On reccupere les donnes du championnat
			String[] sChampionnat = sc.nextLine().split(",");
			String[] sCoupe = sc.nextLine().split(",");

			// On lit le nombre de club du championnat
			int nombreClubs = sc.nextInt();
			sc.nextLine();

			ArrayList<Club> clubs = new ArrayList<>();

			// On parcours les clubs
			for (int j = 0; j < nombreClubs; j++) {

				// On lit les information du club
				String[] club = sc.nextLine().split(",");

				// On crée le club
				clubs.add(new Club(club[0], Double.valueOf(club[1]), Integer.parseInt(club[2])));

				// On lit son nombre de joueur
				int nombreJoueur = sc.nextInt();
				sc.nextLine();

				// On parcours les joueurs
				for (int l = 0; l < nombreJoueur; l++) {

					// On lit les information du joueur
					String[] sJoueur = sc.nextLine().split(",");
					String nom = sJoueur[1];
					double nombrePointsAttaque = Double.valueOf(sJoueur[2]);
					double nombrePointsDefense = Double.valueOf(sJoueur[3]);
					double niveauGardien = Double.valueOf(sJoueur[4]);
					double caractere = Double.valueOf(sJoueur[5]);
					int numero = Integer.parseInt(sJoueur[6]);

					Joueur joueur = null;
					// On crée le joueur selon son poste
					switch (sJoueur[0]) {
					case "A": {
						joueur = new Attaquant(nom, nombrePointsAttaque, nombrePointsDefense, niveauGardien, caractere,
								numero);
						break;
					}
					case "D": {
						joueur = new Defenseur(nom, nombrePointsAttaque, nombrePointsDefense, niveauGardien, caractere,
								numero);
						break;
					}
					case "G": {
						joueur = new Gardien(nom, nombrePointsAttaque, nombrePointsDefense, niveauGardien, caractere,
								numero);
						break;
					}
					default:
						throw new NonAutoriseException("Le poste du joueur n'est pas valide");
					}
					// On ajoute le joueur au club
					clubs.get(j).ajouterJoueur(joueur);
				}
				clubs.get(j).changerFormation(club[3]);
			}

			// On lit le nombre d'arbitres
			int nombreArbitres = sc.nextInt();
			sc.nextLine();

			ArrayList<Arbitre> arbitres = new ArrayList<>();

			// On créer les arbitres des championnat
			for (int l = 0; l < nombreArbitres; l++) {
				String[] sArbitre = sc.nextLine().split(",");
				arbitres.add(new Arbitre(sArbitre[0], Double.valueOf(sArbitre[1]), Double.valueOf(sArbitre[2])));
			}

			// On ajoute le championnat créer
			jeu.championnats.add(new Championnat(sChampionnat[0], Double.valueOf(sChampionnat[1]),
					Integer.valueOf(sChampionnat[2]), clubs, arbitres));

			// On ajoute les coupes
			jeu.coupes.add(new Tournoi(sCoupe[0], Double.valueOf(sCoupe[1]), Integer.valueOf(sCoupe[2]),
					new ArrayList<>(clubs), arbitres));

			jeu.tousLesClubs.addAll(clubs);
			jeu.tousLesArbitres.addAll(arbitres);
		}
		sc.close();

		// Les Tournois européens
		Collections.shuffle(jeu.tousLesClubs);
		Collections.shuffle(jeu.tousLesArbitres);

		jeu.tournois.add(new Tournoi("Ligue des champions", 400, 20,
				new ArrayList<>(jeu.tousLesClubs.subList(0, jeu.tousLesClubs.size() / 2)),
				new ArrayList<>(jeu.tousLesArbitres.subList(0, jeu.tousLesArbitres.size() / 2))));

		jeu.tournois.add(new Tournoi("Europa ligue", 300, 12,
				new ArrayList<>(jeu.tousLesClubs.subList(jeu.tousLesClubs.size() / 2, jeu.tousLesClubs.size())),
				new ArrayList<>(
						jeu.tousLesArbitres.subList(jeu.tousLesArbitres.size() / 2, jeu.tousLesArbitres.size()))));

		// On sauvegarde le jeu dans le fichier de sauvegarde
		Sauvegardable.save(FILE_SAVE, jeu);
		return jeu;
	}

	/**
	 * Permet de choisir un club avec lequel l'utilisateur joue
	 */
	public boolean choisirClub() {

		// l'utilisateur choisis le championnat ou il veut jouer
		String rep = "le numéro du championnat dans lequel vous voulez participer : \n";
		for (int i = 0; i < championnats.size(); i++)
			rep += "\n\t" + i + " - " + championnats.get(i).nom;

		Championnat championnat = null;
		int choix;
		int size;
		String repClub;

		// On boucle jusqu'à confirmation
		while (true) {

			// On reccupere le choix de l'utilisateur
			choix = Clavier.getChoix(rep, championnats.size());

			// On reccupere le championnat
			championnat = championnats.get(choix);
			size = championnat.getListeClubs().size();

			// L'utilisateur choisis son club
			repClub = "votre club : \n\n" + championnat + "\n\t" + size + " - Retour";

			choix = Clavier.getChoix(repClub, size + 1);

			if (choix == size)
				continue;

			break;
		}

		// Si l'utilisateur n'a pas assez d'experience pour jouer avec le club
		if (championnat.getListeClubs().get(choix).experienceRequise > experience) {
			System.out.println("Vous n'avez pas assez d'experience pour jouer avec ce club");
			Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...");
			return false;
		}

		// Si il a deja joue une saison et il a change de club on remet le club
		// precedent en background
		if (clubChoisis != null) {
			clubChoisis.setChoix(false);
			tousLesClubs.add(clubChoisis);
		}

		// On affecte le nouveau club choisis
		clubChoisis = championnat.getListeClubs().get(choix);
		clubChoisis.setChoix(true);
		tousLesClubs.remove(clubChoisis);

		// On mets à jour toutes les competitions
		for (Competition competition : championnats)
			competition.updateParticipation();

		for (Competition competition : tournois)
			competition.updateParticipation();

		for (Competition competition : coupes)
			competition.updateParticipation();

		System.out.println("\nFélicitation ! Vous êtes maintenant le dirigeant de : " + clubChoisis.nom + "\n");

		try {
			// On sauvegarde l'etat du jeu
			Sauvegardable.save(FILE_SAVE, this);
		} catch (IOException e) {
			System.out.println("\n - " + e.getMessage());
			Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
		}
		return true;
	}

	/**
	 * Permet de jouer les competitions, sauf celle dans laquelle le club choisis
	 * joue
	 * 
	 * @param competitions : la liste des competitions a jouer
	 * @return La competition dans laquel le club choisis joue
	 * @throws NullPointerException : Si un element n'existe pas
	 * @throws NonAutoriseException : Si on a un probleme d'autorisation
	 */
	private Competition jouerCompetitions(ArrayList<? extends Competition> competitions)
			throws NullPointerException, NonAutoriseException {

		Competition competitionParticipe = null;
		// On parcours les competitions
		for (Competition competition : competitions) {

			// Si le club choisis participe on reccupere la competition et on passe à la
			// suivante
			if (competition.estParticipe()) {
				competitionParticipe = competition;
				continue;
			}

			// On joue la competition
			competition.jouerTour();
		}
		return competitionParticipe;
	}

	/**
	 * Permet de changer la formation du club choisis
	 * 
	 * @param competitionParticipe : La competition dans laquelle le club participe
	 */
	private void changerFormation(String competitionParticipe) {

		// On genere un menu
		String rep = ": \n\n\t0 - Changer formation.\n\t1 - Changer type formation.\n\t2 - Remplacer.\n\t3 - Afficher les détails des joueurs."
				+ "\n\t4 - Sauvegarder la formation."
				+ ((clubChoisis.estSauvegarderFormation()) ? "\n\t5 - Charger la formation.\n\t6 -" : "\n\t5 -")
				+ " Valider.";

		int max = (clubChoisis.estSauvegarderFormation()) ? 6 : 5;
		int c;

		System.out.println("\n La formation actuelle : \n\n" + clubChoisis.getFormation());

		// On reccupere le choix de l'utilisateur
		c = Clavier.getChoix(rep, max + 1);

		// On traite les requetes
		if (c == 0) { // Changement de formation
			String formation = Clavier.saisirLigne("\n\t - Entrer votre formation : ");
			try {
				clubChoisis.changerFormation(formation);
			} catch (FormationNonValideException | NonAutoriseException e) {
				System.out.println("\n - " + e.getMessage());
				Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...");
			}

		} else if (c == 1) { // On change le type de la formation
			rep = "\n\n\t0 - Normal.\n\t1 - Offensif.\n\t2 - Defensif";
			int type = Clavier.getChoix(rep, 3);
			try {
				clubChoisis.changerFormation(type);
			} catch (FormationNonValideException | NonAutoriseException e) {
				System.out.println("\n - " + e.getMessage());
				Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...");
			}

		} else if (c == 2) { // On remplace les joueurs

			int size = clubChoisis.getListeJoueurs().size();
			String[][] tabJoueurs = new String[size + 1][2];

			ArrayList<Integer> joueursTitu = clubChoisis.getFormation().getJoueursTitulaires();

			// Pour l'affichage
			tabJoueurs[0][0] = "\tTitulaires : ";
			tabJoueurs[0][1] = "Remplaçants : \n";

			int titu = 1, rem = 1;
			for (Joueur joueur : clubChoisis.getListeJoueurs()) {

				if (joueursTitu.contains(joueur.getNumero()))
					tabJoueurs[titu++][0] = joueur.toString();
				else
					tabJoueurs[rem++][1] = joueur.toString();
			}

			for (int i = 0; i < size; i++) {
				if (tabJoueurs[i][0] == null && tabJoueurs[i][1] == null)
					break;

				System.out.println("\t"
						+ ((tabJoueurs[i][0] == null) ? String.format("%-35s", "")
								: String.format("%-35s", tabJoueurs[i][0]))
						+ "\t" + ((tabJoueurs[i][1] == null) ? "" : tabJoueurs[i][1]));
			}

			// L'utilisateur introduit les numeros des joueurs entrant et sortant
			System.out.println("\n");
			rep = "\t - Le numéro du joueur sortant : ";
			int sortant = Clavier.saisirEntier(rep);

			rep = "\n\t - Le numéro du joueur entrant : ";
			int entrant = Clavier.saisirEntier(rep);

			Joueur joueurEntrant = clubChoisis.getJoueur(entrant);

			if (joueurEntrant != null && joueurEntrant.getNombreMatchSuspendu(competitionParticipe) != -1) {
				System.out.println("\n - Le joueur est suspendu, il ne peut pas jouer");
				Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");

			} else {
				// On remplace
				try {
					clubChoisis.remplacer(sortant, entrant);
				} catch (NullPointerException | FormationNonValideException | NonAutoriseException e) {
					System.out.println("\n - " + e.getMessage());
					Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...");
				}
			}
		} else if (c == 3) { // On affiche les details des joueurs

			rep = "\t  " + String.format("%-37s", " ") + "Attaque\tDefense\tGardien\tBlesse\tSusspendu\n";
			System.out.println(rep);
			for (Joueur joueur : clubChoisis.getListeJoueurs())
				System.out.println("\t - " + joueur.getEtat(competitionParticipe));

			Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...");

		} else if (c == 4) { // On sauvegarde la formation
			try {
				clubChoisis.sauvegarderFormation();
			} catch (FormationNonValideException | NonAutoriseException e) {
				System.out.println("\n - " + e.getMessage());
				Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...");
			}
		} else if (c == max) // On valide
			return;

		else { // On charger la formation sauvegarder
			try {
				clubChoisis.chargerFormationSauvegardee();
			} catch (FormationNonValideException | NonAutoriseException e) {
				System.out.println("\n - " + e.getMessage());
				Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...");
			}
		}

		// Tantque l'utilisateur n'a pas validé
		changerFormation(competitionParticipe);
	}

	/**
	 * Permet de lancer le mercato durant le jeu
	 */
	private void mercato() throws NullPointerException, NonAutoriseException {
		// On melange les clubs
		Collections.shuffle(tousLesClubs);

		// Les acheteurs sont les autres clubs :
		// On selectionne au hasard les achteurs
		ArrayList<Club> bgAcheteurs = new ArrayList<>(tousLesClubs.subList(0, tousLesClubs.size() / 4));

		for (Club acheteur : bgAcheteurs) {

			// On reccupere un vendeur
			Club vendeur = clubChoisis;
			if (Math.random() > 0.12)
				vendeur = tousLesClubs.get((int) (Math.random() * tousLesClubs.size()));

			// On selectionne au hasard un joueur dans le club vendeur
			Joueur joueur = vendeur.getListeJoueurs().get((int) (Math.random() * vendeur.getListeJoueurs().size()));

			// Pour une securite !!
			if (joueur == null)
				continue;

			/*
			 * On genere un prix de vente et si le club vendeur est le club choisis on
			 * demande l'avis de l'utilisateur
			 */
			double prix = 2. / 3. * joueur.getPuissance() + Math.random() * 1000;

			if (vendeur.equals(clubChoisis)) {
				System.out.println("- Le club " + acheteur.nom + " veut vous acheter le joueur : \n" + " \n\t ** "
						+ joueur.nom + " au prix de " + String.format("%.3f", prix).replace(',', '.'));

				System.out.println("\nVoulez vous accepter cette offre ? ");
				int choix = Clavier.getChoix(": \n\n\t0 - Accepter\n\t1 - Décliner", 2);

				if (choix == 1) {
					System.out.println();
					continue;
				}

			} else if (Math.random() > 0.3)
				continue;

			// Si la reponse est affirmative on transfert le joueur
			try {
				Transfert.transferer(joueur, vendeur, acheteur, prix);
				if (vendeur.equals(clubChoisis)) {
					System.out.println("\n\t - Succes le club " + acheteur.nom + " a recruté votre joueur\n");
					Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
				}
			} catch (TransfertImpossibleException | NonAutoriseException | FormationNonValideException
					| NullPointerException e) {
				// Si probleme de tranfert
				if (vendeur.equals(clubChoisis)) {
					System.out.println("\n - Echec : " + e.getMessage());
					Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
				}
			}
		}

		// On demande à l'utilisateur si il veut participer au mercato
		System.out.println("Voulez vous participer au mercato ? ");
		int choix = Clavier.getChoix("\n\n\t0 - Oui\n\t1 - Non", 2);
		if (choix == 1) {
			System.out.println();
			return;
		}

		// On selectionne le club ou acheter
		String rep = " un club ou acheter : \n";
		for (int i = 0; i < tousLesClubs.size(); i++)
			rep += "\n\t" + String.format("%3d", i) + " - " + tousLesClubs.get(i).nom;

		rep += "\n\t" + String.format("%3d", tousLesClubs.size()) + " - Annuler";

		// Tantque non validation
		while (true) {
			choix = Clavier.getChoix(rep, tousLesClubs.size() + 1);
			if (choix == tousLesClubs.size())
				return;

			Club club = tousLesClubs.get(choix);
			ArrayList<Joueur> joueurs = club.getListeJoueurs();

			System.out.println("\t\t\t ** " + pseudoPlayer + " : " + experience + " Exp, club : " + clubChoisis.nom
					+ ". Budget : " + String.format("%.3f", clubChoisis.getBudget()).replace(',', '.') + " **\n");

			// On selectionne le joueur à acheter
			String repJ = " un joueur : \n";
			for (int i = 0; i < joueurs.size(); i++)
				repJ += "\n\t" + String.format("%3d", i) + " - " + joueurs.get(i).nom;

			repJ += "\n\t" + String.format("%3d", joueurs.size()) + " - Retourner";
			choix = Clavier.getChoix(repJ, joueurs.size() + 1);
			if (choix == joueurs.size())
				continue;

			// On propose une offre
			double prix = Clavier.saisirDouble("\n - Proposer une offre (Prix) : ");

			Joueur joueur = joueurs.get(choix);
			if (2. / 3. * joueur.getPuissance() + Math.random() * 1000 >= prix) {
				System.out.println("\n\t - Le club a refusé votre offre ! \n");
				return;
			}

			// Si l'offre est accepte on transfert le joueur
			try {
				Transfert.transferer(joueur, club, clubChoisis, prix);
				System.out.println("\n\t - Félicitation ! vous avez recruté " + joueur.nom + " \n");

			} catch (TransfertImpossibleException | NonAutoriseException | FormationNonValideException
					| NullPointerException e) {
				System.out.println("\n - " + e.getMessage());
				Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
			}
			return;
		}
	}

	/**
	 * On lance la saison
	 * 
	 * @return true si le joueur à quitter le jeu, false sinon
	 */
	public boolean lancer() throws NullPointerException, NonAutoriseException {

		Competition competitionParticipe = null;

		// PReccupere le max de journes dans les championnat
		double maxJournees = 0;
		for (Championnat championnat : championnats)
			maxJournees = Math.max(maxJournees,
					(championnat.getListeJournee() == null) ? 0 : championnat.getListeJournee().size());

		// Tantque la saison n'est pas fini et l'utilisateur n'a pas quitter
		while (true) {

			// Mercato : La periode des transferts
			if (iterationSaison > 1. / 3. * maxJournees && iterationSaison <= 2. / 3. * maxJournees)
				mercato();

			if (iterationSaison % QUANTUM_JEU == 2) // On joue les coupes
				competitionParticipe = jouerCompetitions(coupes);

			else if (iterationSaison % QUANTUM_JEU == 5) // On joue les tournois europeens
				competitionParticipe = jouerCompetitions(tournois);

			else // On joue les championnats
				competitionParticipe = jouerCompetitions(championnats);

			// On reccupere les tours de la competition ou joue le club choisis
			Tour tourCourant = competitionParticipe.getTourCourant();

			Tour tourPrecedent = (competitionPrecedente == null) ? null : competitionPrecedente.getTourPrecedent();

			System.out.println("\t\t\t ** " + pseudoPlayer + " : " + experience + " Exp, club : " + clubChoisis.nom
					+ ". Budget : " + String.format("%.3f", clubChoisis.getBudget()).replace(',', '.') + " **\n");

			// On genere un menu
			int iter = 0;
			String rep = "";

			if (tourPrecedent != null)
				rep += "\t" + iter++ + " - Afficher les résultats du tour précedent.\n";

			if (competitionParticipe instanceof Championnat)
				rep += "\t" + iter++ + " - Afficher le classement.\n";

			if (!competitionParticipe.estFini())
				rep += "\t" + iter++ + " - Afficher les matchs à venir.\n";

			rep += "\t" + iter++ + " - Passer au prochain tour.\n";
			rep += "\t" + iter + " - Quitter.";

			int choix;

			// Tanque le joueur n'a pas quitter et n'a pas decider de lancer le tour
			while (true) {

				System.out.println("\t ** " + competitionParticipe.nom + " " + tourCourant.nom);

				// On reccupere le choix de l'utilisateur sur l'operation a effectue
				choix = Clavier.getChoix("\n\n" + rep, iter + 1);

				if (choix == iter) { // Quitter
					try {
						Sauvegardable.save(FILE_SAVE, this);
					} catch (IOException e) {
						System.out.println("\n - " + e.getMessage());
						Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
					}
					return false;
				}

				if (choix == iter - 1) // Jouer le tour
					break;

				if (tourPrecedent != null && choix == 0) { // Afficher les resultats des matchs precedents
					System.out.println("\n\tLes résultats des matchs de " + tourPrecedent.nom + " : \n"
							+ competitionPrecedente.getResultat() + "\n");
					Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
					continue;
				}

				// Affiche le classement si on est dans le championnat
				if ((choix == 0 || (choix == 1 && tourPrecedent != null))
						&& competitionParticipe instanceof Championnat) {

					System.out.println(((Championnat) competitionParticipe).getClassement() + "\n");
					Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
					continue;
				}

				// Afficher les matchs à venir si y'en a
				if (choix == 2 || choix == 0
						|| (choix == 1 && (!(competitionParticipe instanceof Championnat) || tourPrecedent == null))) {

					System.out.println("\n\tLes matchs à venir : \n" + tourCourant + "\n");
					Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
				}
			}

			// Tantque l'utilisateur n'a pas decider de lancer le match (si biensur il joue
			// le match)
			while (competitionParticipe != null && !competitionParticipe.estFini() && tourCourant.estParticipe()) {

				// On genere le menu : L'avant match
				System.out.println(
						"\n- Avant Match de " + tourCourant.nom + " dans la competition " + competitionParticipe.nom);

				// Si le club choisis ne peut pas jouer, le match est déclaré forfait
				if (!clubChoisis.peutJouerLeMatch(competitionParticipe.nom)) {
					System.out.println(
							"\n\t - Ce match est déclaré forfait ! car vous n'avez pas assez de joueurs éligibles à jouer\n");

					Clavier.saisirLigne("Appuyez sur entrer pour continuer ...\n");
					break;
				}

				rep = ": \n\n\t0 - Jouer le match.\n\t1 - Changer Formation.\n\t2 - Quitter.";

				// L'utilisateur choisis quoi faire
				choix = Clavier.getChoix(rep, 3);

				if (choix == 0) { // Jouer le match si tout est ok

					if (clubChoisis.getNombreJoueursExclus(competitionParticipe.nom) != 0) {
						System.out.println(
								"\n\t - Attention ! Vous avez des joueurs non éligibles pour jouer au match, foudra les remplacer\n");

						Clavier.saisirLigne("Appuyez sur entrer pour continuer ...\n");
						continue;
					}

					break;
				}

				if (choix == 1) { // Changement de formation
					changerFormation(competitionParticipe.nom);
					continue;
				}

				try { // Quitter et sauvegarder
					Sauvegardable.save(FILE_SAVE, this);
				} catch (IOException e) {
					System.out.println("\n - " + e.getMessage());
					Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
				}
				return false;
			}

			competitionParticipe.jouerTour(); // On jouer le tour
			competitionPrecedente = competitionParticipe; // On garde la competition precedente pour l'affichage

			competitionParticipe.afficherMatchUtilisateur(); // On affiche le match de l'utilisateur
			Clavier.saisirLigne("\n\nAppuyez sur entrer pour continuer ...\n");

			// Si la competition est fini, on affiche le resultat, et on met à jour les
			// recompences
			if (competitionParticipe.estFini() && !competitionParticipe.dejaAffichee()) {

				Club vainqueur = competitionParticipe.getVainqueur();

				System.out.println(competitionParticipe.getResultat());

				if (competitionParticipe instanceof Championnat)
					System.out.println(((Championnat) competitionParticipe).getClassement() + "\n");

				vainqueur.augmenterBudget(competitionParticipe.recompense);

				experience += competitionParticipe.getExperienceGagnee(clubChoisis);
			}

			/*
			 * Tantque il reste des matchs a jouer : on compte le nombre de competitions
			 * finis il doit etre egal au nombre de competitions total
			 */
			if (championnats.stream().filter((c) -> c.estFini()).count() == championnats.size())
				break;

			iterationSaison++;

			try {
				// On sauvegarde le jeu
				Sauvegardable.save(FILE_SAVE, this);
			} catch (IOException e) {
				System.out.println("\n - " + e.getMessage());
				Clavier.saisirLigne("\nAppuyez sur entrer pour continuer ...\n");
			}
		}

		// L'utilisateur n'a pas quitter
		return true;
	}

	/**
	 * Permet de changer la saison
	 */
	public void changementSaison() throws NullPointerException, NonAutoriseException, IOException {

		// On diminue le budget des clubs
		for (Club club : tousLesClubs)
			club.diminuerBudget(1. / 5. * club.getBudget());

		clubChoisis.diminuerBudget(1. / 5. * clubChoisis.getBudget());

		// L'utilisateur choisis de changer de club ou non
		System.out.println("Voulez vous changer de club pour la nouvelle saison ? ");
		int choix = Clavier.getChoix(": \n\n\t0 - Oui\n\t1 - Non", 2);

		if (choix == 0)
			choisirClub();

		// On reccupere les 1/2 premiers et derniers clubs de chaque championnat
		ArrayList<ArrayList<Club>> clubsQualifies = new ArrayList<>();
		clubsQualifies.add(new ArrayList<>());
		clubsQualifies.add(new ArrayList<>());

		int i = 0;
		for (Championnat championnat : championnats) {

			int j = 0;
			int size = championnat.getListeClubs().size();
			for (Map.Entry<Club, Integer> lcp : championnat.trierClubs()) {
				if (j < size / 2)
					clubsQualifies.get(0).add(lcp.getKey());
				else
					clubsQualifies.get(1).add(lcp.getKey());
				j++;
			}

			championnat.clear();
			Tournoi coupeI = coupes.get(i);
			coupes.set(i, new Tournoi(coupeI.nom, coupeI.recompense, coupeI.gainExperience, coupeI.getListeClubs(),
					coupeI.getArbitres()));
			i++;
		}

		tournois.clear();

		// On regenre les Tournois européens
		Collections.shuffle(tousLesArbitres);

		// Les 1/2 premiers
		tournois.add(new Tournoi("Ligue des champions", 400, 20, new ArrayList<>(clubsQualifies.get(0)),
				new ArrayList<>(tousLesArbitres.subList(0, tousLesArbitres.size() / 2))));

		// Les 1/2 derniers
		tournois.add(new Tournoi("Europa ligue", 300, 12, new ArrayList<>(clubsQualifies.get(1)),
				new ArrayList<>(tousLesArbitres.subList(tousLesArbitres.size() / 2, tousLesArbitres.size()))));

		// On sauvegarde le jeu
		Sauvegardable.save(FILE_SAVE, this);
		iterationSaison = 0;
	}

	/**
	 * Permet de savoir si le joueur à choisis un club ou pas
	 * 
	 * @return true si il a choisis, false sinon
	 */
	public boolean estChoisis() {
		return clubChoisis != null;
	}

	/**
	 * Permet d'obtenir l'experience du joueur
	 * 
	 * @return l'experience du joueur
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * Permet de changer le pseudo du player
	 * 
	 * @param pseudoPlayer : le nouveau pseudo
	 */
	public void setPseudoPlayer(String pseudoPlayer) {
		this.pseudoPlayer = (pseudoPlayer == null) ? this.pseudoPlayer : pseudoPlayer;
	}

	/**
	 * Permet d'augementer l'experience du joueur
	 * 
	 * @param experience : l'augmentation
	 */
	public void updateExperience(int experience) {
		this.experience += (experience <= 0) ? 10 : experience;
	}
}