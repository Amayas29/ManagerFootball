package outils;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de repésenter l'exception qu'un transfert est
 *         impossible à effectué
 *         </p>
 */
public class TransfertImpossibleException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de l'exception
	 * 
	 * @param raison : la raison pour laquelle le transfert est impossible à
	 *               effectué
	 */
	public TransfertImpossibleException(String raison) {
		super(raison);
	}
}