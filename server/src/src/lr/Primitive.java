package lr;

/**
 * Classe abstraite commune à tous les primitives géométriques pouvant
 * apparaître dans une scène. Cette classe ne contient aucune information
 * concernant la géométrie de l'objet. Par contre elle contient les informations
 * permettant de connaître les propriétés du matériau dont l'objet est constitué
 * (voir la classe Materiau).
 */
public abstract class Primitive {

    // attributs communs aux primitives
    private Materiau mat;
    /**
     * représentation d'un seuil pour les approximations numériques
     */
    protected static float EPSILON = 1E-4f;

    // Constructeurs
    Primitive() {
        mat = new Materiau();
    }// Primitive()

    /**
     * Crée une primitive. Celle-ci est caractérisée uniquement par ses
     * caractéristiques photométriques, c'est à dire les différents coefficients de
     * réflexion utilisé dans le modèle de Phong.
     * 
     * @param ambiant    un tableau contenant les coefficients de réflexion ambiante
     *                   du matériau de la primitive
     * @param diffus     un tableau contenant les coefficients de réflexion diffuse
     *                   du matériau de la primitive
     * @param speculaire un tableau contenant les coefficients de réflexion
     *                   spéculaire du matériau de la primitive
     * @param brillance  le coefficient de brillance de l'objet. Cette valeur est
     *                   supérieure à 0.
     */
    public Primitive(float[] ambiant, float[] diffus, float[] speculaire, float brillance) {
        mat = new Materiau(ambiant, diffus, speculaire, brillance);
    }

    // méthodes abstraites communes aux primitives
    /**
     * Calcul de l'intersection entre un rayon et la primitive
     * 
     * @param r le rayon avec lequel calculer l'intersection
     * @return l'intersection trouvée ou null si il n'existe aucune intersection
     */
    public abstract Intersection intersecte(Rayon r);

    /**
     * Permet de déterminer si un rayon coupe la primitive. La différence par
     * rapport à la méthode intersecte réside dans le fait que cette méthode ne
     * calcule pas le point d'intersection éventuel, ce qui permet un gain de temps.
     * Cette méthode est utile pour calculer l'intensité lumineuses transmise par un
     * rayon d'ombrage.
     * 
     * @param r    le rayon avec lequel tester l'intersection
     * @param tmax la distance paramétrique maximale depuis l'origine du rayon au
     *             delà de laquelle la primitive n'est pas supposée occultante
     * @return true si le rayon coupe la primitive à une distance positive
     *         inférieure à tmax, false sinon
     */
    public abstract boolean coupe(Rayon r, float tmax);

    // abstract Rayon transmis(Point i, Vecteur incident);

    /**
     * Permet de calculer la normale à la surface de la primitive au point
     * d'intersection fournit en paramètre.
     * 
     * @param i un point situé sur la primitive
     * @return un vecteur représentant la normale à la primitive au point i
     */
    public abstract Vecteur getNormale(Point i);

    // méthodes communes à toutes les primitives

    /**
     * Calcule l'éclairage de la source au point d'intersection i situé sur la
     * primitive courante. La formule de calcul utilisée est la formule de Phong,
     * prenant en compte l'éclairage ambiant, diffus et spéculaire.
     * 
     * @param intersection   le point d'intersection situé sur la primitive courant
     * @param source   la source dont on souhaite calculer la contribution
     * @param observateur la position de l'observateur
     * @return l'intensité calculée
     */
    public Intensite computeSourceContribution(Point intersection, Source source, Point observateur) {

        Intensite light = source.getIntensity();

        // calcul de la contribution ambiante
        float[] coeffAmbiant = mat.getAmbient();
        float r = coeffAmbiant[0] * light.getRed();
        float v = coeffAmbiant[1] * light.getGreen();
        float b = coeffAmbiant[2] * light.getBlue();

        // calcul de la contribution diffuse
        float[] coeffDiffus = mat.getDiffuse();
        Vecteur normale = this.getNormale(intersection);
        Vecteur versSource = new Vecteur(intersection, source.getPosition());
        versSource.normalise();
        float ps = normale.produitScalaire(versSource);
        r += coeffDiffus[0] * ps * light.getRed();
        v += coeffDiffus[1] * ps * light.getGreen();
        b += coeffDiffus[2] * ps * light.getBlue();

        // calcul de la contribution spéculaire
        Vecteur versObservateur = new Vecteur(intersection, observateur);
        versObservateur.normalise();
        Vecteur ref = (this.reflechi(intersection, versSource)).direction();
        ref.normalise();
        ps = ref.produitScalaire(versObservateur);

        if ((ps > 0.0f)) {// test prenant en compte les incertitudes sur le calcul
            ps = (float) Math.pow(ps, mat.getCoeffSpec());

            float[] coeffSpec = mat.getSpecular();
            r += coeffSpec[0] * ps * light.getRed();
            v += coeffSpec[1] * ps * light.getGreen();
            b += coeffSpec[2] * ps * light.getBlue();
        }

        // création du résultat
        return new Intensite(r, v, b);

    }

    // méthodes communes à toutes les primitives

    /**
     * Calcule l'éclairage ambiant du à la source au point d'intersection i situé
     * sur la primitive courante. Seul l'éclairage ambiant apparaissant dans la
     * forumle de Phong est pris en compte.
     * 
     * @param s   la source dont on souhaite calculer la contribution
     * @return l'intensité calculée
     */
    public Intensite computeSourceAmbientContribution(Source s) {

        Intensite light = s.getIntensity();

        // calcul de la contribution ambiante
        float[] coeffAmbiant = mat.getAmbient();
        float r = coeffAmbiant[0] * light.getRed();
        float v = coeffAmbiant[1] * light.getGreen();
        float b = coeffAmbiant[2] * light.getBlue();

        // création du résultat
        return new Intensite(r, v, b);
    }

    /**
     * Calcule le rayon réflechi au point d'intersection. Le rayon réflechi est
     * obtenu en considérant une réflexion spéculaire pure.
     * 
     * @param i        le point d'intersection situé sur la primitive courant
     * @param incident le vecteur correspondant à la direction d'incidence du rayon
     *                 qui est à l'origine de l'intersection.
     * @return le rayon réfléchi
     */
    public Rayon reflechi(Point i, Vecteur incident) {
        Vecteur r = this.getNormale(i);
        float ps = r.produitScalaire(incident);
        r.multiply(2.0f * ps);
        r.sub(incident);

        return new Rayon(i, r);
    }

    /**
     * Détermine si l'objet est composé d'un matériau comportant une composante
     * spéculaire.
     * 
     * @return true si le matériau de l'objet comporte une composante spéculaire,
     *         false sinon
     */
    public boolean isSpecular() {
        return mat.isSpecular();
    }

    public String fullToString() {
        return this.toString() + "\n" + mat;
    }

}// Primitive
