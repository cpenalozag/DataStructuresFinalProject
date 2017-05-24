package estructuras.test;

import estructuras.IndexMaxPQ;

import junit.framework.TestCase;

public class TestIndexMaxPQ extends TestCase {
	
	// -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------
	
	private IndexMaxPQ<String> cola;
	
	 // -----------------------------------------------------------------
    // M�todos
    // -----------------------------------------------------------------
	
	private void setUpEscenario1 ()
	{
		cola = new IndexMaxPQ<>(5);
		cola.insert(0, "Prueba 1");
		cola.insert(1, "Prueba 2");
		cola.insert(2, "Prueba 3");
		cola.insert(3, "Prueba 4");
		cola.insert(4, "Prueba 5");
	}
	
	private void setUpEscenario2 ()
	{
		cola = new IndexMaxPQ<>(5);
		cola.insert(0, "Prueba 5");
		cola.insert(1, "Prueba 4");
		cola.insert(2, "Prueba 3");
		cola.insert(3, "Prueba 2");
		cola.insert(4, "Prueba 1");
	}
	
	public void testMinIndex()
	{
		setUpEscenario1();
		assertEquals("El indice no es correcto", 4, cola.maxIndex());
		
		setUpEscenario2();
		assertEquals("El indice no es correcto", 0, cola.maxIndex());
	}
	
	public void testDelMin()
	{
		setUpEscenario1();
		cola.delMax();
		assertEquals("No elimino correctamente", 3 , cola.maxIndex());
		
		setUpEscenario2();
		cola.delMax();
		assertEquals("No elimino correctamente", 1 , cola.maxIndex());
	}
	
	public void testCambiarLlave()
	{
		setUpEscenario1();
		cola.changeKey(0, "Prueba 6");
		assertEquals("La llave no es la esperada", "Prueba 6", cola.keyOf(0));
		assertEquals("El indice no es correcto", 0, cola.maxIndex());
		
		setUpEscenario2();
		cola.changeKey(4, "Prueba 6");
		assertEquals("La llave no es la esperada", "Prueba 6", cola.keyOf(4));
		assertEquals("El indice no es correcto", 4, cola.maxIndex());
	}

}
