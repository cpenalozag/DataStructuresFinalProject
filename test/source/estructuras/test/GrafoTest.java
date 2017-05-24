package estructuras.test;

import estructuras.Arco;
import estructuras.Grafo;
import estructuras.Grafo.Dijkstra;
import estructuras.IIterator;
import junit.framework.TestCase;
import mundo.ComparadorConexion;
import mundo.Conexion;
import mundo.Vuelo;

public class GrafoTest extends TestCase {

	private Grafo<String, Double> grafo;
	private Grafo<VueloPractica,Conexion> grafoD;
	private VueloPractica v1;
	private VueloPractica v2;
	private VueloPractica v5;
	private VueloPractica v7;

	protected void setUp()
	{
		grafo = new Grafo<>(10);
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		grafo.addEdge("A", "B", 1.0);
		grafo.addEdge("B", "C", 2.0);
		grafo.addEdge("C", "D", 3.0);
		grafo.addEdge("D", "A", 3.0);
	}
	

	protected void setUp1()
	{
		grafo = new Grafo<>(10);
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		grafo.addVertex("E");

		grafo.addEdge("A", "B", 1.0);
		grafo.addEdge("A", "C", 2.0);
		grafo.addEdge("C", "D", 3.0);
		grafo.addEdge("D", "A", 4.0);
		grafo.addEdge("C", "B", 5.0);
	}

	protected void setUp2(){
		grafoD = new Grafo<>(10);
		v1 = new VueloPractica("Acandi", "Medellin", 7.0);
		v2 = new VueloPractica("Medellin", "Barranquilla", 6.0);
		VueloPractica v3 = new VueloPractica("Barranquilla", "Cali", 2.0 );
		VueloPractica v4 = new VueloPractica("Cali", "Bogota", 3.0 );
		v5 = new VueloPractica("Bogota", "Acandi", 2.0 );
		VueloPractica v6 = new VueloPractica("Barranquilla", "Acandi", 15.0 );
		
		v7 = new VueloPractica("Hola", "Chao", 0.0);
		
		Conexion cv1v2 = new Conexion(v2.tarifa,2.0,1,0.0);
		Conexion cv2v3 = new Conexion(v3.tarifa,1.8,1,0.0);
		Conexion cv3v4 = new Conexion(v4.tarifa,2.0,1,0.0);
		Conexion cv4v5 = new Conexion(v5.tarifa,2.0,1,0.0);
		Conexion cv5v1 = new Conexion(v1.tarifa,1.0,1,0.0);
		Conexion cv2v6 = new Conexion(v6.tarifa,1.5,1,0.0);
		Conexion cv6v1 = new Conexion(v1.tarifa,1.5,1,0.0);
	
		grafoD.addVertex(v1);
		grafoD.addVertex(v2);
		grafoD.addVertex(v3);
		grafoD.addVertex(v4);
		grafoD.addVertex(v5);
		grafoD.addVertex(v6);
		
		grafoD.addVertex(v7);
		
		grafoD.addEdge(v1, v2, cv1v2);
		grafoD.addEdge(v2, v3, cv2v3);
		grafoD.addEdge(v3, v4, cv3v4);
		grafoD.addEdge(v4, v5, cv4v5);
		grafoD.addEdge(v5, v1, cv5v1);
		grafoD.addEdge(v2, v6, cv2v6);
		grafoD.addEdge(v6, v1, cv6v1);
	}
	
	public void testVertextCount(){
		setUp();
		assertEquals(4, grafo.vertexCount());
		grafo.removeVertex("A");
		assertEquals(3, grafo.vertexCount());
		grafo.removeVertex("B");
		assertEquals(2, grafo.vertexCount());
		grafo.removeVertex("C");
		assertEquals(1, grafo.vertexCount());
	}

	public void testContainsVertex(){
		setUp();
		assertTrue(grafo.containsVertex("A"));
		assertTrue(grafo.containsVertex("B"));

		assertFalse(grafo.containsVertex("X"));
		assertFalse(grafo.containsVertex("Z"));
	}

	public void testContainsEdge(){
		setUp();
		assertTrue(grafo.containsEdge("A", "B"));
		assertTrue(grafo.containsEdge("B","C"));

		grafo.removeEdge("A","B");
		assertFalse(grafo.containsEdge("A", "B"));

		grafo.removeEdge("B","C");
		assertFalse(grafo.containsEdge("B", "C"));

		grafo.removeEdge("C","D");
		assertFalse(grafo.containsEdge("C", "D"));
	}


	public void testRemoveVertex(){
		setUp();
		assertTrue(grafo.containsVertex("A"));
		assertTrue(grafo.containsVertex("B"));

		grafo.removeVertex("A");
		assertFalse(grafo.containsVertex("A"));
		assertFalse(grafo.containsEdge("A", "B"));

		grafo.removeVertex("C");
		assertFalse(grafo.containsVertex("C"));
		assertFalse(grafo.containsEdge("B", "C"));
		assertFalse(grafo.containsEdge("C", "D"));
	}

	public void testRemoveEdge(){
		setUp();
		assertFalse(grafo.removeEdge("A", "Z"));
		assertFalse(grafo.removeEdge("X", "Z"));

		assertTrue(grafo.containsEdge("A", "B"));
		assertTrue(grafo.containsEdge("B", "C"));
		assertTrue(grafo.containsEdge("C", "D"));
	}

	public void testKosaraju()
	{
		setUp1();
		String mensaje = grafo.componentesConectados();
		System.out.println(mensaje);

		System.out.println("Segundo intento");

		setUp();
		String mensaje2 = grafo.componentesConectados();
		System.out.println(mensaje2);
	}

	public void testDijsktraCosto()
	{
		setUp2();
		Dijkstra<VueloPractica, Conexion> d = new Dijkstra<VueloPractica,Conexion>(grafoD,v2,Conexion.PESO_COSTO);
		assertTrue(d.hasPathTo(v1));
		assertTrue(d.hasPathTo(v5));
		assertFalse(d.hasPathTo(v7));
		
		assertEquals(14.0, d.distTo(v1));
		assertEquals(7.0, d.distTo(v5));
		
		IIterator<Arco<VueloPractica,Conexion>> it = d.pathTo(v1);
		assertEquals("Cali", it.next().darNodoFin().darVertice().destino);
		assertEquals("Bogota", it.next().darNodoFin().darVertice().destino);
		assertEquals("Acandi", it.next().darNodoFin().darVertice().destino);
		assertEquals("Medellin", it.next().darNodoFin().darVertice().destino);
	}
	
	public void testDijsktraDuracion()
	{
		setUp2();
		Dijkstra<VueloPractica, Conexion> d = new Dijkstra<VueloPractica,Conexion>(grafoD,v2,Conexion.PESO_DURACION);
		assertTrue(d.hasPathTo(v1));
		assertTrue(d.hasPathTo(v5));
		assertFalse(d.hasPathTo(v7));
		
		assertEquals(3.0, d.distTo(v1));
		assertEquals(5.8, d.distTo(v5));
		
		IIterator<Arco<VueloPractica,Conexion>> it = d.pathTo(v1);
		assertEquals("Acandi", it.next().darNodoFin().darVertice().destino);
		assertEquals("Medellin", it.next().darNodoFin().darVertice().destino);
	}

	protected class VueloPractica implements Comparable<VueloPractica>
	{
		private static final long serialVersionUID = 1L;

		private String origen;
		private String destino;
		private Double tarifa;
		private Double duracion;

		public VueloPractica( String pOrigen, String pDestino, Double pTarifa)
		{
			origen=pOrigen;
			destino=pDestino;
			tarifa=pTarifa;
		}


		@Override
		public int compareTo(VueloPractica o) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}