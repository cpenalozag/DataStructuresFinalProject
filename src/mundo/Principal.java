package mundo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import estructuras.AdjacencyList;
import estructuras.Arco;
import estructuras.Cola;
import estructuras.EdmondsChuLiu;
import estructuras.Grafo;
import estructuras.Grafo.Dijkstra;
import estructuras.Grafo.Kosaraju;
import estructuras.IIterator;
import estructuras.ListaEncadenada;
import estructuras.Nodo;
import estructuras.TablaHashLP;

public class Principal {

	/**
	 * Ruta del archivo de tarifas.
	 */
	public static final String RUTA_TARIFAS = "./data/Tarifas.csv";

	/**
	 * Ruta del archivo de tarifas.
	 */
	public static final String RUTA_TARIFAS_NACIONALES = "./data/TarifasNacionales.csv";


	/**
	 * Ruta del archivo de vuelos.
	 */
	public static final String RUTA_VUELOS = "./data/Vuelos.csv";

	/**
	 * Ruta del archivo de vuelos nacionales.
	 */
	public static final String RUTA_VUELOS_NACIONALES = "./data/VuelosNacionales.csv";

	/**
	 * Tabla hash con las aerolineas
	 */
	private TablaHashLP<String, Aerolinea> aerolineas;

	/**
	 * Lista de vertices del grafo (vuelos)
	 */
	private TablaHashLP<Integer,Vuelo> vuelos;

	/**
	 * Grafo que modela el sistema de vuelos de la aerocivil
	 */
	private Grafo<Vuelo, Conexion> grafoVuelos;

	/**
	 * Grafo que modela la conexion entre las ciudades
	 */
	private Grafo<Ciudad, Conexion> grafoCiudades;
	/** 
	 * Ciudades que existen en la base de datos
	 */
	private TablaHashLP<String, Ciudad> ciudades;

	private int contador;

	private int indiceVuelos = 0;

	private int contadorCiudades;

	private TablaHashLP<String, Grafo<Ciudad, Conexion>> tablaAeroGrafo;

	private Grafo<Vuelo, Conexion> grafoCiudadesMST;

	private Grafo<Vuelo, Conexion> mst10;

	private int contadorMST;
	private int inVuelo;

	public Principal(){
		aerolineas = new TablaHashLP<>(20);
		ciudades = new TablaHashLP<>();
		vuelos = new TablaHashLP<Integer,Vuelo>(1777);	
		tablaAeroGrafo = new TablaHashLP<>();
		//		grafoVuelos.componentesConectados();
	}

	public void cargarYconectar(){
		cargarArchivos();
		grafoVuelos = new Grafo<Vuelo,Conexion>(vuelos.size());
		grafoCiudades = new Grafo<Ciudad, Conexion>(ciudades.size());
		//contador = 0;
		//contadorCiudades = 0;
		conectarVuelos();
		conectarCiudades();
		IIterator<String> ite = aerolineas.keys();
		while(ite.hasNext())
		{
			String act = ite.next();
			Aerolinea aero = aerolineas.get(act);
			aero.conectarVuelos();
		}
	}

	/**
	 * Metodo que carga los archivos
	 */
	public void cargarArchivos()
	{

		try 
		{
			@SuppressWarnings("resource")
			BufferedReader l = new BufferedReader(new FileReader(RUTA_TARIFAS_NACIONALES));
			l.readLine();
			String linea = "";
			while ((linea = l.readLine()) != null)
			{

				String [] lineaD = linea.split(",");
				String nombre = lineaD[0];
				Double tarifaMinima = Double.parseDouble(lineaD[1].split(" ")[0]);
				int sillas = Integer.parseInt(lineaD[2]);

				agregarAerolinea(nombre, tarifaMinima, sillas);
			}
			l = new BufferedReader(new FileReader(RUTA_VUELOS_NACIONALES));
			l.readLine();
			String aerolineaActual = "";
			String numeroVueloActual = "";
			String origenActual = "";
			String destinoActual = "";
			String horaSalidaActual = "";
			String horaLlegadaActual = "";
			linea = "";

			while((linea = l.readLine())!=null)
			{
				String [] partes = linea.split(";");
				String aerolinea = partes[0];
				if (aerolinea.isEmpty())
				{
					aerolinea = aerolineaActual;
				}

				String numVuelo = partes[1];
				if (numVuelo.isEmpty())
				{
					numVuelo = numeroVueloActual;
				}

				String origen = partes[2];
				if (origen.isEmpty())
				{
					origen = origenActual;
				}


				String destino = partes[3];
				if (destino.isEmpty())
				{
					destino = destinoActual;
				}

				String horaSalida = partes[4];
				if (horaSalida.isEmpty())
				{
					horaSalida = horaSalidaActual;
				}

				String horaLlegada = partes[5];
				if (horaLlegada.isEmpty())
				{
					horaLlegada = horaLlegadaActual;
				}

				String tipoEquipo = partes[6];
				int numSillas = Integer.parseInt(partes[7]);

				boolean[] dias = new boolean [7];

				for (int i = 0; i < 7; i++)
				{
					dias[i] = !partes[i+8].isEmpty();
				}

				numeroVueloActual = numVuelo;
				aerolineaActual = aerolinea;
				origenActual = origen;
				destinoActual = destino;
				horaSalidaActual = horaSalida;
				horaLlegadaActual = horaLlegada;

				Aerolinea aeroVuelo = aerolineas.get(aerolineaActual);
				Vuelo aux = agregarVuelo(aeroVuelo.darNombre(), numSillas ,numVuelo, origen, destino, horaSalida, horaLlegada, tipoEquipo, dias);
				agregarCiudadDestino(destino, aux);
				agregarCiudadOrigen(origen, aux);
				aeroVuelo.agregarVuelo(aux);

			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Recorre la lista de vuelos creando las conexiones. Para esto, recorre la lista de vuelos y conecta todos en los
	 * que la ciudad destino del vuelo 1 sea la ciudad origen del vuelo 2 y haya máximo una semana entre ellos.
	 */
	public void conectarVuelos(){
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
		//System.out.println("Se han creado " + contador + " conexiones entre vuelos");
	}

	public void conectarCiudades(){
		IIterator<Integer> iter = vuelos.keys();
		TablaHashLP<String, ListaEncadenada<String>> conexionCiudades = new TablaHashLP<String, ListaEncadenada<String>>();
		while(iter.hasNext())
		{
			Integer act = iter.next();
			Vuelo aux = vuelos.get(act);
			String origen = aux.darCiudadOrigen();
			String destino = aux.darCiudadDestino();

			if(conexionCiudades.get(origen) == null)
			{
				ListaEncadenada<String> lista = new ListaEncadenada<String>();
				lista.add(destino);
				conexionCiudades.put(origen, lista);
			}
			else
			{
				ListaEncadenada<String> lista = conexionCiudades.get(origen);
				IIterator<String> it = lista.iterador();
				boolean yaExiste = false;
				while (it.hasNext() && !yaExiste)
				{
					String ciudad = it.next();
					if(ciudad.equals(destino))
					{
						yaExiste = true;
					}
				}
				if(!yaExiste)
				{
					lista.add(destino);
				}
			}

			//System.out.println("ACA ACA ACA " + conexionCiudades.size());
		}

		IIterator<String> iterator = conexionCiudades.keys();
		while(iterator.hasNext())
		{
			String actual = iterator.next();
			Ciudad origen = ciudades.get(actual);
			ListaEncadenada<String> listaDestinos = conexionCiudades.get(actual);
			IIterator<String> itDestinos = listaDestinos.iterador();
			while(itDestinos.hasNext())
			{
				String destino = itDestinos.next();
				Ciudad destino2 = ciudades.get(destino);
				agregarConexionCiudades(origen, destino2);
			}
		}

		System.out.println("Se han creado " + contadorCiudades + " conexiones entre ciudades");
	}

	public void conectarCiudadesAerolinea (String pAerolinea)
	{
		Aerolinea aero = aerolineas.get(pAerolinea);
		TablaHashLP<Integer, Vuelo> pvuelos = aero.darVuelos();
		IIterator<Integer> iter = pvuelos.keys();
		TablaHashLP<String, ListaEncadenada<String>> conexionCiudades = new TablaHashLP<String, ListaEncadenada<String>>();
		while(iter.hasNext())
		{
			Integer act = iter.next();
			Vuelo aux = pvuelos.get(act);
			String origen = aux.darCiudadOrigen();
			String destino = aux.darCiudadDestino();

			if(conexionCiudades.get(origen) == null)
			{
				ListaEncadenada<String> lista = new ListaEncadenada<String>();
				lista.add(destino);
				conexionCiudades.put(origen, lista);
			}
			else
			{
				ListaEncadenada<String> lista = conexionCiudades.get(origen);
				IIterator<String> it = lista.iterador();
				boolean yaExiste = false;
				while (it.hasNext() && !yaExiste)
				{
					String ciudad = it.next();
					if(ciudad.equals(destino))
					{
						yaExiste = true;
					}
				}
				if(!yaExiste)
				{
					lista.add(destino);
				}
			}
		}

		Grafo<Ciudad, Conexion> grafito = new Grafo<Ciudad, Conexion>(conexionCiudades.size());
		IIterator<String> iterator = conexionCiudades.keys();
		while(iterator.hasNext())
		{
			String actual = iterator.next();
			Ciudad origen = ciudades.get(actual);
			ListaEncadenada<String> listaDestinos = conexionCiudades.get(actual);
			IIterator<String> itDestinos = listaDestinos.iterador();
			while(itDestinos.hasNext())
			{
				String destino = itDestinos.next();
				Ciudad destino2 = ciudades.get(destino);
				agregarConexionCiudadGrafo(grafito, origen, destino2);
			}
		}
		tablaAeroGrafo.put(pAerolinea, grafito);
		System.out.println(grafito.componentesConectados());

	}


	/**
	 * Retorna un arreglo con los vuelos
	 */
	public Vuelo[] darVuelos(){
		Vuelo[] arregloVuelos = new Vuelo[vuelos.size()];
		IIterator<Integer> it = vuelos.keys();
		int i=0;
		while(it.hasNext()){
			int k = it.next();
			Vuelo actual = vuelos.get(k);
			arregloVuelos[i]=actual;
			i++;
		}
		return arregloVuelos;
	}

	public void eliminarCiudad(String pNombreCiudad){
		Ciudad eliminar = ciudades.get(pNombreCiudad);
		ListaEncadenada<Vuelo> vuelosEntrada = eliminar.darVuelosEntrada();
		ListaEncadenada<Vuelo> vuelosSalida = eliminar.darVuelosSalida();

		IIterator<Vuelo> iterE = vuelosEntrada.iterador();
		while (iterE.hasNext()){
			Vuelo actualE = iterE.next();
			int idE = actualE.darIdentificador();
			vuelos.delete(idE);
			grafoVuelos.removeVertex(actualE);

		}
		IIterator<Vuelo> iterS = vuelosSalida.iterador();
		while (iterS.hasNext()){
			Vuelo actualS = iterS.next();
			int idS = actualS.darIdentificador();
			vuelos.delete(idS);
			grafoVuelos.removeVertex(actualS);
		}
		ciudades.delete(pNombreCiudad);
	}

	public int darNumeroAerolineas(){
		return aerolineas.size();
	}

	public int darNumeroCiudades(){
		return ciudades.size();
	}

	public int darNumeroVuelos(){
		return vuelos.size();
	}

	public void agregarCiudad(String pNombre){
		Ciudad nueva = new Ciudad(pNombre);
		ciudades.put(pNombre, nueva);
	}

	public void agregarAerolinea(String pNombre, Double pTarifa, int pSillas){
		Aerolinea nueva = new Aerolinea(pNombre, pTarifa, pSillas);
		aerolineas.put(pNombre, nueva);
	}

	public void eliminarAerolinea(String pNombre){
		aerolineas.delete(pNombre);
		IIterator<Integer> iter = vuelos.keys();
		while(iter.hasNext()){
			Integer llave = iter.next();
			Vuelo act = vuelos.get(llave);
			String actual = act.darAerolinea().darNombre();
			if (actual.equals(pNombre)){
				grafoVuelos.removeVertex(act);
			}
		}
	}

	public Vuelo agregarVuelo(String pAerolinea, int pNumSillas, String numVuelo, String pOrigen, String pDestino, String horaSalida, String horaLlegada, String tipoEquipo, boolean[] pDias){
		Aerolinea aero = aerolineas.get(pAerolinea);
		Vuelo nuevo = new Vuelo(Integer.parseInt(numVuelo), pNumSillas, aero, pOrigen, pDestino, horaSalida, horaLlegada, pDias, tipoEquipo);
		indiceVuelos++;
		vuelos.put(indiceVuelos,nuevo);
		return nuevo;
	}


	public void imprimirCiudadesConectadas()
	{
		System.out.println(grafoCiudades.componentesConectados());
	}


	public void imprimirCiudadesConectadasPorAerolinea()
	{
		IIterator<String> it = aerolineas.keys();
		while(it.hasNext())
		{
			String act = it.next();
			System.out.println("Los componentes conectados de la aeroline " + act + " son: ");
			conectarCiudadesAerolinea(act);
			System.out.println(" \n");
		}
	}

	public void probarConexionesCiudad(){
		IIterator<ListaEncadenada<Arco<Vuelo, Conexion>>> it = grafoVuelos.darListasArcos();
		while (it.hasNext()){
			ListaEncadenada<Arco<Vuelo, Conexion>> lista = it.next();
			IIterator<Arco<Vuelo, Conexion>> arcos = lista.iterador();
			while (arcos.hasNext()){
				Arco<Vuelo, Conexion> actual = arcos.next();
				Vuelo nodo1 = actual.darNodoInicio().darVertice();
				Vuelo nodo2 = actual.darNodoFin().darVertice();
				System.out.println();
			}
		}
	}

	public void imprimirItinerarioTodas(String ciudadOrigen, String pAerolinea, String criterio){
		Aerolinea a = aerolineas.get(pAerolinea);
		IIterator<String> itC = a.darCiudades().iterador();
		int diaPartida = 0;
		while (itC.hasNext()){
			String ciudadDestino = itC.next();
			Itinerario i = itinerario(ciudadOrigen, ciudadDestino, diaPartida, criterio);
			if (i!=null){
				StringBuilder sb = i.darItinerario();
				diaPartida=i.darUltimoDia();
				System.out.println(sb);
			}
		}

	}

	public void imprimirItinerario(String ciudadOrigen, String ciudadDestino, int diaPartida){
		Itinerario i = itinerario(ciudadOrigen, ciudadDestino, diaPartida, Conexion.PESO_COSTO);
		StringBuilder sb = i.darItinerario();
		System.out.println(sb);
	}

	public void itinAero(String ciudadOrigen, String ciudadDestino, String nAerolinea, String criterio){

		Itinerario it = itinerarioPorAerolinea(ciudadOrigen, ciudadDestino, criterio, nAerolinea);
		if (it!=null){
			System.out.println(it.darItinerario().toString());
			System.out.println("Costo total: "+it.darPesoTotal());
			System.out.println();
			String destino = it.darDestino();
			it = itinerarioPorAerolinea(destino, ciudadOrigen, criterio, nAerolinea);
		}
	}

	public void rutaMasLargaAerolinea(String pAerolinea, int dia, Double hora){
		IIterator<Vuelo> vuelos = aerolineas.get(pAerolinea).darVuelos().values();
		while (vuelos.hasNext()){
			Vuelo act = vuelos.next();
			Dijkstra<Vuelo, Conexion> d = new Dijkstra<Vuelo, Conexion>(grafoVuelos, act, Conexion.PESO_COSTO);
			IIterator<Vuelo> vue = aerolineas.get(pAerolinea).darVuelos().values();
			while (vue.hasNext()){
				int di = -1;
				Vuelo dest = vue.next();
				if (vue!=act){
					if (d.hasPathTo(dest)){
						if (di == -1){
							Itinerario  i = itinerario(act.darCiudadDestino(), dest.darCiudadDestino(), dia, Conexion.PESO_COSTO);
							di = i.darUltimoDia();
							StringBuilder sb = i.darItinerario();
							System.out.println(sb);
						}
						else{
							Itinerario  i = itinerario(act.darCiudadDestino(), dest.darCiudadDestino(), di, Conexion.PESO_COSTO);
							StringBuilder sb = i.darItinerario();
							System.out.println(sb);
						}
					}
				}
			}
		}

	}

	public void rutaMas(int dia, Double hora){
		IIterator<Vuelo> vuelo = vuelos.values();
		while (vuelo.hasNext()){
			Vuelo act = vuelo.next();
			Dijkstra<Vuelo, Conexion> d = new Dijkstra<Vuelo, Conexion>(grafoVuelos, act, Conexion.PESO_COSTO);
			IIterator<Vuelo> vue = vuelos.values();
			while (vue.hasNext()){
				int di = -1;
				Vuelo dest = vue.next();
				if (vue!=act){
					if (d.hasPathTo(dest)){
						if (di == -1){
							Itinerario  i = itinerario(act.darCiudadDestino(), dest.darCiudadDestino(), dia, Conexion.PESO_COSTO);
							di = i.darUltimoDia();
							StringBuilder sb = i.darItinerario();
							System.out.println(sb);
						}
						else{
							Itinerario  i = itinerario(act.darCiudadDestino(), dest.darCiudadDestino(), di, Conexion.PESO_COSTO);
							StringBuilder sb = i.darItinerario();
							System.out.println(sb);
						}
					}
				}
			}
		}

	}

	public void imprimirItinerariosAerolineas(String ciudadOrigen, String ciudadDestino, int diaPartida){
		IIterator<Aerolinea> itAerolineas = aerolineas.values();
		ArrayList<Itinerario> its = new ArrayList<>();
		while(itAerolineas.hasNext()){
			Aerolinea actual = itAerolineas.next();
			Itinerario it = itinerarioPorAerolineaYDia(ciudadOrigen, ciudadDestino, diaPartida, Conexion.PESO_COSTO, actual.darNombre());
			if (it!=null){
				its.add(it);
			}
		}
		while (!its.isEmpty()){
			Itinerario minIt = its.get(0);
			Double min = minIt.darPesoTotal();
			for (int j=1;j<its.size();j++){
				if (its.get(j).darPesoTotal()<min){
					minIt = its.get(j);
					min = minIt.darPesoTotal();
				}
			}

			System.out.println(minIt.darItinerario().toString());
			System.out.println("Costo total: "+minIt.darPesoTotal());
			System.out.println();
			its.remove(minIt);
		}
	}

	private Itinerario itinerarioPorAerolinea(String ciudadOrigen, String ciudadDestino, String criterio, String nAerolinea){
		Ciudad partida = ciudades.get(ciudadOrigen);
		Ciudad llegada = ciudades.get(ciudadDestino);
		IIterator<Vuelo> itSalida = partida.darVuelosSalida().iterador();

		Vuelo salidaMin = null;
		Vuelo llegadaMin = null;
		Double pesoMin = 10000.0;

		while(itSalida.hasNext()){
			Vuelo actual = itSalida.next();
			if (!actual.darAerolinea().equals(nAerolinea)){
				continue;
			}
			Grafo.Dijkstra<Vuelo, Conexion> dijkstra = new Grafo.Dijkstra<Vuelo, Conexion>(grafoVuelos, actual, criterio);
			IIterator<Vuelo> itLlegada = llegada.darVuelosEntrada().iterador();
			while (itLlegada.hasNext()){
				Vuelo actualLlegada = itLlegada.next();
				if (dijkstra.hasPathTo(actualLlegada)&&actualLlegada.darAerolinea().equals(nAerolinea)){
					Double pesoActual = dijkstra.distTo(actualLlegada);

					if (pesoActual<pesoMin&&pesoActual!=0.0){
						salidaMin = actual;
						llegadaMin = actualLlegada;
						pesoMin = pesoActual;
					}
				}
			}
		}
		Grafo.Dijkstra<Vuelo, Conexion> d = new Grafo.Dijkstra<Vuelo, Conexion>(grafoVuelos, salidaMin, criterio);
		IIterator<Arco<Vuelo, Conexion>> iteradorCaminoMasCorto = d.pathTo(llegadaMin);
		Itinerario itinerario = new Itinerario(iteradorCaminoMasCorto, criterio);
		return itinerario;
	}

	private Itinerario itinerarioPorAerolineaYDia(String ciudadOrigen, String ciudadDestino, int diaPartida, String criterio, String aerolinea){
		IIterator<Vuelo> itSalida = aerolineas.get(aerolinea).darVuelos().values();

		Vuelo salidaMin = null;
		Vuelo llegadaMin = null;
		Double pesoMin = 10000.0;

		while(itSalida.hasNext()){
			Vuelo actual = itSalida.next();
			if (actual.darCiudadOrigen().equals(ciudadOrigen)){

				boolean[] diasActual = actual.darDias();
				if (!diasActual[diaPartida])
				{
					continue;
				}
				Grafo.Dijkstra<Vuelo, Conexion> dijkstra = new Grafo.Dijkstra<Vuelo, Conexion>(aerolineas.get(aerolinea).darGrafo(), actual, criterio);
				IIterator<Vuelo> itLlegada = aerolineas.get(aerolinea).darVuelos().values();
				while (itLlegada.hasNext()){
					Vuelo actualLlegada = itLlegada.next();
					if (actualLlegada.darCiudadDestino().equals(ciudadDestino)){
						if (dijkstra.hasPathTo(actualLlegada)){
							Double pesoActual = dijkstra.distTo(actualLlegada);

							if (pesoActual<pesoMin&&pesoActual!=0.0){
								salidaMin = actual;
								llegadaMin = actualLlegada;
								pesoMin = pesoActual;
							}
						}
					}
				}
			}
		}
		Itinerario itinerario = null;
		if (salidaMin!=null){
			Grafo.Dijkstra<Vuelo, Conexion> d = new Grafo.Dijkstra<Vuelo, Conexion>(grafoVuelos, salidaMin, criterio);
			IIterator<Arco<Vuelo, Conexion>> iteradorCaminoMasCorto = d.pathTo(llegadaMin);
			itinerario = new Itinerario(iteradorCaminoMasCorto, criterio);
		}
		return itinerario;
	}

	//Ciudad origen, ciudad destino 
	/**
	 * @param ciudadOrigen
	 * @param ciudadDestino
	 * @param diaPartida
	 */

	private Itinerario itinerario(String ciudadOrigen, String ciudadDestino, int diaPartida, String criterio){
		Ciudad partida = ciudades.get(ciudadOrigen);
		Ciudad llegada = ciudades.get(ciudadDestino);
		IIterator<Vuelo> itSalida = partida.darVuelosSalida().iterador();

		Vuelo salidaMin = null;
		Vuelo llegadaMin = null;
		Double pesoMin = 10000.0;

		while(itSalida.hasNext()){
			Vuelo actual = itSalida.next();
			boolean[] diasActual = actual.darDias();
			if (!diasActual[diaPartida])
			{
				continue;
			}
			Grafo.Dijkstra<Vuelo, Conexion> dijkstra = new Grafo.Dijkstra<Vuelo, Conexion>(grafoVuelos, actual, criterio);
			IIterator<Vuelo> itLlegada = llegada.darVuelosEntrada().iterador();
			while (itLlegada.hasNext()){
				Vuelo actualLlegada = itLlegada.next();
				if (dijkstra.hasPathTo(actualLlegada)){
					Double pesoActual = dijkstra.distTo(actualLlegada);

					if (pesoActual<pesoMin&&pesoActual!=0.0){
						salidaMin = actual;
						llegadaMin = actualLlegada;
						pesoMin = pesoActual;
					}
				}
			}
		}
		Itinerario itinerario = null;
		if (salidaMin!=null){
			Grafo.Dijkstra<Vuelo, Conexion> d = new Grafo.Dijkstra<Vuelo, Conexion>(grafoVuelos, salidaMin, criterio);
			IIterator<Arco<Vuelo, Conexion>> iteradorCaminoMasCorto = d.pathTo(llegadaMin);
			itinerario = new Itinerario(iteradorCaminoMasCorto, criterio);
		}
		return itinerario;
	}

	private Itinerario itinerario2(String ciudadOrigen, String ciudadDestino, int diaPartida, String criterio){
		Ciudad partida = ciudades.get(ciudadOrigen);
		Ciudad llegada = ciudades.get(ciudadDestino);
		IIterator<Vuelo> itSalida = partida.darVuelosSalida().iterador();

		Vuelo salidaMin = null;
		Vuelo llegadaMin = null;
		Double pesoMin = 10000.0;

		while(itSalida.hasNext()){
			Vuelo actual = itSalida.next();

			Grafo.Dijkstra<Vuelo, Conexion> dijkstra = new Grafo.Dijkstra<Vuelo, Conexion>(grafoVuelos, actual, criterio);
			IIterator<Vuelo> itLlegada = llegada.darVuelosEntrada().iterador();
			while (itLlegada.hasNext()){
				Vuelo actualLlegada = itLlegada.next();
				if (dijkstra.hasPathTo(actualLlegada)){
					Double pesoActual = dijkstra.distTo(actualLlegada);

					if (pesoActual<pesoMin&&pesoActual!=0.0){
						salidaMin = actual;
						llegadaMin = actualLlegada;
						pesoMin = pesoActual;
					}
				}
			}
		}
		Itinerario itinerario = null;
		if (salidaMin!=null){
			Grafo.Dijkstra<Vuelo, Conexion> d = new Grafo.Dijkstra<Vuelo, Conexion>(grafoVuelos, salidaMin, criterio);
			IIterator<Arco<Vuelo, Conexion>> iteradorCaminoMasCorto = d.pathTo(llegadaMin);
			itinerario = new Itinerario(iteradorCaminoMasCorto, criterio);
		}
		return itinerario;
	}

	public void imprimirMejorRutaTodasCiudadesAerolinea(String pCiudadOrigen, String pAerolinea, String criterio){
		Aerolinea buscada = aerolineas.get(pAerolinea);
		Grafo vuelosA = buscada.darGrafo();
		ArrayList<Itinerario> its = new ArrayList<>();
		IIterator<Vuelo> it = aerolineas.get(pAerolinea).darVuelos().values();
		while (it.hasNext()){
			Vuelo actual = it.next();
			if (actual.darCiudadOrigen().equals(pCiudadOrigen)){
				Dijkstra<Vuelo, Conexion> dij = new Dijkstra<Vuelo, Conexion>(vuelosA, actual,criterio);
				IIterator<Vuelo> iter = aerolineas.get(pAerolinea).darVuelos().values();
				while (iter.hasNext()){
					Vuelo act = iter.next();
					String origenAct = act.darCiudadOrigen();
					String destinoActual = act.darCiudadDestino();

					if (origenAct!=pCiudadOrigen){
						if (dij.hasPathTo(act)){
							Itinerario itin = new Itinerario(dij.pathTo(act), criterio);
							its.add(itin);
						}
					}
				}
			}
		}
		boolean encontro = false;
		while (!encontro){
			Itinerario minIt = its.get(0);
			Double min = minIt.darPesoTotal();
			for (int j=1;j<its.size();j++){
				if (its.get(j).darPesoTotal()<min){
					minIt = its.get(j);
					min = minIt.darPesoTotal();
					StringBuilder sb = minIt.darItinerario();
					System.out.println(sb.toString());
				}
			}
			encontro = true;
			System.out.println(minIt);
			StringBuilder sb = minIt.darItinerario();
			System.out.println(sb.toString());
			System.out.println();
		}
	}

	public void imprimirMejorRutaTodasCiudadesAerolineaConDia(String pCiudadOrigen, int diaPartida, String pAerolinea){
		Aerolinea buscada = aerolineas.get(pAerolinea);
		Grafo vuelosA = buscada.darGrafo();
		ArrayList<Itinerario> its = new ArrayList<>();
		IIterator<Vuelo> it = aerolineas.get(pAerolinea).darVuelos().values();
		System.out.println("PUNTO 1");
		while (it.hasNext())
		{System.out.println("PUNTO 2");
		Vuelo actual = it.next();
		if (actual.darCiudadOrigen().equals(pCiudadOrigen)){
			boolean[] diasActual = actual.darDias();
			if (!diasActual[diaPartida])
			{
				continue;
			}
			Dijkstra<Vuelo, Conexion> dij = new Dijkstra<Vuelo, Conexion>(vuelosA, actual,Conexion.PESO_COSTO);
			IIterator<Vuelo> iter = aerolineas.get(pAerolinea).darVuelos().values();
			while (iter.hasNext()){
				Vuelo act = iter.next();
				String origenAct = act.darCiudadOrigen();
				String destinoActual = act.darCiudadDestino();

				if (origenAct!=pCiudadOrigen){
					if (dij.hasPathTo(act)){
						Itinerario itin = new Itinerario(dij.pathTo(act), Conexion.PESO_COSTO);
						its.add(itin);
					}
				}
			}
		}
		}
		boolean encontro = false;
		while (!encontro){
			Itinerario minIt = its.get(0);
			Double min = minIt.darPesoTotal();
			for (int j=1;j<its.size();j++){
				if (its.get(j).darPesoTotal()<min){
					minIt = its.get(j);
					min = minIt.darPesoTotal();
					StringBuilder sb = minIt.darItinerario();
					System.out.println(sb);
					System.out.println("Costo total: "+minIt.darPesoTotal());
				}
			}
			encontro = true;
			System.out.println(minIt);
			StringBuilder sb = minIt.darItinerario();
			System.out.println(sb);
			System.out.println("Costo total: "+minIt.darPesoTotal());
			System.out.println();
		}
	}


	/**
	 * Crea nodos con los vuelos, los conecta y agrega a la tabla que representa el grafo
	 * @param pOrigen Vuelo origen
	 * @param pDestino Vuelo destino
	 * @param pTarifa Tarifa del vuelo origen (Peso)
	 * @param pDuracion Duración total desde que sale el vuelo origen hasta que sale el vuelo destino (Peso)
	 */
	private void agregarConexion(Vuelo origen, Vuelo destino, Double pTarifa, Double pDuracion, int pDia, Double pCostoAnterior){
		Conexion conexion = new Conexion(pTarifa, pDuracion, pDia, pCostoAnterior);
		grafoVuelos.addVertex(origen);
		grafoVuelos.addVertex(destino);
		grafoVuelos.addEdge(origen, destino, conexion);
		contador++;
	}

	private void agregarConexionCiudades(Ciudad pOrigen, Ciudad pDestino){
		Conexion conexion = new Conexion(0.0,0.0, 0,0.0);
		grafoCiudades.addVertex(pOrigen);
		grafoCiudades.addVertex(pDestino);
		grafoCiudades.addEdge(pOrigen, pDestino, conexion);
		contadorCiudades++;
	}

	private void agregarConexionCiudadGrafo(Grafo<Ciudad, Conexion> grafito, Ciudad pOrigen, Ciudad pDestino)
	{
		Conexion conexion = new Conexion(0.0, 0.0, 0, 0.0);
		grafito.addVertex(pOrigen);
		grafito.addVertex(pDestino);
		grafito.addEdge(pOrigen, pDestino, conexion);
		contadorCiudades++;
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
	private Double calcularTarifa(int pDia, Vuelo vuelo){
		Double resp = 0.0;
		if (pDia>=0&&pDia<=3){
			resp = vuelo.costoEntreSemana(); 
		}
		else if (pDia>3&&pDia<7){
			resp = vuelo.costoFinSemana();
		}
		return resp;
	}

	private void agregarCiudadDestino(String pNombre, Vuelo pVueloEntrada)
	{
		if(!ciudades.contains(pNombre))
		{
			Ciudad nueva = new Ciudad(pNombre);
			nueva.anhadirVueloEntrada(pVueloEntrada);
			ciudades.put(pNombre, nueva);
		}
		else
		{
			Ciudad actual = ciudades.get(pNombre);
			actual.anhadirVueloEntrada(pVueloEntrada);
		}
	}

	private void agregarCiudadOrigen(String pNombre, Vuelo pVueloSalida)
	{
		if(!ciudades.contains(pNombre))
		{
			Ciudad nueva = new Ciudad(pNombre);
			nueva.anhadirVueloSalida(pVueloSalida);
			ciudades.put(pNombre, nueva);
		}
		else
		{
			Ciudad actual = ciudades.get(pNombre);
			actual.anhadirVueloSalida(pVueloSalida);
		}
	}

	public void grafoCiudadesComponentesMST(String ciudad, String aerolinea, int req) throws Exception 
	{
		Cola<Object> colaCiudades = grafoCiudades.ciudadesConectadasMST();
		System.out.println("Tama�o cola ciudades " + colaCiudades.size());
		TablaHashLP<Integer, Vuelo> vuelosHash = new TablaHashLP<Integer,Vuelo>();
		boolean cambio = false;
		int cont = 0;
		while(!cambio && !colaCiudades.isEmpty()){
			vuelosHash = new TablaHashLP<Integer, Vuelo>();
			grafoCiudadesMST = new Grafo<>(colaCiudades.size());

			while(!colaCiudades.isEmpty()&&!cambio){

				Object c = colaCiudades.dequeue();
				if(c.toString().equals("cambio"))
				{
					cambio=true;
					break;
				}
				Ciudad ciu = ciudades.get(c.toString());
				IIterator<Vuelo> it = ciu.darVuelosSalida().iterador();
				while(it.hasNext())
				{
					Vuelo v = it.next();
					vuelosHash.put(v.darNumeroVuelo(),v );
				}
			}
			if(vuelosHash.size()>=0&&vuelosHash.size()<2)
				break;
			IIterator<Integer> iter = vuelosHash.keys();
			while(iter.hasNext()){
				Integer llave = iter.next();
				Vuelo actual = vuelosHash.get(llave);
				String destinoActual = actual.darCiudadDestino();
				IIterator<Integer> it = vuelosHash.keys();
				while (it.hasNext()){
					Integer cLllave = it.next();
					Vuelo comparado = vuelosHash.get(cLllave);
					String origenComp = comparado.darCiudadOrigen();
					if (destinoActual.equals(origenComp)){
						boolean[] diasActual = actual.darDias();
						boolean[] diasComparado = comparado.darDias();
						boolean siguiente = false;

						for (int k=0; k<7&&diasActual[k]; k++){

							for (int h=0;h<7 && diasComparado[k];h++)
							{
								if (h<=k||siguiente)
								{
									Double duracion = calcularDuracionTotal(actual, comparado, k, h, false);
									Double tarifa = calcularTarifa(k, actual);
									agregarMSTConexion(actual, comparado, tarifa, duracion, k);
								}
								else
								{
									Double duracion = calcularDuracionTotal(actual, comparado, k, h, true);
									Double tarifa = calcularTarifa(k, actual);
									agregarMSTConexion(actual, comparado, tarifa, duracion, k);
								}

							}
						}
					}

				}

			}
			System.out.println(pruebaEdmonds(vuelosHash,ciudad,aerolinea,"LUNES",req));	
			contadorMST = 0;
			cont++;
			cambio = false;
		}
	}



	private void agregarMSTConexion(Vuelo origen, Vuelo destino, double tarifa, double duracion, int dia)
	{
		Conexion conexion = new Conexion(tarifa, duracion,dia,0.0);
		grafoCiudadesMST.addVertex(origen);
		grafoCiudadesMST.addVertex(destino);
		grafoCiudadesMST.addEdge(origen, destino, conexion);
		contadorMST++;
	}
	private void agregarMSTConexionAero(Vuelo origen, Vuelo destino, double tarifa, double duracion, int dia)
	{
		contadorMST++;
		Conexion conexion = new Conexion(tarifa, duracion,dia,0.0);
		mst10.addVertex(origen);
		mst10.addVertex(destino);
		mst10.addEdge(origen, destino, conexion);
	}

	public String pruebaEdmonds(TablaHashLP<Integer, Vuelo> vuelosEntra, String ciudad, String aerolinea, String dia, int req) throws Exception
	{
		String mensaje = "";
		if(grafoCiudadesMST.vertexCount()!=0)
		{

			Integer key = 0;
			Ciudad ciu = ciudades.get(ciudad);
			IIterator<Vuelo> itl = ciu.darVuelosSalida().iterador();
			if(req==1){
				Vuelo l = itl.next();
				if(l==null)
				{
					System.out.println("El componente no contiene la ciudad indicada");
				}
			}

			AdjacencyList<Vuelo, Conexion> adj  = new AdjacencyList<>();
			TablaHashLP<Nodo<Vuelo, Conexion>, ListaEncadenada<Arco<Vuelo, Conexion>>> ta = new TablaHashLP<>();
			int cont = 0;
			if(req==2){
				Aerolinea aerol = aerolineas.get(aerolinea);
				IIterator<Integer> irt = aerol.darVuelos().keys();
				while(irt.hasNext())
				{
					Vuelo v = vuelosEntra.get(irt.next());
					while(itl.hasNext())
					{
						if(v.darCiudadOrigen().equals(itl.next().darCiudadOrigen())){
							key = v.darIdentificador();
						}

					}
				}
			}
			IIterator<Integer> hola = vuelosEntra.keys();
			int conto = 0;
			while(hola.hasNext()){
				int in = hola.next();
				if(conto == 2)
					key = in;
				Vuelo vu = vuelosEntra.get(in);
				Nodo<Vuelo, Conexion> nod = new Nodo<Vuelo, Conexion>(vu);
				ListaEncadenada<Arco<Vuelo, Conexion>> ady = grafoCiudadesMST.darAdyacencias(vu);
				ta.put(nod, ady);
				conto++;
			}


			Vuelo v = vuelosEntra.get(key);


			Nodo<Vuelo, Conexion > nodo = new Nodo<Vuelo, Conexion>(v);

			adj.setAdjacencyList(ta);
			EdmondsChuLiu<Vuelo, Conexion> ed = new EdmondsChuLiu<>();
			AdjacencyList<Vuelo, Conexion> resp = ed.getMinBranching(nodo, adj, "duracion");
			IIterator<Nodo<Vuelo,Conexion>> g = resp.iteradorNodos();
			while(g.hasNext())
			{
				Nodo<Vuelo,Conexion> t = g.next();
				ListaEncadenada<Arco<Vuelo, Conexion>> lista = t.darArcos();
				IIterator<Arco<Vuelo, Conexion>> listItr = lista.iterador();
				Arco<Vuelo, Conexion> arc = null;
				while(listItr.hasNext())
				{
					arc = listItr.next();


				}
				if(arc.darNodoInicio().darVertice().darIdentificador() == t.darVertice().darIdentificador()){
					mensaje += ("< " + t.darVertice().darCiudadOrigen() + " > , < " + t.darVertice().darCiudadDestino() + "> , <"
							+ t.darVertice().darAerolinea().darNombre() + " > , < "+ t.darVertice().darHoraSalida() + "-"+ t.darVertice().darHoraLlegada()+ " > , < \n");
					//+ t.darVertice().darDuracionString() + " > , < "+ darDia(arc.darInformacion().darDia()) + ">"+ t.darVertice().darNumeroVuelo()+" \n"  );
				}
			}

		}

		return mensaje;
	}

	public void MSTAerolinea(String pCiudad, String pAerolinea)
	{
		Grafo<Ciudad, Conexion> grafito = tablaAeroGrafo.get(pAerolinea);
		grafito.componentesConectados();
		Cola<Object> cola = grafito.ciudadesConectadasMST();

		boolean cambio = false;
		int cont =0;
		while(!cambio&&!cola.isEmpty())
		{
			TablaHashLP<Integer, Vuelo> vuelosHash = new TablaHashLP<>();
			mst10 = new Grafo<>(cola.size());
			while (!cola.isEmpty()&&!cambio)
			{
				Object c = cola.dequeue();
				if(c.toString().equals("cambio"))
				{
					cambio=true;
					break;
				}
				Ciudad ciud = ciudades.get(c.toString());
				IIterator<Vuelo > it = ciud.darVuelosSalida().iterador();
				while (it.hasNext())
				{
					Vuelo v = it.next();
					if(v.darAerolinea().darNombre().equals(pAerolinea))
						vuelosHash.put(v.darNumeroVuelo(), v);
				}

			}
			if(vuelosHash.size()>=0&&vuelosHash.size()<2)
				break;

			IIterator<Integer> iter = vuelosHash.keys();
			while(iter.hasNext())
			{
				Integer llave = iter.next();
				Vuelo actual = vuelosHash.get(llave);
				String destinoActual = actual.darCiudadDestino();
				IIterator<Integer> it = vuelosHash.keys();
				while (it.hasNext()){
					Integer cLllave = it.next();
					Vuelo comparado = vuelosHash.get(cLllave);
					String origenComp = comparado.darCiudadOrigen();
					if (destinoActual.equals(origenComp)){
						boolean[] diasActual = actual.darDias();
						boolean[] diasComparado = comparado.darDias();
						boolean siguiente = false;

						for (int k=0; k<7&&diasActual[k]; k++)
						{

							for (int h=0;h<7 && diasComparado[k];h++)
							{

								if (h<=k||siguiente)
								{
									Double duracion = calcularDuracionTotal(actual, comparado, k, h, false);
									Double tarifa = calcularTarifa(k, actual);
									agregarMSTConexionAero(actual, comparado, tarifa, duracion, k);
								}
								else
								{
									Double duracion = calcularDuracionTotal(actual, comparado, k, h, true);
									Double tarifa = calcularTarifa(k, actual);
									agregarMSTConexionAero(actual, comparado, tarifa, duracion, k);
								}

							}
						}
					}

				}

			}

			int j = vuelosHash.size();
			System.out.println("Se han creado " + contadorMST + " conexiones entre vuelos MST.");
			System.out.println( "Componente " + (cont+1));
			System.out.println(p10(vuelosHash, pCiudad, pAerolinea));
			System.out.println(j);
			contadorMST = 0;
			cont++;
			cambio = false;
		}
		mst10 = new Grafo<Vuelo, Conexion>(0);

	}

	public String p10(TablaHashLP<Integer, Vuelo> vuelosEntra, String ciudad, String aerolinea )
	{
		String mensaje = "";
		if(mst10.vertexCount()!=0)
		{
			IIterator<Vuelo> ire =vuelosEntra.values();
			Vuelo vue = null;
			vue = ire.next();
			AdjacencyList<Vuelo, Conexion> adj  = new AdjacencyList<>();
			TablaHashLP<Nodo<Vuelo, Conexion>, ListaEncadenada<Arco<Vuelo, Conexion>>> ta = new TablaHashLP<>();
			IIterator<Nodo<Vuelo, Conexion>> it = mst10.darNodos();
			while (it.hasNext())
			{
				Nodo<Vuelo, Conexion> nod = it.next();
				ta.put(nod, nod.darArcos());
			}
			adj.setAdjacencyList(ta);
			Nodo<Vuelo, Conexion> nodi = new Nodo<Vuelo, Conexion>(vue);
			EdmondsChuLiu<Vuelo, Conexion> ed = new EdmondsChuLiu<>();
			AdjacencyList<Vuelo, Conexion> resp = ed.getMinBranching(nodi, adj, "costo");
			IIterator<Nodo<Vuelo,Conexion>> g = resp.iteradorNodos();
			while(g.hasNext())
			{
				Nodo<Vuelo,Conexion> t = g.next();
				ListaEncadenada<Arco<Vuelo, Conexion>> lista = t.darArcos();
				IIterator<Arco<Vuelo, Conexion>> listItr = lista.iterador();
				Arco<Vuelo, Conexion> arc = null;
				while(listItr.hasNext())
				{
					arc = listItr.next();


				}
				if(arc.darNodoInicio().darVertice().darIdentificador() == t.darVertice().darIdentificador()){
					mensaje += ("< " + t.darVertice().darCiudadOrigen() + " > , < " + t.darVertice().darCiudadDestino() + "> , <"
							+ t.darVertice().darAerolinea().darNombre() + " > , < "+ t.darVertice().darHoraSalida() + "-"+ t.darVertice().darHoraLlegada()+ " > , < \n");
					//+ t.darVertice().darDuracionString() + " > , < "+ darDia(arc.darInformacion().darDia()) + ">"+ t.darVertice().darNumeroVuelo()+" \n"  );
				}


			}

		}
		return mensaje;
	}

	public String P11 (TablaHashLP<Integer, Vuelo> vuelosEntra, String ciudad,String dia )
	{
		String mensaje = "";
		if(grafoCiudadesMST.vertexCount()!=0)
		{
			IIterator<Vuelo> ire =vuelosEntra.values();
			Vuelo vue = null;
			vue = ire.next();
			AdjacencyList<Vuelo, Conexion> adj  = new AdjacencyList<>();
			TablaHashLP<Nodo<Vuelo, Conexion>, ListaEncadenada<Arco<Vuelo, Conexion>>> ta = new TablaHashLP<>();
			IIterator<Nodo<Vuelo, Conexion>> it = grafoCiudadesMST.darNodos();
			while (it.hasNext())
			{
				Nodo<Vuelo, Conexion> nod = it.next();
				IIterator<Arco<Vuelo, Conexion>> listaas = nod.darArcos().iterador();
				ListaEncadenada<Arco<Vuelo, Conexion>> listaFin = new ListaEncadenada<>();
				while(listaas.hasNext())
				{
					Arco<Vuelo, Conexion> arc = listaas.next();
					String diaS = darDia(arc.darInformacion().darDia());
					String diaE = dia;
					if(diaS.equalsIgnoreCase(diaE))
						listaFin.add(arc);
				}
				ta.put(nod, listaFin);
			}
			adj.setAdjacencyList(ta);
			Nodo<Vuelo, Conexion> nodi = new Nodo<Vuelo, Conexion>(vue);
			EdmondsChuLiu<Vuelo, Conexion> ed = new EdmondsChuLiu<>();
			AdjacencyList<Vuelo, Conexion> resp = ed.getMinBranching(nodi, adj, "costo");
			IIterator<Nodo<Vuelo,Conexion>> g = resp.iteradorNodos();
			while(g.hasNext())
			{
				Nodo<Vuelo,Conexion> t = g.next();
				ListaEncadenada<Arco<Vuelo, Conexion>> lista = t.darArcos();
				IIterator<Arco<Vuelo, Conexion>> listItr = lista.iterador();
				Arco<Vuelo, Conexion> arc = null;
				while(listItr.hasNext())
				{
					arc = listItr.next();


				}
				if(arc.darNodoInicio().darVertice().darIdentificador() == t.darVertice().darIdentificador()){
					mensaje += ("< " + t.darVertice().darCiudadOrigen() + " > , < " + t.darVertice().darCiudadDestino() + "> , <"
							+ t.darVertice().darAerolinea().darNombre() + " > , < "+ t.darVertice().darHoraSalida() + "-"+ t.darVertice().darHoraLlegada()+ " > , < \n");
					//+ t.darVertice().darDuracion() + " > , < "+ darDia(arc.darInformacion().darDia()) + ">"+ t.darVertice().darNumeroVuelo()+" \n"  );
				}
			}

		}
		return mensaje;
	}

	private String darDia(int pDia){
		String resp = null;
		switch (pDia){
		case 0:
			resp = "Lunes";
			break;
		case 1:
			resp = "Martes";
			break;
		case 2:
			resp = "Miercoles";
			break;
		case 3:
			resp = "Jueves";
			break;
		case 4:
			resp = "Viernes";
			break;
		case 5:
			resp = "Sabado";
			break;
		case 6:
			resp = "Domingo";
			break;
		}
		return resp;
	}

	public void MSTPorDia(String pCiudad, String dia)
	{
		Cola<Object> colaCiudades = grafoCiudades.ciudadesConectadasMST();
		TablaHashLP<Integer, Vuelo> vuelosHash = new TablaHashLP<>();

		boolean cambio = false;
		int cont =0;
		while(!cambio&&!colaCiudades.isEmpty()){
			vuelosHash = new TablaHashLP<Integer, Vuelo>();
			grafoCiudadesMST = new Grafo<>(colaCiudades.size());
			while(!colaCiudades.isEmpty()&&!cambio){

				Object c = colaCiudades.dequeue();
				if(c.toString().equals("cambio"))
				{
					cambio=true;
					break;
				}
				Ciudad ciu = ciudades.get(c.toString());
				IIterator<Vuelo> it = ciu.darVuelosSalida().iterador();
				while(it.hasNext())
				{
					Vuelo v = it.next();
					vuelosHash.put(v.darNumeroVuelo(),v );
				}
			}
			if(vuelosHash.size()>=0&&vuelosHash.size()<2)
				break;


			IIterator<Integer> iter = vuelosHash.keys();
			while(iter.hasNext())
			{
				Integer llave = iter.next();
				Vuelo actual = vuelosHash.get(llave);
				String destinoActual = actual.darCiudadDestino();
				IIterator<Integer> it = vuelosHash.keys();
				while (it.hasNext())
				{
					Integer cLllave = it.next();
					Vuelo comparado = vuelosHash.get(cLllave);
					String origenComp = comparado.darCiudadOrigen();
					if (destinoActual.equals(origenComp))
					{
						boolean[] diasActual = actual.darDias();
						boolean[] diasComparado = comparado.darDias();
						boolean siguiente = false;

						for (int k=0; k<7&&diasActual[k]; k++)
						{

							for (int h=0;h<7 && diasComparado[k];h++)
							{

								if (h<=k||siguiente){
									Double duracion = calcularDuracionTotal(actual, comparado, k, h, false);
									Double tarifa = calcularTarifa(k, actual);
									agregarMSTConexion(actual, comparado, tarifa, duracion, k);

								}
								else
								{
									Double duracion = calcularDuracionTotal(actual, comparado, k, h, true);
									Double tarifa = calcularTarifa(k, actual);
									agregarMSTConexion(actual, comparado, tarifa, duracion, k);

								}

							}
						}
					}

				}

			}
			int hd = vuelosHash.size();
			IIterator<Integer> kit = vuelosHash.keys();
			int dsf = kit.next();
			System.out.println(hd);
			System.out.println(dsf);
			System.out.println("Se han creado " + contadorMST + " conexiones entre vuelos MST.");
			System.out.println( "Componente " + (cont+1));
			System.out.println(P11(vuelosHash, pCiudad, dia));
			contadorMST = 0;
			cont++;
			cambio = false;
		}
	}

	public void imprimirItinerarioCiudadesCosto(String pCiudad, String pAerolinea)
	{
		ListaEncadenada<Itinerario> i = recorrerCiudadesPorCosto(pAerolinea, pCiudad);
		IIterator<Itinerario> j = i.iterador();
		while(j.hasNext())
		{
			Itinerario k = j.next();
			StringBuilder sb = k.darItinerario();
			System.out.println(sb);
		}
	}

	public ListaEncadenada<Itinerario> recorrerCiudadesPorCosto(String pAero, String pCiudad)
	{
		ListaEncadenada<Itinerario> resp = new ListaEncadenada<Itinerario>();
		Aerolinea aero = aerolineas.get(pAero);
		TablaHashLP<String, String> sinUsar = new TablaHashLP<String, String>();
		IIterator<String> momento = aero.darCiudades().iterador();
		while(momento.hasNext())
		{
			String ayuda = momento.next();
			sinUsar.put(ayuda, ayuda);
		}
		String act1 = pCiudad;
		sinUsar.delete(pCiudad);

		while(!sinUsar.isEmpty())
		{
			Itinerario resp2 = null;
			IIterator<String> it = aero.darCiudades().iterador();
			String act2 = it.next();
			if(!act1.equals(act2))
			{
				resp2 = itinerarioSinDiaEspecifico(act1, act2, Conexion.PESO_COSTO);
			}
			while(it.hasNext())
			{
				act2 = it.next();
				if(sinUsar.contains(act2))
				{
					if(!act1.equals(act2))
					{
						Itinerario aux = itinerarioSinDiaEspecifico(act1, act2, Conexion.PESO_COSTO);
						if(resp2.darPesoTotal() < aux.darPesoTotal())
						{
							act1 = act2;
							resp2 = aux;
							sinUsar.delete(act1);
						}
					}
				}
			}
			resp.add(resp2);
		}
		return resp;
	}

	/**
	 * @param ciudadOrigen
	 * @param ciudadDestino
	 * @param diaPartida
	 */

	private Itinerario itinerarioSinDiaEspecifico(String ciudadOrigen, String ciudadDestino, String criterio){
		Ciudad partida = ciudades.get(ciudadOrigen);
		Ciudad llegada = ciudades.get(ciudadDestino);
		IIterator<Vuelo> itSalida = partida.darVuelosSalida().iterador();

		Vuelo salidaMin = null;
		Vuelo llegadaMin = null;
		Double pesoMin = 10000.0;

		while(itSalida.hasNext()){
			Vuelo actual = itSalida.next();

			System.out.println("Antes dijkstra");
			Grafo.Dijkstra<Vuelo, Conexion> dijkstra = new Grafo.Dijkstra<Vuelo, Conexion>(grafoVuelos, actual, criterio);
			System.out.println("Despues dijkstra");
			IIterator<Vuelo> itLlegada = llegada.darVuelosEntrada().iterador();
			while (itLlegada.hasNext()){
				Vuelo actualLlegada = itLlegada.next();
				if (dijkstra.hasPathTo(actualLlegada)){
					Double pesoActual = dijkstra.distTo(actualLlegada);
					if (pesoActual<pesoMin){
						salidaMin = actual;
						llegadaMin = actualLlegada;
						pesoMin = pesoActual;
					}
				}
			}
		}
		Grafo.Dijkstra<Vuelo, Conexion> d = new Grafo.Dijkstra<Vuelo, Conexion>(grafoVuelos, salidaMin, criterio);
		IIterator<Arco<Vuelo, Conexion>> iteradorCaminoMasCorto = d.pathTo(llegadaMin);
		Itinerario itinerario = new Itinerario(iteradorCaminoMasCorto, criterio);
		return itinerario;
	}

	public void darRutaMasLargaAerolinea( String pAero)
	{
		System.out.println("may may may maya" + tablaAeroGrafo.size());
		Grafo grafito = tablaAeroGrafo.get(pAero);
		Kosaraju ko = grafito.hacerKosaraju();

		int cant = ko.darNumeroComponentes();
		Cola<Integer>[] componentes = (Cola<Integer>[]) new Cola[cant];
		for(int i = 0; i<cant ; i++)
		{
			componentes[i] = new Cola<Integer>();
		}
		for(int j = 0; j< grafito.vertexCount(); j++)
		{
			componentes[ko.idOf(j)].enqueue(j);
		}

		int maximo = 0;
		Cola<Integer> resultado = new Cola<Integer>();

		for(int z = 0; z<cant; z++)
		{
			if(componentes[z].size()>maximo)
			{
				resultado = componentes[z];
				maximo = componentes[z].size();
			}
		}

		Object[] ciudades = ko.darArregloLlaves();

		int aux = resultado.dequeue();
		Ciudad primero = (Ciudad) ciudades[aux];

		System.err.println(resultado.size());

		System.out.println("La ciudad con la que debe comenzar el viajero es: " + primero.darNombre());
		System.out.print("El recorrido debe ser:");
		System.out.print(primero.darNombre());
		while(!resultado.isEmpty())
		{	
			int aux2 = resultado.dequeue();
			Ciudad actual = (Ciudad) ciudades[aux2];

			System.out.print(" --- " + actual.darNombre());
		}
		System.out.println();
	}

	public void darRutaMasLarga()
	{

		Kosaraju ko = grafoCiudades.hacerKosaraju();

		int cant = ko.darNumeroComponentes();
		Cola<Integer>[] componentes = (Cola<Integer>[]) new Cola[cant];
		for(int i = 0; i<cant ; i++)
		{
			componentes[i] = new Cola<Integer>();
		}
		for(int j = 0; j< grafoCiudades.vertexCount(); j++)
		{
			componentes[ko.idOf(j)].enqueue(j);
		}

		int maximo = 0;
		Cola<Integer> resultado = new Cola<Integer>();

		for(int z = 0; z<cant; z++)
		{
			if(componentes[z].size()>maximo)
			{
				resultado = componentes[z];
				maximo = componentes[z].size();
			}
		}

		Object[] ciudades = ko.darArregloLlaves();

		int aux = resultado.dequeue();
		Ciudad primero = (Ciudad) ciudades[aux];

		System.err.println(resultado.size());

		System.out.println("La ciudad con la que debe comenzar el viajero es: " + primero.darNombre());
		System.out.print("El recorrido debe ser:");
		System.out.print(primero.darNombre());
		while(!resultado.isEmpty())
		{	
			int aux2 = resultado.dequeue();
			Ciudad actual = (Ciudad) ciudades[aux2];

			System.out.print(" --- " + actual.darNombre());
		}
		System.out.println();
	}

	public void requerimiento17(String pAerolinea)
	{
		int dia = -1;
		Double hora=-0.0;
		Vuelo comienzo = null;
		Double pesoMin = 10000.0;
		Aerolinea a = aerolineas.get(pAerolinea);
		IIterator<Vuelo> iterador = a.darVuelos().values();
		while (iterador.hasNext()){
			Vuelo v = iterador.next();
			Dijkstra<Vuelo, Conexion> dij = new Dijkstra<Vuelo, Conexion>(a.darGrafo(), v, Conexion.PESO_COSTO);
			IIterator<Vuelo> it = a.darVuelos().values();
			while (it.hasNext()){
				Vuelo llegada = it.next();
				if (v!=llegada&&dij.hasPathTo(llegada)){
					if (dij.distTo(llegada)<pesoMin&&dij.distTo(llegada)>0.0){
						pesoMin = dij.distTo(llegada);
						comienzo = llegada;
					}
				}
			}
		}
		for (int i = 0; i<7; i++){
			boolean[] dias = comienzo.darDias();
			if (dias[i]){
				dia = i;
			}
		}
		hora = comienzo.darHoraSalida();
		if (comienzo!=null){
			System.out.println("El viajero debe salir de " + comienzo.darCiudadOrigen() + " El " + darDia(dia) + " a las " + hora + " ");
		}
		else{
			System.out.println("No hay camino.");
		}
	}


}
