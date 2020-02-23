package mainpackage;

/**
 * @author Team 3
 *
 */
public class PointNode implements Comparable<PointNode> {

	public PointCoordinates pc;
	public int depth;
	public PointNode parent;
	public PointNode left;
	public PointNode right;

	public PointNode(PointCoordinates pc) {
		this.pc = pc;
		this.depth = 0;
		parent = left = right = null;
	}

	public PointNode(PointCoordinates pc, int depth) {
		this.pc = pc;
		this.depth = depth;
		parent = left = right = null;
	}

	public static int compareTo(int depth, PointCoordinates pc1, PointCoordinates pc2) {
		int coordinate = depth % 3;
		if (coordinate == 0) {
			if (pc1.getX() == pc2.getX())
				return 0;
			else
				return pc1.getX() > pc2.getX() ? 1 : -1;
		} else if (coordinate == 1) {
			if (pc1.getY() == pc2.getY())
				return 0;
			else
				return pc1.getY() > pc2.getY() ? 1 : -1;
		} else {
			if (pc1.getZ() == pc2.getZ())
				return 0;
			else
				return pc1.getZ() > pc2.getZ() ? 1 : -1;
		}
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("Node Depth : ");
		stringbuilder.append(depth);
		stringbuilder.append(" , PointCoordinates : ");
		stringbuilder.append(pc.toString());
		return stringbuilder.toString();
	}

	@Override
	public int compareTo(PointNode pn) {
		// TODO Auto-generated method stub
		return this.compareTo(this.depth, this.pc, pn.pc);
	}

}
