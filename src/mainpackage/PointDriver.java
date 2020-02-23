/**
 * 
 */
package mainpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * @author Team 3
 *
 */
public class PointDriver {

	public static void main(String[] args) {
		List<PointCoordinates> points = new ArrayList<PointCoordinates>();
		long startTime = System.currentTimeMillis();

		String path = "C:\\Users\\ro_gup\\Desktop\\";
		String fileName = "LA2.txt";
		File inputFile = new File(path + fileName);
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(inputFile));

			int numberOfRecords = 10;
			int count = 0;
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				line = line.substring(1, line.length() - 1);
				String[] values = line.split(",");

				PointCoordinates p1 = new PointCoordinates(Double.parseDouble(values[0]), Double.parseDouble(values[1]),
						Double.parseDouble(values[2]));
				points.add(p1);
				// System.out.println(values[0]);
				count++;
				if (count == numberOfRecords)
					break;
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PointsKdTree<PointCoordinates> pointsKdTree = null;
		try {
			pointsKdTree = new PointsKdTree<PointCoordinates>(points);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long insertionTime = System.currentTimeMillis();
		System.out.println("Insertion Time: " + (insertionTime - startTime));
		System.out.println("Depth : " + pointsKdTree.maxDepth);
		System.out.println("Enter the 2 coordinates for the Range query.");
		Scanner scanner = new Scanner(System.in);
		PointCoordinates p2 = null;
		PointCoordinates p3 = null;

		for (int i = 0; i < 2; i++) {
			String input = scanner.nextLine();
			String[] str = input.split(",");
			double x = Double.parseDouble(str[0]);
			double y = Double.parseDouble(str[1]);
			double z = Double.parseDouble(str[2]);
			if (p2 != null)
				p3 = new PointCoordinates(x, y, z);
			else
				p2 = new PointCoordinates(x, y, z);
		}

		long startRangeTime = System.currentTimeMillis();
		try {
			int val = p2.compareTo(p3);
			if (p2.compareTo(p3) < 0)
				pointsKdTree.range(p2, p3);
			else
				pointsKdTree.range(p3, p2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total no. of range records: " + pointsKdTree.noOfRangeRecords);
		long endRangeTime = System.currentTimeMillis();
		// (420.2333, 552.6783, 84.50268)
		System.out.println("Range query Time: " + (endRangeTime - startRangeTime));

		System.out.println("Enter the coordinate for nearest neighbour: ");
		String input = scanner.nextLine();
		String[] str = input.split(",");
		double x = Double.parseDouble(str[0]);
		double y = Double.parseDouble(str[1]);
		double z = Double.parseDouble(str[2]);
		PointCoordinates p4 = new PointCoordinates(x, y, z);
		long startnearestNeighbourTime = System.currentTimeMillis();

		List<PointCoordinates> result = pointsKdTree.nearestNeighbourQuery(1, p4);
		System.out.println(result);
		long endnearestNeighbourTime = System.currentTimeMillis();

		System.out.println("Nearest Neighbour Time: " + (endnearestNeighbourTime - startnearestNeighbourTime));
	}
}
