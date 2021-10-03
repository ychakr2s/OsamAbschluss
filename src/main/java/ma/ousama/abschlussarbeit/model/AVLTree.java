package ma.ousama.abschlussarbeit.model;

import java.util.ArrayList;

/**
 * @author Masud Taher
 * @version 1.0
 */

public class AVLTree<T extends Comparable<T>> {
	private class Node {
		private Node l; // linker Teilbaum
		private Node r; // rechter Teilbaum
		private int b; // hoehe(linker TB) - hoehe(rechter TB)
		private T v; // Wert am Knoten

		public Node(T v) {
			this.v = v;
		}
	}

	private Node w; // Wurzel


	public boolean isEmpty() {
		return w == null;
	}

	private int hoehe(Node k) {
		return k == null ? 0 : 1 + Math.max(hoehe(k.l), hoehe(k.r));
	}

	public int hoehe() {
		return hoehe(w);
	}

	public int size() {
		return size(w);
	}

	public int size(Node k) {
		return k == null ? 0 : 1 + size(k.l) + size(k.r);
	}

	public int total() {
		return 1 + 4 * size();
	}

	public T element() {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException();
		}
		Node tmp = new Node(w.v);
		return tmp.v;
	}

	public T next(T vSkript) {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException();
		}

		Node wurzel = w;

		int c = wurzel.v.compareTo(vSkript);

		//Achtung, kann sein dass das element nicht existiert
		while (c != 0){
			//Wenn Wurzel größer ist als v, suche im linken Teilbaum
			if (c > 0 ){
				try {
					wurzel = wurzel.l;
					c = wurzel.v.compareTo(vSkript);
				}
				catch (NullPointerException e){
					System.out.println("Knoten wurde nicht im AVL-Baum gefunden");
					throw new java.util.NoSuchElementException();
				}
			}

			//Sonst such im rechten Teilbaum
			else{
				try {
					wurzel = wurzel.r;
					c = wurzel.v.compareTo(vSkript);
				}
				catch (NullPointerException e){
					System.out.println("Knoten wurde nicht im AVL-Baum gefunden");
					throw new java.util.NoSuchElementException();
				}
			}
		}


		T res = null;
		//linker Nachfolger
		if (wurzel.l != null){
			res = wurzel.l.v;
		}

		//rechter Nachfolger
		if (wurzel.r != null){
			res = wurzel.r.v;
		}


		return res;
	}

	public T prev(T vSkript) {

		if (isEmpty()) {
			throw new java.util.NoSuchElementException();
		}

		Node wurzel = w;

		int c = wurzel.v.compareTo(vSkript);
		T prev = wurzel.v;
		//Achtung, kann sein dass das element nicht existiert
		while (c != 0){
			//Wenn Wurzel größer ist als v, suche im linken Teilbaum
			if (c > 0 ){
				try {
					prev = wurzel.v;
					wurzel = wurzel.l;
					c = wurzel.v.compareTo(vSkript);
				}
				catch (NullPointerException e){
					System.out.println("Knoten wurde nicht im AVL-Baum gefunden");
					throw new java.util.NoSuchElementException();
				}
			}

			//Sonst such im rechten Teilbaum
			else{
				try {
					prev = wurzel.v;
					wurzel = wurzel.r;
					c = wurzel.v.compareTo(vSkript);
				}
				catch (NullPointerException e){
					System.out.println("Knoten wurde nicht im AVL-Baum gefunden");
					throw new java.util.NoSuchElementException();
				}
			}
		}
		return prev;
	}


	public T naechste(T v) {
		ArrayList <T> inorder = new ArrayList<T>();
		linearisiereInOrder(inorder);

		return inorder.get(inorder.indexOf(v)+1);
	}


	public T vorgaenger(T v) {
		ArrayList <T> inorder = new ArrayList<T>();
		linearisiereInOrder(inorder);
		return inorder.get(inorder.indexOf(v)-1);
	}

	public void linearisiereInOrder(ArrayList<T> ps, Node k) {
		if (k != null) {
			linearisiereInOrder(ps, k.l);
			ps.add(k.v);
			linearisiereInOrder(ps, k.r);
		}
	}

	public void linearisiereInOrder(ArrayList<T> ps) {
		linearisiereInOrder(ps, w);
	}



	public boolean contains(T v) {
		Node k = w;
		while (k != null) {
			int c = k.v.compareTo(v);
			if (c == 0) { // Element gefunden
				return true;
			} else if (c > 0) { // falls k.v > v,
				k = k.l; // dann v im linken Teilbaum
			} else { // falls k.v < v,
				k = k.r; // dann v im rechten Teilbaum
			}
		}
		return false;
	}

	public boolean insert(T v) {
		return insert(w, null, false, v) >= 0;
	}

	private int insert(Node k, Node m, boolean istLinks, T v) {
		if (k == null) { // an leerem Teilbaum angekommen
			k = new Node(v); // dann wird hier ein neuer Knoten erzeugt
			if (m == null) { // nur moeglich, wenn w==null, also Baum leer
				w = k; // dann besteht der Baum jetzt aus genau einem Knoten
			} // ansonsten muessen wir untersuchen, ob der neue Knoten
			else if (istLinks) { // linkes oder rechtes Kind seiner Mutter wird.
				m.l = k;
			} else {
				m.r = k;
			}
			return 1; // Teilbaum mit k als Wurzel ist gewachsen
		}

		// Wenn wir hier angekommen sind, gilt k != null.

		int c = k.v.compareTo(v);
		if (c == 0) { // Wert v schon im Baum vorhanden
			return -1; // kein Einfuegen, keine Hoehenaenderung
		} else if (c > 0) { // k.v > v => in linken Teilbaum einfuegen
			int r = insert(k.l, k, true, v);
			if (r == 1) { // Wenn sich die Hoehe des Teilbaums aendert,
				k.b++; // erhoeht sich der balance-Wert von k.
			} else { // sonst keine Einfuegung bzw. keine Hoehenaenderung
				return r; // im Teilbaum (kein Ausgleich bei k noetig)
			}
		} else { // k.v < v => in rechten Teilbaum einfuegen
			int r = insert(k.r, k, false, v);
			if (r == 1) { // Wenn sich die Hoehe des Teilbaums aendert,
				k.b--; // verringert sich der balance-Wert von k.
			} else { // sonst keine Einfuegung bzw. keine Hoehenaenderung
				return r; // im Teilbaum (kein Ausgleich bei k noetig)
			}
		}

		// Die Hoehe des Teilbaumes, in den eingefuegt wurde, ist gewachsen.

		if (k.b == 0) { // Baum balanciert => Fall 2
			return 0; // Hoehe des Baumes mit Wurzel k ist gleich geblieben
		}

		if (k.b == 1 || k.b == -1) { // AVL Kriterium noch erfuellt => Fall 1
			return 1; // Hoehe des Baumes mit Wurzel k ist gewachsen
		}

		// Wenn wir hier angekommen sind, ist die Hoehe des Baumes mit k als Wurzel
		// erhoeht,
		// und das AVL-Kriterium ist verletzt => Fall 3

		if (k.b == 2) { // der linke Teilbaum ist hoeher als der rechte
			if (k.l.b == 1) {
				rotateLL(k, m); // Fall 3a
			} else if (k.l.b == -1) {
				rotateLR(k, m); // Fall 3b
			} else {
				assert false : "Illegaler Zustand im AVL-Baum";
			}
		} else if (k.b == -2) { // der rechte Teilbaum ist hoeher als der linke
			if (k.r.b == -1) {
				rotateRR(k, m); // Fall symmetrisch zu 3a
			} else if (k.r.b == 1) {
				rotateRL(k, m); // Fall symmetrisch zu 3b
			} else {
				assert false : "Illegaler Zustand im AVL-Baum";
			}
		} else {
			assert false : "Illegaler Zustand im AVL-Baum";
		}

		// Ausgleichsrotationen fuehren beim Einfuegen stets dazu, dass der Teilbaum mit
		// k als Wurzel nicht gewachsen ist.
		return 0;
	}

	public boolean remove(T v) {
		return remove(w, null, false, null, v) >= 0;
	}

	private int remove(Node k, Node m, boolean istLinks, Node kAlt, T v) {
		if (k == null) { // an leerem Teilbaum angekommen
			return -1; // Element nicht vorhanden: fertig, keine Hoehenaenderung
		}

		// Wenn wir hier angekommen sind, gilt k != null.

		if (kAlt != null) { // zum Ersetzen groesstes Element des Teilbaums loeschen
			if (k.r == null) { // k enthaelt groesstes Element, wird entfernt
				// k ist Blatt oder hat links Blatt als Kind
				kAlt.v = k.v; // ueberschreibe zu loeschenden Wert
				// m ist nicht null, da wir im linken Teilbaum von kAlt sind
				if (m == kAlt) {
					m.l = k.l; // k ist linkes Kind von kAlt
				} else {
					m.r = k.l; // k liegt auf rechtem Pfad im linken Teilbaum
				}
				return 1; // Hoehe des Teilbaums hat sich geaendert
			} else { // suche weiter nach groesstem Element
				int r = remove(k.r, k, false, kAlt, null);
				if (r == 1) { // aendert sich Hoehe des Teilbaums,
					// <hier Code einfuegen>
				} else {
					return r; // Element nicht vorhanden oder Hoehe ist gleich geblieben
				}
			}
		} else { // v suchen und entfernen
			int c = k.v.compareTo(v);
			if (c == 0) { // Wert v gefunden
				if (k.l == null || k.r == null) { // k ist Blatt oder hat nur 1 Kind
					Node kind = (k.l == null) ? k.r : k.l;
					if (m == null) { // k ist w
						w = kind;
					} else { // k hat Mutter
						if (istLinks) {
							m.l = kind;
						} else {
							m.r = kind;
						}
					}
					return 1; // Hoehe des Teilbaums hat sich geaendert
				} else { // k hat 2 Teilbaeume => Fall (c)
					// entferne den groessten Wert im linken Teilbaum
					// Verbesserungsmoeglichkeit: entferne kleinsten Wert im rechten, falls dieser
					// hoeher ist
					int r = remove(k.l, k, true, k, null);
					if (r == 1) { // aendert sich Hoehe des Teilbaums,
						k.b--; // verringert sich der balance-Wert von k.
					} else {
						return r; // Hoehe des Baumes mit Wurzel k ist gleich geblieben
					}
				}
			} else if (c > 0) { // k.v > v => aus linkem Teilbaum entfernen
				int r = remove(k.l, k, true, null, v);
				if (r == 1) { // Wenn sich die Hoehe des Teilbaums aendert,
					k.b--; // verringert sich der balance-Wert von k.
				} else {
					return r; // Hoehe des Baumes mit Wurzel k ist gleich geblieben
				}
			} else { // k.v < v => aus rechtem Teilbaum entfernen
				// <hier Code einfuegen>
			}
		}

		// Die Hoehe des Teilbaumes, aus dem entfernt wurde, hat sich verringert.

		if (k.b == 0) { // Baum balanciert => Fall 2
			// <hier Code einfuegen>
		}

		if (k.b == 1 || k.b == -1) { // AVL Kriterium noch erfuellt => Fall 1
			// <hier Code einfuegen>
		}

		// Das AVL-Kriterium ist verletzt => Fall 3

		// Fall 3a/b/c
		if (k.b == 2) { // der linke Teilbaum ist hoeher als der rechte
			if (k.l.b == 1) {
				rotateLL(k, m); // Fall 3a
			} else if (k.l.b == -1) {
				rotateLR(k, m); // Fall 3b
			} else if (k.l.b == 0) {
				rotateLL(k, m); // Fall 3c
				return 0; // keine Hoehenaenderung fuer Teilbaum mit Wurzel k
			} else {
				assert false : "Illegaler Zustand im AVL-Baum";
			}
		} else if (k.b == -2) { // der rechte Teilbaum ist hoeher als der linke
			// <hier Code einfuegen>
		} else {
			assert false : "Illegaler Zustand im AVL-Baum";
		}

		// Ausgleichsrotationen (ausser fuer Fall 3c) fuehren beim Entfernen stets dazu,
		// dass sich die
		// Hoehe des Teilbaum mit k als Wurzel verringert.
		return 1;
	}

	private void rotateLL(Node k, Node m) { // Fall 3a/c
		Node v = k.l;
		Node a1 = v.l;
		Node a2 = v.r;
		Node b = k.r;

		if (m == null) { // wenn Rotation an Wurzel
			w = v; // dann haben wir jetzt eine neue Wurzel
		} else if (k == m.l) { // ansonsten hat m ein neues linkes oder rechtes Kind
			m.l = v;
		} else {
			m.r = v;
		}

		// Verweise umhaengen
		v.r = k;
		v.l = a1;
		k.l = a2;
		k.r = b;

		if (v.b == 1) { // Fall 3a
			v.b = 0; // beide Teilbaeume sind jetzt ausgeglichen
			k.b = 0;
		} else if (v.b == 0) { // Fall 3c
			v.b = -1;
			k.b = 1;
		} else {
			assert false : "Illegaler Zustand im AVL-Baum";
		}
	}

	private void rotateRR(Node k, Node m) { // symmetrisch zu Fall 3a/c
		Node v = k.r;
		Node a1 = v.l;
		Node a2 = v.r;
		Node b = k.l;

		if (m == null) { // wenn Rotation an Wurzel
			w = v; // dann haben wir jetzt eine neue Wurzel
		} else if (k == m.l) { // ansonsten hat m ein neues linkes oder rechtes Kind
			m.l = v;
		} else {
			m.r = v;
		}

		// Verweise umhaengen
		v.l = k;
		v.r = a2;
		k.r = a1;
		k.l = b;

		if (v.b == -1) { // Fall 3a
			v.b = 0; // beide Teilbaeume sind jetzt ausgeglichen
			k.b = 0;
		} else if (v.b == 0) { // Fall 3c
			v.b = 1;
			k.b = -1;
		} else {
			assert false : "Illegaler Zustand im AVL-Baum";
		}
	}

	private void rotateLR(Node k, Node m) { // Fall 3b
		Node v = k.l;
		Node u = v.r;
		Node a1 = v.l;
		Node a2 = u.l;
		Node a3 = u.r;
		Node b = k.r;
		int bAlt = u.b; // alte balance von u sichern, da u sich aendert

		if (m == null) { // wenn Rotation an Wurzel
			w = u; // dann haben wir jetzt eine neue Wurzel
		} else if (k == m.l) { // ansonsten hat m ein neues linkes oder rechtes Kind
			m.l = u;
		} else {
			m.r = u;
		}

		u.l = v;
		u.r = k;
		v.l = a1;
		v.r = a2;
		k.l = a3;
		k.r = b;

		switch (bAlt) {
		case 0: // hoehe(A2)=hoehe(A3)=h-1
			v.b = 0;
			k.b = 0;
			break;
		case 1: // hoehe(A2)=h-1, hoehe(A3)=h-2
			v.b = 0;
			k.b = -1;
			break;
		case -1: // hoehe(A2)=h-2, hoehe(A3)=h-1
			v.b = 1;
			k.b = 0;
			break;
		}
		u.b = 0;
	}

	private void rotateRL(Node k, Node m) { // symmetrisch zu Fall 3b
		Node v = k.r;
		Node u = v.l;
		Node a1 = v.r;
		Node a2 = u.r;
		Node a3 = u.l;
		Node b = k.l;
		int bAlt = u.b; // alte balance von u sichern, da u sich aendert

		if (m == null) { // wenn Rotation an Wurzel
			w = u; // dann haben wir jetzt eine neue Wurzel
		} else if (k == m.l) { // ansonsten hat m ein neues linkes oder rechtes Kind
			m.l = u;
		} else {
			m.r = u;
		}

		u.r = v;
		u.l = k;
		v.l = a2;
		v.r = a1;
		k.r = a3;
		k.l = b;

		switch (bAlt) {
		case 0: // hoehe(A2)=hoehe(A3)=h-1
			v.b = 0;
			k.b = 0;
			break;
		case 1: // hoehe(A2)=h-1, hoehe(A3)=h-2
			v.b = -1;
			k.b = -0;
			break;
		case -1: // hoehe(A2)=h-2, hoehe(A3)=h-1
			v.b = 0;
			k.b = 1;
			break;
		}
		u.b = 0;
	}

	public String toString() {
		return toString(w);
	}

	public String toString(Node k) {
		return k == null ? "" : "(" + toString(k.l) + k.v + toString(k.r) + ")";
	}
}