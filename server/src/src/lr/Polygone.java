package lr;

import java.lang.Math;

// classe de polygone CONVEXES ...
/**
 * classe des polygones CONVEXES
 */
public class Polygone extends Primitive {

	// attributs spécifiques aux polygones
	private Point[] tabSom; // tableau contenant les sommets
	private Vecteur[] tabNormale; // tableau contenant la normale en chaque sommet

	private Vecteur normale; // normale unique si tabNormale==null

	/**
	 * Constructeur par défaut de polygone. Crée un carré horizontal, de côté 1,
	 * centré sur l'origine. Ses coordonnées sont :<BR>
	 * (0,5 ; 0 ; 0,5)<BR>
	 * (0,5 ; 0 ; -0,5)<BR>
	 * (-0,5 ; 0 ; -0,5)<BR>
	 * (-0,5 ; 0 ; 0,5)<BR>
	 * <BR>
	 * La normale (0,1,0) est identique en tout point.
	 */
	public Polygone() { // crée par défaut un carré de côté 1, centré à l'origine
		tabSom = new Point[5];

		tabSom[0] = new Point(0.5f, 0.0f, 0.5f);
		tabSom[1] = new Point(0.5f, 0.0f, -0.5f);
		tabSom[2] = new Point(-0.5f, 0.0f, -0.5f);
		tabSom[3] = new Point(-0.5f, 0.0f, 0.5f);
		tabSom[4] = new Point(tabSom[0]);

		normale = new Vecteur(0.0f, 1.0f, 0.0f);
	}// Polygone()

	/**
	 * Crée un polygone à partir d'une suite de points et d'un materiau. La suite
	 * des points est donnée par un tableau de Points et le materiau est une
	 * instance de la classe Materiau.
	 * 
	 * @param tabSom Tableau de points qui définit le polygone.
	 * @param m      instance de la classe Materiau qui défini les propriétés de
	 *               réflexion du materiau.
	 */
	public Polygone(Point[] tabSom, Materiau m) {
		// appel du constructeur complet Polygone(points, normales, materiaux)
		this(tabSom, null, m);
	}// Polygone(tabSom, m)

	/**
	 * Crée un polygone à partir d'une suite de points, de leur normale et d'un
	 * materiau. La suite des points est donnée par un tableau de Points. La suite
	 * des normales est donnée par un tableau de Vecteurs. Le materiau est une
	 * instance de la classe Materiau.
	 * 
	 * @param tabSom     Liste des sommets. Donnée sous la forme d'un tableau de
	 *                   points.
	 * @param tabNormale Liste des normales. Donnée sous la forme d'un tableau de
	 *                   vecteurs.
	 * @param m          matériau : propriétés de réflexion du polygone.
	 */
	public Polygone(Point[] tabSom, Vecteur[] tabNormale, Materiau m) {

		// appel du constructeur de Primitive
		super(m.getAmbient(), m.getDiffuse(), m.getSpecular(), m.getCoeffSpec());

		// on ne crée pas un polygone de 2 côtés ou moins.
		if (tabSom.length < 3) {
			System.out.println("impossible de construire un polygone avec " + tabSom.length + " côtés");
			return;
		}

		// instanciation de l'attribut tabSom = tableau de points des sommets
		// (deux cas : polygone fermé ou non)
		if (!tabSom[0].equals(tabSom[tabSom.length - 1])) {
			// gérer la fermeture du polygone
			this.tabSom = new Point[tabSom.length + 1];
			this.tabSom[tabSom.length] = new Point(tabSom[0]);
		} else {
			this.tabSom = new Point[tabSom.length];
		}

		// replissage de l'attribut tabSom avec le paramètre tabSom
		for (int i = 0; i < tabSom.length; i++)
			this.tabSom[i] = new Point(tabSom[i]);

		// remplissage de l'attribut tabNormal avec le paramètre tabNormal
		// sauf si null est passé en paramètre
		if (tabNormale != null) {
			// instanciation du tableau de vecteur
			if (this.tabSom.length != tabSom.length) {
				// gestion de la fermeture
				this.tabNormale = new Vecteur[this.tabSom.length];
				this.tabNormale[tabSom.length] = new Vecteur(tabNormale[0]);
			} else {
				this.tabNormale = new Vecteur[tabSom.length];
			}

			// remplissage du tableau de vecteur
			for (int i = 0; i < tabSom.length; i++)
				this.tabNormale[i] = new Vecteur(tabNormale[i]);
		}

		// calcul de la normale au plan support du polygone
		Vecteur v1 = new Vecteur(tabSom[1], tabSom[2]);
		Vecteur v2 = new Vecteur(tabSom[1], tabSom[0]);

		this.normale = v1.produitVectoriel(v2);
		this.normale.normalise();

	}// Polygone(tabSom, tabNormale, m)

	// implantation des méthodes abstraites communes aux primitives

	public Intersection intersecte(Rayon r) {
		Vecteur d = r.direction();
		Point o = r.origine();

		// calcul du coefficient constant de l'équation du plan support
		float dd = -(normale.getX() * tabSom[0].x + normale.getY() * tabSom[0].y + normale.getZ() * tabSom[0].z);

		// calcul de l'intersection entre le plan support et le rayon
		float denominateur = normale.getX() * d.getX() + normale.getY() * d.getY() + normale.getZ() * d.getZ();
		if (denominateur == 0.0)
			return null; // plan et rayon parallèles

		float numerateur = normale.getX() * o.x + normale.getY() * o.y + normale.getZ() * o.z + dd;

		float t = -numerateur / denominateur;
		if (t < EPSILON)
			return null; // plan derrière l'origine du rayon

		Point pi = new Point(o.x + t * d.getX(), o.y + t * d.getY(), o.z + t * d.getZ());

		if (interne(pi))
			return new Intersection(pi.x, pi.y, pi.z, this, t);

		return null;
	}

	public boolean coupe(Rayon r, float tmax) {
		Intersection inter = this.intersecte(r);

		if (inter == null)
			return false;

		float t = inter.getDistance();
		return ((t >= EPSILON) && (t <= tmax - EPSILON));
	}

	public Vecteur getNormale(Point i) {
		if (tabNormale == null)
			return new Vecteur(normale);
		else // interpoler la normale par rapport aux normales aux sommets
			return new Vecteur(normale);

	}// getNormale

	public String toString() {
		String res = "polygone a " + (tabSom.length - 1) + " cotes : ";
		for (int s = 0; s < tabSom.length - 1; s++)
			res += (s != tabSom.length - 2) ? tabSom[s] + "-" : tabSom[s] + ";";
		return res;
	}
	// vérifier si le point d'intersection se trouve dans l'un des
	// (n-2) triangles composant le polygone CONVEXE
	// voir algo de badouel GGEMS1

	private boolean interne(Point pointInterne) {
		float axeDominant, u0, u1, u2, v0, v1, v2, alpha, beta;
		boolean inter;
		int i;

		/* cette fonction admet tout polygone convexe */

		axeDominant = Math.max(Math.abs(normale.getX()), Math.abs(normale.getY()));
		axeDominant = Math.max(Math.abs(normale.getZ()), axeDominant);

		inter = false;
		i = 2;

		if (axeDominant == Math.abs(normale.getZ())) {
			u0 = pointInterne.x - tabSom[0].x;
			v0 = pointInterne.y - tabSom[0].y;
			do {
				/* The polygon is viewed as (n-2) triangles. */
				u1 = tabSom[i - 1].x - tabSom[0].x;
				v1 = tabSom[i - 1].y - tabSom[0].y;
				u2 = tabSom[i].x - tabSom[0].x;
				v2 = tabSom[i].y - tabSom[0].y;
				if (u1 == 0) {
					beta = u0 / u2;
					if ((beta >= 0.) && (beta <= 1.)) {
						alpha = (v0 - beta * v2) / v1;
						inter = ((alpha >= -EPSILON) && ((alpha + beta) <= (1.0 + EPSILON)));
					}
				} else {
					beta = (v0 * u1 - u0 * v1) / (v2 * u1 - u2 * v1);
					if ((beta >= 0.) && (beta <= 1.)) {
						alpha = (u0 - beta * u2) / u1;
						inter = ((alpha >= -EPSILON) && ((alpha + beta) <= (1.0 + EPSILON)));
					}
				}
			} while ((!inter) && (++i < tabSom.length - 1));

		} else if (axeDominant == Math.abs(normale.getY())) {
			u0 = pointInterne.x - tabSom[0].x;
			v0 = pointInterne.z - tabSom[0].z;
			do {
				/* The polygon is viewed as (n-2) triangles. */
				u1 = tabSom[i - 1].x - tabSom[0].x;
				v1 = tabSom[i - 1].z - tabSom[0].z;
				u2 = tabSom[i].x - tabSom[0].x;
				v2 = tabSom[i].z - tabSom[0].z;
				if (u1 == 0) {
					beta = u0 / u2;
					if ((beta >= 0.) && (beta <= 1.)) {
						alpha = (v0 - beta * v2) / v1;
						inter = ((alpha >= -EPSILON) && ((alpha + beta) <= (1.0 + EPSILON)));
					}
				} else {
					beta = (v0 * u1 - u0 * v1) / (v2 * u1 - u2 * v1);
					if ((beta >= 0.) && (beta <= 1.)) {
						alpha = (u0 - beta * u2) / u1;
						inter = ((alpha >= -EPSILON) && ((alpha + beta) <= (1.0 + EPSILON)));
					}
				}
			} while ((!inter) && (++i < tabSom.length - 1));

		} else if (axeDominant == Math.abs(normale.getX())) {
			u0 = pointInterne.z - tabSom[0].z;
			v0 = pointInterne.y - tabSom[0].y;
			do {
				/* The polygon is viewed as (n-2) triangles. */
				u1 = tabSom[i - 1].z - tabSom[0].z;
				v1 = tabSom[i - 1].y - tabSom[0].y;
				u2 = tabSom[i].z - tabSom[0].z;
				v2 = tabSom[i].y - tabSom[0].y;
				if (u1 == 0) {
					beta = u0 / u2;
					if ((beta >= 0.) && (beta <= 1.)) {
						alpha = (v0 - beta * v2) / v1;
						inter = ((alpha >= -EPSILON) && ((alpha + beta) <= (1.0 + EPSILON)));
					}
				} else {
					beta = (v0 * u1 - u0 * v1) / (v2 * u1 - u2 * v1);
					if ((beta >= 0.) && (beta <= 1.)) {
						alpha = (u0 - beta * u2) / u1;
						inter = ((alpha >= -EPSILON) && ((alpha + beta) <= (1.0 + EPSILON)));
					}
				}
			} while ((!inter) && (++i < tabSom.length - 1));

		}

		return (inter);

	}// interne

}// classe Polygone
