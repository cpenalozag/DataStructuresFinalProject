package mundo;

import java.math.BigDecimal;
import java.math.MathContext;
import estructuras.Arco;
import estructuras.IIterator;

public class Itinerario {
	
	private Double pesoTotal;
	private StringBuilder sb;
	private int ultimoDia;
	private String destino="";

	public Itinerario(IIterator<Arco<Vuelo,Conexion>> iterador, String criterio){
		pesoTotal=0.0;
		sb = new StringBuilder();
		ultimoDia=0;
		String dia = "";
		if (iterador.hasNext()){
			Arco<Vuelo, Conexion> primero = iterador.next();
			dia = darDia(primero.darInformacion().darDia());
			Double d = primero.darInformacion().darCostoAnterior();
			BigDecimal bd = new BigDecimal(d);
			bd = bd.round(new MathContext(3));
			double rounded1 = bd.doubleValue();
			Double d1 = primero.darInformacion().darCosto();
			BigDecimal bd1 = new BigDecimal(d1);
			bd1 = bd1.round(new MathContext(3));
			double rounded2 = bd1.doubleValue();
			ultimoDia = primero.darInformacion().darDia();
			sb.append(dia+ ":\n");
			sb.append(primero.darNodoInicio().darVertice().darCiudadOrigen() + " a " + primero.darNodoInicio().darVertice().darCiudadDestino() +
					" en el vuelo " + primero.darNodoInicio().darVertice().darNumeroVuelo() + "Hora: "+  primero.darNodoInicio().darVertice().darHoraSalida() +  " Aerolinea: " + primero.darNodoInicio().darVertice().darAerolinea().darNombre() + ". Costo: " + rounded1 + " USD \n");
			sb.append(primero.darNodoInicio().darVertice().darCiudadDestino() + " a " + primero.darNodoFin().darVertice().darCiudadDestino() +
					" en el vuelo " +  primero.darNodoFin().darVertice().darNumeroVuelo() + "Hora: "+  primero.darNodoFin().darVertice().darHoraSalida() + " Aerolinea: " + primero.darNodoFin().darVertice().darAerolinea().darNombre() +". Costo: " + rounded2 + " USD \n");
			destino =primero.darNodoFin().darVertice().darCiudadDestino();
			if (criterio.equals(Conexion.PESO_DURACION)){
				pesoTotal+=primero.darInformacion().darDuracion();
			}
			else if (criterio.equals(Conexion.PESO_COSTO)){
				pesoTotal+=primero.darInformacion().darCosto();
				pesoTotal+=primero.darInformacion().darCostoAnterior();
			}
		}
		while (iterador.hasNext()){
			Arco<Vuelo,Conexion> actual = iterador.next();
			Double d = actual.darInformacion().darCosto();
			BigDecimal bd = new BigDecimal(d);
			bd = bd.round(new MathContext(3));
			double rounded = bd.doubleValue();
			if (!darDia(actual.darInformacion().darDia()).equals(dia)){
				sb.append(darDia(actual.darInformacion().darDia()) + ": \n");
				ultimoDia=actual.darInformacion().darDia();
			}
			sb.append(actual.darNodoFin().darVertice().darCiudadOrigen() + " a " + actual.darNodoFin().darVertice().darCiudadDestino()+
				" en el vuelo " + actual.darNodoFin().darVertice().darNumeroVuelo()	+ " Hora: " + actual.darNodoFin().darVertice().darHoraSalida() + " Aerolinea: "+ actual.darNodoFin().darVertice().darNumeroVuelo() +" Costo: "+ rounded + " USD \n");
			destino = actual.darNodoFin().darVertice().darCiudadDestino();
		}
	}
	
	public Double darPesoTotal(){
		return pesoTotal;
	}
	
	public StringBuilder darItinerario(){
		return sb;
	}
	
	public int darUltimoDia(){
		return ultimoDia;
	}
	
	public String darDestino(){
		return destino;
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
}
