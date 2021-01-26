package equipes;

import java.util.ArrayList;

import outils.FormationNonValideException;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de repésenter une formation pour un club de
 *         football
 *         </p>
 */
public class Formation implements outils.Sauvegardable {

	private static final long serialVersionUID = -8633899248463486670L;

	/**
	 * Formation normale
	 */
	public static final int NORMAL = 0;

	/**
	 * Formation offensive
	 */
	public static final int OFFENSIF = 1;

	/**
	 * Formation defensive
	 */
	public static final int DEFENSIF = 2;

	/**
	 * Une formation par defaut pour un club
	 */
	public static final String DEFAULT_FORMATION = "433";

	/**
	 * Le type de la formation
	 */
	private int type;

	/**
	 * La formation
	 */
	private String formation;

	/**
	 * La liste des numeros des defenseurs
	 */
	private int[] listeDefenseurs;

	/**
	 * La liste des numeros des milieux
	 */
	private int[] listeMilieux;

	/**
	 * La liste des numeros des attaquants
	 */
	private int[] listeAttaquants;

	/**
	 * Le numero du gardien
	 */
	private int gardien;

	/**
	 * La liste des titulaires
	 */
	private ArrayList<Integer> listeTitulaire;

	/**
	 * Constructeur de Formation à 3 parametres
	 * 
	 * @param type              : le type de la formation
	 * @param formation         : la formation
	 * @param joueursTitulaires : la liste des numeros des joueurs titulaires
	 * 
	 * @throws FormationNonValideException : si la formation n'est pas valide (le
	 *                                     type ou la formation sont incorrets)
	 */
	public Formation(String formation, int type, ArrayList<Integer> joueursTitulaires)
			throws FormationNonValideException {

		// On genere la formation avec les parametres
		change(formation, type, joueursTitulaires);
	}

	/**
	 * Permet de changer la formation et le type
	 * 
	 * @param formation : la nouvelle formation
	 * @type : le nouveau type de la formation
	 */
	public void changerFormation(String formation, int type) throws FormationNonValideException {

		// Si c'est la meme formation
		if (this.formation.equals(formation)) {
			this.type = type;
			return;
		}

		// On construit la nouvelle formation
		change(formation, type, getJoueursTitulaires());
	}

	/**
	 * Permet de changer la formation
	 * 
	 * @param formation : la nouvelle formation
	 */
	public void changerFormation(String formation) throws FormationNonValideException {
		changerFormation(formation, type);
	}

	/**
	 * Permet de changer le type de la formation
	 * 
	 * @param type : le nouveau type de la formation
	 */
	public void changerFormation(int type) throws FormationNonValideException {
		changerFormation(formation, type);
	}

	/**
	 * Permet de remplacer un joueur par un autre joueur
	 * 
	 * @param joueurSortant : Le numero du joueur sortant
	 * @param joueurEntrant : Le numero du joueur entrant
	 * @throws FormationNonValideException : Si le joueur sortant ne joue pas
	 */
	public void remplacer(int joueurSortant, int joueurEntrant) throws FormationNonValideException {

		// Si c'est le meme joueur on fait rien
		if (joueurSortant == joueurEntrant)
			return;

		if (!listeTitulaire.contains(joueurSortant))
			// Si il n'est pas titulaire
			throw new FormationNonValideException("Le joueur sortant n'est pas titulaire");

		// Si les deux joueurs jouent on permute que leurs postes
		if (listeTitulaire.contains(joueurEntrant)) {
			permute(joueurEntrant, joueurSortant);
			return;
		}

		// Si c'est le gardien on change
		if (gardien == joueurSortant) {
			gardien = joueurEntrant;
			listeTitulaire.set(listeTitulaire.indexOf(joueurSortant), joueurEntrant);
			return;
		}

		// Si il appartient aux autres listes on change dans les listes de postes
		if (replace(joueurSortant, joueurEntrant, listeDefenseurs)
				|| replace(joueurSortant, joueurEntrant, listeAttaquants)
				|| replace(joueurSortant, joueurEntrant, listeMilieux))
			return;
	}

	/**
	 * Permet de permuter les postes des deux joueurs
	 * 
	 * @param joueur1 : le numero du premier joueur
	 * @param joueur2 : le numero du deuxieme joueur
	 */
	private void permute(int joueur1, int joueur2) {
		// POur reccuperer la liste ou appartient le joueur
		int[] tabJoueur1 = null;
		int[] tabJoueur2 = null;

		// Pour reccuperer l'indice du joueur dans les listes
		int indexJoueur1 = -1;
		int indexJoueur2 = -1;

		// Si on a pas trouver les postes des joueurs on cherche dans le tableau
		for (int i = 0; (indexJoueur1 == -1 || indexJoueur2 == -1) && i < listeDefenseurs.length; i++) {
			if (listeDefenseurs[i] == joueur1 && indexJoueur1 == -1) {
				indexJoueur1 = i;
				tabJoueur1 = listeDefenseurs;
			}
			if (listeDefenseurs[i] == joueur2 && indexJoueur2 == -1) {
				indexJoueur2 = i;
				tabJoueur2 = listeDefenseurs;
			}
		}

		// De meme pour la liste des milieux
		for (int i = 0; (indexJoueur1 == -1 || indexJoueur2 == -1) && i < listeMilieux.length; i++) {
			if (listeMilieux[i] == joueur1 && indexJoueur1 == -1) {
				indexJoueur1 = i;
				tabJoueur1 = listeMilieux;
			}
			if (listeMilieux[i] == joueur2 && indexJoueur2 == -1) {
				indexJoueur2 = i;
				tabJoueur2 = listeMilieux;
			}
		}

		// De meme pour la liste des attaquants
		for (int i = 0; (indexJoueur1 == -1 || indexJoueur2 == -1) && i < listeAttaquants.length; i++) {
			if (listeAttaquants[i] == joueur1 && indexJoueur1 == -1) {
				indexJoueur1 = i;
				tabJoueur1 = listeAttaquants;
			}
			if (listeAttaquants[i] == joueur2 && indexJoueur2 == -1) {
				indexJoueur2 = i;
				tabJoueur2 = listeAttaquants;
			}
		}

		int tmp;

		// Si le premier joueur est un gardien
		if (indexJoueur1 == -1) {
			tmp = gardien;
			gardien = tabJoueur2[indexJoueur2];
			tabJoueur2[indexJoueur2] = tmp;
			return;
		}

		// Si le deuxieme joueur est un gardien
		if (indexJoueur2 == -1) {
			tmp = gardien;
			gardien = tabJoueur1[indexJoueur1];
			tabJoueur1[indexJoueur1] = tmp;
			return;
		}

		// Sinon on permute les postes
		tmp = tabJoueur2[indexJoueur2];
		tabJoueur2[indexJoueur2] = tabJoueur1[indexJoueur1];
		tabJoueur1[indexJoueur1] = tmp;
	}

	/**
	 * Permet de remplacer un joueur par un autre dans une liste d'un poste
	 * 
	 * @param joueurSortant : le joueur sortant
	 * @param joueurEntrant : le joueur entrant
	 * @param liste         : la liste du poste
	 * @return : true si le remplacement est effectue, false sinon
	 */
	private boolean replace(int joueurSortant, int joueurEntrant, int[] liste) {
		// Cette methode sert à factoriser le code
		// On reccupere l'indice de l'element et on remplace par le joueur entrant
		int index;

		for (index = 0; index < liste.length; index++)
			if (liste[index] == joueurSortant)
				break;

		if (index != liste.length) {
			liste[index] = joueurEntrant;
			listeTitulaire.set(listeTitulaire.indexOf(joueurSortant), joueurEntrant);
			genererPostes();
			return true;
		}

		// Si le joueur n'existe pas dans la liste
		return false;
	}

	/**
	 * Permet de changer la formation du club
	 * 
	 * @param formation      : la nouvelle formation
	 * @param type           : le nouveau le type
	 * @param listeTitulaire : la liste des numeros des joueurs titulaires
	 * 
	 * @throws FormationNonValideException : Si la formation n'est pas valide
	 */
	private void change(String formation, int type, ArrayList<Integer> listeTitulaire)
			throws FormationNonValideException {

		// Si le type n'est pas valide
		if (type != NORMAL && type != OFFENSIF && type != DEFENSIF)
			throw new FormationNonValideException("Le type de la formation n'est pas correcte");

		// Si la formation n'existe pas, ou la formation ne contient pas 3 chiffres
		if (formation == null || formation.length() != 3)
			throw new FormationNonValideException(formation + " n'est pas une formation valide");

		// Si la formation n'est pas numerique
		try {
			Integer.parseInt(formation);
		} catch (NumberFormatException e) {
			throw new FormationNonValideException("La formation doit être numerique");
		}

		// Si la liste des titulraire n'existe pas ou ne contient pas 11 joueurs
		if (listeTitulaire == null || listeTitulaire.size() != 11)
			throw new FormationNonValideException("La liste des titulaire n'est pas valide");

		// On reccupere les nombres dans chaque poste
		int nombreDefense = Integer.parseInt(String.valueOf(formation.charAt(0)));
		int nombreMilieu = Integer.parseInt(String.valueOf(formation.charAt(1)));
		int nombreAttaque = Integer.parseInt(String.valueOf(formation.charAt(2)));

		// Si les nombre ne forme pas 10 joueurs
		if (nombreDefense + nombreMilieu + nombreAttaque != 10)
			throw new FormationNonValideException("La formation n'est pas valide");

		// Si les minima des postes ne sont pas respectes
		if (nombreDefense < 2 || nombreMilieu < 2 || nombreAttaque < 1 || nombreDefense > 5 || nombreMilieu > 5
				|| nombreAttaque > 4)
			throw new FormationNonValideException(
					formation + " manque de joueurs ([2-5] defenseurs, [2-5] milieux, [1-4] attaquants)");

		this.listeTitulaire = listeTitulaire;
		this.formation = formation;
		this.type = type;

		// On genere les tableaux des postes à partir de la liste des titulaires
		genererPostes();
	}

	/**
	 * Permet de genrer les listes des postes à partir de la liste des titulaire
	 */
	public void genererPostes() {

		// On reccupere les nombres
		int nombreDefense = Integer.parseInt(String.valueOf(formation.charAt(0)));
		int nombreMilieu = Integer.parseInt(String.valueOf(formation.charAt(1)));
		int nombreAttaque = Integer.parseInt(String.valueOf(formation.charAt(2)));

		listeDefenseurs = new int[nombreDefense];
		listeMilieux = new int[nombreMilieu];
		listeAttaquants = new int[nombreAttaque];

		// Le premier est le gardien
		gardien = listeTitulaire.get(0);
		int i;

		// les nombreDefense suivant sont les defenseurs
		for (i = 1; i <= nombreDefense; i++)
			listeDefenseurs[i - 1] = listeTitulaire.get(i);

		// les nombreMilieu suivant sont les milieux
		for (i = nombreDefense + 1; i <= nombreDefense + nombreMilieu; i++)
			listeMilieux[i - nombreDefense - 1] = listeTitulaire.get(i);

		// le reste sont les attaquants
		for (i = nombreDefense + nombreMilieu + 1; i < listeTitulaire.size(); i++)
			listeAttaquants[i - nombreDefense - nombreMilieu - 1] = listeTitulaire.get(i);
	}

	/**
	 * Permet de genrer l'affichage pour une liste de joueurs de poste commun
	 * 
	 * @param liste  : la liste des joueurs
	 * @param max    : la taille max des listes de postes
	 * @param espace : le nombre d'espaces
	 * @return l'affichage de la liste
	 */
	private static String getRep(int[] liste, double max, int espace) {
		String rep = "";
		int len = liste.length;
		int div = (len == 2) ? 3 : 2;

		// On accumule les string representant les numero des joueurs avec des espaces
		for (int joueur : liste)
			if (len == max)
				rep += joueur + String.format("%" + max / len * espace + "s", "");
			else
				rep += String.format("%" + (int) Math.floor(max / len * espace / 2) + "s", joueur)
						+ String.format("%" + (int) Math.floor(max / len * espace / div) + "s", "");
		return rep + "\n\n";
	}

	@Override
	public String toString() {
		int espace = 8;
		double max = Math.max(Math.max(listeDefenseurs.length, listeMilieux.length), listeAttaquants.length);

		// On generer la representation de tous les postes
		return "\t\tType : " + afficherType() + "\n\n\t"
				+ String.format("%" + (int) Math.floor(max * espace / 2.1) + "s\n\n", gardien) + "\t"
				+ getRep(listeDefenseurs, max, espace) + "\t" + getRep(listeMilieux, max, espace) + "\t"
				+ getRep(listeAttaquants, max, espace);
	}

	/**
	 * Permet d'afficher le message corresepondant au type
	 * 
	 * @return le type
	 */
	private String afficherType() {
		return (type == NORMAL) ? "normal" : (type == DEFENSIF) ? "defensif" : "offensif";
	}

	/**
	 * Permet de cloner la formation
	 *
	 * @return la nouvelle formation
	 */
	public Formation clone() {
		try {
			return new Formation(formation, type, new ArrayList<Integer>(listeTitulaire));
		} catch (FormationNonValideException e) {
			return null;
		}
	}

	/**
	 * Retourne la formation
	 * 
	 * @return formation
	 */
	public String getFormation() {
		return formation;
	}

	/**
	 * Permet d'obtenir le type de la formation
	 * 
	 * @return le type de la formation
	 */
	public int getType() {
		return type;
	}

	/**
	 * Accesseur à la liste des defenseurs
	 * 
	 * @return la liste des defenseurs
	 */
	public int[] getListeDefenseurs() {
		return listeDefenseurs;
	}

	/**
	 * Accesseur à la liste des milieux de terrain
	 * 
	 * @return la liste des milieux de terrain
	 */
	public int[] getListeMilieux() {
		return listeMilieux;
	}

	/**
	 * Accesseur à la liste des attaquants
	 * 
	 * @return la liste des attaquants
	 */
	public int[] getListeAttaquants() {
		return listeAttaquants;
	}

	/**
	 * Accesseur au gardien
	 * 
	 * @return le gardien
	 */
	public int getGardien() {
		return gardien;
	}

	/**
	 * Permet de retourner la liste des numeros des joueurs titulaires
	 * 
	 * @return la liste de titulaires
	 */
	public ArrayList<Integer> getJoueursTitulaires() {
		return listeTitulaire;
	}
}