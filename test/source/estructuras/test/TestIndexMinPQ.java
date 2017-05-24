package estructuras.test;

import estructuras.IndexMinPQ;
import junit.framework.TestCase;

public class TestIndexMinPQ extends TestCase{
	
	// -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------
	
	private IndexMinPQ<String> cola;
	
	 // -----------------------------------------------------------------
    // M�todos
    // -----------------------------------------------------------------

	private void setUpEscenario1 ()
	{
		cola = new IndexMinPQ<>(5);
		cola.insert(0, "Prueba 1");
		cola.insert(1, "Prueba 2");
		cola.insert(2, "Prueba 3");
		cola.insert(3, "Prueba 4");
		cola.insert(4, "Prueba 5");
	}
	
	private void setUpEscenario2 ()
	{
		cola = new IndexMinPQ<>(5);
		cola.insert(0, "Prueba 5");
		cola.insert(1, "Prueba 4");
		cola.insert(2, "Prueba 3");
		cola.insert(3, "Prueba 2");
		cola.insert(4, "Prueba 1");
	}
	
	public void testMinIndex()
	{
		setUpEscenario1();
		assertEquals("El indice no es correcto", 0, cola.minIndex());
		
		setUpEscenario2();
		assertEquals("El indice no es correcto", 4, cola.minIndex());
	}
	
	public void testDelMin()
	{
		setUpEscenario1();
		cola.delMin();
		assertEquals("No elimino correctamente", 1 , cola.minIndex());
		
		setUpEscenario2();
		cola.delMin();
		assertEquals("No elimino correctamente", 3 , cola.minIndex());
	}
	
	public void testCambiarLlave()
	{
		setUpEscenario1();
		cola.changeKey(0, "Prueba 6");
		assertEquals("La llave no es la esperada", "Prueba 6", cola.keyOf(0));
		assertEquals("El indice no es correcto", 1, cola.minIndex());
		
		setUpEscenario2();
		cola.changeKey(4, "Prueba 6");
		assertEquals("La llave no es la esperada", "Prueba 6", cola.keyOf(4));
		assertEquals("El indice no es correcto", 3, cola.minIndex());
	}
}
