package personnes;

import java.util.HashMap;

import outils.NonAutoriseException;

/**
 * @author Hamid KOLLI
 * @author Amayas SADI
 * 
 *         <p>
 *         Classe qui gere les gardiens
 *         </p>
 */
public class Gardien extends Joueur {

	private static final long serialVersionUID = 6715956306877662701L;

	/**
	 * un dictionnaire qui gere les nombre de buts encaisses dans chaque competition
	 */
	private HashMap<String, Integer> nombreButEncaisses;

	/**
	 * un dictionnaire qui gere les nombres de matchs joues dans chaque competition
	 * Pour elire le meuilleur gardien de la saison (celui qui possede le meuilleur
	 * quotient nombre buts encaisses / nombre matchs joues)
	 */
	protected HashMap<String, Integer> nombreMatchsJouees;

	/**
	 * Constructeur de Gardien à 6 parametres
	 * 
	 * @param nom                 : le nom du gardien
	 * @param nombrePointsAttaque : nombre points d'attaque
	 * @param nombrePointsDefense : nombre points defense
	 * @param niveauGardien       : nombre points gardien
	 * @param caractere           : le caractere du gardien
	 * @param numero              : numero du gardien
	 */
	public Gardien(String nom, double nombrePointsAttaque, double nombrePointsDefense, double niveauGardien,
			double caractere, int numero) {

		super(nom, nombrePointsAttaque, nombrePointsDefense, niveauGardien, caractere, numero);

		nombreButEncaisses = new HashMap<>();
		nombreMatchsJouees = new HashMap<>();
	}

	/**
	 * Permet d'obtenir au nombre de buts encaisses durant une competition
	 * 
	 * @param competition : la competion ou on cherche le nombre de buts encaisses
	 * @return le nombre de but encaisses, -1 si le gardien ne participe pas a la
	 *         competition
	 */
	public int getNombreButEncaisses(String competition) throws NullPointerException {
		return get(nombreButEncaisses, competition);
	}

	/**
	 * Encaisser un but durant une competition
	 * 
	 * @param competition : la competition
	 */
	public void encaisserBut(String competition) throws NullPointerException, NonAutoriseException {
		add(nombreButEncaisses, competition);
	}

	/**
	 * on ajoute un match a la competition si le joueur a jouer ce match
	 * 
	 * @param competition : la competition ou le joueur joue
	 */
	public void ajouterMatchJoue(String competition) throws NullPointerException, NonAutoriseException {
		add(nombreMatchsJouees, competition);
	}

	/**
	 * Permet d'obtenir le nombre de matchs joues par le gardien durant une
	 * competition
	 * 
	 * @param competition : la competition dont on cherche le nombre de matchs
	 * @return le nombre de matchs joues par le gardien durant la competition passee
	 *         en parametre
	 */
	public int getNombreMatchsJoues(String competition) throws NullPointerException {
		return get(nombreMatchsJouees, competition);

	}

	@Override
	public double getPuissance() {
		// On favorise le niveau du gardien
		return (4 * niveauGardien + 2 * nombrePointsDefense + nombrePointsAttaque) / 7.;
	}

	@Override
	public void clear() {
		super.clear();
		nombreMatchsJouees.clear();
		nombreButEncaisses.clear();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gardien other = (Gardien) obj;
		if (nombreButEncaisses == null) {
			if (other.nombreButEncaisses != null)
				return false;
		} else if (!nombreButEncaisses.equals(other.nombreButEncaisses))
			return false;
		if (nombreMatchsJouees == null) {
			if (other.nombreMatchsJouees != null)
				return false;
		} else if (!nombreMatchsJouees.equals(other.nombreMatchsJouees))
			return false;
		return true;
	}

	@Override
	public Joueur clone() {
		Gardien gardien = new Gardien(nom, nombrePointsAttaque, nombrePointsDefense, niveauGardien, caractere, numero);

		gardien.nombreButs = new HashMap<>(nombreButs);
		gardien.nombreMatchsSuspondus = new HashMap<>(nombreMatchsSuspondus);
		gardien.nombrePoints = new HashMap<>(nombrePoints);
		gardien.nombreButEncaisses = new HashMap<>(nombreButEncaisses);
		gardien.nombreMatchsJouees = new HashMap<>(nombreMatchsJouees);

		return gardien;
	}
}