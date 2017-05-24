package estructuras;

import java.util.NoSuchElementException;

/**
 * Clase que representa una lista sencillamente encadenada
 * @param <T>
 */
public class ListaEncadenada<T>
{
	protected int n;        // number of elements on list
	private Node pre;     // sentinel before first item
	private Node post;    // sentinel after last item

	public ListaEncadenada() {
		pre  = new Node();
		post = new Node();
		pre.next = post;
		post.prev = pre;
	}

	// linked list node helper data type
	private class Node {
		private T item;
		private Node next;
		private Node prev;
	}

	public boolean isEmpty()    { return n == 0; }
	public int size()           { return n;      }

	public T first(){
		return pre.next.item;
	}
	
	public void clear(){
		pre.next = post;
		post.prev = pre;
		n = 0;
	}
	
	// add the item to the list
	public void add(T item) {
		Node last = post.prev;
		Node x = new Node();
		x.item = item;
		x.next = post;
		x.prev = last;
		post.prev = x;
		last.next = x;
		n++;
	}
	
	public boolean contains(T item){
		if (n>0){
			Node pr = pre.next;
			Node ul = post.prev;
			while (pr!=post && ul!= pre){
				if (pr.item.equals(item)||ul.item.equals(item)){
					return true;
				}
				pr=pr.next;
				ul=ul.prev;
			}
		}
		return false;
	}
	
	public void addFirst(T item){
		Node first = pre.next;
		Node x = new Node();
		x.item = item;
		x.next = first;
		first.prev = x;
		x.prev = pre;
		pre.next = x;
		n++;
	}
	
	/**
	 * Elimina el primer nodo y retorna su elemento
	 */
	public T removeFirst( )
	{
		T resp = null;
		if(pre.next != null)
		{
			if (pre.next.next==null)
			{
				resp = pre.next.item;
				pre.next = null;
				post.prev = null;
			}
			else{
				resp = pre.next.item;
				pre.next = pre.next.next;
			}
			n--;
		}
		return resp;
	}


	public IIterator<T> iterador()  { return new IteradorLista(); }

	// assumes no calls to DoublyLinkedList.add() during iteration
	private class IteradorLista implements IIterator<T> {
		private Node current      = pre.next;  // the node that is returned by next()
		private Node lastAccessed = null;      // the last node to be returned by prev() or next()
		// reset to null upon intervening remove() or add()
		private int index = 0;

		public boolean hasNext()      { return index < n; }
		public boolean hasPrevious()  { return index > 0; }
		public int previousIndex()    { return index - 1; }
		public int nextIndex()        { return index;     }

		public T next() {
			if (!hasNext()) throw new NoSuchElementException();
			lastAccessed = current;
			T item = current.item;
			current = current.next; 
			index++;
			return item;
		}

		public T previous() {
			if (!hasPrevious()) throw new NoSuchElementException();
			current = current.prev;
			index--;
			lastAccessed = current;
			return current.item;
		}

		// replace the item of the element that was last accessed by next() or previous()
		// condition: no calls to remove() or add() after last call to next() or previous()
		public void set(T item) {
			if (lastAccessed == null) throw new IllegalStateException();
			lastAccessed.item = item;
		}

		// remove the element that was last accessed by next() or previous()
		// condition: no calls to remove() or add() after last call to next() or previous()
		public void remove() { 
			if (lastAccessed == null) throw new IllegalStateException();
			Node x = lastAccessed.prev;
			Node y = lastAccessed.next;
			x.next = y;
			y.prev = x;
			n--;
			if (current == lastAccessed)
				current = y;
			else
				index--;
			lastAccessed = null;
		}

		// add element to list 
		public void add(T item) {
			Node x = current.prev;
			Node y = new Node();
			Node z = current;
			y.item = item;
			x.next = y;
			y.next = z;
			z.prev = y;
			y.prev = x;
			n++;
			index++;
			lastAccessed = null;
		}
	}
}
