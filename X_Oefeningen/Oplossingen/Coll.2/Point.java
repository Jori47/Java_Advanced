package be.ap.javadv.collections;

public class Point {
	public float x;
	public float y;
	public float z;

	public Point(float x, float y, float z) {
		if (!inRange(x) || !inRange(y) || !inRange(z)){
			throw new IllegalArgumentException();
		}

		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean inRange (float number){
		return number>=0 && number <9.5;
	}

	@Override
	public int hashCode() {
		return 100 * Math.round(x) + 10 * Math.round(y) + Math.round(z);
	}
}