package mundo;

public class IdentificadorVuelo {
	
	private int identificador;
	
	public IdentificadorVuelo(int pNumVuelo, int pNumSillas, String nombreAerolinea,String pOrigen, String pDestino, Double horaSalida, Double horaLlegada){
		identificador+=nombreAerolinea.hashCode();
		identificador+=pNumVuelo;
		identificador+=pOrigen.hashCode();
		identificador+=pDestino.hashCode();
		identificador+=Double.doubleToLongBits(horaSalida);
		identificador+=Double.doubleToLongBits(horaLlegada);
	}
	
	public int darIdentificador(){
		return identificador;
	}
}
