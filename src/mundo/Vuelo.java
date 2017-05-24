package mundo;

public class Vuelo implements Comparable<Vuelo> {
	
	/**
	 * Numero del vuelo
	 */
	private int numeroVuelo;
	
	/**
	 * Aerolinea a la que pertenece el vuelo
	 */
	private Aerolinea aerolinea;
	
	/**
	 * Ciudad de origen del vuelo
	 */
	private String origen;
	
	/**
	 * Ciudad de destino del vuelo
	 */
	private String destino;
	
	/**
	 * Hora salida del vuelo
	 */
	private Double horaSalida;
	
	/**
	 * Hora llegada del vuelo
	 */
	private Double horaDestino;
	
	/**
	 * Duracion del vuelo
	 */
	private Double duracion;
	
	/**
	 * Arreglo que guarda los dias que opera el vuelo
	 */
	private boolean[] dias;
	
	/**
	 * Tipo de avion del vuelo
	 */
	private String tipoEsquipo;
	
	/**
	 * Numero de sillas del vuelo
	 */
	private int numeroSillas;
	
	/**
	 * Identificador del vuelo
	 */
	private int identificador;
	
	
	/**
	 *  Crea un nuevo vuelo
	 * @param pNumVuelo Numero del vuelo
	 * @param pNumSillas Numero sillas vuelo
	 * @param pAerolinea Aerolinea del vuelo
	 * @param pOrigen Ciudad origen del vuelo
	 * @param pDestino Ciudad destino del vuelo
	 * @param pHoraSalida Hora salida del vuelo
	 * @param pHoraLlegada Hora llegada del vuelo
	 * @param pDias Dias que opera el vuelo
	 * @param pTipo Tipo de avion del vuelo
	 */
	public Vuelo(int pNumVuelo, int pNumSillas, Aerolinea pAerolinea, String pOrigen, String pDestino, String pHoraSalida, String pHoraLlegada, boolean[] pDias, String pTipo){
		numeroVuelo = pNumVuelo;
		numeroSillas = pNumSillas;
		aerolinea = pAerolinea;
		origen = pOrigen;
		destino = pDestino;
		definirFechas(pHoraSalida, pHoraLlegada);
		dias = pDias;
		tipoEsquipo = pTipo;
		identificador=crearIdentificador();
	}
	
	public int darNumeroVuelo(){
		return numeroVuelo;
	}
	
	public int darNumeroSillas(){
		return numeroSillas;
	}
	
	public Aerolinea darAerolinea(){
		return aerolinea;
	}
	
	public String darCiudadOrigen(){
		return origen;
	}
	
	public String darCiudadDestino(){
		return destino;
	}
	
	public void definirFechas(String pHoraSalida, String pHoraLlegada ){
		String[] partesSalida = pHoraSalida.split(":");
		int horaSal = Integer.parseInt(partesSalida[0]);
		Double minutoSalida = ((Integer.parseInt(partesSalida[1]))/60.0);
		horaSalida = horaSal+minutoSalida;
		
		String[] partesLlegada = pHoraLlegada.split(":");
		int horaLlegada = Integer.parseInt(partesLlegada[0]);
		Double minutoLlegada = ((Integer.parseInt(partesLlegada[1]))/60.0);
		horaDestino = horaLlegada+minutoLlegada;
		
		if (horaSalida > horaDestino){
			duracion = (24-horaSalida)+horaDestino;
		}
		else{
			duracion = horaDestino-horaSalida;
		}
	}
	
	public Double darHoraSalida(){
		return horaSalida;
	}
	
	public Double darHoraLlegada(){
		return horaDestino;
	}
	
	public boolean[] darDias(){
		return dias;
	}
	
	public String darTipoEquipo(){
		return tipoEsquipo;
	}
	
	public Double darDuracion(){
		return duracion;
	}
	
	private int crearIdentificador(){
		IdentificadorVuelo identificador = new IdentificadorVuelo(numeroVuelo, numeroSillas, aerolinea.darNombre(), origen, destino, horaSalida, horaDestino);
		return identificador.darIdentificador();
	}
	
	public int darIdentificador(){
		return identificador;
	}

	public double costoEntreSemana()
	{
		double TM = aerolinea.darTarifaMinuto();
		Double TSMaximo = aerolinea.darNumeroSillas()+0.0;
		
		double costo = TM*(TSMaximo/numeroSillas)*(duracion*60);
		return costo;
	}
	
	public double costoFinSemana()
	{
		double TM = aerolinea.darTarifaMinuto();
		Double TSMaximo = aerolinea.darNumeroSillas()+0.0;
		
		double costo = (TM*(TSMaximo/numeroSillas)*(duracion*60))*(1.3);
		return costo;
	}
	
	@Override
	public int compareTo(Vuelo o) {
		ComparadorVuelo c = new ComparadorVuelo();
		return c.compare(this, o).intValue();
	}
}