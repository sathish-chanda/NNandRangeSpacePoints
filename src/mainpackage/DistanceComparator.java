/**
 * 
 */
package mainpackage;

import java.util.Comparator;

/**
 * @author Team 3
 *
 */
public class DistanceComparator implements Comparator<PointNode> {

	private final PointCoordinates pc;

	public DistanceComparator(PointCoordinates point) {
		this.pc = point;
	}

	@Override
	public int compare(PointNode o1, PointNode o2) {
		Double d1 = pc.getDistance(o1.pc);
		Double d2 = pc.getDistance(o2.pc);
		if (d1.compareTo(d2) < 0)
			return -1;
		else if (d2.compareTo(d1) < 0)
			return 1;
		return o1.pc.compareTo(o2.pc);
	}
}