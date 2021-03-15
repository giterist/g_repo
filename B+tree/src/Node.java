import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class Node {
	ArrayList<Integer> keys = new ArrayList<>();
	TreeMap <Integer, Node> knp = new TreeMap<>();// key n left child node
	TreeMap<Integer, Long> knv = new TreeMap<>(); // key n value
	Node pn = null; // parent node
	Node rm = null; // rightmost node
	int numofchild = 0;
	
	public Node(){}
	
	public boolean isRoot() {
		if(pn == null) return true;
		else return false;
	}
	public boolean isLeaf() {
		if(knp.isEmpty()) return true;
		else return false;
	}
	public void inputKey(int key) {
		keys.add(key);
		Collections.sort(keys);
	}
	public void RemoveKey(int key){
		keys.remove(keys.indexOf(key));
		Collections.sort(keys);
	}
	public boolean isOverflow(){
		if(keys.size() > 4) return true;
		else return false;
	}
	public boolean isUnderflow(){
		if(keys.size() < 2) return true;
		else return false;
	}

}
