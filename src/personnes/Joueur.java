package personnes;

import java.util.HashMap;

import outils.NonAutoriseException;

/**
 * @author Hamid KOLLI
 * @author Amayas SADI
 * 
 *         <p>
 *         Classe qui gere les joueurs, elle hérite de personne
 *         </p>
 */
public abstract class Joueur extends Personne {

	private static final long serialVersionUID = -6972681644196956915L;

	/*
	 * Le max des competences d'un joueur
	 */
	public static final double MAX_STATE = 100;

	/**
	 * un dictionnaire qui gere les nombres de buts de chaque competition
	 */
	protected HashMap<String, Integer> nombreButs;

	/**
	 * un dictionnaire qui gere les nombres de points de chaque competition
	 */
	protected HashMap<String, Integer> nombrePoints;

	/**
	 * un dictionnaire qui gere les nombres de matchs suspondus de chaque
	 * competition
	 */
	protected HashMap<String, Integer> nombreMatchsSuspondus;

	/**
	 * la capacite du joueur en attaque
	 */
	protected double nombrePointsAttaque;

	/**
	 * la capacite du joueur en defense
	 */
	protected double nombrePointsDefense;

	/**
	 * la capacite du joueur en gardien
	 */
	protected double niveauGardien;

	/**
	 * le nombre de jours dont le joueur a besoin pour se remettre de sa blessure, 0
	 * si il n'est pas blesse
	 */
	protected int nombreAbsencesBlessure;

	/**
	 * le numero du joueur
	 */
	protected int numero;

	/**
	 * Constructeur pour cree un joueur
	 * 
	 * @param nom                 : le nom du joueur
	 * @param nombrePointsAttaque : nombre points d'attaque
	 * @param nombrePointsDefense : nombre points defense
	 * @param niveauGardien       : nombre points gardien
	 * @param caractere           : le caractere du joueur
	 * @param numero              : le numero du joueur
	 */
	public Joueur(String nom, double nombrePointsAttaque, double nombrePointsDefense, double niveauGardien,
			double caractere, int numero) {

		super(nom, caractere);

		this.nombrePointsAttaque = (nombrePointsAttaque > 0) ? nombrePointsAttaque : 0;

		this.nombrePointsDefense = (nombrePointsDefense > 0) ? nombrePointsDefense : 0;

		this.niveauGardien = (niveauGardien > 0) ? niveauGardien : 0;

		this.numero = (numero >= 0) ? numero : (int) (Math.random() * 1000);

		nombreButs = new HashMap<>();

		nombreMatchsSuspondus = new HashMap<>();

		nombrePoints = new HashMap<>();
	}

	/**
	 * on ajoute un but a la competition si le joueur le marque
	 * 
	 * @param competition : la competition ou le joueur a marquer le but
	 */
	public void marquerBut(String competition) throws NullPointerException, NonAutoriseException {
		add(nombreButs, competition);
	}

	/**
	 * return le nombre de buts marques dans une competition par le joueur
	 * 
	 * @param competition : la competition dont on cherche le nombre de buts
	 * @return le nombre de buts de la competition passee en parametre
	 */
	public int getNombreButsMarques(String competition) throws NullPointerException {
		return get(nombreButs, competition);
	}

	/**
	 * on ajoute des points au joueur dans la competition si le joueur a fait
	 * quelque chose remarquable durant le match
	 * 
	 * @param competition : la competition ou on ajoute des points au joueur
	 * @param points      : les points a ajouter
	 */
	public void ajouterPoints(String competition, int points) throws NonAutoriseException, NullPointerException {
		add(nombrePoints, competition, points);
	}

	/**
	 * return le nombre de points du joueur dans une competition
	 * 
	 * @param competition : la competition dont on cherche le nombre de points
	 * @return le nombre de points du joueur durant la competition passee en
	 *         parametre
	 */
	public int getPoints(String competition) throws NullPointerException {
		return get(nombrePoints, competition);
	}

	/**
	 * Permet de supprimer des points pour le joueur dans la competition
	 * 
	 * @param competition : La competition ou on veut supprimer les points
	 * @param points      : le nombre de points à supprimer
	 */
	public void supprimerPoints(String competition, int points) throws NullPointerException, NonAutoriseException {
		remove(nombrePoints, competition, points);
	}

	/**
	 * on ajoute les matchs de suspension a un joueur durant une competition
	 * 
	 * @param competition          : la competition ou le joueur est suspendu
	 * @param nombreMatchsSuspendu : le nombre de match de suspension
	 */
	public void ajouterMatchSuspendu(String competition, int nombreMatchsSuspendu)
			throws NonAutoriseException, NullPointerException {
		add(nombreMatchsSuspondus, competition, nombreMatchsSuspendu);
	}

	/**
	 * return le nombre de matchs de suspension d'un joueur dans une competition
	 * 
	 * @param competition : la competition dont on cherche le nombre de matchs de
	 *                    suspension du joueur
	 * @return le nombre de matchs de suspension
	 */
	public int getNombreMatchSuspendu(String competition) throws NullPointerException {
		return get(nombreMatchsSuspondus, competition);
	}

	/**
	 * diminuer un jour du nombre de match de suspension
	 * 
	 * @param competition: la competition ou le joueur est suspendun
	 */
	public void diminueMatchSuspendu(String competition) throws NullPointerException, NonAutoriseException {
		remove(nombreMatchsSuspondus, competition);
	}

	/**
	 * diminuer un jour du nombre de jours de blessure
	 */
	public void diminuerBlesse() {
		nombreAbsencesBlessure = (nombreAbsencesBlessure <= 0) ? 0 : nombreAbsencesBlessure--;
	}

	/**
	 * methode qui calcule la puissance d'un joueur
	 * 
	 * @return la puissance
	 */
	public abstract double getPuissance();

	@Override
	public abstract Joueur clone(); // Pour avoir le polymorphisme (joueur.clone())

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Joueur other = (Joueur) obj;
		if (nombreAbsencesBlessure != other.nombreAbsencesBlessure)
			return false;
		if (niveauGardien != other.niveauGardien)
			return false;
		if (nombreButs == null) {
			if (other.nombreButs != null)
				return false;
		} else if (!nombreButs.equals(other.nombreButs))
			return false;
		if (nombreMatchsSuspondus == null) {
			if (other.nombreMatchsSuspondus != null)
				return false;
		} else if (!nombreMatchsSuspondus.equals(other.nombreMatchsSuspondus))
			return false;
		if (nombrePoints == null) {
			if (other.nombrePoints != null)
				return false;
		} else if (!nombrePoints.equals(other.nombrePoints))
			return false;
		if (nombrePointsAttaque != other.nombrePointsAttaque)
			return false;
		if (nombrePointsDefense != other.nombrePointsDefense)
			return false;
		if (numero != other.numero)
			return false;
		return true;
	}

	/**
	 * Savoir si le joueur est blessé ou pas
	 * 
	 * @return true si le nombre de jours de blessure different de 0
	 */
	public boolean estBlesse() {
		return nombreAbsencesBlessure != 0;
	}

	/**
	 * mets le nombre de joueur de blessure a jour
	 * 
	 * @param nombreJours : le nombre de jour que le joueur doit se reposer
	 */
	public void seBlesse(int nombreJours) {
		nombreAbsencesBlessure = (nombreJours > 0) ? nombreJours : 0;
	}

	/**
	 * Accesseur au nombres de points d'attaque
	 * 
	 * @return les points d'attaque
	 */
	public double getNombrePointsAttaque() {
		return nombrePointsAttaque;
	}

	/**
	 * permet de modifier le nombre de points d'attaque
	 * 
	 * @param nombrePointsAttaque : le nouveau nombre de points d'attaque
	 */
	public void setNombrePointsAttaque(double nombrePointsAttaque) {
		this.nombrePointsAttaque = (nombrePointsAttaque > 0)
				? ((nombrePointsAttaque > MAX_STATE) ? MAX_STATE : nombrePointsAttaque)
				: 0;
	}

	/**
	 * Accesseur au nombre de points de defense
	 * 
	 * @return les points de defense
	 */
	public double getNombrePointsDefense() {
		return nombrePointsDefense;
	}

	/**
	 * permet de modifier le nombre de points de defense
	 * 
	 * @param nombrePointsDefense : le nouveau nombre points de defense
	 */
	public void setNombrePointsDefense(double nombrePointsDefense) {
		this.nombrePointsDefense = (nombrePointsDefense > 0)
				? ((nombrePointsDefense > MAX_STATE) ? MAX_STATE : nombrePointsDefense)
				: 0;
	}

	/**
	 * Accesseur au niveau gardien
	 * 
	 * @return le niveau gardien
	 */
	public double getNiveauGardien() {
		return niveauGardien;
	}

	/**
	 * permet de modifier le niveau gardien
	 * 
	 * @param niveauGardien : le nouveau niveau gardien
	 */
	public void setNiveauGardien(double niveauGardien) {
		this.niveauGardien = (niveauGardien > 0) ? ((niveauGardien > MAX_STATE) ? MAX_STATE : niveauGardien) : 0;
	}

	/**
	 * Accesseur au numero du joueur
	 * 
	 * @return le numero du joueur
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * Permet de modifier le numero du joueur
	 * 
	 * @param numero : le nouveau numero du joueur
	 */
	public void setNumero(int numero) {
		this.numero = (numero >= 0) ? numero : (int) (Math.random() * 1000);
	}

	@Override
	public String toString() {
		return String.format("%-9s", getClass().getSimpleName()) + " : " + numero + " " + nom;
	}

	/**
	 * Permet de reccupere l'etat du joueur (sa representation + etat de sante +
	 * etat de suspension)
	 * 
	 * @param competition : La competition dans laquelle on veut reccuperer l'etat
	 * @return : La representation
	 */
	public String getEtat(String competition) {
		// On reccupere le nombre de matchs suspendus
		int nombreMatch = getNombreMatchSuspendu(competition);

		// Les competences du joueurs
		String stats = String.format("%.3f", nombrePointsAttaque).replace(',', '.') + "\t"
				+ String.format("%.3f", nombrePointsDefense).replace(',', '.') + "\t"
				+ String.format("%.3f", niveauGardien).replace(',', '.');

		// Le nom + les competence + les blessures + les matchs suspendus
		return String.format("%-35s", toString()) + " " + stats + "  "
				+ (nombreAbsencesBlessure == 0 ? String.format("%-10s", " ") : String.format("%-10s", nombreAbsencesBlessure + " jours"))
				+ (nombreMatch == -1 ? "" : nombreMatch + " matchs\t");
	}

	/**
	 * Permet de mettre à zéro un joueur (nouvelle saison)
	 */
	public void clear() {
		nombreAbsencesBlessure = 0;
		nombreButs.clear();
		nombrePoints.clear();
		// Les suspensions ne sont pas mise à zéro car elles affectent la nouvelle
		// saison
	}

	/**
	 * permet d'ajouter les valeur dans un dictionnaire
	 * 
	 * @param hach        : le dictionnaire
	 * @param competition : la cle
	 * @param nombres     : les valeurs
	 * @throws NonAutoriseException : si le nombre est negatif
	 * @throws NullPointerException : si la competition est null
	 */
	protected static void add(HashMap<String, Integer> hach, String competition, int... nombres)
			throws NonAutoriseException, NullPointerException {

		if (competition == null)
			throw new NullPointerException("la competition n'exite pas");

		// On initilaise nombre à 1 au cas ou nombres serait vide, dans ce cas on ajoute
		// que de 1
		int nombre = 1;
		// Sinon on reccupere le nombre à ajouter
		if (nombres != null && nombres.length >= 1) {
			// Si le nombre n'est pas positif
			if (nombres[0] < 0)
				throw new NonAutoriseException("le nombre n'est pas valide");
			// On reccupere le nombre
			nombre = nombres[0];
		}

		// Si la competition n'existe pas on l'ajoute avec le nombre
		if (!hach.containsKey(competition)) {
			hach.put(competition, nombre);
			return;
		}

		// Sinon on incremente la valeur de la competition
		hach.replace(competition, hach.get(competition) + nombre);
	}

	/**
	 * permet de retourner une valeur d'une cle dans un dictionnaire
	 * 
	 * @param hach        : le dictionnaire
	 * @param competition : la cle
	 * @return la valeur d'une cle si elle existe ou -1
	 * @throws NullPointerException : si la competition est null
	 */
	protected static int get(HashMap<String, Integer> hach, String competition) throws NullPointerException {

		if (competition == null)
			throw new NullPointerException("la competition n'exite pas");

		// Si la competition n'existe pas, on retourne -1
		if (!hach.containsKey(competition))
			return -1;

		// Sinon on retourne la valeur associée à la competition
		return hach.get(competition);
	}

	/**
	 * Permet de diminuer la valeur d'une cle dans un dictionnaire
	 * 
	 * @param hach        : Le dictionnaire
	 * @param competition : La cle
	 * @param nombres      : La diminution
	 * @throws NullPointerException : Si la competition n'existe pas (null)
	 * @throws NonAutoriseException
	 */
	private static void remove(HashMap<String, Integer> hach, String competition, int... nombres)
			throws NullPointerException, NonAutoriseException {

		if (competition == null)
			throw new NullPointerException("la competition n'exite pas");

		// Si la competition n'existe pas dans le HashMap, on fait rien
		if (!hach.containsKey(competition))
			return;

		// On initilaise nombre à 1 au cas ou nombres serait vide, dans ce cas on
		// diminue que de 1
		int nombre = 1;
		// Sinon on reccupere le nombre à diminuer
		if (nombres != null && nombres.length >= 1) {
			// Si le nombre n'est pas positif
			if (nombres[0] < 0)
				throw new NonAutoriseException("le nombre n'est pas valide");
			// On reccupere le nombre
			nombre = nombres[0];
		}

		// Sinon on reccupere la valeur de la cle
		int getNombre = hach.get(competition);

		// On calcule le nouvelle valeur
		hach.replace(competition, getNombre - nombre);

		// Si le nombre egale (<=) à 0, on supprime la competition du hashMap
		if (getNombre - nombre <= 0)
			hach.remove(competition);
	}
}