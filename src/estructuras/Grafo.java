package estructuras;

import mundo.Conexion;

/**
 * Clase que representa un dirigido con pesos en los arcos.
 * @param K tipo del identificador de los vertices (Comparable)
 * @param E tipo de la informacion asociada a los arcos

 */
public class Grafo<K extends Comparable <K>, E extends Comparable<E>> {

	private TablaHashLP<K,Nodo<K,E>> nodos;

	private TablaHashLP<K, ListaEncadenada<Arco<K,E>>> adjacencyList;

	private int cantidadArcos;
	
	private int maxComp;
	private int[] idComp;
	private Object[] llaves;

	public Grafo(int pTamanho) {
		adjacencyList = new TablaHashLP<>(pTamanho);
		nodos = new TablaHashLP<>(pTamanho);
	}

	/**
	 * Adds a vertex to the graph.
	 * @param vertex        vertex to add
	 */
	public boolean addVertex(K vertex) {
		if (nodos.contains(vertex)) {
			return false;
		}
		Nodo<K,E> nuevo = new Nodo<K,E>(vertex);
		nodos.put(vertex, nuevo);
		ListaEncadenada<Arco<K, E>> lista = new ListaEncadenada<Arco<K, E>>();
		adjacencyList.put(vertex, lista);
		return true;
	}

	public IIterator<K> darLlaves(){
		return adjacencyList.keys();
	}
	
	public IIterator<Nodo<K,E>> darNodos() {
		return nodos.values();
	}
	
	public IIterator<ListaEncadenada<Arco<K, E>>> darListasArcos(){
		return adjacencyList.values();
	}
	
	public ListaEncadenada<Arco<K, E>> darAdyacencias (K buscado){
		return adjacencyList.get(buscado);
	}

	/**
	 * Adds a weighted directed edge between two vertices in the graph.
	 * @param vertex1       vertex where the (directed) edge begins
	 * @param vertex2       vertex where the (directed) edge ends
	 * @param coleccion     coleccion de pesos del grafo
	 */
	public boolean addEdge(K vertex1, K vertex2, E coleccion) {
		if (!containsVertex(vertex1) || !containsVertex(vertex2)) {
			throw new RuntimeException("Vertex does not exist");
		}
		// add the edge
		Nodo<K,E> nodo1 = getNode(vertex1);
		Nodo<K,E> nodo2 = getNode(vertex2);
		Arco<K, E> nuevo = new Arco<K,E>(nodo1, nodo2, coleccion);
		if (nuevo!=null && !nodo1.tieneArco(nodo2,coleccion)){
			ListaEncadenada<Arco<K,E>> lista = adjacencyList.get(vertex1);
			lista.add((Arco<K, E>) nuevo);
			nodo1.agregarArco(nodo2, coleccion);
			cantidadArcos++;
			return true;
		}

		return false;
	}

	/**
	 * Remove a vertex from the graph.
	 * @param vertex to be removed
	 * @return true if the vertex was removed, false if no such vertex was found.
	 */
	public boolean removeVertex(K vertex) {
		if (!adjacencyList.contains(vertex)) {
			return false;
		}

		// get node to be removed
		final Nodo<K,E> toRemove = getNode(vertex);
		nodos.delete(vertex);
		// remove all incoming edges to node
		IIterator<K> iterator = adjacencyList.keys();
		while(iterator.hasNext()){
			K actual = iterator.next();
			ListaEncadenada<Arco<K, E>> lista = adjacencyList.get(actual);
			IIterator<Arco<K,E>> it = lista.iterador();
			while (it.hasNext()){
				Arco<K,E> arcoActual = it.next();
				if (arcoActual.darNodoFin().equals(toRemove)){
					it.remove();
				}
			}
		}
		// remove the node
		adjacencyList.delete(vertex);
		return true;
	}

	/**
	 * Method to remove a directed edge between two vertices in the graph.
	 * @param vertex1       vertex where the (directed) edge begins
	 * @param vertex2       vertex where the (directed) edge ends
	 * @return  true if the edge was removed, false if no such edge was found.
	 */
	public boolean removeEdge(K vertex1, K vertex2) {
		if (!containsVertex(vertex1) || !containsVertex(vertex2)) {
			return false;
		}

		Arco<K,E> arco = null;
		ListaEncadenada<Arco<K,E>> lista = adjacencyList.get(vertex1);
		IIterator<Arco<K, E>> it = lista.iterador();
		while (it.hasNext()){
			arco=it.next();
			if (arco.darNodoFin().darVertice().equals(vertex2)){
				it.remove();
				nodos.get(vertex1).removerArco(getNode(vertex2));
				cantidadArcos--;
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to get the number of vertices in the graph.
	 * @return      count of vertices
	 */
	public int vertexCount() {
		return adjacencyList.size();
	}

	/**
	 * Method to get the number of edges in the graph.
	 * @return      count of edges
	 */
	public int edgeCount() {
		return cantidadArcos;
	}

	/**
	 * Method to check if a vertex exists in the graph.
	 * @param vertex    vertex which is to be checked
	 * @return  returns true if the graph contains the vertex, false otherwise
	 */
	public boolean containsVertex(K vertex) {
		return adjacencyList.contains(vertex);
	}

	/**
	 * Method to check if an edge exists in the graph.
	 * @param vertex1       vertex where the (directed) edge begins
	 * @param vertex2       vertex where the (directed) edge ends
	 * @return   returns true if the graph contains the edge, false otherwise
	 */
	public boolean containsEdge(K vertex1, K vertex2) {
		if (!containsVertex(vertex1) || !containsVertex(vertex2)) {
			return false;
		}
		Nodo<K,E> nodo1 = getNode(vertex1);
		Nodo<K,E> nodo2 = getNode(vertex2);
		if (nodo1.existeArco(nodo2)){
			return true;
		}
		return false;
	}

	public Nodo<K,E> getNode(K value) {
		return nodos.get(value);
	}
	


	/**
	 *Retorna un grafo con los arcos invertidos
	 * @return Una lista de adjacencia de un nuevo grafo con arcos invertidos
	 */
	public TablaHashLP<K, ListaEncadenada<Arco<K,E>>> reversarGrafo()
	{
		TablaHashLP<K, ListaEncadenada<Arco<K,E>>> nuevaAdjacencia = new TablaHashLP<>();

		IIterator<K> iter = adjacencyList.keys();
		while (iter.hasNext()){
			K aux = iter.next();
			ListaEncadenada<Arco<K, E>> listaAdj = adjacencyList.get(aux);
			if(!listaAdj.isEmpty())
			{
				IIterator<Arco<K, E>> it = listaAdj.iterador();
				while(it.hasNext())
				{
					Arco<K,E> act = it.next();
					Nodo<K,E> desde = act.darNodoInicio();
					Nodo<K,E> hacia = act.darNodoFin();
					K nuevoDesde = hacia.darVertice();
					double peso = act.darPeso();
					Arco<K,E> nuevoArco = new Arco<>(hacia, desde, peso);

					if(nuevaAdjacencia.contains(nuevoDesde))
					{
						ListaEncadenada<Arco<K,E>> lista = nuevaAdjacencia.get(nuevoDesde);
						lista.add(nuevoArco);
					}
					else
					{
						ListaEncadenada<Arco<K,E>> nuevaLista = new ListaEncadenada<Arco<K,E>>();
						nuevaLista.add(nuevoArco);
						nuevaAdjacencia.put(nuevoDesde, nuevaLista);					
					}
					if(!nuevaAdjacencia.contains(aux))
					{
						ListaEncadenada<Arco<K,E>> nuevaLista = new ListaEncadenada<Arco<K,E>>();
						nuevaAdjacencia.put(aux, nuevaLista);
					}
				}
			}
			else
			{
				ListaEncadenada<Arco<K,E>> nuevaLista = new ListaEncadenada<Arco<K,E>>();
				nuevaAdjacencia.put(aux, nuevaLista);
			}
		}
		return nuevaAdjacencia;
	}
	
	public Kosaraju hacerKosaraju()
	{
		return new Kosaraju();
	}

	public String componentesConectados()
	{
		String mensaje = "";
		Kosaraju kosaraju = hacerKosaraju();
		idComp = kosaraju.darArregloIdComponenes();
		llaves = kosaraju.darArregloLlaves();
		maxComp = kosaraju.darNumeroComponentes();
		for(int i = 0; i < maxComp; i++)
		{
			int x = i+1;
			mensaje += "En el componente " + x + " estan las ciudades: \n {";
			for(int j = 0; j < idComp.length ; j++)
			{
				if(idComp[j] == i)
				{
					mensaje += llaves[j].toString() + ", ";
				}
			}
			
			mensaje += "} \n";
		}
		return mensaje;
	}
	
	public Cola<Object> ciudadesConectadasMST()
	{
		Cola<Object> ciudades = new Cola<Object>();
		String grupo = "";
		for(int i = 0; i < maxComp; i++)
		{
//			int x = i+1;
//			mensaje += "En el componente " + x + " estan las ciudades: \n {";
			for(int j = 0; j < idComp.length ; j++)
			{
				if(idComp[j] == i)
				{
					grupo += llaves[j].toString() + ", ";
					ciudades.enqueue(llaves[j]);
				}
			}
			Object c = new Object();
			String s = "cambio";
			c =s;
			ciudades.enqueue(c);
		}
		return ciudades;
		
	}
	
	/**
	 *  The {@code DijkstraSP} class represents a data type for solving the
	 *  single-source shortest paths problem in edge-weighted digraphs
	 *  where the edge weights are nonnegative.
	 *  <p>
	 *  This implementation uses Dijkstra's algorithm with a binary heap.
	 *  The constructor takes time proportional to <em>E</em> log <em>V</em>,
	 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
	 *  Afterwards, the {@code distTo()} and {@code hasPathTo()} methods take
	 *  constant time and the {@code pathTo()} method takes time proportional to the
	 *  number of edges in the shortest path returned.
	 *  <p>
	 *  For additional documentation,    
	 *  see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of    
	 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
	 *
	 *  @author Robert Sedgewick
	 *  @author Kevin Wayne
	 */
	public static class Dijkstra<K extends Comparable<K>,E extends Comparable<E>> {
		private TablaHashLP<K, Double> distTo;          // distTo[v] = distance  of shortest s->v path
		private TablaHashLP<K, Arco<K, E>> edgeTo;    // edgeTo[v] = last edge on shortest s->v path
		private TablaHashLP<K, Integer> vertices;
		private IndexMinPQ<Double> pq;    // priority queue of vertices

		/**
		 * Computes a shortest-paths tree from the source vertex {@code s} to every other
		 * vertex in the edge-weighted digraph {@code G}.
		 *
		 * @param  G the edge-weighted digraph
		 * @param  s the source vertex
		 * @throws IllegalArgumentException if an edge weight is negative
		 * @throws IllegalArgumentException unless {@code 0 <= s < V}
		 */
		public Dijkstra(Grafo<K, E> g, K s, String pesoPpal) {
			IIterator<ListaEncadenada<Arco<K, E>>> it = g.darListasArcos();
			while (it.hasNext()){
				ListaEncadenada<Arco<K, E>> listaActual = it.next();
				IIterator<Arco<K, E>> itLista = listaActual.iterador();
				while (itLista.hasNext()){
					Arco<K, E> arcActual = itLista.next();
					Double pesoActual = ((Conexion)arcActual.darInformacion()).darPeso(pesoPpal);
					if(pesoActual<0.0){
						System.out.println("Peso negativo");
						throw new IllegalArgumentException("El arco " + arcActual.darInformacion() + " tiene pesos negativos");
					}
				}
			}
			distTo = new TablaHashLP<>();
			edgeTo = new TablaHashLP<>();
			vertices = new TablaHashLP<>();
			IIterator<K> nodos = g.darLlaves();
			int i = 0;
			while(nodos.hasNext())
			{
				K key = nodos.next();

				vertices.put(key, i);
				distTo.put(key, Double.POSITIVE_INFINITY);
				i++;
			}
			distTo.put(s, 0.0);
			// relax vertices in order of distance from s
			pq = new IndexMinPQ<>(g.vertexCount());
			int pos = vertices.get(s);
			//int po1 = pos.intValue();
			K llave = null;
			pq.insert(pos, distTo.get(s));
			while (!pq.isEmpty()) {
				int v = (pq.delMin());

				IIterator<K> itera = vertices.keys();
				while(itera.hasNext()){
					K obj = itera.next();
					int valu = vertices.get(obj);
					if(valu==v){
						llave = obj;
						break;
					}
				}
				ListaEncadenada<Arco<K, E>> list = g.darAdyacencias(llave);
				if(list!=null){
					IIterator<Arco<K,E>> iterador = list.iterador();
					while(iterador.hasNext())
						relax(iterador.next(), pesoPpal);
				}
			}
		}

		// relax edge e and update pq if changed
		private void relax(Arco<K, E> e, String tipo) {
			Nodo<K,E> v = e.darNodoInicio(), w = e.darNodoFin();
			int wval = vertices.get(w.darVertice()).intValue();
			if (distTo.get(w.darVertice()) > distTo.get(v.darVertice()) + ((Conexion)e.darInformacion()).darPeso(tipo)) {
				distTo.put(w.darVertice(), distTo.get(v.darVertice())+((Conexion)e.darInformacion()).darPeso(tipo));
				edgeTo.put(w.darVertice(), e);
				if (pq.contains(wval)) pq.decreaseKey(wval, distTo.get(w.darVertice()));
				else pq.insert(wval, distTo.get(w.darVertice()));
			}
		}

		/**
		 * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
		 * @param  v the destination vertex
		 * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
		 *         {@code Double.POSITIVE_INFINITY} if no such path
		 */
		public double distTo(K v) {
			return distTo.get(v);
		}

		/**
		 * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
		 *
		 * @param  v the destination vertex
		 * @return {@code true} if there is a path from the source vertex
		 *         {@code s} to vertex {@code v}; {@code false} otherwise
		 */
		public boolean hasPathTo(K v) {
				return distTo.get(v) < Double.POSITIVE_INFINITY;
		}

		/**
		 * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
		 *
		 * @param  v the destination vertex
		 * @return a shortest path from the source vertex {@code s} to vertex {@code v}
		 *         as an iterable of edges, and {@code null} if no such path
		 */
		public IIterator<Arco<K, E>> pathTo(K v) {
			if (!hasPathTo(v)) return null;
			Stack<Arco<K, E>> path = new Stack<Arco<K,E>>();
			for (Arco<K, E> e = edgeTo.get(v); e != null; e = edgeTo.get(e.darNodoInicio().darVertice())) {
				path.push(e);
			}
			return path.iterador();
		}

	}

	public class Kosaraju 
	{
		private boolean[] marked;
		private int[] idComponente;
		private Object[] llaves;
		private int contar;
		private TablaHashLP<K, ListaEncadenada<Arco<K, E>>> nuevaAdjacencia;

		public Kosaraju()
		{
			nuevaAdjacencia = reversarGrafo();
			IIterator<K> cola = nuevaAdjacencia.keys();
			llaves = new Object [nuevaAdjacencia.size()];
			marked = new boolean[nuevaAdjacencia.size()];
			idComponente = new int [nuevaAdjacencia.size()];

			//			System.err.println(adjacencyList.size());

			for(int j = 0; j < llaves.length ; j++)
			{
				K act = cola.next();
				llaves[j] = act;				
			}

			for (int i = 0; i<llaves.length; i++)
			{
				K act = (K) llaves[i];
				if(!marked[i])
				{	
					dfs(act , i);
					contar ++;
				}
			}
			//			System.err.println(contar);
		}	

		private void dfs(K llave, int pos)
		{
			marked[pos] = true;
			idComponente[pos] = contar;
			ListaEncadenada<Arco<K, E>> adjacencias = nuevaAdjacencia.get(llave);
			IIterator<Arco<K,E>> it = adjacencias.iterador();

			while(it.hasNext())
			{
				boolean encontro = false;
				int posAd = -1;
				Arco<K, E> aux = it.next();
				K adjacent = aux.darNodoFin().darVertice();
				for(int i = 0; i < llaves.length && !encontro; i ++ )
				{
					//					System.out.println(llaves[i] + "arr");
					//					System.out.println(adjacent + "add");
					//					if (llaves[i]!=null){
					if(llaves[i].equals(adjacent))
					{
						posAd = i;
						encontro = true;
					}
					//					}
				}

				if(!marked[posAd])
				{
					dfs(adjacent, posAd);
				}
			}
		}

		public Object[] darArregloLlaves()
		{
			return llaves;
		}

		public int[] darArregloIdComponenes()
		{
			return idComponente;
		}

		public int darNumeroComponentes()
		{
			return contar;
		}
		
		public int idOf(int vertex)
		{
			return idComponente[vertex];
		}
	}
	
}