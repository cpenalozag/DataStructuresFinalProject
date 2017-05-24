package estructuras.test;

import java.util.ArrayList;
import java.util.Random;

import estructuras.TablaHashLP;
import junit.framework.TestCase;

public class HashLPTest extends TestCase {

	public HashLPTest() {

		hashTable = new TablaHashLP<>(15);
		elements = new ArrayList<>(3 * hashTable.size());

		for (int i = 0; i < 3 * hashTable.size(); i++) {
			Object key = new Integer(getUniqueRandom());
			Object data = new Integer(getRandom());
			elements.add(new ElementoHash((int)key, (int)data));
		}

		for (ElementoHash e : elements) {
			hashTable.put(e.darLlave(), e.darDato());
		}

	}


	/**
	 * Busca todos los elementos de la tabla por su llave.
	 */
	 public void testGet() {

		 for (ElementoHash e : elements) {
			 Object key = e.darLlave();
			 Object data = e.darDato();
			 Integer resp = hashTable.get((Integer) key);
			 assertTrue(resp != null);
			 assertTrue(resp.equals(data));
		 }
	 }


	 /**
	  * Elimina todos los elementos de la tabla verificando que se eliminó el elemento correcto,
	  * después se trata de eliminar nuevamente para confirmar que se realizó la operación.
	  * Al final se confirma que la tabla este vacia.
	  */
	 public void testDelete() {

		 assertTrue(hashTable.size() == elements.size());

		 for (ElementoHash e : elements) {
			 Object key = e.darLlave();
			 int resp = hashTable.get((Integer)key);
			 hashTable.delete((Integer)key);
			 resp = hashTable.get((Integer)key);
			 assertEquals(null, resp);
		 }

		 assertTrue(hashTable.size() == 0);
	 }

	 // Metodos y atributos privados

	 private static Random random = new Random();

	 private static final int LIMIT = 1000;

	 private static int getRandom() {
		 return random.nextInt(LIMIT);
	 }

	 private int getUniqueRandom() {
		 int n = 0;
		 do {
			 n = getRandom();
		 }
		 while ( alreadyUsed(n) );
		 return n;
	 }

	 private boolean alreadyUsed(int key) {
		 for (ElementoHash i : elements) {
			 if ( i.darLlave() == key ) return true;
		 }
		 return false;
	 }

	 private TablaHashLP<Integer, Integer> hashTable;

	 private ArrayList<ElementoHash> elements;

	 protected class ElementoHash
	 {

		 private int llave;

		 private int dato;

		 public ElementoHash( int nLlave, int nDato )
		 {
			 llave = nLlave;
			 dato = nDato;
		 }

		 public int darLlave( )
		 {
			 return llave;
		 }

		 public int darDato( )
		 {
			 return dato;
		 }
	 }

}
