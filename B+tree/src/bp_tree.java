import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.TreeMap;

public class bp_tree {
	static Node root = null;
	static Node Firstleaf = null;
	
	public static void main(String[] args) {
		String inputfile = "insert.txt";
		String deletefile = "delete.txt";
		
		//insertion
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputfile));
			String str;
			
			int i =1;
		
			while((str = br.readLine()) != null) {
				String tmp[] = str.split("\t");
				insert(Integer.parseInt(tmp[0]),Long.parseLong(tmp[1]));
//				if(i>=40) {
//					break;
//				}
				System.out.println(i);
				i++;
			}

			br.close();
			System.out.println("insert complete");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// delete
		try {
			
//			delete(6,1494828198);
//			delete(12,1492643926);
//			delete(9,1853336516);
//			delete(7,868738912);
			
			BufferedReader br = new BufferedReader(new FileReader(deletefile));
			String str;
			
			int i =1;
		
			while((str = br.readLine()) != null) {
//				if(i>=41) {
//					System.out.println(str);
//					break;
//				}
				String tmp[] = str.split("\t");
				delete(Integer.parseInt(tmp[0]),Long.parseLong(tmp[1]));
				System.out.println(i);
				i++;
			}

			br.close();
			System.out.println("delete complete");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//validation
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputfile));
			BufferedWriter bw = new BufferedWriter(new FileWriter("RESULT.txt"));
			String str;
			String result;
			while((str = br.readLine()) != null) {
				
				String tmp[] = str.split("\t");
				int searchkey = Integer.parseInt(tmp[0]);
				result = Search(root,searchkey);
				bw.write(result);
				bw.newLine();
			}
			br.close();
			bw.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static String leaftest(Node n) {
		if(n == null) return null;
		System.out.println(n.keys.toString());
		return leaftest(n.rm);
	}
	
	public static void delete(int key, long value){
		Node temp = Delete_finder(root,key);
		if(temp.keys.size() > 2){ //지워도 언더 플로우 발생X		
			if(temp.pn.knp.get(temp.pn.knp.firstKey()) == temp){ //temp가 첫번째 자식
				temp.knv.remove(key, value);
				temp.RemoveKey(key);
			}else{
				if(temp.knv.firstKey() == key){
					int a = temp.knv.higherKey(key);
					temp.knv.remove(key, value);
					temp.RemoveKey(key);
					Node A = temp.pn.knp.get(key);
					temp.pn.knp.remove(key,A);
					temp.pn.knp.put(a, A);
					temp.pn.RemoveKey(key);
					temp.pn.inputKey(a);
				}else{
					temp.knv.remove(key, value);
					temp.RemoveKey(key);
				}
			}
		}else{ // 지울경우 underflow
			Reconstruction(temp,key,value);
		}
	}
	
	public static void Reconstruction(Node n, int k, long v){
		//인접 형제노드 중에서 빌려올 수 있다  -> 로테이션
		// 없다 -> merge (인접 형제노드가 둘다 2개의 key값만 가지고있다.)-> 부모 올라가면서 재귀체크
		Node bro = Sibling(n);
		if(bro != null){
			// borrowing
			// 동생인지 형인지 판단후 재분배 수행
			if(n.keys.size() == 1){ // n is internal node
				if(n.keys.get(0) < bro.keys.get(0)){	//bbro
					int a = n.knp.firstKey();
					int tk = n.pn.knp.higherKey(a);
					int nk = bro.knp.higherKey(a);
					Node nv = bro.knp.get(nk);
					Node mv = n.rm;
					
					n.knp.put(tk,mv);
					n.inputKey(tk);
					n.rm = nv;
					n.numofchild++;
					
					bro.knp.remove(nk,nv);
					bro.RemoveKey(nk);
					bro.numofchild--;
					
					n.pn.knp.remove(tk,n);
					n.pn.RemoveKey(tk);
					n.pn.knp.put(nk, n);
					n.pn.inputKey(nk);
					
				}else{	// sbro
					int a = n.knp.firstKey();
					int tk = n.pn.knp.lowerKey(a);
					int nk = bro.knp.lowerKey(a);
					Node nv = bro.knp.get(nk);
					Node mv = bro.rm;
							
					n.knp.put(tk, mv);
					n.inputKey(tk);
					mv.pn = n;
					n.numofchild++;
					
					bro.knp.remove(nk,nv);
					bro.RemoveKey(nk);
					bro.rm = nv;					
					bro.numofchild--;
					
					n.pn.knp.remove(tk,bro);
					n.pn.RemoveKey(tk);
					n.pn.knp.put(nk, bro);
					n.pn.inputKey(nk);
				}
			}else{
				if(n.keys.get(0) < bro.keys.get(0)){	//bbro
					if(n.knv.firstKey() == k){
						Node ntmp = n.pn.knp.get(k);
						n.knv.remove(k,v);
						n.RemoveKey(k);
						int nk2 = n.knv.firstKey();
						int nk = bro.knv.firstKey();
						long nv = bro.knv.get(nk);
						int tk = bro.knv.higherKey(nk);
						n.knv.put(nk, nv);
						n.inputKey(nk);
						bro.knv.remove(nk, nv);
						bro.RemoveKey(nk);
						
						n.pn.knp.remove(nk, n);
						n.pn.RemoveKey(nk);
						n.pn.knp.put(tk, n);
						n.pn.inputKey(tk);
						
						n.pn.knp.remove(k,ntmp);
						n.pn.RemoveKey(k);
						n.pn.knp.put(nk2, ntmp);
						n.pn.inputKey(nk2);
						
					}else{
						n.knv.remove(k,v);
						n.RemoveKey(k);
						int nk = bro.knv.firstKey();
						long nv = bro.knv.get(nk);
						int tk = bro.knv.higherKey(nk);
						
						n.knv.put(nk, nv);
						n.inputKey(nk);
						bro.knv.remove(nk, nv);
						bro.RemoveKey(nk);
						
						n.pn.knp.remove(nk, n);
						n.pn.RemoveKey(nk);
						n.pn.knp.put(tk, n);
						n.pn.inputKey(tk);
					}
				}else{	// sbro
					int a = n.knv.firstKey();
					n.knv.remove(k,v);
					n.RemoveKey(k);
					int nk = bro.knv.lowerKey(a);
					long nv = bro.knv.get(nk);
					
					bro.knv.remove(nk,nv);
					bro.RemoveKey(nk);
					n.knv.put(nk, nv);
					n.inputKey(nk);
					
					n.pn.knp.remove(a, bro);
					n.pn.RemoveKey(a);
					n.pn.knp.put(nk, bro);
					n.pn.inputKey(nk);
				}
			}
		}else{
			// merge
			// internal node
			if(n.isRoot() && n.keys.size()==1) return; // root는 한개의 key값을 가져도 허용
			if(n.pn.isRoot() && n.pn.keys.size()==1){
				// root와 merge 후  root변경
				if(n.pn.knp.get(n.pn.knp.firstKey()) == n){
					Node bbro = n.pn.rm;
					int tk = n.pn.knp.firstKey();
					int nk = n.knp.firstKey();
					Node nv = n.knp.get(nk);
					
					bbro.knp.put(tk, n.rm);
					bbro.knp.put(nk,nv);
					nv.pn = bbro;
					n.rm.pn = bbro;
					bbro.inputKey(tk);
					bbro.inputKey(nk);
					bbro.numofchild++;
					bbro.numofchild++;
					bbro.pn = null;
					root = bbro;
				}else{
					int tk = n.pn.knp.firstKey();
					Node sbro = n.pn.knp.get(tk);
					int nk = n.knp.firstKey();
					Node nv = n.knp.get(nk);
					
					sbro.knp.put(tk, sbro.rm);
					sbro.knp.put(nk, nv);
					sbro.inputKey(tk);
					sbro.inputKey(nk);
					nv.pn = sbro;
					n.rm.pn = sbro;
					sbro.rm = n.rm;
					sbro.numofchild++;
					sbro.numofchild++;
					
					sbro.pn = null;
					root = sbro;
				}
				return;
			}
			else if (n.keys.size() == 1) { // n의 key 개수가 1개일때 -> 아래에서 merge가 일어난 경우
				if (n.pn.knp.get(n.pn.knp.firstKey()) == n) { // n이 첫째
					int tk = n.pn.knp.firstKey();
					int a = n.pn.keys.get(1);
					Node bbro = n.pn.knp.get(a);
					int nk = n.knp.firstKey();
					Node nv = n.knp.get(nk);
					bbro.knp.put(tk, n.rm);
					bbro.knp.put(nk, nv);
					bbro.inputKey(tk);
					bbro.inputKey(nk);
					bbro.numofchild++;
					bbro.numofchild++;
					nv.pn = bbro;
					n.rm.pn = bbro;
					n.pn.knp.remove(tk, n);
					n.pn.RemoveKey(tk);
					n.pn.numofchild--;
					
				} else if (n.pn.rm == n) { // n이 막내
					int nk = n.knp.firstKey();
					int tk = n.pn.knp.lowerKey(nk);
					Node sbro = n.pn.knp.get(tk);
					Node nv = n.knp.get(nk);
					sbro.knp.put(tk, sbro.rm);
					sbro.knp.put(nk, nv);
					sbro.inputKey(tk);
					sbro.inputKey(nk);
					nv.pn = sbro;
					n.rm.pn = sbro;
					
					sbro.rm = n.rm;
					sbro.numofchild++;
					sbro.numofchild++;

					n.pn.knp.remove(tk, sbro);
					n.pn.RemoveKey(tk);
					n.pn.numofchild--;
					n.pn.rm = sbro;
				} else {	// n이 중간 자식
					int nk = n.knp.firstKey();
					int tk = n.pn.knp.higherKey(nk);
					Node bbro;
					if (n.pn.knp.higherKey(tk) == null) {
						bbro = n.pn.rm;
					} else {
						int tk2 = n.pn.knp.higherKey(tk);
						bbro = n.pn.knp.get(tk2);
					}
					Node nv = n.knp.get(nk);
					bbro.knp.put(nk, nv);
					bbro.knp.put(tk, n.rm);
					bbro.inputKey(tk);
					bbro.inputKey(nk);
					nv.pn = bbro;
					n.rm.pn = bbro;
					bbro.numofchild++;
					bbro.numofchild++;

					n.pn.knp.remove(tk, n);
					n.pn.RemoveKey(tk);
					n.pn.numofchild--;
				}
				
				if(n.pn.isUnderflow())
					Reconstruction(n.pn, k, v);
			}else{
				// n의 key 개수는 2개 
				if(n.pn.knp.get(n.pn.knp.firstKey()) == n){ // n이 첫번째 자식
					if(n == Firstleaf){
						Node bbro = n.rm;
						n.knv.remove(k, v);
						n.RemoveKey(k);
						int nk = n.knv.firstKey();
						long nv = n.knv.get(nk);
						bbro.knv.put(nk, nv);
						bbro.inputKey(nk);
						int c = n.pn.knp.firstKey();
						n.pn.knp.remove(c, n);
						n.pn.RemoveKey(c);
						n.pn.numofchild--;
						Firstleaf = bbro;
					}
					else{
						Node bbro = n.rm;
						n.knv.remove(k, v);
						n.RemoveKey(k);
						int nk = n.knv.firstKey();
						long nv = n.knv.get(nk);
						bbro.knv.put(nk, nv);
						bbro.inputKey(nk);	
						PreLeafNode(n).rm = bbro;
						int c = n.pn.knp.firstKey();
						n.pn.knp.remove(c, n);
						n.pn.RemoveKey(c);
						n.pn.numofchild--;
					}
				} else if (n.pn.rm == n) { // n이 마지막 자식
					if(n.rm == null){
						if(k == n.knv.firstKey()){
							Node pren = PreLeafNode(n);
							n.knv.remove(k, v);
							n.RemoveKey(k);
							int nk = n.knv.firstKey();
							long nv = n.knv.get(nk);
							pren.knv.put(nk, nv);
							pren.inputKey(nk);
							pren.rm = null;
							int dtmp = n.pn.knp.lowerKey(nk);
							n.pn.knp.remove(dtmp, pren);
							n.pn.RemoveKey(dtmp);
							n.pn.numofchild--;
							n.pn.rm = pren;
						}else{
							Node pren = PreLeafNode(n);
							n.knv.remove(k, v);
							n.RemoveKey(k);
							int nk = n.knv.firstKey();
							long nv = n.knv.get(nk);
							pren.knv.put(nk, nv);
							pren.inputKey(nk);
							pren.rm = null;
							n.pn.knp.remove(nk, pren);
							n.pn.RemoveKey(nk);
							n.pn.numofchild--;
							n.pn.rm = pren;
						}
					}else{
						if(k == n.knv.firstKey()){
							Node bbro = n.rm;
							Node pren = PreLeafNode(n);
							n.knv.remove(k, v);
							n.RemoveKey(k);
							int nk = n.knv.firstKey();
							long nv = n.knv.get(nk);
							pren.knv.put(nk, nv);
							pren.inputKey(nk);
							pren.rm = bbro;
							int dtmp = n.pn.knp.lowerKey(nk);
							n.pn.knp.remove(dtmp, pren);
							n.pn.RemoveKey(dtmp);
							n.pn.numofchild--;
							n.pn.rm = pren;
						}else{
							Node bbro = n.rm;
							Node pren = PreLeafNode(n);
							n.knv.remove(k, v);
							n.RemoveKey(k);
							int nk = n.knv.firstKey();
							long nv = n.knv.get(nk);
							pren.knv.put(nk, nv);
							pren.inputKey(nk);
							pren.rm = bbro;
							n.pn.knp.remove(nk, pren);
							n.pn.RemoveKey(nk);
							n.pn.numofchild--;
							n.pn.rm = pren;
						}
					}
				}else{	//n이 중간자식
					if(k == n.knv.firstKey()){
						Node bbro = n.rm;
						Node pren = PreLeafNode(n);
						n.knv.remove(k,v);
						n.RemoveKey(k);
						int nk = n.knv.firstKey();
						long nv = n.knv.get(nk);
						bbro.knv.put(nk, nv);
						bbro.inputKey(nk);
						pren.rm = bbro;
						int c = n.pn.knp.lowerKey(nk);
						int e = n.pn.knp.higherKey(nk);
						
						n.pn.knp.remove(c,pren);
						n.pn.RemoveKey(c);
						n.pn.knp.remove(e,n);
						n.pn.RemoveKey(e);
						n.pn.knp.put(nk, pren);
						n.pn.inputKey(nk);
						n.pn.numofchild--;				
					}else{
						Node bbro = n.rm;
						Node pren = PreLeafNode(n);
						n.knv.remove(k,v);
						n.RemoveKey(k);
						int nk = n.knv.firstKey();
						long nv = n.knv.get(nk);
						bbro.knv.put(nk, nv);
						bbro.inputKey(nk);
						pren.rm = bbro;
						int c = nk;
						int e = n.pn.knp.higherKey(c);
						
						n.pn.knp.remove(c,pren);
						n.pn.RemoveKey(c);
						n.pn.knp.remove(e,n);
						n.pn.RemoveKey(e);
						n.pn.knp.put(c, pren);
						n.pn.inputKey(c);
						n.pn.numofchild--;
					}
				}
				
				if(n.pn.isUnderflow())
					Reconstruction(n.pn, k, v);
			}
		}
	
	}
	
	public static Node Sibling(Node n) {
		// 빌려줄 형제가 있으면 반환, 없으면 null
		if(n.isRoot()){
			return null;
		}else if (n.pn.isRoot() && n.pn.keys.size() == 1) {
			Node bbro = n.pn.rm;
			if (bbro.keys.size() > 2)
				return bbro;
			else
				return null;
			
		}else if(n.pn.rm == n){ // n이 마지막 형제, 그 이전 형제만 살펴본다
			int a = n.pn.keys.get(n.pn.keys.size()-1); // pn.keys 의 마지막 key값
			if(n.pn.knp.get(a).keys.size() > 2) return n.pn.knp.get(a);
			else return null;
		}else{
			Iterator<Integer> iter = n.pn.knp.keySet().iterator();
			int x;
			while(iter.hasNext()){
				x= iter.next();
				if(n.pn.knp.get(x) == n){
					if(n.pn.knp.firstKey() == x){ // n이 첫번째 자식, 그 다음 형제만 살펴본다
						Node bbro = n.pn.knp.get(n.pn.knp.higherKey(x));
						if(bbro.keys.size() > 2) return bbro;
						else return null;
					}else{
						Node sbro = n.pn.knp.get(n.pn.knp.lowerKey(x));
						if(sbro.keys.size() > 2) return sbro;
						else{
							if(n.pn.knp.higherKey(x) == null){
								Node bbro = n.rm;
								if(bbro.keys.size() > 2) return bbro;
								else return null;
							}else{
								Node bbro = n.pn.knp.get(n.pn.knp.higherKey(x));
								if(bbro.keys.size() > 2) return bbro;
								else return null;
							}
						}
					}
				}
			}
			
			// n이 맨 마지막 자식일 경우
			int tmp = n.keys.get(0);
			Node sbro = n.pn.knp.get(tmp);
			if (sbro != null) {
				if (sbro.keys.size() > 2)
					return sbro;
				else
					return null;
			} else {
				sbro = n.pn.knp.get(n.pn.knp.lowerKey(tmp));
				if (sbro.keys.size() > 2)
					return sbro;
				else
					return null;
			}
		}
	}
	
	public static void insert(int key, long value) {
		if(root == null) {
			root = new Node();
			Firstleaf = root;
			root.knv.put(key, value);
			root.inputKey(key);
		}else {
			Node temp = Navigator(root,key);
			if(temp == null) return;
			
			if(temp.keys.size()<5) {
				temp.knv.put(key, value);
				temp.inputKey(key);
				if(temp.isOverflow()){
					splitNode(temp);
				}
			}
		}
	}

	public static String Search(Node n, int searchkey) {
		if(n.isLeaf()) {
			if(n.knv.containsKey(searchkey))
				return String.valueOf(n.knv.get(searchkey));
			else return "N/A";
		}else {
			for(int i=0;i<n.keys.size();i++){
				int temp = n.keys.get(i);
				if(searchkey < temp){
					return Search(n.knp.get(temp), searchkey);
				}
			}
			return Search(n.rm, searchkey);
		}
	}
	
	public static Node Delete_finder(Node n, int key) {

		if (n.isLeaf()) {
			if (n.keys.contains(key)) {
				return n;
			} else {
				System.out.println("삭제할 key값이 없습니다.");
				return null;
			}
		} else {
			int Fkey = n.knp.firstKey();
			if (Fkey > key) {
				return Delete_finder(n.knp.get(Fkey), key);
			} else {
				Iterator<Integer> iter = n.knp.keySet().iterator();
				while (iter.hasNext()) {
					int temp = iter.next();
					if (key < temp) {
						return Delete_finder(n.knp.get(temp), key);
					}
				}
				return Delete_finder(n.rm, key);
			}
		}

	}
	
	public static Node Navigator(Node n ,int key) {
		if(n.keys.contains(key)) {
			System.out.println("중복된 key입력"); 
			return null;
		}else{
			if(n.isLeaf()) {
				return n;
			}else {
				int Fkey = n.knp.firstKey();
				if(Fkey > key) {
					return Navigator(n.knp.get(Fkey), key);
				}else {
					Iterator<Integer> iter = n.knp.keySet().iterator();
					while(iter.hasNext()) {
						int temp = iter.next();
						if(key < temp) {
							return Navigator(n.knp.get(temp), key);
						}
					}
					return Navigator(n.rm, key);
				}
			}
		}
	}

	public static void splitNode(Node nod) {
		/* split occur both of leaf & internal */ 
		
		if(nod.isLeaf()) {	// Leaf끼리 연결 검사
			if(nod.isRoot()) {
				Node temp = new Node(); //new root
				Node Ltemp = new Node();
				Node Rtemp = new Node();
				TreeMap<Integer, Long> Lsknv = new TreeMap<>();
				TreeMap<Integer, Long> Rsknv = new TreeMap<>();
				int Midkey = nod.keys.get(2);
				for(int i=0;i<nod.keys.size();i++) {
					int tmp = nod.keys.get(i);
					if(i<2) {
						Lsknv.put(tmp,nod.knv.get(tmp));
						Ltemp.inputKey(tmp);
					}else if(i>=2 && i<5) {
						Rsknv.put(tmp, nod.knv.get(tmp));
						Rtemp.inputKey(tmp);
					}
				}
				temp.knp.put(Midkey,Ltemp);
				temp.inputKey(Midkey);
				temp.rm = Rtemp;
				temp.numofchild++;
				temp.numofchild++;
							
				Ltemp.knv = Lsknv;
				Rtemp.knv = Rsknv;
				Ltemp.pn = temp;
				Rtemp.pn = temp;
				
				root = temp;
				Firstleaf = Ltemp;
				Ltemp.rm = Rtemp;
			}else{
				// leaf && non-root
				Node Ltemp = new Node();
				Node Rtemp = new Node();
				TreeMap<Integer, Long> Lsknv = new TreeMap<>();
				TreeMap<Integer, Long> Rsknv = new TreeMap<>();
				int Midkey = nod.keys.get(2);
				for(int i=0;i<nod.keys.size();i++) {
					int tmp = nod.keys.get(i);
					if(i<2) {
						Lsknv.put(tmp,nod.knv.get(tmp));
						Ltemp.inputKey(tmp);
					}
					else if(i>=2 && i<5) {
						Rsknv.put(tmp, nod.knv.get(tmp));
						Rtemp.inputKey(tmp);
					}
				}
				
				// leaf노드 끼리 연결
				if(PreLeafNode(nod) == null){ // split node is first leaf-node
					Firstleaf = Ltemp;
					Ltemp.rm = Rtemp;
					Rtemp.rm = nod.rm;
				}else {
					PreLeafNode(nod).rm = Ltemp;
					Ltemp.rm = Rtemp;
					if(nod.rm != null) {
						Rtemp.rm = nod.rm;
					}
				}
				
				// nod.pn에 연결
				nod.pn.knp.put(Midkey, Ltemp);
				nod.pn.inputKey(Midkey);
				
				if(nod.pn.rm == nod){
					nod.pn.rm = Rtemp;
					nod.pn.numofchild++;
					Ltemp.knv = Lsknv;
					Rtemp.knv = Rsknv;
					Ltemp.pn = nod.pn;
					Rtemp.pn = nod.pn;
				}else{
					int pivot = nod.rm.knv.firstKey();
					nod.pn.knp.replace(pivot, nod, Rtemp);
					nod.pn.numofchild++;
					Ltemp.knv = Lsknv;
					Rtemp.knv = Rsknv;
					Ltemp.pn = nod.pn;
					Rtemp.pn = nod.pn;
				}
				
				if(nod.pn.isOverflow()){	// split후 부모에게서 오버플로우가 발생하는지 검사
					splitNode(nod.pn);
				}
			}
		}else { // internal Node -> 부모에게서 overflow 발생 검사
			if(nod.isRoot()) {
				Node temp = new Node(); //new root
				Node Ltemp = new Node();
				Node Rtemp = new Node();
				TreeMap<Integer, Node> Lsknp = new TreeMap<>();
				TreeMap<Integer, Node> Rsknp = new TreeMap<>();
				int Midkey = nod.keys.get(2);
				for(int i=0;i<nod.keys.size();i++) {
					int tmp = nod.keys.get(i);
					if(i<2) {
						Lsknp.put(tmp,nod.knp.get(tmp));
						Ltemp.numofchild++;
						Ltemp.inputKey(tmp);
						nod.knp.get(tmp).pn = Ltemp;
					}else if(i==2){
						//nothing
					}else if(i<5) {
						Rsknp.put(tmp, nod.knp.get(tmp));
						Rtemp.numofchild++;
						Rtemp.inputKey(tmp);
						nod.knp.get(tmp).pn = Rtemp;
					}
				}
				temp.knp.put(Midkey,Ltemp);
				temp.inputKey(Midkey);
				temp.rm = Rtemp;
				temp.numofchild++;
				temp.numofchild++;
							
				Ltemp.knp = Lsknp;
				Rtemp.knp = Rsknp;
				Ltemp.pn = temp;
				Rtemp.pn = temp;
				
				Ltemp.rm = nod.knp.get(Midkey);
				nod.knp.get(Midkey).pn = Ltemp;
				Ltemp.numofchild++;
				Rtemp.rm = nod.rm;
				nod.rm.pn = Rtemp;
				Rtemp.numofchild++;
				
				root = temp;
			}else{	// internal && non root
				Node Ltemp = new Node();
				Node Rtemp = new Node();
				TreeMap<Integer, Node> Lsknp = new TreeMap<>();
				TreeMap<Integer, Node> Rsknp = new TreeMap<>();
				int Midkey = nod.keys.get(2);
				for(int i=0;i<nod.keys.size();i++) {
					int tmp = nod.keys.get(i);
					if(i<2) {
						Lsknp.put(tmp,nod.knp.get(tmp));
						Ltemp.numofchild++;
						Ltemp.inputKey(tmp);
						nod.knp.get(tmp).pn = Ltemp;
					}else if(i==2){
						//nothing
					}else if(i<5) {
						Rsknp.put(tmp, nod.knp.get(tmp));
						Rtemp.numofchild++;
						Rtemp.inputKey(tmp);
						nod.knp.get(tmp).pn = Rtemp;
					}
				}
				
				nod.pn.knp.put(Midkey, Ltemp);
				nod.pn.inputKey(Midkey);
				
				if(nod.pn.rm == nod){
					nod.pn.rm = Rtemp;
					nod.pn.numofchild++;
				}
				else{
					int pivot = nod.pn.knp.higherKey(Midkey);
					nod.pn.knp.replace(pivot, nod, Rtemp);
					nod.pn.numofchild++;
				}
								
				Ltemp.pn = nod.pn;
				Rtemp.pn = nod.pn;
				Ltemp.knp = Lsknp;
				Rtemp.knp = Rsknp;
			
				Ltemp.rm = nod.knp.get(Midkey);
				nod.knp.get(Midkey).pn = Ltemp;
				Ltemp.numofchild++;
				Rtemp.rm = nod.rm;
				nod.rm.pn = Rtemp;
				Rtemp.numofchild++;
				
				if(nod.pn.isOverflow()){	// split후 부모에게서 오버플로우가 발생하는지 검사
					splitNode(nod.pn);
				}
			}
		}
	}
	
	public static Node PreLeafNode(Node n) {
		if(n == Firstleaf) {
			return null;
		}else {
			int c = n.pn.keys.get(0);
			if(n == n.pn.knp.get(c)) {
				Node gn = n.pn.pn;
				if(gn.knp.lowerKey(c) != null) {
					int a = gn.knp.lowerKey(c);
					return gn.knp.get(a).rm;
				}else {
					Node temp = PreLeafNode(n.pn);
					while(!temp.isLeaf()){
						temp = temp.rm;
					}		
					return temp;
				}
			}else{
				return n.pn.knp.get(n.keys.get(0));
			}
		}
	}
	
}

