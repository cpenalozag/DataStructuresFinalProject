package estructuras.test;

import estructuras.Cola;
import junit.framework.TestCase;

public class ColaTest extends TestCase
{
	protected Cola<ElementoBasico> cola = new Cola<>();


	public void testEnqueue()
	{
		cola.enqueue( new ElementoBasico( "B" ) );
		assertEquals("B", cola.peek().darIdentificador());
		assertEquals(1, cola.size());
		cola.enqueue( new ElementoBasico( "C" ) );
		assertEquals("B", cola.peek().darIdentificador());
		assertEquals(2, cola.size());
		cola.enqueue( new ElementoBasico( "D" ) );
		assertEquals("B", cola.peek().darIdentificador());
		assertEquals(3, cola.size());
	}

	public void testDequeue( )
	{
		ElementoBasico a = new ElementoBasico( "A" );   
		ElementoBasico b = new ElementoBasico( "B" );
		ElementoBasico c = new ElementoBasico( "C" );
		ElementoBasico d = new ElementoBasico( "D" );
		cola.enqueue( a );
		cola.enqueue( b );
		cola.enqueue( c );
		cola.enqueue( d );

		assertEquals( 4, cola.size( ) );

		ElementoBasico r = new ElementoBasico(null);
		assertEquals (a, r = cola.dequeue());
		assertEquals( 3, cola.size( ) );

		ElementoBasico s = new ElementoBasico(null);
		assertEquals (b, s = cola.dequeue());
		assertEquals( 2, cola.size( ) );

		ElementoBasico t = new ElementoBasico(null);
		assertEquals (c, t = cola.dequeue());
		assertEquals( 1, cola.size( ) );

		cola.dequeue();

		assertEquals( 0, cola.size( ) );

	}

	public void testClear( )
	{
		cola.enqueue( new ElementoBasico( "B" ) );
		cola.enqueue( new ElementoBasico( "C" ) );
		cola.enqueue( new ElementoBasico( "D" ) );

		cola.dequeue();
		cola.dequeue();
		cola.dequeue();

		assertTrue( cola.isEmpty( ) );
	}

	public void testIsEmpty( )
	{
		assertTrue( cola.isEmpty( ) );
		ElementoBasico d = new ElementoBasico( "D" );
		cola.enqueue( d );
		assertFalse( cola.isEmpty( ) );
	}


	public void testSize( )
	{
		ElementoBasico a = new ElementoBasico( "A" );
		ElementoBasico b = new ElementoBasico( "B" );
		ElementoBasico c = new ElementoBasico( "C" );
		ElementoBasico d = new ElementoBasico( "D" );

		assertEquals( 0, cola.size( ) );

		cola.enqueue( a );
		cola.enqueue( b );
		cola.enqueue( c );
		cola.enqueue( d );

		assertEquals( 4, cola.size( ) );

		cola.dequeue();

		assertEquals( 3, cola.size( ) );
		
		cola.dequeue();
		
		cola.dequeue();
		
		cola.dequeue();
		
		assertEquals( 0, cola.size( ) );

	}

	protected class ElementoBasico
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

	}
}
