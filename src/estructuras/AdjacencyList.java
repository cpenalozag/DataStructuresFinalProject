package estructuras;


public class AdjacencyList<K extends Comparable<K>, E extends Comparable<E>> {
	private TablaHashLP<Nodo<K,E>, ListaEncadenada<Arco<K,E>>> adjacencies = new TablaHashLP<>();

	public AdjacencyList() {
	}

	public void setAdjacencyList(TablaHashLP<Nodo<K,E>, ListaEncadenada<Arco<K,E>>> pAdjacencies) {
		
		IIterator<Nodo<K,E>> it = pAdjacencies.keys();
		ListaEncadenada<Arco<K, E>> actual;
		while (it.hasNext()){
			Nodo<K,E> llave = it.next();
			actual = pAdjacencies.get(llave);
			adjacencies.put(llave, actual);
		}
	}

	public void addEdge(Nodo<K,E> source, Nodo<K,E> target, E obj ) {
		ListaEncadenada<Arco<K,E>> list;
		if (!adjacencies.contains(source)) {
			list = new ListaEncadenada<Arco<K,E>>();
			adjacencies.put(source, list);
		} else {
			list = adjacencies.get(source);
		}
		list.add(new Arco<K,E>(source, target, obj));
	}


	public ListaEncadenada<Arco<K, E>> getAdjacent(Nodo<K,E> source) {
		return adjacencies.get(source);
	}


	public void reverseEdge(Arco<K,E> arc){
		IIterator<Arco<K, E>> it = adjacencies.get(arc.darNodoInicio()).iterador();
		while (it.hasNext()){
			Arco<K, E> actual = it.next();
			if (actual.equals(arc)){
				it.remove();
			}
		}

		addEdge(arc.darNodoFin(), arc.darNodoInicio(), arc.darInformacion());
	}

	public void reverseGraph() {
		setAdjacencies(getReversedList().adjacencies);
	}

	public AdjacencyList<K,E> getReversedList() {
		AdjacencyList<K,E> newlist = new AdjacencyList<K,E>();
		IIterator<ListaEncadenada<Arco<K, E>>> iter = adjacencies.values();
		while (iter.hasNext()){
			ListaEncadenada<Arco<K, E>> actual = iter.next();
			IIterator<Arco<K, E>> it = actual.iterador();
			while (it.hasNext()){
				Arco<K, E> act = it.next();
				newlist.addEdge(act.darNodoFin(), act.darNodoInicio(), act.darInformacion());
			}
		}
		return newlist;
	}

	public 	IIterator<Nodo<K,E>> getSourceNodeSet() {
		return adjacencies.keys();
	}

	/**
	 * 
	 * @return
	 */
	public ListaEncadenada<Arco<K, E>> getAllEdges() {
		ListaEncadenada<Arco<K, E>> arcos = new ListaEncadenada<Arco<K, E>>();
		IIterator<ListaEncadenada<Arco<K, E>>> listas = adjacencies.values();
		while (listas.hasNext()){
			ListaEncadenada<Arco<K, E>> actual = listas.next();
			IIterator<Arco<K, E>> itarcos = actual.iterador();
			while (itarcos.hasNext()){
				Arco<K, E> act = itarcos.next();
				arcos.add(act);
			}
		}
		return arcos;
	}

	private void setAdjacencies(TablaHashLP<Nodo<K,E>, ListaEncadenada<Arco<K, E>>> pAdjacencies) {
		this.adjacencies = pAdjacencies;
	}


	public void clear() {
		if (this.adjacencies != null) {
			adjacencies.clear();
		}
	}
	
	public IIterator<Nodo<K, E>> iteradorNodos (){
		return adjacencies.keys();
	}
	
}
