public class MyNode {
	private int[] tour;
	private int nTour;
	private final int nEff;
	private double cost;

	public MyNode(int _nodeCount) {
		this.nEff = _nodeCount;
		this.tour = new int[this.nEff];
		for (int i = 0; i < this.nEff; i++) {
			this.tour[i] = -999;
		}
		this.nTour = 0;
		this.cost = 0;
	}
	public int getTourAtIdx(int idx) {
		return this.tour[idx];
	}
	public int[] getTour() {
		return this.tour;
	}
	public int getnTour() {
		return this.nTour;
	}
	public double getCost() {
		return this.cost;
	}
	public int getnEff() {
		return this.nEff;
	}
	public void setTourAtIdx(int idx, int val) {
		this.tour[idx] = val;
	}
	public void setTour(int[] _tour) {
		this.tour = _tour;
	}
	public void setnTour(int _nTour) {
		this.nTour = _nTour;
	}
	public void setCost(double _cost) {
		this.cost = _cost;
	}
	public boolean isNodeInTour(int i) {
		boolean found = false;
		int idx = 0;
		while ((idx < getnTour()) && (!found)) {
			if (getTour()[idx] == i) {
				found = true;
			} else {
				idx++;
			}
		}
		return found;
	}
	public boolean isTourComplete() {
		return (getnTour() == getnEff());
	}
	public MyNode CopyNode() {
		MyNode temp = new MyNode(getnEff());
		temp.setCost(this.getCost());
		temp.setnTour(this.getnTour());
		for (int i = 0; i < this.getnTour(); i++) {
			temp.setTourAtIdx(i, this.getTourAtIdx(i));
		}
		return temp;
	}
}
