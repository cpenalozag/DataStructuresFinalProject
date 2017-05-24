package cliente;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;

import mundo.AeroCivil;
import mundo.Conexion;

/**
 *
 * @author Samuel S. Salazar, Gustavo Alegria, Fernando De la Rosa
 */
public class Proyecto3Cliente {

	private static Scanner in = new Scanner(System.in);

	// Definir el atributo(s) del modelo del mundo (incluye el grafo(s) dirigido(s) con los datos de ciudades y vuelos)
	private AeroCivil api;

	/** ***************************************************************************************************
	 * 								OPCIONES DEL MENU 
	 * ***************************************************************************************************
	 * 	- N: Numero total de opciones
	 *  - Las opciones se organizan en un arreglo de entradas que van de 0..N-1
	 *  - La opcion i (0..N-1) corresponde al requerimiento i+1 del menu.
	 * A continuacion se describe cada dato de entrada para cada requerimiento i (1..N-1):
	 * opciones[i][0] = Nombre de la opcion/requerimiento 
	 * 	 	+se define j como un entero que va de 1...p donde p es el numero de parametros de la opcion.
	 * 		+ Estos parametros (de tipo String) seran pasados en forma de arreglo al metodo que resuelve el requerimiento
	 * 		+ El metodo que resuelve el requerimiento debe llamarse req<i>(String[] params)
	 * opciones[i][j] = entrada/parametro j de la opcion (con j > 0)
	 * 
	 */
	private static  String[][] opciones ={
			// Req 1. Caso 1. En caso que se quiera leer al principio los vuelos del catalogo (definidos en archivo) usar la siguiente definicion:
			//		{"req1","Cargar un catalogo de vuelos desde archivo de datos","ruta archivo"}, //R1
			// Req 1. Caso 2. En caso que se quiera iniciar el catalogo de vuelos vacio usar la siguiente definicion:
			{"req1","Cargar un catalogo de vuelos desde archivo de datos"}, //R1
			{"req2","Agregar una aerolinea al catalogo de vuelos", "Nombre aerolinea", "Tarifa por minuto","Numero sillas maximo"}, //R2
			{"req3","Eliminar una aerolinea del catalogo de vuelos", "Nombre aerolinea"},// R3
			{"req4","Agregar y eliminar ciudades autorizadas para realizar vuelos autorizados","Agregar o Eliminar?","Nombre Ciudad"}, //R4 
			{"req5","Agregar un vuelo al catalogo de vuelos", "# de vuelo", "Aerolinea", "Ciudad origen", "Ciudad destino","Hora de salida", "Hora de llegada", "Tipo de avion", "Cupo del vuelo", "Dias de operacion"}, //R5
			{"req6", "Calcular y actualizar las tarifas de los vuelos"},
			{"req7", "Informar los conjuntos de ciudades que se pueden comunicar entre si pero que no tienen comunicacion con el resto del pais sin importar las aerolinea"},
			{"req8", "Informar los conjuntos de ciudades que se pueden comunicar entre si pero que no tienen comunicacion con el resto del pais para cada aerolinea"},
			{"req9", "Calcular e imprimir el MST para vuelos nacionales, a partir de una ciudad especifica, utilizando el tiempo del vuelo como peso de los arcos", "Ciudad origen"},
			{"req10", "Calcular e imprimir el MST para vuelos nacionales de una aerolinea particular, a partir de una ciudad especifica, utilizando el costo de los vuelos como peso de los arcos", "Nombre Aerolinea", "Ciudad origen"},
			{"req11", "Calcular e imprimir el MST  a partir de una ciudad especifica y de un dia particular, sin importar cambios de aerolinea en el viaje", "Ciudad origen", "Dia partida"},
			{"req12", "Calcular e imprimir el itinerario de costo minimo para cada aerolinea","Ciudad origen","Ciudad destino","Dia partida"},
			{"req13", "Calcular e imprimir el itinerario de costo minimo para diferentes aerolineas","Ciudad origen","Ciudad destino","Dia partida"},
			{"req14", "Calcular e imprimir la ruta de costo minimo para ir a todas las otras ciudades cubiertas por una aerolinea","Ciudad","Nombre aerolinea"},
			{"req15", "Calcular e imprimir la ruta de menor tiempo para ir a todas las otras ciudades cubiertas por una aerolinea","Ciudad","Nombre aerolinea"},
			{"req16", "Calcular e imprimir la ruta optima (por precio) para visitar todas las otras ciudades cubiertas por una aerolínea.","Ciudad","Nombre aerolinea","Dia"},
			{"req17", "Calcular la ciudad, el día y la hora en la que un viajero debería comenzar su viaje, de tal forma que logre visitar \n"
					+ "el mayor número de ciudades posibles con el menor costo posible, en vuelos servidos por una misma aerolínea.,","Nombre aerolinea"},
			{"req18", " Dado un día y una hora específicos, calcule e imprima la ruta mas larga (mayor número de ciudades) que puede hacer \n"
					+ "un viajero en un aerolínea de su predilección. Indique claramente cuando haya cambio de día en el itinerario.","Dia","Hora","Aerolinea"},
			{"req19", "Dado un día y una hora específicos, calcule e imprima la ruta mas larga (mayor número de ciudades) que puede hacer un \n"
					+ " viajero, así deba conectar con vuelos de diferentes aerolíneas.","Día","NHora"},
			{"req20", "Calcular e imprimir la ruta de menor tiempo para ir a todas las otras ciudades cubiertas por una aerolinea","Ciudad","Nombre aerolinea"},

			//TODO Agregar los requerimientos opcionales en caso de querer realizar el bono
			//Nota: los metodos req16, ..., req20 ya aparecen definidos y falta completar segun la documentacion 

			{"exit", "Salir"}
	};

	/**
	 * Constructor del cliente que prueba los requerimientos del proyecto 3
	 */
	public Proyecto3Cliente()
	{
		// TODO
		api = new AeroCivil();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception
	{
		Proyecto3Cliente cli = new Proyecto3Cliente();

		// Ciclo de interaccion x consola: despliegue opciones, seleccion de opcion, lectura de datos necesarios, ejecucion del metodo respectivo, tiempo de ejecucion
		while(true)
		{
			String[] opc= menu();
			if(opc != null)
			{
				try 
				{
					long start = System.currentTimeMillis();
					Method metodo = cli.getClass().getMethod(opc[0], String[].class);
					metodo.invoke(cli,new Object[]{Arrays.copyOfRange(opc, 2, opc.length)});
					System.out.println(String.format(">>Tiempo de ejecucion: %d ms ", System.currentTimeMillis()-start));
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					System.out.println(">>Ocurrio un error\n>>Detalles: "+e.getMessage());

				}
			}
		}
	}

	/**
	 * Despliega las opciones del API, lee una opcion y sus datos de entrada (tipo String)
	 * @return String compuesto con el nombre del metodo que resuelve el requerimiento y sus datos de entreda
	 */
	private static String[] menu()
	{
		System.out.println("---------------------------------------------------------------------------------------------");
		System.out.println("------------------------------ API AERONAUTICA CIVIL ----------------------------------------");
		System.out.println("---------------------------------------------------------------------------------------------");

		for (int i = 0; i < opciones.length; i++) {
			System.out.println(String.format("[%d] %s", i+1, opciones[i][1]));

		}
		int opc = in.nextInt();
		// Revision de opcion invalida
		if(opc < 1 || opc> opciones.length)
		{
			System.out.println(">> Opcion invalida");
			return null;
		}

		// definir la informacion del metodo que resuelve el requerimiento con sus datos de entrada (parametros)
		String[] rta = new String[opciones[opc-1].length];
		rta[0] = opciones[opc-1][0];    // identificador metodo del requerimiento 
		rta[1] = Integer.toString(opc); // numero de la opcion seleccionada
		in.nextLine();
		// lectura de los parametros necesarios para el requerimiento
		for (int i = 2; i < opciones[opc-1].length; i++) {
			System.out.println(">>"+opciones[opc-1][i]);
			rta[i] = in.nextLine();
		}
		return rta;
	}

	/**
	 * Metodo de terminacion del cliente
	 * @param params (ninguno)
	 */
	public void exit(String[] params)
	{
		System.out.println("exit: parametros"+Arrays.toString(params));
		System.out.println(">> Adios");
		System.exit(0);
	}

	/**
	 * Metodo encargar de crear (caso 1) o cargar (caso 2) un catalogo de vuelos
	 * @param params 
	 */
	public  void req1(String[] params)
	{
		//TODO
		System.out.println("Por favor espere mientras se cargan los datos...");
		api.cargarArchivo();
	}

	/**
	 * Agrega una aerolinea al catalogo
	 * @param params
	 * params[0] = Nombre de la aerolinea
	 */
	public void req2(String[] params)
	{
		System.out.println("Req2: parametros"+Arrays.toString(params));
		String nombreAerolinea = params[0];
		Double tarifaMin = Double.parseDouble(params[1]);
		int numSillasMax = Integer.parseInt(params[2]);
		api.agregarAerolinea(nombreAerolinea, tarifaMin, numSillasMax);
		//TODO
		// Completar segun documentacion del requerimiento

		int nAerolineas =  api.darCantidadDeAerolineas();
		System.out.println("Numero de aerolineas: " + nAerolineas);

	}
	/**
	 * Elimina una aerolinea del catalogo
	 * @param params
	 * params[0] = Nombre de la aerolinea
	 */
	public void req3(String[] params)
	{
		System.out.println("Req3: parametros"+Arrays.toString(params));
		String id = params[0];
		api.eliminarAerolinea(id);
		//TODO
		int nAerolineas = api.darCantidadDeAerolineas();
		System.out.println("Numero de aerolineas: " + nAerolineas);
	}

	/**api
	 * Agregar y eliminar ciudades autorizadas para realizar vuelos autorizados
	 * @param params
	 * params[0] = Agregar o eliminar una ciudad
	 * params[1]= Nombre ciudad autorizada
	 */
	public void req4(String[] params)
	{
		System.out.println("Req4: parametros"+Arrays.toString(params));
		String opcion = params[0];
		String nombreCiudad = params[1];
		int nCiudades = api.darCantidadCiudades();
		System.out.println("Numero de ciudades: " + nCiudades);
		//TODO
		if (opcion.equalsIgnoreCase("Agregar")){
			api.agregarCiudad(nombreCiudad);
		}
		else if (opcion.equalsIgnoreCase("Eliminar")){
			api.eliminarCiudad(nombreCiudad);
		}
		nCiudades = api.darCantidadCiudades();
		System.out.println("Numero de ciudades: " + nCiudades);

	}

	/**
	 * Agregar un vuelo al catalogo de vuelos
	 * @param params
	 * params[0] = # de vuelo
	 * params[1]= Aerolinea
	 * params[2]= Ciudad origen
	 * params[3]= Ciudad destino
	 * params[4] = Hora de salida
	 * params[5] = Hora de llegada
	 * params[6] = Tipo de avion
	 * params[7] = Cupo del vuelo
	 * params[8] = Dias de operacion
	 */
	public void req5(String[] params)
	{
		System.out.println("Req5: parametros"+Arrays.toString(params));
		String numVuelo = params[0];
		String aerolinea = params[1];
		String origen = params[2];
		String destino = params[3];
		String horaSalida = params[4];
		String horaLlegada = params[5];
		String tipoAvion = params[6];
		String cupoVuelo = params[7];
		String diasOperacion = params[8];

		String[] dias = diasOperacion.split(",");
		boolean[] diasB = new boolean [7];
		int i=0;
			if (dias[i].equalsIgnoreCase("Lunes")){
				diasB[0]=true;
			}
			if (dias[i].equalsIgnoreCase("Martes")){
				diasB[1]=true;
			}
			if (dias[i].equalsIgnoreCase("Miercoles")){
				diasB[2]=true;
			}
			if (dias[i].equalsIgnoreCase("Jueves")){
				diasB[3]=true;
			}
			if (dias[i].equalsIgnoreCase("Viernes")){
				diasB[4]=true;
			}
			if (dias[i].equalsIgnoreCase("Sabado")){
				diasB[5]=true;
			}
			if (dias[i].equalsIgnoreCase("Lunes")){
				diasB[6]=true;
			}
		//TODO
		// Completar segun documentacion del requerimiento
		api.agregarVuelo(aerolinea, Integer.parseInt(cupoVuelo), numVuelo, origen, destino, horaSalida, horaLlegada, tipoAvion, diasB);

		int nVuelos = api.darNumeroVuelos();
		System.out.println("Numero de vuelos: " + nVuelos);

	}

	/**
	 * Calcular y actualizar las tarifas de los vuelos
	 * @param params
	 */
	public void req6(String[] params){
		System.out.println("Req6: parametros"+Arrays.toString(params));
		System.out.println("Las tarifas de los vuelos se actualizan automaticamente con la formula dada.");
		// TODO 
		// La informacion del requerimiento 6 corresponde a calcular la formula para
		// calcular la tarifa/costo de un tiquete de acuerdo al dia, a la aerolinea, al numero de sillas y al tiempo de vuelo (en minutos)
		// Este calculo se debe hacer para cada vuelo que se agregue al modelo del mundo
	}

	/**
	 * Informar los conjuntos de ciudades que se pueden comunicar entre si  
	 * pero que no tienen comunicacion con el resto del pais sin importar las aerolinea
	 * @param params
	 */
	public void req7(String[] params){
		System.out.println("Req7: parametros"+Arrays.toString(params));
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar cada conjunto de la forma {ciudad1, ciudad2, ..., ciudadN}
		api.imprimirCiudadesConectados();
	}

	/**
	 * Informar los conjuntos de ciudades que se pueden comunicar entre si  
	 * pero que no tienen comunicacion con el resto del pais para cada aerolinea
	 * @param params
	 */
	public void req8(String[] params){
		System.out.println("Req8: parametros"+Arrays.toString(params));
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar cada conjunto de la forma <Aerolinea>: {ciudad1, ciudad2, ..., ciudadN}
		api.imprimirCiudadesConectadosPorAerolinea();
	}

	/**
	 * Calcular e imprimir el MST para vuelos nacionales, a partir de una ciudad especifica, 
	 * utilizando como peso de los arcos el tiempo del vuelo
	 * @param params
	 * params[0]= Ciudad origen
	 */
	public void req9(String[] params){
		System.out.println("Req9: parametros"+Arrays.toString(params));
		String origen = params[0];
		
		try {
			api.pruebaEdmonds(origen, null, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar para ciudad en el MST al menos <Ciudad Origen>, <Ciudad Destino>, <Aerolinea>, <Numero Vuelo>, <Hora Vuelo>, <Tiempo Vuelo>, <Dia Vuelo>  
	}

	/**
	 * Calcular e imprimir el MST para vuelos nacionales de una aerolinea particular, a partir de una ciudad especifica, 
	 * utilizando como peso de los arcos el costo de los vuelos
	 * @param params
	 * params[0]= Nombre aerolinea
	 * params[1]= Ciudad origen
	 */
	public void req10(String[] params){
		System.out.println("Req10: parametros"+Arrays.toString(params));
		String aerolinea = params[0];
		String origen = params[1];
		
		api.pruebaEdmondsAerolinea(origen, aerolinea);
		
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar para ciudad en el MST al menos <Ciudad Origen>, <Ciudad Destino>, <Numero Vuelo>, <Hora Vuelo>, <Tarifa Vuelo>, <Dia Vuelo>  
	}

	/**
	 * Calcular e imprimir el MST  a partir de una ciudad especifica y de un dia particular, 
	 * sin importar cambios de aerolinea en el viaje
	 * @param params
	 * params[0]= Ciudad origen
	 * params[1]= Dia de partida
	 */
	public void req11(String[] params){
		System.out.println("Req11: parametros"+Arrays.toString(params));
		String origen = params[0];
		String dia = params[1];
		
		api.MST11(origen, dia);
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar para ciudad en el MST al menos <Ciudad Origen>, <Ciudad Destino>, <Aerolinea>, <Numero Vuelo>, <Hora Vuelo>, <Tiempo Vuelo>, <Dia Vuelo>  
	}

	/**
	 * Calcular e imprimir el itinerario de costo minimo para cada aerolinea
	 * @param params
	 * params[0]= Ciudad origen
	 * params[1]= Ciudad destino
	 * params[2]= Dia de partida
	 */
	public void req12(String[] params){
		System.out.println("Req12: parametros"+Arrays.toString(params));
		String origen = params[0];
		System.out.println(origen);
		String destino = params[1];
		System.out.println(destino);
		String dia = params[2];
		int idia = cambiarDia(dia);
		if(idia==-1){
			System.out.println("No es un día válido");
		}
		else{
			api.implimirItinerarioAerolineas(origen, destino, idia);
		}

		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar cada itinerario como <Aerolinea> {<Ciudad Origen>-<Vuelo>-<Dia>-<Hora>-<Ciudad Intermedia1>-<Costo>, ..., 
		//                                           <Ciudad Intermedia>-<Vuelo>-<Dia>-<Hora>-<Ciudad Destino>-<Costo>}, <Costo-Total-Minimo>
	}

	/**
	 * Calcular e imprimir el itinerario de costo minimo para diferentes aerolineas
	 * @param params
	 * params[0]= Ciudad origen
	 * params[1]= Ciudad destino
	 * params[2]= Dia de partida
	 */
	public void req13(String[] params){
		System.out.println("Req13: parametros"+Arrays.toString(params));
		String origen = params[0];
		String destino = params[1];
		String dia = params[2];
		int idia = cambiarDia(dia);
		if(idia==-1){
			System.out.println("No es un día válido");
		}
		else{
			api.imprimirItinerario(origen, destino, idia);
		}
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar el itinerario como {<Ciudad Origen>-<Aerolinea>-<Vuelo>-<Dia>-<Hora>-<Ciudad Intermedia1>-<Costo>, ..., 
		//                             <Ciudad Intermedia>-<Aerolinea>-<Vuelo>-<Dia>-<Hora>-<Ciudad Destino>-<Costo>}, <Costo-Total-Minimo>
	}


	/**
	 * Calcular e imprimir la ruta de costo minimo para ir a todas las otras ciudades cubiertas por una aerolinea
	 * @param params
	 * params[0]= Nombre ciudad
	 * params[1]= Nombre aerolinea
	 * 
	 */
	public void req14(String[] params){
		System.out.println("Req14: parametros"+Arrays.toString(params));
		String ciudad = params[0];
		String aerolinea = params[1];
		
		api.rutaMasLarga(ciudad, aerolinea, Conexion.PESO_COSTO);
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar la ruta como {<Ciudad Origen>-<Vuelo>-<Dia>-<Hora>-<Ciudad A>-<Costo-Origen-A>, ..., 
		//                       <Ciudad ?>-<Vuelo>-<Dia>-<Hora>-<Ciudad X>-<Costo-Origen-X>}
	}

	/**
	 * Calcular e imprimir la ruta de menor tiempo para ir a todas las otras ciudades cubiertas por una aerolinea
	 * @param params
	 * params[0]= Nombre ciudad
	 * params[1]= Nombre aerolinea
	 */
	public void req15(String[] params){
		String ciudad = params[0];
		String aerolinea = params[1];
		
		api.rutaMasLarga(ciudad, aerolinea, Conexion.PESO_DURACION);
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar la ruta como {<Ciudad Origen>-<Vuelo>-<Dia>-<Hora>-<Ciudad A>-<Tiempo-Origen-A>, ..., 
		//                       <Ciudad ?>-<Vuelo>-<Dia>-<Hora>-<Ciudad X>-<Tiempo-Origen-X>}

	}

	/**
	 * Calcular e imprimir la ruta de minimo precio para visitar todas las otras ciudades cubiertas por una aerolinea
	 * @param params
	 * params[0]= Nombre ciudad
	 * params[1]= Dia de la semana
	 * params[2]= Nombre aerolinea
	 */
	public void req16(String[] params){
		System.out.println("Req16: parametros"+Arrays.toString(params));
		String ciudad = params[0];
		String dia = params[1];
		String aerolinea = params[2];
		int idia = cambiarDia(dia);
		api.imprimirMejorTodasCiudades(ciudad,idia,aerolinea);
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar la ruta como {<Ciudad Origen>-<Vuelo>-<Dia>-<Hora>-<Ciudad A>-<Tiempo-Origen-A>, ..., 
		//                       <Ciudad ?>-<Vuelo>-<Dia>-<Hora>-<Ciudad X>-<Tiempo-Origen-X>}

	}

	/**
	 * Buscar la aerolinea, ciudad, dia de la semana y hora para iniciar un viaje que permita a un viajero visitar la mayor cantidad de ciudades a minimo costo, en vuelos de una misma aerolinea.
	 * @param params
	 */
	public void req17(String[] params){
		System.out.println("Req17: parametros"+Arrays.toString(params));
		String aerolinea = params[0];
		api.req17(aerolinea);
		
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar <Aerolinea>, <Ciudad Origen>, <Dia de semana> y <Hora Salida> inicial del viaje
		// Mostrar la ruta como {<Ciudad Origen>-<Vuelo>-<Dia>-<Hora>-<Ciudad A>-<Costo-Origen-A>, 
		//                       <Ciudad A>-<Vuelo>-<Dia>-<Hora>-<Ciudad B>-<Costo-A-B>, ..., 
		//                       <Ciudad X>-<Vuelo>-<Dia>-<Hora>-<Ciudad Ultima>-<Costo-X-Ultima>}
		// Mostrar <Costo total viaje>

	}

	/**
	 * Buscar el dia de la semana y hora para iniciar un viaje que permita a un viajero visitar la mayor cantidad de ciudades, en vuelos de una aerolinea preferida.
	 * @param params
	 * params[0]= Nombre aerolinea
	 */
	public void req18(String[] params){
		System.out.println("Req18: parametros"+Arrays.toString(params));
		int dia = cambiarDia(params[0]);
		String[] h = params[1].split(":");
		Double hora = Double.parseDouble(h[0]);
		hora+=(Double.parseDouble(h[1])/60.0);
		String aerolinea = params[2];
		
		api.rutaMasLargaAerolinea(hora,dia,aerolinea);
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar <Ciudad Origen>, <Dia de semana> y <Hora Salida> inicial del viaje
		// Mostrar la ruta como {<Ciudad Origen>-<Vuelo>-<Dia>-<Hora>-<Ciudad A>, 
		//                       <Ciudad A>-<Vuelo>-<Dia>-<Hora>-<Ciudad B>, ..., 
		//                       <Ciudad X>-<Vuelo>-<Dia>-<Hora>-<Ciudad Ultima>}
		// Mostrar <Numero ciudades visitas>

	}

	/**
	 * Buscar el dia de la semana y hora para iniciar un viaje que permita a un viajero visitar la mayor cantidad de ciudades, con posibilidad de cambios de aerolinea.
	 * @param params
	 */
	public void req19(String[] params){
		int dia = cambiarDia(params[0]);
		String[] h = params[1].split(":");
		Double hora = Double.parseDouble(h[0]);
		hora+=(Double.parseDouble(h[1])/60.0);
		
		api.rutaMasLarga(hora,dia);
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar <Ciudad Origen>, <Dia de semana> y <Hora Salida> inicial del viaje
		// Mostrar la ruta como {<Ciudad Origen>-<Vuelo>-<Dia>-<Hora>-<Aerolinea>-<Ciudad A>, 
		//                       <Ciudad A>-<Vuelo>-<Dia>-<Hora>-<Aerolinea>-<Ciudad B>, ..., 
		//                       <Ciudad X>-<Vuelo>-<Dia>-<Hora>-<Aerolinea>-<Ciudad Ultima>}
		// Mostrar <Numero ciudades visitas>, <Costo total viaje>, <Tiempo total en vuelos>

	}

	/**
	 * Buscar la ruta para visitar un conjunto de ciudades dadas bajo las restricciones definidas.
	 * Nota: No se requiere visitar las ciudades en el orden de ingreso en los parametros
	 * @param params
		String ciudad = params[0];
		String dia = params[1];
		String hora = params[2];
		String ciudad intermedia 1 = params[3];
		...
		String ciudad intermedia N = params[3+N-1];		
	 */
	public void req20(String[] params){
		System.out.println("Req20: parametros"+Arrays.toString(params));
		String ciudad_Origen = params[0];
		String dia = params[1];
		String hora = params[2];
		// A continuacion deben venir los nombres de las ciudades intermedias a visitar (N)
		String ciudad_I1 = params[3]; // Nombre de ciudad intermedia a visitar
		// ...
		// String ciudad_IN = params[3+N-1];  // Nombre de ciudad intermedia a visitar 
		//TODO
		// Completar segun documentacion del requerimiento
		// Mostrar <Ciudad Origen>, <Dia de semana> y <Hora Salida> inicial del viaje
		// Mostrar la ruta como {<Ciudad Origen>-<Vuelo>-<Dia>-<Hora>-<Aerolinea>-<Ciudad Intermedia X>, 
		//                       <Ciudad Intermedia X>-<Vuelo>-<Dia>-<Hora>-<Aerolinea>-<Ciudad Intermedia �?>, ..., 
		//                       <Ciudad Intermedia �?>-<Vuelo>-<Dia>-<Hora>-<Aerolinea>-<Ciudad intermedia Ultima>,
		//                       <Ciudad Intermedia Ultima>-<Vuelo>-<Dia>-<Hora>-<Aerolinea>-<Ciudad Origen>}

	}

	private int cambiarDia(String dia){
		int idia = -1;
		if (dia.equalsIgnoreCase("lunes")){
			idia=0;
		}
		if (dia.equalsIgnoreCase("martes")){
			idia=1;
		}
		if (dia.equalsIgnoreCase("miercoles")){
			idia=2;
		}
		if (dia.equalsIgnoreCase("jueves")){
			idia=3;
		}
		if (dia.equalsIgnoreCase("viernes")){
			idia=4;
		}
		if (dia.equalsIgnoreCase("sabado")){
			idia=5;
		}
		if (dia.equalsIgnoreCase("domingo")){
			idia=6;
		}
		if (idia==-1){
			System.err.println("Dia negativo");
		}
		return idia;
	}

}
