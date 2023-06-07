package lr;

/**
 * Classe qui représente un rayon. Un rayon est défini par son origine (un
 * point) et sa direction (un vecteur).
 */

public class Rayon {

    // attributs
    private float x, y, z; /* coordonnées de l'origine du rayon */
    private float dx, dy, dz; /* direction du rayon */

    /**
     * crée un rayon d'origine (0,0,0) et de direction (0,0,-1)
     */
    public Rayon() {
        x = y = z = 0.0f;
        dx = 0.0f;
        dy = 0.0f;
        dz = -1.0f;
    }

    /**
     * crée un rayon d'origine (x,y,z) et de direction (dx,dy,dz)
     * 
     * @param x  l'abscisse de l'origine du rayon
     * @param y  l'ordonnée de l'origine du rayon
     * @param z  la hauteur de l'origine du rayon
     * @param dx abscisse du vecteur direction du rayon
     * @param dy ordonnée du vecteur direction du rayon
     * @param dz hauteur du vecteur direction du rayon
     */
    public Rayon(float x, float y, float z, float dx, float dy, float dz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    /**
     * crée un rayon d'origine le point p1 et de direction le vecteur p1p2
     * 
     * @param p1 origine du rayon
     * @param p2 point vers lequel se dirige le rayon
     */
    public Rayon(Point p1, Point p2) {
        this.x = p1.x;
        this.y = p1.y;
        this.z = p1.z;
        this.dx = p2.x - p1.x;
        this.dy = p2.y - p1.y;
        this.dz = p2.z - p1.z;
    }

    /**
     * crée un rayon d'origine le point p1 et de direction le vecteur v
     * 
     * @param p1 origine du rayon
     * @param v  vecteur direction du rayon
     */
    public Rayon(Point p1, Vecteur v) {
        Point p = new Point(v.getX(), v.getY(), v.getZ());
        this.x = p1.x;
        this.y = p1.y;
        this.z = p1.z;
        this.dx = p.x;
        this.dy = p.y;
        this.dz = p.z;
    }

    /**
     * fournit la direction du rayon
     * 
     * @return le vecteur direction
     */
    public Vecteur direction() {
        return new Vecteur(dx, dy, dz);
    }

    /**
     * fournit l'origine du rayon
     * 
     * @return l'origne du rayon
     */
    public Point origine() {
        return new Point(x, y, z);
    }

    /**
     * Retourne une chaîne de caractères qui représente un rayon. Le point d'origine
     * suivi de son vecteur direction sont retournés. La méthode toString de Point
     * et de Vecteur est appelée.
     * 
     * @return Une chaîne de caractères qui représente un rayon.
     */
    public String toString() {
        return ("(" + x + "," + y + "," + z + ")->(" + dx + "," + dy + "," + dz + ")");
    }

}// Rayon
