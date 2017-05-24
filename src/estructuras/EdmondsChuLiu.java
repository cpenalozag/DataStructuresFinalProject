package estructuras;

import mundo.Conexion;

public class EdmondsChuLiu <K extends Comparable<K>,E extends Comparable<E>> {

	private ListaEncadenada<Nodo<K,E>> cycle;

	public AdjacencyList<K,E> getMinBranching(Nodo<K,E> root, AdjacencyList<K,E> list, String pesoPpal) {
		AdjacencyList<K,E> reverse = list.getReversedList();
		// remove all edges entering the root
		if (reverse.getAdjacent(root) != null) {
			reverse.getAdjacent(root).clear();
		}
		AdjacencyList<K,E> outEdges = new AdjacencyList<K,E>();
		// for each node, select the edge entering it with smallest weight
		//        for (Nodo<K,E> n : reverse.getSourceNodeSet()) {
		//            List<Edge> inEdges = reverse.getAdjacent(n);
		//            if (inEdges.isEmpty()) {
		//                continue;
		//            }
		//            Edge min = inEdges.get(0);
		//            for (Edge e : inEdges) {
		//                if (e.getWeight() < min.getWeight()) {
		//                    min = e;
		//                }
		//            }
		//            outEdges.addEdge(min.getTo(), min.getFrom(), min.getBond());
		//        }

		IIterator<Nodo<K, E>> it = reverse.getSourceNodeSet();
		while(it.hasNext())
		{
			Nodo<K,E> nodo = it.next();
			ListaEncadenada<Arco<K,E>> inEdges = reverse.getAdjacent(nodo);
			if(inEdges.isEmpty())
				continue;
			Arco<K,E> min = inEdges.first();
			IIterator<Arco<K, E>> ed = inEdges.iterador();
			while(ed.hasNext())
			{
				Arco<K,E> e = ed.next();
				Double pesoActual = ((Conexion)e.darInformacion()).darPeso(pesoPpal);
				Double pesoMin = ((Conexion)min.darInformacion()).darPeso(pesoPpal);
				if(pesoActual<pesoMin)
					min = e;
			}
			outEdges.addEdge(min.darNodoFin(), min.darNodoInicio(), min.darInformacion());
		}

		// detect cycles
		ListaEncadenada<ListaEncadenada<Nodo<K,E>>> cycles = new ListaEncadenada<ListaEncadenada<Nodo<K,E>>>();
		cycle = new ListaEncadenada<>();
		getCycle(root, outEdges);
		cycles.add(cycle);
		//        for (Nodo<K,E> n : outEdges.getSourceNodeSet()) {
		//            if (!n.isVisited()) {
		//                cycle = new ArrayList<Node>();
		//                getCycle(n, outEdges);
		//                cycles.add(cycle);
		//            }
		//        }
		IIterator<Nodo<K,E>> iterador = outEdges.getSourceNodeSet();
		while(iterador.hasNext())
		{
			
			Nodo<K,E> n = iterador.next();
			if(!n.fueVisitado())
			{
				cycle = new ListaEncadenada<>();
				getCycle(n, outEdges);
				cycles.add(cycle);
			}
		}

		// for each cycle formed, modify the path to merge it into another part of the graph
		AdjacencyList<K,E> outEdgesReverse = outEdges.getReversedList();

		IIterator<ListaEncadenada<Nodo<K,E>>> itera = cycles.iterador();
		while(itera.hasNext())
		{
			ListaEncadenada<Nodo<K,E>> x = itera.next();
			if(x.contains(root))
				continue;
			mergeCycles(x, list, reverse, outEdges, outEdgesReverse,pesoPpal);
		}
		return outEdges;
		//        for (ListaEncadenada<Nodo<K,E>> x : cycles) {
		//            if (x.contains(root)) {
		//                continue;
		//            }
		//            mergeCycles(x, list, reverse, outEdges, outEdgesReverse);
		//        }

	}

	private void mergeCycles(ListaEncadenada<Nodo<K,E>> cycle, AdjacencyList<K,E> list, AdjacencyList<K,E> reverse, AdjacencyList<K,E> outEdges, AdjacencyList<K,E> outEdgesReverse, String pesoPpal) {
		ListaEncadenada<Arco<K,E>> cycleAllInEdges = new ListaEncadenada<>();
		Arco<K,E> minInternalEdge = null;
		// find the minimum internal edge weight
		IIterator<Nodo<K,E>> n = cycle.iterador();
		while(n.hasNext())
		{

			ListaEncadenada<Arco<K,E>> e = reverse.getAdjacent(n.next());
			IIterator<Arco<K,E>> iterador = e.iterador();
			while(iterador.hasNext()){
				Arco<K,E> arc = iterador.next();
				if(cycle.contains(arc.darNodoFin())){
					if(minInternalEdge==null||((Conexion)minInternalEdge.darInformacion()).darPeso(pesoPpal)>((Conexion)arc.darInformacion()).darPeso(pesoPpal))
					{
						minInternalEdge = arc;
						continue;
					}
				}
				else{
					cycleAllInEdges.add(arc);
				}
			}


		}
//		for (Nodo<K,E> n : cycle) {
//			for (Edge e : reverse.getAdjacent(n)) {
//				if (cycle.contains(e.getTo())) {
//					if (minInternalEdge == null || minInternalEdge.getWeight() > e.getWeight()) {
//						minInternalEdge = e;
//						continue;
//					}
//				} else {
//					cycleAllInEdges.add(e);
//				}
//			}
//		}
		// find the incoming edge with minimum modified cost
		Arco<K,E> minExternalEdge = null;
		double minModifiedWeight = 0;
		IIterator<Arco<K, E>> it = cycleAllInEdges.iterador();
		while(it.hasNext())
		{
			Arco<K,E> arc = it.next();
			ListaEncadenada<Arco<K,E>> listaIn = outEdgesReverse.getAdjacent(arc.darNodoInicio());
			double w = ((Conexion)arc.darInformacion()).darPeso(pesoPpal) - ((Conexion)listaIn.first().darInformacion()).darPeso(pesoPpal);
			if(minExternalEdge==null||minModifiedWeight>w)
			{
				minExternalEdge= arc;
				minModifiedWeight = w;
			}
		}
//		for (Arco<K,E> e : cycleAllInEdges) {
//			int w = e.getWeight() - (outEdgesReverse.getAdjacent(e.getFrom()).get(0).getWeight() - minInternalEdge.getWeight());
//			if (minExternalEdge == null || minModifiedWeight > w) {
//				minExternalEdge = e;
//				minModifiedWeight = w;
//			}
//		}
		// add the incoming edge and remove the inner-circuit incoming edge
		Arco<K,E> removing = outEdgesReverse.getAdjacent(minExternalEdge.darNodoInicio()).first();
		outEdgesReverse.getAdjacent(minExternalEdge.darNodoInicio()).clear();
		outEdgesReverse.addEdge(minExternalEdge.darNodoFin(), minExternalEdge.darNodoInicio() ,minExternalEdge.darInformacion());
		ListaEncadenada<Arco<K,E>> adj = outEdges.getAdjacent(removing.darNodoFin());
		for (IIterator<Arco<K,E>> i = adj.iterador(); i.hasNext();) {
			if (i.next().darNodoFin() == removing.darNodoInicio()) {
				i.remove();
				break;
			}
		}
		outEdges.addEdge(minExternalEdge.darNodoFin(), minExternalEdge.darNodoInicio(), minExternalEdge.darInformacion());
	}

	private void getCycle(Nodo<K,E> n, AdjacencyList<K,E> outEdges) {
		n.cambiarVisitado(true);
		cycle.add(n);
		if (outEdges.getAdjacent(n) == null) {
			return;
		}
		//        for (Arco<K,E> e : outEdges.getAdjacent(n)) {
		//            if (!e.getTo().isVisited()) {
		//                getCycle(e.getTo(), outEdges);
		//            }
		//        }
		ListaEncadenada<Arco<K,E>> lista = outEdges.getAdjacent(n);
		IIterator<Arco<K,E>> it = lista.iterador();
		while(it.hasNext())
		{
			Arco<K,E> e = it.next();
			if(!e.darNodoFin().fueVisitado())
			{
				getCycle(e.darNodoFin(), outEdges);
			}
		}

	}

}
