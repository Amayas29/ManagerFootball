package outils;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de repésenter l'exception que quelque chose n'est
 *         pas autorisé
 *         </p>
 */
public class NonAutoriseException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de l'exception
	 * 
	 * @param raison : la raison pour laquelle on a l'erreur
	 */
	public NonAutoriseException(String raison) {
		super(raison);
	}
}