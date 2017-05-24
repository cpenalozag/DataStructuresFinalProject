package estructuras;

/**
 * Representa un Arco con peso en un grafo.
 * Cada arco consta de un Nodo inicio, un Nodo fin y un costo. Adicionalmente, el Arco puede
 * guardar informaciÃ³n adicional en un objeto (tipo E).
 * @param K tipo del identificador de los vertices (comparable)
 * @param E tipo de la informacion asociada a los arcos
 */
public class Arco<K extends Comparable<K>,E extends Comparable<E>> {

  /**
   * Peso de ir de nodo inicio a nodo fin
   */
  private double peso;

  /**
   * Informacion adicional que se puede guardar en el arco
   */
  //actualizacion
  private E obj;

  /**
   * Nodo 1 
   */
  private Nodo<K,E> nodo1;

  /**
   * Nodo 2
   */
  private Nodo<K,E> nodo2;
  
  /**
   * Construye un nuevo arco desde un nodo inicio hasta un nodo fin.
   * @param inicio Nodo inicial del arco.
   * @param fin Nodo final del arco.
   * @param costo Costo del arco. 
   */
  public Arco(Nodo<K,E> nodo1, Nodo<K,E> nodo2, double peso) {
    this.peso = peso;
    this.nodo1 = nodo1;
    this.nodo2 = nodo2;
  }
  
  /**
   * Construye un nuevo arco desde un nodo inicio hasta un nodo fin.
   * @param inicio Nodo inicial del arco.
   * @param fin Nodo final del arco.
   * @param costo Costo del arco. 
   */
  public Arco(Nodo<K,E> nodo1, Nodo<K,E> nodo2, E obj) {
    this.obj = obj;
    this.nodo1 = nodo1;
    this.nodo2 = nodo2;
  }


  /**
   * Devuelve el nodo inicio del arco
   * @return Nodo inicio
   */
  public Nodo<K,E> darNodoInicio() {
    return nodo1;
  }

  /**
   * Devuelve el nodo final del arco
   * @return Nodo fin
   */
  public Nodo<K,E> darNodoFin() 
  {
    return nodo2;
  }

  /**
   * Devuelve el peso del arco
   * @return peso
   */
  public double darPeso() {
    return peso;
  }

  /**
   * Asigna un objeto como informacion adicional asociada al arco
   * @param info Objeto (tipo E) que se desea guardar como informaciÃ³n adicional
   * @return este arco con la informaciÃ³n adicional asignada
   */
  public  Arco<K,E> asignarInformacion(E info) {
    obj = info; 
    return this;
  }

  /**
   * Devuelve la informaciÃ³n adicional asociada al arco
   * @return objeto (tipo E) asociado como informaciÃ³n adicional
   */
  public   E darInformacion() {
    return obj;
  }
  
  public boolean isBetween(Nodo<K,E> nodo1, Nodo<K,E> nodo2) {
        return (this.nodo1 == nodo1 && this.nodo2 == nodo2);
    }
}
