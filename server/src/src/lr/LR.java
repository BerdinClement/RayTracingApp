package lr;

import lr.format.simple.FormatSimple;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Classe principale
 * 
 * auteurs : Christophe Renaud, Samuel Delepoulle, Franck Vandewiele
 */
class LR {
	static final int LARGEUR = 1980;
	static final int HAUTEUR = 1080;
	static int NBRAYONS = 20;
	static final int NIVEAU = 1;
	static int THREADS = 4;

	public static void main(String[] args) {

		if (args.length > 0) {
			NBRAYONS = Integer.parseInt(args[0]);
			THREADS = Integer.parseInt(args[1]);
		} else {
			NBRAYONS = 10;
			THREADS = 4;
		}

		Renderer r = new Renderer(LARGEUR, HAUTEUR);
		Scene sc = new FormatSimple().charger("simple.txt");
		sc.display();
		r.setScene(sc);
		r.setNiveau(NIVEAU);

		// r.renderFullImage(NBRAYONS);

		//for (int i = 0; i < HAUTEUR; i++) {
		//	r.renderLine(i, NBRAYONS);
		//}

		ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

		int [] sizeDividedByThreads = divideByThreads(HAUTEUR, THREADS);

		long t1 = System.currentTimeMillis();

		Set<Callable<String>> callables = new HashSet<Callable<String>>();

		for (int i = 0; i < THREADS; i++) {
			callables.add(new ParallelRenderer(r, sizeDividedByThreads[i], sizeDividedByThreads[i + 1]));
		}

		try {
			executorService.invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long t2 = System.currentTimeMillis();

		executorService.shutdown();

		//long t2 = System.currentTimeMillis();
		System.out.println("Temps de calcul "+ LR.THREADS +" threads : " + (t2 - t1) + " ms");


		Image image = r.getIm();

		image.save("image" + NIVEAU, "png");

		//new MaterialFormat().charger("chaise_plan.mtl");
	}

	private static int[] divideByThreads(int hauteur, int threads) {
		int[] sizeDividedByThreads = new int[threads + 1];
		int size = hauteur / threads;
		for (int i = 0; i < threads; i++) {
			sizeDividedByThreads[i] = i * size;
		}
		sizeDividedByThreads[threads] = hauteur;
		return sizeDividedByThreads;
	}
}
