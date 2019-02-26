package be.ap.javadv.collections;

import java.util.Random;

public class PointTest {
	public static void main(String[] args) {
		final int MAX = 10_000;
		final int[] N = { 10, 100, 1000, 10_000, 100_000, 1_000_000 };
		Point[] points = new Point[MAX];
		Random r = new Random();

		// Fun Fact:
		// OK, no exception:
		// Point myPointOK = new Point ( 9.499999f, 0.0f, 0.0f);
		// NOK, throws IllegalArgumentException:
		// Point myPointNOK = new Point ( 9.4999999f, 0.0f, 0.0f);

		for (int i = 0; i < MAX; i++) {
			points[i] = new Point(rand(), rand(), rand());
		}

		System.out.println("Gemiddelde afstand:\n");

		for (int n : N) {
			double gemiddelde = 0;

			for (int i = 0; i < n; i++) {
				int first = r.nextInt(MAX);
				int secnd = r.nextInt(MAX);

				gemiddelde += dist(points[first], points[secnd]);
			}
			gemiddelde = gemiddelde / n;

			System.out.printf("%7d koppels: %.3f\n", n, gemiddelde);
		}
	}

	public static float rand() {
		return (float) (Math.random() * 9.5);
	}

	public static double dist(Point first, Point secnd) {
		int hc1 = first.hashCode();
		int hc2 = secnd.hashCode();

		int x1 = hc1 / 100;
		int y1 = (hc1 - x1 * 100) / 10;
		int z1 = hc1 - x1 * 100 - y1 * 10;

		int x2 = hc2 / 100;
		int y2 = (hc2 - x2 * 100) / 10;
		int z2 = hc2 - x2 * 100 - y2 * 10;

		double dist = Math.sqrt(
			Math.pow(x2 - x1, 2) +
			Math.pow(y2 - y1, 2) +
			Math.pow(z2 - z1, 2));

		return dist;
	}
}