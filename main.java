import java.io.*;
import java.util.*;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;

public class main {
	static int[][] costMatrix = new int[10][10];
	static int nodeCount;
	static long startingTime, endTime;
	static double rootCost;

	public static void main(String[] args) {
		TSP T = new TSP();
		T.ReadFile(costMatrix, nodeCount);
		T.PrintMatrix(costMatrix, nodeCount);
		startingTime = System.nanoTime(); // Waktu awal program berjalan
		/* PROCESS */
		Comparator<Node> comparator = new NodeCostComparator();
		Queue<Node> Q = new PriorityQueue<Node>(100, comparator);
		/* Mengisi node pertama yaitu root */
		Node root = new Node(nodeCount);
		double rootCost = T.countRootCost(costMatrix, nodeCount);
		root.setCost(rootCost);
		// root.getTour()[0] = 0;
		// root.setTourAtIdx(0, 0);
		Q.add(root);
		double solution = 9999; // inisialisasi solusi awal
		/* */
		while (Q.size() != 0) {
			Node N1 = Q.poll();
			if ((!N1.isTourComplete()) && (T.totalCost(N1, costMatrix, nodeCount) < solution)) {
				for (int i = 0; i < nodeCount; i++) {
					if (!N1.isNodeInTour(i)) {
						Node N2 = N1.CopyNode();
						N2.getTour()[N2.getnTour()+1] = i;
						// N2.setTourAtIdx(N2.getnTour()+1, i);
						N2.setnTour(N2.getnTour()+1);
						double temp = T.totalCost(N2, costMatrix, nodeCount);
						N2.setCost(temp);
						Q.add(N2);
					}
				}
			}
		}
		endTime = System.nanoTime(); // pencatatan waktu ketika program berakhir
	}
}