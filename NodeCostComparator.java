import java.util.Comparator;

public class NodeCostComparator implements Comparator<MyNode> {
	@Override
	public int compare(MyNode N1, MyNode N2) {
		if (N1.getCost() > N2.getCost()) {
			return -1;
		}
		if (N1.getCost() < N2.getCost()) {
			return 1;
		}
		return 0;
	}
}