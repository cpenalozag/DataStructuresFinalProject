package estructuras.test;

import estructuras.Nodo;
import junit.framework.TestCase;

public class NodoTest extends TestCase
{
	private Nodo<String, ElementoBasico> nodo1 = new Nodo<String, ElementoBasico>("A");
	private Nodo<String, ElementoBasico> nodo2 = new Nodo<String, ElementoBasico>("B");
	private Nodo<String, ElementoBasico> nodo3 = new Nodo<String, ElementoBasico>("C");
	
	
	public void testDarVertice()
	{
		assertEquals("A", nodo1.darVertice());
		assertEquals("B", nodo2.darVertice());
		assertEquals("C", nodo3.darVertice());
	}
	
	public void testAgregarArco(){
		assertNotNull(nodo1.agregarArco(nodo2, 1.0));
		assertNotNull(nodo1.agregarArco(nodo3, 2.0));
		
		assertNotNull(nodo2.agregarArco(nodo1, 3.0));
		assertNotNull(nodo2.agregarArco(nodo3, 4.0));
	}
	
	public void testRemoverArco(){
		nodo1.agregarArco(nodo2, 1.0);
		nodo1.agregarArco(nodo3, 2.0);
		assertTrue(nodo1.removerArco(nodo2));
		assertFalse(nodo1.removerArco(nodo2));
		assertTrue(nodo1.removerArco(nodo3));
	}
	
	public void darCantidadArcos(){
		nodo1.agregarArco(nodo2, 1.0);
		nodo1.agregarArco(nodo3, 2.0);
		assertEquals(2, nodo1.darCantidadArcos());
		
		nodo2.agregarArco(nodo1, 1.0);
		nodo2.agregarArco(nodo2, 0.0);
		nodo2.agregarArco(nodo3, 2.0);
		assertEquals(3, nodo2.darCantidadArcos());
	}

	protected class ElementoBasico implements Comparable<ElementoBasico>
	{

		private String valor;

		public ElementoBasico( String nValor )
		{
			valor = nValor;
		}

		public String darIdentificador( )
		{
			return valor;
		}

		@Override
		public int compareTo(ElementoBasico o) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
