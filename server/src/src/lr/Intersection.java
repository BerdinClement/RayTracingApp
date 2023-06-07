package lr;

import java.util.Iterator;

/** classe qui représente une intersection */

public class Intersection {

    // attributs
    private final Primitive objet; // référence à l'objet intersecté
    private float t; // "distance" paramétrique depuis l'origine du rayon

    public final Point point; // point d'intersection lui-même

    /**
     * crée une intersection. L'objet correspondant contient les coordonnées du
     * point d'intersection, l'objet sur lequel se trouve ce point et la distance
     * paramétrique à laquelle se trouve ce point par rapport à l'origine du rayon
     * qui l'a généré.
     * 
     * @param x     l'abscisse du point d'intersection
     * @param y     l'ordonnée du point d'intersection
     * @param z     la hauteur du point d'intersection
     * @param objet l'objet intersecté
     * @param t     la distance paramétrique de cet objet par rapport à l'origine du
     *              rayon
     */
    public Intersection(float x, float y, float z, Primitive objet, float t) {
        this.point = new Point(x, y, z);
        this.objet = objet;
        this.t = t;
    }

    /**
     * compare deux intersections. Le test de comparaison d'effectue sur la distance
     * paramétrique des deux intersections comparées.
     * 
     * @param i l'intersection avec laquelle l'objet doit être comparé
     * @return 0 si les deux intersections sont à même distance de l'origine duu
     *         rayon ; un nombre positif si l'intersection i est plus proche de
     *         l'origine du rayon que l'objet courant ; un nombre négatif sinon.
     */
    public int compareTo(Intersection i) {
        if (i == null)
            return -1;
        if (t < i.t)
            return -1;
        if (t > i.t)
            return 1;
        return 0;
    }

    /**
     * fournit l'objet sur lequel se trouve l'intersection courante.
     * 
     * @return l'objet intersecté
     */
    public Primitive getObjet() {
        return objet;
    }

    /**
     * fournit la distance paramétrique à laquelle se trouve l'objet intersecté par
     * le rayon.
     * 
     * @return la distance paramétrique
     */
    public float getDistance() {
        return t;
    }

    /**
     * Calcule l'éclairage du point d'intersection courant par la méthode du lancer
     * de rayons. Seuls l'éclairage direct (rayons d'ombrage) et l'éclairage par
     * réflexions sont pris en compte dans cette fonction. Le paramètre niveau
     * permet de régler la profondeur de l'arbre des rayons.
     * 
     * @param scene      la scène utilisée
     * @param obs    la position de l'observateur
     * @param niveau le nombre maximum de niveaux de réflexion qui doit être pris en
     *               compte
     * @param px     l'abscisse du pixel en cours de calcul
     * @param py     l'ordonnée du pixel en cours de calcul
     * @return l'éclairage calculé
     */
    public Intensite eclairer(Scene scene, Point obs, int niveau, int px, int py) {
        Intensite i = new Intensite(0.0f, 0.0f, 0.0f);

        // lancer les rayons d'ombrage
        Iterator<Source> it = scene.sourcesIterator();
        while (it.hasNext()) {
            Source source = it.next();

            if (!scene.coupe(this.point, source.getPosition())) {
                i.add(objet.computeSourceContribution(this.point, source, obs));
            } else {
                i.add(objet.computeSourceAmbientContribution(source));
            }
        }

        // lancer l'éclairage par réflexion et / ou par réfraction
        if (objet.isSpecular() && (niveau > 0)) {

            // éclairage par réflexion
            Vecteur incident = new Vecteur(this.point, obs);
            Rayon ref = objet.reflechi(this.point, incident);
            Intersection intRef = scene.intersecte(ref);

            if (intRef != null) {

                i.add(intRef.eclairer(scene, this.point, niveau - 1, px, py));

            } // else rajouter la couleur de fond

        }
        return i;

    } // eclairer

    public String toString() {
        return ("intersection en " + super.toString() + " avec " + objet);
    }

}// Intersection
