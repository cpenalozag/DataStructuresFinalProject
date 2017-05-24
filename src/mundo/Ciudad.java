package mundo;

import estructuras.ListaEncadenada;

public class Ciudad implements Comparable<Ciudad> {
	
	/**
	 * Nombre de la ciudad
	 */
	private String nombre;
	
	/**
	 * Lista de vuelos que entran a la ciudad
	 */
	private ListaEncadenada<Vuelo> vuelosEntrada;
	
	/**
	 * Lista de vuelos que sales de la ciudad
	 */
	private ListaEncadenada<Vuelo> vuelosSalida;
	
	/**
	 * Constructor de la ciudad, crea una nueva ciudad con el nombre que entra por parametro
	 * @param pNombre Nombre de la ciudad
	 */
	public Ciudad (String pNombre)
	{
		nombre = pNombre;
		vuelosEntrada = new ListaEncadenada<>();
		vuelosSalida = new ListaEncadenada<>();
	}
	
	public String darNombre()
	{
		return nombre;
	}
	/**
	 * A�ada el vuelo que entra por parametro a la lista de vuelos que entran a la ciudad
	 * @param pVuelo Vuelo que se desea agregar a la lista
	 */
	public void anhadirVueloEntrada( Vuelo pVuelo)
	{
		vuelosEntrada.add(pVuelo);
	}
	
	/**
	 * A�ade el vuelo que entra por parametro a la lista de vuelos que salen de la ciudad
	 * @param pVuelo Vuelo que se quiere a�adir
	 */
	public void anhadirVueloSalida (Vuelo pVuelo)
	{
		vuelosSalida.add(pVuelo);
	}
	
	/**
	 * Retorna una lista con los vuelos que entran a la ciudad
	 * @return Lista de vuelos que entran a la ciudad
	 */
	public ListaEncadenada<Vuelo> darVuelosEntrada()
	{
		return vuelosEntrada;
	}
	
	/**
	 * Retorna una lista con los vuelos que salen de la ciudad
	 * @return Lista de vuelos que salen de la ciudad
	 */
	public ListaEncadenada<Vuelo> darVuelosSalida()
	{
		return vuelosSalida;
	}

	@Override
	public int compareTo(Ciudad arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString()
	{
		return nombre;
	}
}
