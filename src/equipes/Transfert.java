package equipes;

import outils.FormationNonValideException;
import outils.NonAutoriseException;
import outils.TransfertImpossibleException;
import personnes.Joueur;

/**
 * 
 * @author SADI Amayas
 * @author KOLLI Hamid
 *
 *         <p>
 *         Cette classe permet de repésenter un transfert de joueur entre deux
 *         clubs
 *         </p>
 */
public final class Transfert {

	// Interdire l'instanciation
	private Transfert() {
	}

	/**
	 * Permet de faire le transfert du joueur depuis le club source vers le club
	 * destination
	 * 
	 * @param joueur:    le joueur à transferer
	 * @param clubSrc    : le club source
	 * @param clubDest   : le club destination
	 * @param prixJoueur : le prix du joueur
	 * 
	 * @throws TransfertImpossibleException : Si on a un probleme dans le transfert
	 * @throws NonAutoriseException         : Si on a des problemes d'autorisation
	 * @throws FormationNonValideException  : Si on a un probleme dans les
	 *                                      formations
	 * @throws NullPointerException         : Si un des elements n'existe pas
	 */
	public static void transferer(Joueur joueur, Club clubSrc, Club clubDest, double prixJoueur)
			throws TransfertImpossibleException, NonAutoriseException, FormationNonValideException,
			NullPointerException {

		if (clubSrc == null || clubDest == null)
			throw new NullPointerException("L'un des clubs n'existe pas");

		if (joueur == null)
			throw new NullPointerException("Le joueur n'existe pas");

		if (clubDest.equals(clubSrc))
			throw new NonAutoriseException("Les clubs sont les mêmes");

		if (!clubSrc.getListeJoueurs().contains(joueur))
			throw new NonAutoriseException("Le joueur n'existe pas dans le club source");

		// Si le budget du club destination n'est pas suffisant pour acheter le joueur
		if (clubDest.getBudget() < prixJoueur)
			throw new TransfertImpossibleException("Le budget ne permet pas d'acheter le joueur");

		// On supprime le joueur dans le club source
		if (!clubSrc.supprimerJoueur(joueur))
			throw new TransfertImpossibleException("Le joueur n'existe pas");

		// On lui genere un nombre
		joueur.setNumero(clubDest.genererNumero(joueur.getNumero()));

		// On l'ajoute au club destination
		try {
			clubDest.ajouterJoueur(joueur);

		} catch (NullPointerException | NonAutoriseException e) {
			// Si l'ajout est impossible on le remets dans son club source
			try {
				clubSrc.ajouterJoueur(joueur);
			} catch (NullPointerException | NonAutoriseException e1) {
				throw new TransfertImpossibleException(e1.getMessage());
			}

			// On lance l'exception que le transfert est impossible avec la raison
			// correspondante
			throw new TransfertImpossibleException(e.getMessage());
		}

		// On met à jour les budgets
		clubDest.diminuerBudget(prixJoueur);
		clubSrc.augmenterBudget(prixJoueur);
	}
}