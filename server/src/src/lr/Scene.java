package lr;

import java.util.*;

/**
 * 
 * Représentation de la scène à partir :
 * 
 * - d'une liste de primitives - d'une liste de sources limineuses
 * 
 */

public class Scene {

	private static float EPSILON = 1E-3f;
	private final HashSet<Primitive> objets;
	private final HashSet<Source> sources;

	/**
	 * Construit une scène vide
	 *
	 */
	public Scene(){
		objets = new HashSet<>();
		sources = new HashSet<>();
	}

	/**
	 * Ajoute un objet à la scène.
	 * @param objet l'objet à ajouter
	 */
	public void ajouter(Primitive objet) {
		this.objets.add(objet);
	}

	/**
	 * Ajoute une source à la scène.
	 * @param source la source à ajouter
	 */
	public void ajouter(Source source) {
		this.sources.add(source);
	}

	/**
	 * fournit un iterateur sur la liste des objets composant la scène
	 * 
	 * @return l'itérateur
	 */
	public Iterator<Primitive> objetsIterator() {
		return objets.iterator();
	}

	/**
	 * fournit un iterateur sur la liste des sources éclairant la scène
	 * 
	 * @return l'itérateur
	 */
	public Iterator<Source> sourcesIterator() {
		return sources.iterator();
	}

	/**
	 * Calcule l'intersection entre un rayon et tous les objets composant la scène
	 * 
	 * @param r le rayon qui traverse la scène
	 * @return retourne une intersection ou null si aucune intersection n'a été
	 *         trouvée
	 */
	public Intersection intersecte(Rayon r) {
		Intersection p, pproche;

		pproche = null;
		for (Primitive objet : objets) {
			p = objet.intersecte(r);
			if (p != null) {// conserver l'intersection la plus proche
				if (p.compareTo(pproche) < 0) {
					pproche = p;
				}
			}
		}
		return pproche;

	}// intersecte

	/**
	 * détermine si un objet coupe un segment de droite 3D issu de P1 et s'arrêtant
	 * en P2.
	 * 
	 * @param p1 le point de départ du segment
	 * @param p2 le point d'arrivée du segment
	 * @return true si un objet coupe le segment [p1,p2], false sinon
	 */
	boolean coupe(Point p1, Point p2) {
		Rayon r = new Rayon(p1, p2);

		for (Primitive objet : objets) {
			if (objet.coupe(r, 1.0f))
				return true;
		}
		return false;
	}

	/**
	 * affiche la liste des objets et des sources présents dans la scène
	 */
	public void display() {

		// affichage des sources
		System.out.println("-- liste des sources presentes dans la scene --");

		for (Source s : sources) {
			System.out.println(s);
		}

		// affichage des objets
		System.out.println("-- liste des objets presents dans la scene --");

		for (Primitive p : objets) {
			System.out.println(p.fullToString());
		}
	}
}// Scene
