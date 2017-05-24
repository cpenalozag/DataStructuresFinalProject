package mundo;

public class AeroCivil {
	private Principal principal;
	public AeroCivil(){
		principal  = new Principal();
	}

	public void cargarArchivo(){
		principal.cargarYconectar();
	}

	public void agregarAerolinea (String pNombreAerolinea, Double pTarifaMinuto, int pNumeroSillasMax){
		principal.agregarAerolinea(pNombreAerolinea, pTarifaMinuto, pNumeroSillasMax);
	}

	public void eliminarAerolinea(String pNombreAerolinea){
		principal.eliminarAerolinea(pNombreAerolinea);
	}

	public int darCantidadDeAerolineas(){
		return principal.darNumeroAerolineas();
	}

	public int darCantidadCiudades(){
		return principal.darNumeroCiudades();
	}

	public void agregarCiudad(String pNombreCiudad){
		principal.agregarCiudad(pNombreCiudad);
	}

	public void eliminarCiudad (String pNombreCiudad){
		principal.eliminarCiudad(pNombreCiudad);
	}

	public int darNumeroVuelos(){
		return principal.darNumeroVuelos();
	}

	public void agregarVuelo (String pAerolinea, int pNumSillas, String numVuelo, String pOrigen, String pDestino, String horaSalida, String horaLlegada, String tipoEquipo, boolean[] pDias){
		principal.agregarVuelo(pAerolinea, pNumSillas, numVuelo, pOrigen, pDestino, horaSalida, horaLlegada, tipoEquipo, pDias);
	}

	public void imprimirCiudadesConectados()
	{
		principal.imprimirCiudadesConectadas();
	}

	public void imprimirCiudadesConectadosPorAerolinea()
	{
		principal.imprimirCiudadesConectadasPorAerolinea();
	}

	public void imprimirDijkstra(String ciudadOrigen, String ciudadDestino, int diaPartida){
		principal.imprimirItinerario(ciudadOrigen, ciudadDestino, diaPartida);
	}

	public void pruebaEdmonds(String ciudad, String aerolinea, int req) throws Exception
	{
		principal.grafoCiudadesComponentesMST(ciudad, aerolinea, req);
	}
	public void pruebaEdmondsAerolinea(String ciudad, String aerolinea)
	{
		principal.MSTAerolinea(ciudad, aerolinea);
	}
	public void MST11(String ciudad, String dia)
	{
		principal.MSTPorDia(ciudad,dia);
	}

	public void implimirItinerarioAerolineas(String ciudadOrigen,String ciudadDestino,int diaPartida){
		principal.imprimirItinerariosAerolineas(ciudadOrigen, ciudadDestino, diaPartida);
	}

	public void imprimirItinerario(String ciudadOrigen, String ciudadDestino, int diaPartida){
		principal.imprimirItinerario(ciudadOrigen, ciudadDestino, diaPartida);
	}
	
	public void  rutaMasLarga(String pCiudad, String pAerolinea, String criterioBusqueda){
		principal.imprimirMejorRutaTodasCiudadesAerolinea(pCiudad, pAerolinea, criterioBusqueda);
	}
	
	public void imprimirMejorTodasCiudades(String ciudad,int dia,String aerolinea){
		principal.imprimirMejorRutaTodasCiudadesAerolineaConDia(ciudad, dia, aerolinea);
	}
	
	public void rutaMasLargaAerolinea(Double hora, int dia, String aerolinea){
//		principal.rutaMasLargaAerolinea(aerolinea, dia, hora);
		principal.darRutaMasLargaAerolinea(aerolinea);
	}
	
	public void rutaMasLarga(Double hora, int dia){
//		principal.rutaMas(dia, hora);
		principal.darRutaMasLarga();
	}
	
	public void req17(String aerolinea)
	{
		principal.requerimiento17(aerolinea);
	}
}
