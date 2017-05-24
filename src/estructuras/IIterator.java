package estructuras;

public interface IIterator <E>{

	boolean hasNext();
	
	public E next();
	
	public void remove();
	
}
