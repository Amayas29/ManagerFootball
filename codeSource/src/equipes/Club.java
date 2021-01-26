package equipes;

import java.util.ArrayList;
import java.util.Collections;

import outils.FormationNonValideException;
import outils.NonAutoriseException;
import personnes.Gardien;
import personnes.Joueur;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de repésenter un club de football
 *         </p>
 */
public class Club implements outils.Sauvegardable {

	private static final long serialVersionUID = -2703324657850436911L;

	/**
	 * Le budget par defaut d'un club
	 */
	public static final double DEFAULT_BUDGET = 100;

	/**
	 * Le nombre max de joueurs dans un club
	 */
	public static final int MAX_JOUEURS = 22;

	/**
	 * Le nombre min de joueurs dans un club
	 */
	public static final int MIN_JOUEURS = 17;

	/**
	 * Le nom du club
	 */
	public final String nom;

	/**
	 * Le budget du club
	 */
	private double budget;

	/**
	 * Si le club est choisis par l'utilisateur
	 */
	private boolean choisis;

	/**
	 * Le nombre de points pour pouvoir jouer avec ce club
	 */
	public final int experienceRequise;

	/**
	 * La liste des joueurs du club
	 */
	private ArrayList<Joueur> listeJoueurs;

	/**
	 * La formation du club
	 */
	private Formation formation;

	/**
	 * La formation sauvegardee du club
	 */
	private Formation formationSauvegardee;

	/**
	 * Constructeur de club
	 * 
	 * @param nom               : le nom du club
	 * @param budget            : le budget du club
	 * @param experienceRequise : l'experience requise pour jouer avec ce club
	 */
	public Club(String nom, double budget, int experienceRequise) {
		this.nom = nom;
		this.budget = budget < 0 ? DEFAULT_BUDGET : budget;
		this.experienceRequise = experienceRequise < 0 ? 0 : experienceRequise;

		choisis = false;
		listeJoueurs = new ArrayList<>();
		formation = formationSauvegardee = null;
	}

	/**
	 * Permet d'ajouter un joueur au club
	 * 
	 * @param joueur : le joueur à ajouter
	 * @throws NonAutoriseException : Si il y a probleme dans l'ajout
	 */
	public void ajouterJoueur(Joueur joueur) throws NonAutoriseException, NullPointerException {

		if (joueur == null)
			throw new NullPointerException("Le joueur n'existe pas");

		if (getJoueur(joueur.getNumero()) != null)
			throw new NonAutoriseException("Numéro du joueur déja existant");

		if (listeJoueurs.size() == MAX_JOUEURS)
			throw new NonAutoriseException("Le max de joueurs est atteint");

		listeJoueurs.add(joueur);
	}

	/**
	 * Supprime le joueur passé en argument dans le club
	 * 
	 * @param joueur : le joueur à supprimer
	 * @return vrai si le joueur est supprime
	 * @throws FormationNonValideException : Si on a eu un probleme dans la
	 *                                     regeneration de la formation
	 * @throws NonAutoriseException        : Si il y a un probleme d'autorisation
	 */
	protected boolean supprimerJoueur(Joueur joueur) throws NonAutoriseException, FormationNonValideException {

		if (listeJoueurs.size() <= MIN_JOUEURS)
			throw new NonAutoriseException("Le min de joueurs est atteint");

		// On enleve le joueur de la liste
		if (listeJoueurs.remove(joueur)) { // Si on a bien enleve le joueur

			// On enleve le joueur dans la formation si il existe
			int index = formation.getJoueursTitulaires().indexOf(joueur.getNumero());

			if (index != -1) {
				System.out.println();
				formation.getJoueursTitulaires().remove(index);
				// Si le joueur est enleve dans la formation donc faut regenere la formation
				for (Joueur j : listeJoueurs)
					// On ajoute un joueur arbitrairement dans la formation
					if (!formation.getJoueursTitulaires().contains(j.getNumero())) {
						formation.getJoueursTitulaires().add(index, j.getNumero());
						// On regenrer les postes
						formation.genererPostes();
						break;
					}
			}

			// Si le joueur supprime est present dans la formation sauvegarder on supprime
			// la sauvegarde
			if (formationSauvegardee != null) {
				index = formationSauvegardee.getJoueursTitulaires().indexOf(joueur.getNumero());
				if (index != -1)
					formationSauvegardee = null;
			}

			return true;
		}
		return false;
	}

	/**
	 * Permet de reccuperer la liste de joueurs correspondant à leurs numeros
	 * 
	 * @param listeNumerosJoueurs : la liste des numero
	 * @return la liste de joueurs reccuperee
	 */
	public ArrayList<Joueur> reccupererListJoueurs(int[] listeNumerosJoueurs) {
		ArrayList<Joueur> joueurs = new ArrayList<>();

		for (int num : listeNumerosJoueurs) {
			Joueur joueur = getJoueur(num);
			if (joueur != null)
				joueurs.add(joueur);
		}

		return joueurs;
	}

	/**
	 * Permet de reccuperer la liste de joueurs correspondant à leurs numeros
	 * 
	 * @param listeNumerosJoueurs : la liste des numero
	 * @return la liste de joueurs reccuperee
	 */
	public ArrayList<Joueur> reccupererListJoueurs(ArrayList<Integer> listeNumerosJoueurs) {
		ArrayList<Joueur> joueurs = new ArrayList<>();

		for (int num : listeNumerosJoueurs) {
			Joueur joueur = getJoueur(num);
			if (joueur != null)
				joueurs.add(joueur);
		}

		return joueurs;
	}

	/**
	 * Permet de savoir si l'attaque du club courant contre un autre club est
	 * reussite
	 * 
	 * @param club : le club adversaire
	 * 
	 * @return true si l'attaque est reussite, false sinon
	 */
	public boolean attaque(Club club, String competition) throws NullPointerException, NonAutoriseException {
		double puissanceAttaquant = getPuissance();
		int typeFormation = formation.getType();

		// On reccupere la liste des attaquants
		ArrayList<Joueur> attaquants = reccupererListJoueurs(formation.getListeAttaquants());

		// On reccupere la liste des milieux
		ArrayList<Joueur> milieux = reccupererListJoueurs(formation.getListeMilieux());

		// On melange la liste des milieux
		Collections.shuffle(milieux);

		/*
		 * Si le type est OFFENSIF ou NORMAL on ajoute un certain nombre de milieux vers
		 * les attaquants
		 */
		if (typeFormation != Formation.DEFENSIF)
			for (int i = 0; i <= typeFormation; i++)
				attaquants.add(milieux.get(i));

		// On recalcule la force de tout le club avec les attaquants selectionnés
		for (Joueur joueur : attaquants)
			puissanceAttaquant += joueur.getPuissance();

		double puissanceDefendant = club.getPuissance();
		ArrayList<Joueur> defenseurs = club.defendre();

		// On recalcule la force de tout le club adversaire avec les defenseurs
		// selectionnés
		for (Joueur joueur : defenseurs)
			puissanceDefendant += joueur.getPuissance();

		// On normalise les puissance
		puissanceAttaquant = (Math.random() * puissanceAttaquant / 2. + puissanceAttaquant) / attaquants.size();
		puissanceDefendant /= defenseurs.size();

		/*
		 * Si la puissance d'attaque du club attaquant depasse celle de la defense du
		 * club defendant On ajoute les points pour les attaquants et on diminue pour
		 * les defenseurs Et on retourne true, l'attaque est reussite
		 */
		if (puissanceAttaquant > puissanceDefendant) {
			for (Joueur joueur : attaquants) {
				if (Math.random() < 0.6)
					joueur.setNombrePointsAttaque(joueur.getNombrePointsAttaque() + Math.random());
				Joueur def = defenseurs.get((int) (Math.random() * defenseurs.size()));
				def.setNombrePointsDefense(def.getNombrePointsDefense() - Math.random());
				joueur.ajouterPoints(competition, (int) (Math.random() * 2));
			}
			return true;
		}

		/*
		 * Sinon on ajoute les points pour les defenseurs, et on les diminuer pour les
		 * attaquants, et on retourne false l'attaque n'est pas reussite
		 */
		for (Joueur joueur : defenseurs) {
			if (Math.random() < 0.6)
				joueur.setNombrePointsDefense(joueur.getNombrePointsDefense() + Math.random());
			Joueur att = attaquants.get((int) (Math.random() * attaquants.size()));
			att.setNombrePointsAttaque(att.getNombrePointsAttaque() - Math.random());
			joueur.ajouterPoints(competition, (int) (Math.random() * 3));
		}

		return false;
	}

	/**
	 * Permet de former une liste de defenseurs pour defendre une attaque
	 * 
	 * @return la liste des joueurs qui vont defendre
	 */
	private ArrayList<Joueur> defendre() {

		int typeFormation = formation.getType();

		// On reccupere la liste des attaquants
		ArrayList<Joueur> defenseurs = reccupererListJoueurs(formation.getListeDefenseurs());

		// On reccupere la liste des milieux
		ArrayList<Joueur> milieux = reccupererListJoueurs((formation.getListeMilieux()));

		// On melange la liste
		Collections.shuffle(milieux);

		/*
		 * Si le type est DEFENSIF ou NORMAL on ajoute un certain nombre de milieux vers
		 * les defenseurs
		 */
		if (typeFormation != Formation.OFFENSIF)
			for (int i = 0; i < Math.max(1, typeFormation); i++)
				defenseurs.add(milieux.get(i));

		return defenseurs;
	}

	/**
	 * Permet de charger la formation sauvegardee
	 */
	public void chargerFormationSauvegardee() throws FormationNonValideException, NonAutoriseException {
		if (formationSauvegardee != null)
			formation = formationSauvegardee.clone();
	}

	/**
	 * Permet de sauvegarder la formation
	 */
	public void sauvegarderFormation() throws FormationNonValideException, NonAutoriseException {
		if (formation == null)
			throw new NonAutoriseException("Aucune formation n'existe pas");

		formationSauvegardee = formation.clone();
	}

	/**
	 * Permet de savoir si une formation est déja
	 * 
	 * @return true si on a deja un formation sauvegardee, false sinon
	 */
	public boolean estSauvegarderFormation() {
		return formationSauvegardee != null;
	}

	/**
	 * Permet de changer la formation du club
	 * 
	 * @param formation : la nouvelle formation
	 * @param type      : le nouveau type de la formation
	 * @throws FormationNonValideException : Si ya probleme dans le changement de la
	 *                                     formation
	 */
	private void changerFormation(String formation, int type) throws FormationNonValideException {

		ArrayList<Integer> numJoueurs;
		if (this.formation == null) {
			// On reccupere au max les numeros des 11 joueurs non blessés
			numJoueurs = new ArrayList<Integer>();
			for (Joueur joueur : listeJoueurs) {
				if (joueur.estBlesse())
					continue;
				numJoueurs.add(joueur.getNumero());
				if (numJoueurs.size() == 11)
					break;
			}
		} else
			numJoueurs = this.formation.getJoueursTitulaires();

		this.formation = new Formation(formation, type, numJoueurs);
		this.formation.changerFormation(formation, type);
	}

	/**
	 * Permet de changer la formation du club
	 * 
	 * @param formation : la nouvelle formation
	 */
	public void changerFormation(String formation) throws FormationNonValideException, NonAutoriseException {
		changerFormation(formation, Formation.NORMAL);
	}

	/**
	 * Permet de changer la formation du club
	 * 
	 * @param type : le nouveau type de la formation
	 */
	public void changerFormation(int type) throws FormationNonValideException, NonAutoriseException {
		String formation = Formation.DEFAULT_FORMATION;

		if (this.formation != null)
			formation = this.formation.getFormation();

		changerFormation(formation, type);
	}

	/**
	 * Permet de generer un numero unique pour un joueur
	 * 
	 * @param numero : le numero courant du joueur
	 * @return le numero genere pour le joueur (le numero courant si il est unique)
	 */
	public int genererNumero(int numero) {
		if (getJoueur(numero) == null)
			return numero;

		numero = 0;
		while (true) {
			if (getJoueur(++numero) == null)
				return numero;
		}
	}

	/**
	 * Permet de mettre à jour l'etat de tous les joueurs
	 */
	public void updateEtatJoueurs(String competition) throws NullPointerException, NonAutoriseException {
		Joueur gardien = getJoueur(formation.getGardien());
		for (Joueur joueur : listeJoueurs) {
			joueur.updateHumeur();
			joueur.diminuerBlesse();
			joueur.diminueMatchSuspendu(competition);
			if (joueur instanceof Gardien && gardien.equals(joueur))
				((Gardien) joueur).ajouterMatchJoue(competition);
		}
	}

	/**
	 * Permet de remplacer les joueurs du club
	 * 
	 * @param sortant : le numero du joueur sortant
	 * @param entrant : le numero du joueur entrant
	 * 
	 * @throws FormationNonValideException : Si le joueur sortant ne joue pas
	 * @throws NonAutoriseException        : Si le joueur entrant n'est pas autorisé
	 *                                     à joué
	 */
	public void remplacer(int sortant, int entrant) throws FormationNonValideException, NonAutoriseException {

		Joueur joueurSortant = getJoueur(sortant);
		Joueur joueurEntrant = getJoueur(entrant);

		if (joueurSortant == null || joueurEntrant == null)
			throw new NonAutoriseException("L'un des joueurs n'existe pas");

		// Si le joueur entrant est blessé
		if (joueurEntrant.estBlesse())
			throw new NonAutoriseException("Le joueur " + joueurEntrant.nom + " est blessé, il ne peut pas jouer");

		formation.remplacer(sortant, entrant);
	}

	/**
	 * Permet de mettre à zero un club pour la nouvelle saison
	 */
	public void clear() {
		for (Joueur joueur : listeJoueurs)
			joueur.clear();
	}

	@Override
	public String toString() {
		return String.format("%-25s", nom) + " : [" + experienceRequise + " Exp, "
				+ String.format("%.3f", getPuissance()).replace(',', '.') + " Puissance]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Club other = (Club) obj;
		if (budget != other.budget)
			return false;
		if (choisis != other.choisis)
			return false;
		if (experienceRequise != other.experienceRequise)
			return false;
		if (formation == null) {
			if (other.formation != null)
				return false;
		} else if (!formation.equals(other.formation))
			return false;
		if (listeJoueurs == null) {
			if (other.listeJoueurs != null)
				return false;
		} else if (!listeJoueurs.equals(other.listeJoueurs))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

	/**
	 * Permet de savoir si un club peur jouer un match dans une competition
	 * 
	 * @param competition : la competition dans lequelle le match sera joue
	 * @return true si il peut jouer, false sinon
	 */
	public boolean peutJouerLeMatch(String competition) {

		if (formation == null)
			return false;

		// On reccupere le nombre de joueur de la formation qui peuvent pas jouer
		int nombreJoueursExclusTitulaires = getNombreJoueursExclus(competition);

		int nombreJoueursExclusRemplacants = 0;

		// On reccupere le nombre de remplacant qui en peuvent pas jouer
		for (Joueur joueur : listeJoueurs) {
			if (formation.getJoueursTitulaires().contains(joueur.getNumero()))
				continue;

			nombreJoueursExclusRemplacants += (joueur.estBlesse() || joueur.getNombreMatchSuspendu(competition) != -1)
					? 1
					: 0;
		}

		// On calcule le nombre de joueurs qui peuvent jouer, il doit etre >= à 11
		return listeJoueurs.size() - nombreJoueursExclusRemplacants - nombreJoueursExclusTitulaires >= 11;
	}

	/**
	 * Permet de reccupere le nombre de joueurs titulaires blessés ou suspendus dans
	 * la competition
	 * 
	 * @param competition : la competition dans laquelle ou veut reccupere le nombre
	 * @return le nombre de joueurs non éligibles à jouer
	 */
	public int getNombreJoueursExclus(String competition) {
		int nombre = 0;

		for (int numJoueur : formation.getJoueursTitulaires()) {

			Joueur joueur = getJoueur(numJoueur);
			if (joueur.estBlesse() || joueur.getNombreMatchSuspendu(competition) != -1)
				nombre++;
		}
		return nombre;
	}

	@Override
	public Club clone() {
		Club club = new Club(nom, budget, experienceRequise);
		ArrayList<Joueur> joueurs = new ArrayList<>();

		for (Joueur joueur : listeJoueurs)
			joueurs.add(joueur.clone());

		club.choisis = choisis;
		club.listeJoueurs = joueurs;

		if (formation != null)
			club.formation = formation.clone();

		if (formationSauvegardee != null)
			club.formationSauvegardee = formationSauvegardee.clone();

		return club;
	}

	/**
	 * Retourne le joueur dont le numero est passé en argument
	 * 
	 * @param numero : le numero du joueur
	 * @return le joueur correspondant au numero, null si aucun joueur est trouve
	 */
	public Joueur getJoueur(int numero) {
		for (Joueur joueur : listeJoueurs) {
			if (joueur.getNumero() == numero)
				return joueur;
		}
		return null;
	}

	/**
	 * Permet de savoir si le club est choisis par l'utilisateur
	 * 
	 * @return true si le club est choisis, false sinon
	 */
	public boolean estChoisis() {
		return choisis;
	}

	/**
	 * Permet de modifier l'etat du club (choisis ou pas)
	 * 
	 * @param choisis : le nouveau état du club
	 */
	public void setChoix(boolean choisis) {
		this.choisis = choisis;
	}

	/**
	 * Permet de savoir le budget courrant du club
	 * 
	 * @return le budget du club
	 */
	public double getBudget() {
		return budget;
	}

	/**
	 * Permet d'augmenter le budget du club
	 * 
	 * @param aug : l'augmentation
	 */
	public void augmenterBudget(double aug) {
		if (aug < 0)
			return;
		budget += aug;
	}

	/**
	 * Permet de diminuer le budget du club
	 * 
	 * @param dim : la diminution
	 */
	public void diminuerBudget(double dim) {
		if (dim < 0)
			return;
		budget -= dim;
	}

	/**
	 * Permet de calculer la puissance du club
	 * 
	 * @return la puissance du club
	 */
	public double getPuissance() {
		double puissance = 0;

		// La somme des puissance de tous les joueurs du club
		for (Joueur joueur : listeJoueurs)
			puissance += joueur.getPuissance();

		// On normalise la somme
		return (listeJoueurs.size() == 0) ? 0 : puissance / listeJoueurs.size();
	}

	/**
	 * Accesseur à la formation du club
	 * 
	 * @return la formation du club
	 */
	public Formation getFormation() {
		return formation;
	}

	/**
	 * Accesseur à la liste des joueurs
	 * 
	 * @return la liste des joueurs du club
	 */
	public ArrayList<Joueur> getListeJoueurs() {
		return listeJoueurs;
	}
}