package estructuras;


/**
 *  The {@code LinearProbingHashST} class represents a symbol table of generic
 *  key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}�setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a linear probing hash table. It requires that
 *  the key type overrides the {@code equals()} and {@code hashCode()} methods.
 *  The expected time per <em>put</em>, <em>contains</em>, or <em>remove</em>
 *  operation is constant, subject to the uniform hashing assumption.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/34hash">Section 3.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  For other implementations, see {@link ST}, {@link BinarySearchST},
 *  {@link SequentialSearchST}, {@link BST}, {@link RedBlackBST}, and
 *  {@link SeparateChainingHashST},
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class TablaHashLP<Key, Value> implements ITablaHash<Key, Value> {
	private static final int INIT_CAPACITY = 1367;

	private int n;           // number of key-value pairs in the symbol table
	private int m;           // size of linear probing table
	private Key[] keys;      // the keys
	private Value[] vals;    // the values

	private int cantidadColisiones;


	/**
	 * Initializes an empty symbol table.
	 */
	public TablaHashLP() {
		this(INIT_CAPACITY);
	}

	/**
	 * Initializes an empty symbol table with the specified initial capacity.
	 *
	 * @param capacity the initial capacity
	 */
	@SuppressWarnings("unchecked")
	public TablaHashLP(int capacity) {
		m = capacity;
		n = 0;
		cantidadColisiones = 0;
		keys = (Key[])   new Object[m];
		vals = (Value[]) new Object[m];
	}

	public void cambiarCapacidad(int pCapacidad)
	{
		m = pCapacidad;
	}

	/**
	 * Returns the number of key-value pairs in this symbol table.
	 *
	 * @return the number of key-value pairs in this symbol table
	 */
	public int size() {
		return n;
	}

	/**
	 * Returns true if this symbol table is empty.
	 *
	 * @return {@code true} if this symbol table is empty;
	 *         {@code false} otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns true if this symbol table contains the specified key.
	 *
	 * @param  key the key
	 * @return {@code true} if this symbol table contains {@code key};
	 *         {@code false} otherwise
	 * @throws NullPointerException if {@code key} is {@code null}
	 */
	public boolean contains(Key key) {
		if (key == null) throw new NullPointerException("Argument to contains() is null");
		return get(key) != null;
	}

	// hash function for keys - returns value between 0 and M-1
	public int hash(Key key) {
		return ((key.hashCode() & 0x7fffffff)) % m;
	}

	// resizes the hash table to the given capacity by re-hashing all of the keys
	public void resize(int capacity) {
		TablaHashLP<Key, Value> temp = new TablaHashLP<Key, Value>(capacity);
		for (int i = 0; i < m; i++) {
			if (keys[i] != null) {
				temp.put(keys[i], vals[i]);
			}
		}
		keys = temp.keys;
		vals = temp.vals;
		m    = temp.m;
	}

	/**
	 * Inserts the specified key-value pair into the symbol table, overwriting the old 
	 * value with the new value if the symbol table already contains the specified key.
	 * Deletes the specified key (and its associated value) from this symbol table
	 * if the specified value is {@code null}.
	 *
	 * @param  key the key
	 * @param  val the value
	 * @throws NullPointerException if {@code key} is {@code null}
	 */
	public void put(Key key, Value val) {
		if (key == null) throw new NullPointerException("First argument to put() is null");

		if (val == null) {
			delete(key);
			return;
		}

		// double table size if 50% full
		if (n >= m/2) resize(2*m);

		int i;

		for (i = hash(key); keys[i] != null; i = (i + 1) % m,cantidadColisiones++) {
			if (keys[i].equals(key)) {
				vals[i] = val;
				return;
			}
		}
		keys[i] = key;
		vals[i] = val;
		n++;
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with {@code key};
	 *         {@code null} if no such value
	 * @throws NullPointerException if {@code key} is {@code null}
	 */
	public Value get(Key key) {
		if (key == null) throw new NullPointerException("argument to get() is null");
		for (int i = hash(key); keys[i] != null; i = (i + 1) % m)
			if (keys[i].equals(key))
				return vals[i];
		return null;
	}

	/**
	 * Removes the specified key and its associated value from this symbol table     
	 * (if the key is in this symbol table).    
	 *
	 * @param  key the key
	 * @throws NullPointerException if {@code key} is {@code null}
	 */
	public void delete(Key key) {
		if (key == null) throw new NullPointerException("argument to delete() is null");
		if (!contains(key)) return;

		// find position i of key
		int i = hash(key);
		while (!key.equals(keys[i])) {
			i = (i + 1) % m;
		}

		// delete key and associated value
		keys[i] = null;
		vals[i] = null;

		// rehash all keys in same cluster
		i = (i + 1) % m;
		while (keys[i] != null) {
			// delete keys[i] an vals[i] and reinsert
			Key   keyToRehash = keys[i];
			Value valToRehash = vals[i];
			keys[i] = null;
			vals[i] = null;
			n--;
			put(keyToRehash, valToRehash);
			i = (i + 1) % m;
		}

		n--;

		// halves size of array if it's 12.5% full or less
		if (n > 0 && n <= m/8) resize(m/2);

		assert check();
	}
	
	public void clear(){
		m = INIT_CAPACITY;
		n = 0;
		cantidadColisiones = 0;
		keys = (Key[])   new Object[m];
		vals = (Value[]) new Object[m];
	}

	/**
	 * Returns all keys in this symbol table as an {@code Iterable}.
	 * To iterate over all of the keys in the symbol table named {@code st},
	 * use the foreach notation: {@code for (Key key : st.keys())}.
	 *
	 * @return all keys in this symbol table
	 */
	//    public IIterador<Value> values() {
	//        Queue<Value> queue = new Queue<Value>();
	//        for (int i = 0; i < m; i++)
	//            if (vals[i] != null) queue.enqueue(vals[i]);
	//        return (IIterador<Value>) queue;
	//    }

	// integrity check - don't check after each put() because
	// integrity not maintained during a delete()
	private boolean check() {

		// check that hash table is at most 50% full
		if (m < 2*n) {
			System.err.println("Hash table size m = " + m + "; array size n = " + n);
			return false;
		}

		// check that each key in table can be found by get()
		for (int i = 0; i < m; i++) {
			if (keys[i] == null) continue;
			else if (get(keys[i]) != vals[i]) {
				System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
				return false;
			}
		}
		return true;
	}

	public int darColisiones()
	{
		return cantidadColisiones;
	}

	/**
	 * Returns all keys in this symbol table
	 * To iterate over all of the keys in the symbol table named {@code st},
	 * use the foreach notation: {@code for (Key key : st.keys())}.
	 *
	 * @return all keys in this symbol table
	 */
	public IIterator<Key> keys() {
		
		Cola<Key> queue = new Cola<Key>();
		
		for (int i = 0; i < m; i++)
			if (keys[i] != null) queue.enqueue(keys[i]);
		return queue.iterador();
	}

	/**
	 * Returns all values in this symbol table
	 * To iterate over all of the keys in the symbol table named {@code st},
	 * use the foreach notation: {@code for (Key key : st.keys())}.
	 *
	 * @return all keys in this symbol table
	 */
	public IIterator<Value> values() {
		
		Cola<Value> queue = new Cola<Value>();
		IIterator<Key> it = keys();
		while (it.hasNext()){
			Key actual = it.next();
			queue.enqueue(get(actual));
		}
		return queue.iterador();
	}
	
}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/