import java.io.*;
import java.util.*;
import java.util.Scanner;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class TSP {
	// Menyimpan matriks bobot
	static int[][] costMatrix = new int[10][10];
	// Menyimpan jumlah simpul pada graf
	static int nodeCount;
	// Menyimpan waktu mulai dan waktu berakhir program
	static long startingTime, endTime;
	// Penghitung jumlah simpul yang dibangkitkan
	static int nodeBuilt = 0;

	/* Membaca input persoalan dari file teks */
	public static void ReadFile() {
		try {
            // FileReader membaca dari file eksternal yang diberikan user
			System.out.print("Masukkan nama file: ");
            Scanner input = new Scanner(System.in);
            String filename = input.nextLine();
            FileReader inputFile = new FileReader(filename);
            BufferedReader bufferReader = new BufferedReader(inputFile);
            String line = null;
            int row = 0;
            // Membaca file teks per baris
            while ((line = bufferReader.readLine()) != null) {
	            String[] words = line.split(" ");
	            nodeCount = words.length;
	            for (int col = 0; col < words.length; col++) {
	                    int a = Integer.parseInt(words[col]);
	                    costMatrix[row][col] = a; // menyimpan ke dalam matriks cost
	            }
	            row++;
            }
            // Menutup file eksternal
            bufferReader.close();
		} catch(Exception e) {
            System.out.println("Error while reading file line by line:" + e.getMessage());
		}
	}

	// Menghitung bobot minimal dari suatu simpul
	public static MyEdge minCost(int _node1, int[][] costMatrix, int nodeCount) {
		MyEdge min = new MyEdge();
		min.setNode1(_node1);
		for (int i = 0; i < nodeCount; i++) {
			if (i != _node1) {
				if (costMatrix[_node1][i] < costMatrix[_node1][min.getNode2()]) {
					min.setNode2(i);
				}
			}
		}
		return min;
	}

	// Mengembalikan indeks dari suatu simpul tertentu pada array yang menyimpan
	// jalur
	public static int idxOnTour(int _node, MyNode tour) {
		boolean found = false;
		int i = 0;
		while ((i < tour.getnTour()) && (!found)) {
			if (tour.getTourAtIdx(i) == _node) {
				found = true;
			} else {
				i++;
			}
		}
		return i;
	}

	// Menghitung cost dari sebuah simpul pada pohon pencarian
	public static double totalCost (MyNode tour, int[][] costMatrix, int nodeCount) {
		int cost = 0;
		for (int i = 0; i < nodeCount; i++) {		
			MyEdge min1 = new MyEdge();
			MyEdge min2 = new MyEdge();	
			if (tour.isNodeInTour(i) && (tour.getnTour() > 1)) {
				if (idxOnTour(i, tour) == 0) {
					min1.setNode1(i);
					min1.setNode2(tour.getTourAtIdx(idxOnTour(i, tour)+1));
					if (tour.isTourComplete()) {
						min2.setNode1(i);
						min2.setNode2(tour.getTourAtIdx(tour.getnEff()-1));
					} else {
						int[][] costMatrixTemp = CopyMatrix(costMatrix);
						costMatrixTemp[min1.getNode1()][min1.getNode2()] = 999;
						min2 = minCost(i, costMatrixTemp, nodeCount);
					}
				} else if ((idxOnTour(i, tour)+1) == tour.getnTour()) {
					min1.setNode1(i);
					min1.setNode2(tour.getTourAtIdx(idxOnTour(i, tour)-1));
					int[][] costMatrixTemp = CopyMatrix(costMatrix);
					costMatrixTemp[min1.getNode1()][min1.getNode2()] = 999;
					min2 = minCost(i, costMatrixTemp, nodeCount);
				} else {
					min1.setNode1(i);
					min1.setNode2(tour.getTourAtIdx(idxOnTour(i, tour)+1));
					min2.setNode1(i);
					min2.setNode2(tour.getTourAtIdx(idxOnTour(i, tour)-1));
				}
			} else {
				min1 = minCost(i, costMatrix, nodeCount);
				int[][] costMatrixTemp = CopyMatrix(costMatrix);
				costMatrixTemp[min1.getNode1()][min1.getNode2()] = 999;
				min2 = minCost(i, costMatrixTemp, nodeCount);
			}
			cost += costMatrix[min1.getNode1()][min1.getNode2()] + costMatrix[min2.getNode1()][min2.getNode2()];
		}
		return cost/2;
	}

	// Fungsi untuk menduplikasi matriks bobot
	public static int[][] CopyMatrix(int[][] M1) {
		int[][] M2 = new int[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				M2[i][j] = M1[i][j];
			}
		}
		return M2;
	}
	
	// Mengembalikan true jika sisi i-j adalah solusi
	public static boolean isSolution(int i, int j, int[] solution, int nodeCount) {
		// Mencari i pada array solusi
		boolean found = false;
		int idx = 0;
		while ((idx <= nodeCount) && (!found)) {
			if (solution[idx] == i) {
				found = true;
			} else {
				idx++;
			}
		}
		if (idx == 0) {
			return (solution[1] == j) || (solution[nodeCount-1] == j);
		} else {
			if ((solution[idx+1] == j) || (solution[idx-1] == j)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	// Menggambarkan graf ke layar sesuai matriks bobot dan solusi tur terpendek
	public static void visualizeGraph(int[][] mat, int[] solution, int nodeCount) {
		Graph graph = new SingleGraph("Tour Graph");
		graph.addAttribute("ui.stylesheet", styleSheet);
		graph.setAutoCreate(true);
		graph.setStrict(false);
		graph.display();
    
		// Inisialisasi simpul
		for (int i = 0; i < nodeCount; i++) {
			Node temp = graph.addNode((i+1) + "");
			temp.addAttribute("ui.label", (i+1) + "");
		}
    
		// Inisialisasi sisi
		// Jika sebuah sisi merupakan bagian dari tur terpendek, maka diberi
		// warna yang berbeda
		for (int i = 0; i < nodeCount; i++) {
			for (int j = i + 1; j < nodeCount; j++) {
				Edge edge = graph.addEdge((i+1) + "" + (j+1), (i+1) + "", (j+1) + "");
				edge.addAttribute("ui.label", mat[i][j] + "");
				if (isSolution(i, j, solution, nodeCount)) {
					edge.setAttribute("ui.class", "coloredEdge");
				}
			}
		}
	}
	
	protected static String styleSheet = "node {" + " size: 10px; fill-color: blue;"
			+ "text-size: 20px; text-color: blue;" + "}" + "edge {" + " size:"
			+ "2px; fill-color: #CCC; stroke-width: 1px; stroke-mode: plain;"
			+ "text-size: 20px;" + "}" + "edge.coloredEdge {" + " fill-color:"
			+ "#222; stroke-width: 1px; stroke-mode: plain;" + "}";

	
	public static void main(String[] args) {
		ReadFile();
		startingTime = System.nanoTime(); // Waktu awal program berjalan
		Comparator<MyNode> comparator = new NodeCostComparator();
		Queue<MyNode> Q = new PriorityQueue<MyNode>(100, comparator);
		// Mengisi node pertama yaitu root
		MyNode root = new MyNode(nodeCount);
		root.setnTour(1);
		root.setTourAtIdx(0, 0);
		root.setCost(totalCost(root, costMatrix, nodeCount));
		Q.add(root);
		nodeBuilt++;
		int[] solution = new int[nodeCount+1];
		double costSolution = 9999;
		while (Q.size() != 0) {
			MyNode N1 = Q.poll();
			if ((!N1.isTourComplete()) && (totalCost(N1, costMatrix, nodeCount) < costSolution)) {
				for (int i = 0; i < nodeCount; i++) {
					if (!N1.isNodeInTour(i)) {
						MyNode N2 = N1.CopyNode();
						N2.setTourAtIdx(N2.getnTour(), i);
						N2.setnTour(N2.getnTour()+1);
						double temp = totalCost(N2, costMatrix, nodeCount);
						N2.setCost(temp);
						Q.add(N2);
						nodeBuilt++;
					}
				}
			}
			if (N1.isTourComplete() && (totalCost(N1, costMatrix, nodeCount) < costSolution)) {
				for (int i = 0; i < nodeCount; i++) {
					solution[i] = N1.getTourAtIdx(i);
				}
				solution[nodeCount] = N1.getTourAtIdx(0);
				costSolution = totalCost(N1, costMatrix, nodeCount);
			}
		}
		endTime = System.nanoTime(); // pencatatan waktu ketika program berakhir
		System.out.print("Tur terpendek: ");
		for (int i = 0; i <= nodeCount; i++) {
			System.out.print((solution[i]+1) + " ");
		}
		System.out.println();
		System.out.println("Bobot: " + costSolution);
		System.out.println("Jumlah simpul yang dibangkitkan: " + nodeBuilt);
		System.out.println("Waktu eksekusi = " + ((endTime-startingTime)/1000000) + " ms");
		visualizeGraph(costMatrix, solution, nodeCount);
	}
}