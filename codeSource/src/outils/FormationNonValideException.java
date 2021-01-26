package outils;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de repésenter l'exception qu'une formation est
 *         incorrecte/invalide
 *         </p>
 */
public class FormationNonValideException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de l'exception
	 * 
	 * @param raison : la raison pour laquelle la formation est incorrecte/invalide
	 */
	public FormationNonValideException(String raison) {
		super(raison);
	}
}