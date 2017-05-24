package estructuras;

import java.util.NoSuchElementException;

/**
 *  The {@code Stack} class represents a last-in-first-out (LIFO) stack of generic items.
 *  It supports the usual <em>push</em> and <em>pop</em> operations, along with methods
 *  for peeking at the top item, testing if the stack is empty, and iterating through
 *  the items in LIFO order.
 *  <p>
 *  This implementation uses a singly-linked list with a static nested class for
 *  linked-list nodes. See {@link LinkedStack} for the version from the
 *  textbook that uses a non-static nested class.
 *  The <em>push</em>, <em>pop</em>, <em>peek</em>, <em>size</em>, and <em>is-empty</em>
 *  operations all take constant time in the worst case.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/13stacks">Section 1.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Item> the generic type of an item in this stack
 */
public class Stack<Item> extends ListaEncadenada<Item> {
	private int n;                // size of the stack

	/**
	 * Initializes an empty stack.
	 */
	public Stack() {
		super.n = 0;
	}

	/**
	 * Returns true if this stack is empty.
	 *
	 * @return true if this stack is empty; false otherwise
	 */
	public boolean isEmpty() {
		return super.first() == null;
	}

	/**
	 * Returns the number of items in this stack.
	 *
	 * @return the number of items in this stack
	 */
	public int size() {
		return n;
	}

	/**
	 * Adds the item to this stack.
	 *
	 * @param  item the item to add
	 */
	public void push(Item item) {
		super.addFirst(item);
		n++;
	}

	/**
	 * Removes and returns the item most recently added to this stack.
	 *
	 * @return the item most recently added
	 * @throws NoSuchElementException if this stack is empty
	 */
	public Item pop() {
		if (isEmpty()) throw new NoSuchElementException("Stack underflow");
		Item resp = super.removeFirst();
		n--;
		return resp;                   // return the saved item
	}


	/**
	 * Returns (but does not remove) the item most recently added to this stack.
	 *
	 * @return the item most recently added to this stack
	 * @throws NoSuchElementException if this stack is empty
	 */
	public Item peek() {
		if (isEmpty()) throw new NoSuchElementException("Stack underflow");
		return super.first();
	} 

	/**
	 * Returns an iterator to this stack that iterates through the items in LIFO order.
	 *
	 * @return an iterator to this stack that iterates through the items in LIFO order
	 */
	public IIterator<Item> iterador() {
		return super.iterador();
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