package estructuras.test;

import junit.framework.TestCase;
import estructuras.IComparator;
import estructuras.IIterator;
import estructuras.ListaEncadenada;

public class ListaSencillaTest extends TestCase
{
	protected ListaEncadenada<ElementoBasico> lista = new ListaEncadenada<>();

	

	public void testAdd()
	{
		lista.add( new ElementoBasico( "C" ) );
		assertEquals(1, lista.size());
		lista.add( new ElementoBasico( "D" ) );
		assertEquals(2, lista.size());	
		ElementoBasico b = new ElementoBasico( "A" );
		lista.add( b );
		assertEquals(3, lista.size());
		ElementoBasico a = new ElementoBasico( "B" );
		lista.add( a );
		assertEquals(4, lista.size());
		ElementoBasico e = new ElementoBasico("E");
		lista.add( e );
		assertEquals(5, lista.size());
		
	}

	public void testClear( )
	{
		lista.add( new ElementoBasico( "B" ) );
		lista.add( new ElementoBasico( "C" ) );
		lista.add( new ElementoBasico( "D" ) );
		assertFalse( lista.isEmpty());
		lista.clear( );

		assertTrue( lista.isEmpty( ) );

	}
	
	public void testContains( )
	{
		lista.clear();
		ElementoBasico a = new ElementoBasico( "A" );
		ElementoBasico b = new ElementoBasico( "B" );
		ElementoBasico c = new ElementoBasico( "C" );
		ElementoBasico d = new ElementoBasico( "D" );
		assertFalse(lista.contains(a));
		lista.add( a );
		assertTrue(lista.contains(a));
		
		assertFalse(lista.contains(b));
		lista.add( b );
		assertTrue(lista.contains(b));
		
		assertFalse(lista.contains(c));
		lista.add( c );
		assertTrue(lista.contains(c));
		
		assertFalse(lista.contains(d));
		lista.add( d );
		assertTrue(lista.contains(d));
	}

	public void testIsEmpty( )
	{
		assertTrue( lista.isEmpty( ) );
		ElementoBasico d = new ElementoBasico( "D" );
		lista.add( d );
		assertFalse( lista.isEmpty( ) );

	}


	public void testRemove( )
	{
		IComparator<ElementoBasico> co = new ComparadorElementoBasico();
		ElementoBasico a = new ElementoBasico( "A" );   
		ElementoBasico b = new ElementoBasico( "B" );
		ElementoBasico c = new ElementoBasico( "C" );
		ElementoBasico d = new ElementoBasico( "D" );
		lista.add( a );
		lista.add( b );
		lista.add( c );
		lista.add( d );
		assertEquals( 4, lista.size( ) );
		IIterator<ElementoBasico> it = lista.iterador();
		it.next();
		it.remove();
		assertEquals( 3, lista.size( ) );
		
		it.next();
		it.remove();
		assertEquals( 2, lista.size( ) );

		it.next();
		it.remove();
		assertEquals( 1, lista.size( ) );
		
		it.next();
		it.remove();
		assertEquals( 0, lista.size( ) );

	}

	public void testIterador( )
	{

	}

	public void testSize( )
	{
		lista.clear();
		ElementoBasico a = new ElementoBasico( "A" );
		ElementoBasico b = new ElementoBasico( "B" );
		ElementoBasico c = new ElementoBasico( "C" );
		ElementoBasico d = new ElementoBasico( "D" );

		assertEquals( 0, lista.size( ) );
		
		lista.add( a );
		assertEquals( 1, lista.size( ) );
		lista.add( b );
		assertEquals( 2, lista.size( ) );
		lista.add( c );
		assertEquals( 3, lista.size( ) );
		lista.add( d );

		assertEquals( 4, lista.size( ) );

		lista.clear();

		assertEquals( 0, lista.size( ) );

	}

	protected class ElementoBasico
	{
		private static final long serialVersionUID = 1L;

		private String valor;

		public ElementoBasico( String nValor )
		{
			valor = nValor;
		}

		public String toString( )
		{
			return valor;
		}

	}

	protected class ComparadorElementoBasico implements IComparator<ElementoBasico>
	{
		public Double compare(ElementoBasico o1, ElementoBasico o2) 
		{
			return (double) (o1.valor.compareTo(o2.valor));
		}
	}
}
