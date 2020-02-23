package mainpackage;

/**
 * @author Team 3
 *
 */
public class PointCoordinates implements Comparable<PointCoordinates> {
	private double x;
	private double y;
	private double z;

	public PointCoordinates(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getDistance(PointCoordinates pc) {
		return Math.sqrt(Math.pow(x - pc.getX(), 2) + Math.pow(y - pc.getY(), 2) + Math.pow(z - pc.getZ(), 2));
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	@Override
	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("(");
		stringbuilder.append(x);
		stringbuilder.append(",");
		stringbuilder.append(y);
		stringbuilder.append(",");
		stringbuilder.append(z);
		stringbuilder.append(")");
		return stringbuilder.toString();
	}

	@Override
	public int compareTo(PointCoordinates pc) {
		// TODO Auto-generated method stub
		if (this.x == pc.getX()) {
			if (this.y == pc.getY()) {
				if (this.z == pc.getZ())
					return 0;
				else
					return this.z > pc.getZ() ? 1 : -1;
			} else
				return this.y > pc.getY() ? 1 : -1;
		} else
			return this.x > pc.getX() ? 1 : -1;
	}

}
