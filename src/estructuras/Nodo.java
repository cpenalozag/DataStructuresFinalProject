	package estructuras;

/**
 * Representa un Nodo en un grafo.
 * Cada nodo consta de un vertice, una lista de arcos, un padre y un atributo de visitado.
 * @param K tipo del identificador de los vertices (comparable)
 * @param E tipo de la informacion asociada a los arcos
 */
public class Nodo<K extends Comparable<K>,E extends Comparable<E>> {

	/**
	 * Llave del vertice
	 */
	private K vertice;

	/**
	 * Lista de arcos
	 */
	private ListaEncadenada<Arco<K,E>> arcos;

	/**
	 * Nodo padre
	 */
	private Nodo<K,E> padre;

	/**
	 * True si el nodo ya fue visitado, false de lo contrario.
	 */
	private boolean visitado;

	/**
	 * Crea un nuevo nodo con la llave dada por parametro y una lista de arcos inicialmente vacia.
	 * @param pVertice llave del nodo
	 */
	public Nodo(K pVertice) {
		this.vertice = pVertice;
		this.arcos = new ListaEncadenada<Arco<K,E>>();
	}

	/**
	 * Retorna la llave del nodo.
	 * @return vertice Llave del nodo
	 */
	public K darVertice() {
		return vertice;
	}

	/**
	 * Agrega un nuevo arco a la lista de arcos del nodo
	 * @param nodo Nodo hacia el que se agregara un arco
	 * @param peso Peso del arco
	 */
	public Arco<K,E> agregarArco(Nodo<K,E> nodo, Double peso) {
		if (!existeArco(nodo)) {
			Arco<K,E> nuevo = new Arco<K,E>(this, nodo, peso);
			arcos.add(nuevo);
			return nuevo;
		}
		return null;
	}
	
	/**
	 * Agrega un nuevo arco a la lista de arcos del nodo
	 * @param nodo Nodo hacia el que se agregara un arco
	 * @param pesos Pesos del arco
	 */
	public Arco<K,E> agregarArco(Nodo<K,E> nodo, E pesos) {
		if (!tieneArco(nodo, pesos)) {
			Arco<K,E> nuevo = new Arco<K,E>(this, nodo, pesos);
			arcos.add(nuevo);
			return nuevo;
		}
		return null;
	}

	public boolean removerArco(Nodo<K,E> nodo) {
		Arco<K,E> actual = null;
		IIterator<Arco<K, E>> it = arcos.iterador();
		while (it.hasNext()){
			actual = it.next();
			if (actual.darNodoFin().equals(nodo)){
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	public boolean removerArco(Arco<K, E> arco){
		Arco<K,E> actual = null;
		IIterator<Arco<K, E>> it = arcos.iterador();
		while (it.hasNext()){
			actual = it.next();
			if (actual.equals(arco)){
				it.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica si existe un arco hacia un nodo dado por parametro.
	 * @param comparar Nodo con el que se verificara la existencia de un arco
	 * @return true si existe un arco, false de lo contrario
	 */
	public boolean tieneArco(Nodo<K,E> comparar, E pesos){
		Arco<K,E> actual = null;
		IIterator<Arco<K, E>> it = arcos.iterador();
		while (it.hasNext()){
			actual = it.next();
			E infoActual = actual.darInformacion();
			int resp = infoActual.compareTo(pesos);
			boolean menor = resp<=0;
			if (actual.darNodoFin().equals(comparar) && menor){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifica si existe un arco hacia un nodo dado por parametro.
	 * @param comparar Nodo con el que se verificara la existencia de un arco
	 * @return true si existe un arco, false de lo contrario
	 */
	public boolean existeArco(Nodo<K,E> comparar){
		Arco<K,E> actual = null;
		IIterator<Arco<K, E>> it = arcos.iterador();
		while (it.hasNext()){
			actual = it.next();
			if (actual.darNodoFin().equals(comparar)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna la lista de arcos del nodo.
	 * @return arcos Lista de arcos
	 */
	public ListaEncadenada<Arco<K,E>> darArcos() {
		return arcos;
	}

	/**
	 * Retorna la cantidad de arcos saliendo del nodo
	 * @return tamaño de la lista de arcos
	 */
	public int darCantidadArcos() {
		return arcos.size();
	}

	/**
	 * Retorna el padre del nodo
	 * @return padre
	 */
	public Nodo<K,E> darPadre() {
		return padre;
	}

	/**
	 * Indica si el nodo ya fué visitado
	 * @return true si ya fue visitado, false de lo contrario
	 */
	public boolean fueVisitado() {
		return visitado;
	}

	/**
	 * Cambia el atributo de visitado
	 * @param isVisited nuevo estado de visitado
	 */
	public void cambiarVisitado(boolean isVisited) {
		this.visitado = isVisited;
	}

	/**
	 * Cambia el padre del nodo
	 * @param pPadre nuevo padre
	 */
	public void cambiarPadre(Nodo<K,E> pPadre) {
		this.padre = pPadre;
	}
}