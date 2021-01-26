package application;

import java.io.IOException;

import outils.Clavier;
import outils.FormationNonValideException;
import outils.NonAutoriseException;
import outils.Sauvegardable;

public class MainApp {

	public static void main(String[] args) {

		Jeu jeu = null;
		String pseudoPlayer = null;

		System.out.println("\n\t\tBienvenu manager ! \n");

		// Si le fichier de la sauvegarde n'existe pas, donc on demande le pseudo au
		// joueur
		if (!Sauvegardable.existe(Jeu.FILE_SAVE))
			pseudoPlayer = Clavier.saisirLigne(" - Donner votre pseudo : ");

		try {
			// On reccupere le jeu
			jeu = Jeu.getJeu();

			// Si il a pas de pseudo on lui affecte le pseudo
			if (pseudoPlayer != null)
				jeu.setPseudoPlayer(pseudoPlayer);

			// Tantque le joueur n'a pas choisis un club
			while (!jeu.estChoisis())
				jeu.choisirClub();

			// La boucle principale du jeu
			while (true) {
				// On lance la saison
				if (!jeu.lancer())
					break;

				// Si la saison est termine, et le joueur n'a pas quitter on change de saison
				jeu.changementSaison();
			}

		} catch (NullPointerException | NonAutoriseException | IOException | FormationNonValideException e) {
			// Si erreur on affiche l'erreur, et on sauvegarde le jeu
			System.out.println(e.getMessage());
			e.printStackTrace();
			try {
				Sauvegardable.save(Jeu.FILE_SAVE, jeu);
			} catch (IOException e1) {
			}
		}
		// On ferme le flux d'input
		Clavier.close();
	}
}