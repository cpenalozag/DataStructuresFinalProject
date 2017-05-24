package mundo;

import estructuras.Grafo;
import estructuras.IIterator;
import estructuras.ListaEncadenada;
import estructuras.TablaHashLP;

public class Aerolinea {

	/**
	 * Nombre de la aerolinea
	 */
	private String nombre;

	/**
	 * Tarifa por minuto
	 */
	private Double tarifaMinuto;

	/**
	 * Numero de sillas para valor maximo
	 */
	private int numSillasMax;

	/**
	 * Vuelos de la aerolinea
	 */
	private TablaHashLP<Integer, Vuelo> vuelos;

	private Grafo<Vuelo, Conexion> grafoVuelos;

	private ListaEncadenada<String> ciudades;
	
	private int contador;

	public Aerolinea(String pNombre, Double pTarifaMin, int pNumSillasMax){
		nombre = pNombre;
		tarifaMinuto = pTarifaMin;
		numSillasMax = pNumSillasMax;
		vuelos = new TablaHashLP<Integer, Vuelo>();
		contador = 0;
		ciudades = new ListaEncadenada<>();
	}

	public String darNombre(){
		return nombre;
	}

	public Grafo darGrafo(){
		return grafoVuelos;
	}
	
	public Double darTarifaMinuto(){
		return tarifaMinuto;
	}

	public int darNumeroSillas(){
		return numSillasMax;
	}

	public TablaHashLP<Integer, Vuelo> darVuelos()
	{
		return vuelos;
	}

	public void agregarVuelo(Vuelo pvuelo)
	{
		contador++;
		vuelos.put(contador, pvuelo);
	}
	
	public void agregarCiudad(String pCiudad)
	 {
	  if(!ciudades.contains(pCiudad))
	  {
	   ciudades.add(pCiudad);
	  }
	 }

	public void conectarVuelos()
	{
		grafoVuelos = new Grafo<Vuelo, Conexion>(vuelos.size());
		IIterator<Integer> iter = vuelos.keys();
		while(iter.hasNext()){
			Integer llave = iter.next();
			Vuelo actual = vuelos.get(llave);
			String origenActual = actual.darCiudadOrigen();
			String destinoActual = actual.darCiudadDestino();
			IIterator<Integer> it = vuelos.keys();
			while (it.hasNext()){
				int cLllave = it.next();
				Vuelo comparado = vuelos.get(cLllave);
				String origenComp = comparado.darCiudadOrigen();
				String destinoComparado = comparado.darCiudadDestino();
				if (destinoActual.equals(origenComp) && !origenActual.equals(destinoComparado)&&!origenActual.equals(origenComp)){
					boolean[] diasActual = actual.darDias();
					boolean[] diasComparado = comparado.darDias();
					for (int k=0; k<7; k++){
						boolean conectadoEntreSemana = false;
						boolean conectadoFinDeSemana = false;
						if (diasActual[k]){
							for (int h=0;h<7;h++){
								if (diasComparado[h] && (!conectadoEntreSemana||!conectadoFinDeSemana)){
									boolean siguiente = false;
									if (h==k){
										if (comparado.darHoraSalida()<actual.darHoraLlegada()){
											siguiente = true;
										}
									}

									//Si h<k el vuelo es la siguiente semana
									if (h<k||siguiente){
										Double duracion = calcularDuracionTotal(actual, comparado, k, h, false);
										Double tarifa = calcularTarifa(h, comparado);
										Double tarifaAnterior = calcularTarifa(k, actual);
										if (tarifa<=0.0){
											System.err.println("Tarifa es igual a cero.");
										}
										if (duracion<=0.0){
											System.err.println("Duracion es igual a cero.");
										}

										if (h<4&&!conectadoEntreSemana){
											conectadoEntreSemana = true;
											agregarConexion(actual, comparado, tarifa, duracion,k, tarifaAnterior);
											//contador++;
										}
										else if (h>=4&&!conectadoFinDeSemana){
											conectadoFinDeSemana = true;
											agregarConexion(actual, comparado, tarifa, duracion,k, tarifaAnterior);
											//contador++;
										}
									}
									else{
										Double duracion = calcularDuracionTotal(actual, comparado, k, h, true);
										Double tarifa = calcularTarifa(h, comparado);
										Double tarifaAnterior = calcularTarifa(k, actual);
										if (tarifa<=0.0){
											System.err.println("Tarifa es igual a cero.");
										}
										if (duracion<=0.0){
											System.err.println("Duracion es igual a cero.");
											duracion = calcularDuracionTotal(actual, comparado, k, h, true);
										}
										if (h<4&&!conectadoEntreSemana){
											conectadoEntreSemana = true;
											agregarConexion(actual, comparado, tarifa, duracion,k,tarifaAnterior);
											//contador++;
										}
										else if (h>=4&&!conectadoFinDeSemana){
											conectadoFinDeSemana = true;
											agregarConexion(actual, comparado, tarifa, duracion,k,tarifaAnterior);
											//contador++;
										}
									}

								}
							}
						}
					}
				}

			}
		}
	}

	/**
	 * Crea nodos con los vuelos, los conecta y agrega a la tabla que representa el grafo
	 * @param pOrigen Vuelo origen
	 * @param pDestino Vuelo destino
	 * @param pTarifa Tarifa del vuelo origen (Peso)
	 * @param pDuracion Duración total desde que sale el vuelo origen hasta que sale el vuelo destino (Peso)
	 */
	private void agregarConexion(Vuelo origen, Vuelo destino, Double pTarifa, Double pDuracion, int pDia, Double pTarifaAnterior){
		Conexion conexion = new Conexion(pTarifa, pDuracion, pDia, pTarifaAnterior);
		grafoVuelos.addVertex(origen);
		grafoVuelos.addVertex(destino);
		grafoVuelos.addEdge(origen, destino, conexion);
		contador++;
	}

	/**
	 * Calcula el tiempo total desde que sale el vuelo actual hasta que sale el siguiente vuelo.
	 * @param actual Vuelo actual
	 * @param comparado Segundo vuelo
	 * @param pDiaActual Dia en el que sale el vuelo actual
	 * @param pDiaComparado Dia en el que sale el segundo vuelo
	 * @param estaSemana True si el segundo vuelo es en la misma semana que el vuelo actual. False de lo contrario
	 * @return Double con la cantidad de horas
	 */
	private Double calcularDuracionTotal(Vuelo actual, Vuelo comparado, int pDiaActual, int pDiaComparado, boolean estaSemana){
		Double resp = 0.0;
		//Calculo tiempo si es la misma semana
		if(estaSemana){
			if(actual.darHoraSalida()>actual.darHoraLlegada()){
				resp = actual.darDuracion() + (comparado.darHoraSalida() - actual.darHoraLlegada());
			}
			else if (comparado.darHoraSalida()<=actual.darHoraSalida()&&pDiaActual!=pDiaComparado){
				resp = (24-actual.darHoraSalida())+((pDiaComparado+1)-(pDiaActual+1)-1)*24+comparado.darHoraSalida();
			}
			else{
				resp = comparado.darHoraSalida()-actual.darHoraSalida();
			}
		}
		//Calculo tiempo si es un día de la siguiente semana
		else{
			resp = (actual.darDuracion()+(24-actual.darHoraLlegada())+(7-(pDiaActual+1))*24.0+(pDiaComparado)*24+comparado.darHoraSalida());
		}
		return resp;
	}

	/**
	 * Calcula la tarifa con la formula dada.
	 * @param pDuracion duración del vuelo
	 * @param pDia dia en el que sale el vuelo
	 * @param nombreAerolinea nombre de la aerolinea del vuelo
	 * @return Double con la tarifa del vuelo
	 */
	private Double calcularTarifa(int pDia, Vuelo v){
		Double resp = 0.0;
		if (pDia>=0&&pDia<=3){
			resp = v.costoEntreSemana(); 
		}
		else if (pDia>3&&pDia<7){
			resp = v.costoFinSemana();
		}
		return resp;
	}

	public ListaEncadenada<String> darCiudades() {
		return ciudades;
	}
}
