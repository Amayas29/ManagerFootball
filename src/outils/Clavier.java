package outils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe fournie
 * 
 * <p>
 * Cette classe implante des saisies au clavier par lecture d'une ligne.
 * </p>
 */
public class Clavier {

	private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	private Clavier() {
	}

	/**
	 * Affiche le message et retourne un int lu au clavier.
	 */
	public static int saisirEntier(String mess) {
		while (true) {
			try {
				return Integer.parseInt(saisirLigne(mess));
			} catch (NumberFormatException e) {
				mess = "Recommencez : \n-: ";
			}
		}
	}

	/**
	 * Affiche le message et retourne un double lu au clavier. <br>
	 * Accepte une virgule comme separateur entre parties entiere et decimales.
	 */
	public static double saisirDouble(String mess) {
		while (true) {
			try {
				return Double.valueOf(saisirLigne(mess).replace(',', '.')).doubleValue();
			} catch (NumberFormatException e) {
				mess = "Recommencez : \n-: ";
			}
		}
	}

	/**
	 * Affiche le message et retourne une ligne lue au clavier.
	 */
	public static String saisirLigne(String mess) {
		System.out.print(mess);
		try {
			return in.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Permet d'afficher un menu et de demander à l'utilisateur de choisir
	 * 
	 * @param message : le menu
	 * @param max     : le nombre d'options
	 * @return le choix de l'utilisateur
	 */
	public static int getChoix(String message, int max) {
		int choix;
		System.out.println("\nVeuillez choisir " + message + "\n");
		while (true) {
			choix = Clavier.saisirEntier("-: ");

			if (choix >= 0 && choix < max)
				return choix;

			System.out.println("Recommencez : ");
		}
	}

	/**
	 * Ferme le stream
	 */
	public static void close() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}