package mainpackage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Team 3
 *
 */
public class PointsKdTree<T extends PointCoordinates> {
	private PointNode root = null;
	private static final String filename = "C:\\Users\\ro_gup\\Desktop\\output.txt";
	private static FileWriter fw;
	private static BufferedWriter bw;
	public static int maxDepth = 0;
	public static int noOfRangeRecords = 0;
	private static final Comparator<PointCoordinates> X_AXIS_COMPARATOR = new Comparator<PointCoordinates>() {
		@Override
		public int compare(PointCoordinates p1, PointCoordinates p2) {
			if (p1.getX() == p2.getX())
				return 0;
			return p1.getX() > p2.getX() ? 1 : -1;
		}
	};

	private static final Comparator<PointCoordinates> Y_AXIS_COMPARATOR = new Comparator<PointCoordinates>() {
		@Override
		public int compare(PointCoordinates p1, PointCoordinates p2) {
			if (p1.getY() == p2.getY())
				return 0;
			return p1.getY() > p2.getY() ? 1 : -1;
		}
	};

	private static final Comparator<PointCoordinates> Z_AXIS_COMPARATOR = new Comparator<PointCoordinates>() {
		@Override
		public int compare(PointCoordinates p1, PointCoordinates p2) {
			if (p1.getZ() == p2.getZ())
				return 0;
			return p1.getZ() > p2.getZ() ? 1 : -1;
		}
	};

	public PointsKdTree() {
	}

	public PointsKdTree(List<PointCoordinates> pc_list) throws IOException {
		fw = new FileWriter(filename);
		bw = new BufferedWriter(fw);
		root = createPointNode(pc_list, 0);
	}

	public static PointNode createPointNode(List<PointCoordinates> pc_list, int depth) {
		if (depth > maxDepth) {
			maxDepth = depth;
		}
		if (pc_list.size() == 0 || pc_list == null)
			return null;
		int coordinate = depth % 3;
		if (coordinate == 0)
			Collections.sort(pc_list, X_AXIS_COMPARATOR);
		else if (coordinate == 1)
			Collections.sort(pc_list, Y_AXIS_COMPARATOR);
		else
			Collections.sort(pc_list, Z_AXIS_COMPARATOR);

		PointNode node = null;
		List<PointCoordinates> left = new ArrayList<PointCoordinates>(pc_list.size());
		List<PointCoordinates> right = new ArrayList<PointCoordinates>(pc_list.size());
		if (pc_list.size() > 0) {
			int median = pc_list.size() / 2;
			node = new PointNode(pc_list.get(median), depth);
			for (int i = 0; i < pc_list.size(); i++) {
				if (i == median)
					continue;
				PointCoordinates pc = pc_list.get(i);
				if (PointNode.compareTo(depth, pc, node.pc) <= 0)
					left.add(pc);
				else
					right.add(pc);
			}
			if ((median - 1 >= 0) && left.size() > 0) {
				node.left = createPointNode(left, depth + 1);
				node.left.parent = node;
			}

			if (median < pc_list.size() && right.size() > 0) {
				node.right = createPointNode(right, depth + 1);
				node.right.parent = node;
			}
		}
		return node;
	}

	@SuppressWarnings("unchecked")
	public List<T> nearestNeighbourQuery(int count, T pc) {
		if (pc == null || root == null)
			return Collections.EMPTY_LIST;
		TreeSet<PointNode> treeset = new TreeSet<PointNode>(new DistanceComparator(pc));
		PointNode previousNode = null;
		PointNode node = root;
		while (node != null) {
			if (PointNode.compareTo(node.depth, pc, node.pc) <= 0) {
				previousNode = node;
				node = node.left;
			} else {
				previousNode = node;
				node = node.right;
			}
		}
		PointNode leafNode = previousNode;
		if (leafNode != null) {
			Set<PointNode> set = new HashSet<PointNode>();
			node = leafNode;
			while (node != null) {
				searchPointNode(pc, count, node, treeset, set);
				node = node.parent;
			}
		}

		List<T> list = new ArrayList<T>(count);
		for (PointNode pn : treeset)
			list.add((T) pn.pc);
		return list;
	}

	private void searchPointNode(T pc, int count, PointNode node, TreeSet<PointNode> treeset, Set<PointNode> set) {
		// TODO Auto-generated method stub
		set.add(node);
		PointNode lastNode = null;
		Double ldistance = Double.MAX_VALUE;
		if (treeset.size() > 0) {
			lastNode = treeset.last();
			ldistance = lastNode.pc.getDistance(pc);
		}
		Double ndistance = node.pc.getDistance(pc);
		if (ndistance.compareTo(ldistance) < 0) {
			if (treeset.size() == count && lastNode != null)
				treeset.remove(lastNode);
			treeset.add(node);
		} else if (ndistance.equals(ldistance)) {
			treeset.add(node);
		} else if (treeset.size() < count) {
			treeset.add(node);
		}
		lastNode = treeset.last();
		ldistance = lastNode.pc.getDistance(pc);

		int axis = node.depth % 3;
		PointNode left = node.left;
		PointNode right = node.right;

		if (left != null && !set.contains(left)) {
			set.add(left);

			double n = Double.MIN_VALUE;
			double v = Double.MIN_VALUE;
			if (axis == 0) {
				n = node.pc.getX();
				v = pc.getX() - ldistance;
			} else if (axis == 1) {
				n = node.pc.getY();
				v = pc.getY() - ldistance;
			} else {
				n = node.pc.getZ();
				v = pc.getZ() - ldistance;
			}
			boolean linecubeintersect = ((v <= n) ? true : false);

			if (linecubeintersect)
				searchPointNode(pc, count, left, treeset, set);
		}
		if (right != null && !set.contains(right)) {
			set.add(right);

			double n = Double.MIN_VALUE;
			double v = Double.MIN_VALUE;
			if (axis == 0) {
				n = node.pc.getX();
				v = pc.getX() + ldistance;
			} else if (axis == 1) {
				n = node.pc.getY();
				v = pc.getY() + ldistance;
			} else {
				n = node.pc.getZ();
				v = pc.getZ() + ldistance;
			}
			boolean linecubeintersect = ((v >= n) ? true : false);

			if (linecubeintersect)
				searchPointNode(pc, count, right, treeset, set);
		}
	}

	public void rangeQuery(PointNode node, PointCoordinates p1, PointCoordinates p2) throws IOException {
		if (node != null) {
			if (PointNode.compareTo(node.depth, node.pc, p1) >= 0
					&& PointNode.compareTo(node.depth, node.pc, p2) <= 0) {
				if (node.pc.getX() >= p1.getX() && node.pc.getX() <= p2.getX() && node.pc.getY() >= p1.getY()
						&& node.pc.getY() <= p2.getY() && node.pc.getZ() >= p1.getZ() && node.pc.getZ() <= p2.getZ()) {
					noOfRangeRecords++;
					bw.write(node.toString() + "\n");
					// System.out.println(node.toString());

				}
				PointNode left = node.left;
				PointNode right = node.right;
				if (left != null)
					rangeQuery(left, p1, p2);
				if (right != null)
					rangeQuery(right, p1, p2);
			}
			if (PointNode.compareTo(node.depth, node.pc, p1) < 0) {
				if (node.right != null)
					rangeQuery(node.right, p1, p2);
			}
			if (PointNode.compareTo(node.depth, node.pc, p2) >= 0) {
				if (node.left != null)
					rangeQuery(node.left, p1, p2);
			}
		}
	}

	public void range(PointCoordinates p1, PointCoordinates p2) throws IOException {
		rangeQuery(this.root, p1, p2);
		bw.close();
	}

}
