package estructuras;

public interface ITablaHash<K,V> {

	public void resize(int cadena);


	public int hash(K key);

	public int size();

	public boolean isEmpty();

	public boolean contains(K key);

	public V get(K key);

	public void put(K key, V value);

	public void delete(K key);



}
