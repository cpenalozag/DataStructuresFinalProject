package estructuras;

import java.util.NoSuchElementException;

public class Cola<T> extends ListaEncadenada<T>{

    /**
     * Initializes an empty queue.
     */
    public Cola(){
    	n = super.n;
    }

    /**
     * Returns true if this queue is empty.
     * @return {@code true} if this queue is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return super.isEmpty();
    }

    /**
     * Returns the number of items in this queue.
     *
     * @return the number of items in this queue
     */
    public int size() {
        return super.size();
    }

    /**
     * Returns the item least recently added to this queue.
     *
     * @return the item least recently added to this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return super.first();
    }

    /**
     * Adds the item to this queue.
     *
     * @param  item the item to add
     */
    public void enqueue(T item) {
    	this.add(item);
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     *
     * @return the item on this queue that was least recently added
     * @throws NoSuchElementException if this queue is empty
     */
    public T dequeue() {
    	return (T) this.removeFirst();
    }

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     *
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    /**
	 * Crea un nuevo iterador
	 * @return iterador sencillo
	 */

	public IIterator<T> iterador() {
		IIterator<T> nuevo = super.iterador();
		return nuevo;
	}    
}