package lr;

/**
 * Classe permettant la représentation d'une sphère
 */
public class Sphere extends Primitive {

	// attributs spécifiques aux sphères
	private float rayon;
	private Point centre;

	public Sphere() {
		rayon = 1.0f;
		centre = new Point(0.0f, 0.0f, 0.0f);
	}

	public Sphere(float rayon, Point centre, Materiau m) {
		super(m.getAmbient(), m.getDiffuse(), m.getSpecular(), m.getCoeffSpec());
		this.rayon = rayon;
		this.centre = new Point(centre);
	}

	// implantation des méthodes abstraites communes aux primitives
	public Intersection intersecte(Rayon r) {
		float t, t1, t2;
		float delta, a, b, c;
		Vecteur d = r.direction();
		Point o = r.origine();

		a = d.getX() * d.getX() + d.getY() * d.getY() + d.getZ() * d.getZ();
		b = 2 * (d.getX() * (o.x - centre.x) + d.getY() * (o.y - centre.y) + d.getZ() * (o.z - centre.z));
		c = o.x * o.x + o.y * o.y + o.z * o.z + centre.x * centre.x + centre.y * centre.y + centre.z * centre.z
				- 2 * (o.x * centre.x + o.y * centre.y + o.z * centre.z) - rayon * rayon;

		delta = b * b - 4 * a * c;

		if (delta < 0)
			return null;

		if (delta == 0.0) {
			t = -b / (2 * a);
			if (t <= EPSILON)
				return null;
		} else {
			t1 = (-b + (float) Math.sqrt(delta)) / (2 * a);
			t2 = (-b - (float) Math.sqrt(delta)) / (2 * a);
			if (t1 > t2) {
				t = t1;
				t1 = t2;
				t2 = t;
			}
			if (t2 <= EPSILON)
				return null;
			if ((-EPSILON <= t1) && (t1 <= EPSILON))
				return null; // t1 = 0.0
			if (t1 > EPSILON)
				t = t1;
			else
				t = t2;
		}

		// calcul du point d'intersection

		// return new Point(r.x+t*r.dx, r.y+t*r.dy, r.z+t*r.dz);
		return new Intersection(o.x + t * d.getX(), o.y + t * d.getY(), o.z + t * d.getZ(), this, t);

	}// intersecte

	public boolean coupe(Rayon r, float tmax) {
		float t, t1, t2;
		float delta, a, b, c;

		Vecteur d = r.direction();
		Point o = r.origine();

		a = d.getX() * d.getX() + d.getY() * d.getY() + d.getZ() * d.getZ();
		b = 2 * (d.getX() * (o.x - centre.x) + d.getY() * (o.y - centre.y) + d.getZ() * (o.z - centre.z));
		c = o.x * o.x + o.y * o.y + o.z * o.z + centre.x * centre.x + centre.y * centre.y + centre.z * centre.z
				- 2 * (o.x * centre.x + o.y * centre.y + o.z * centre.z) - rayon * rayon;

		delta = b * b - 4 * a * c;

		if (delta < 0)
			return false;

		if (delta == 0.0) {
			t = -b / (2 * a);
			if ((t <= EPSILON) || (t > tmax - EPSILON))
				return false;
		} else {
			t1 = (-b + (float) Math.sqrt(delta)) / (2 * a);
			t2 = (-b - (float) Math.sqrt(delta)) / (2 * a);
			if (t1 > t2) {
				t = t1;
				t1 = t2;
				t2 = t;
			}
			if (t2 <= EPSILON)
				return false;
			if ((t1 > EPSILON) && (t1 < tmax - EPSILON))
				return true;
			return (t2 > EPSILON) && (t2 < tmax - EPSILON);
		}
		return false;
	}

	public Vecteur getNormale(Point i) {
		Vecteur v = new Vecteur(centre, i);
		v.normalise();
		return v;
	}

	public String toString() {
		return "sphere de centre " + centre + " et de rayon " + rayon;
	}

}
