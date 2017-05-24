package mundo;

public class Conexion implements Comparable<Conexion> {
	private Double duracion;
	private Double costo;
	private int dia;
	private Double costoAnterior;
	
	/**
	 * Constante que representa que el peso principal es el costo
	 */
	public final static String PESO_COSTO = "costo";
	
	/**
	 * Constante que representa que el peso principal es el costo
	 */
	public final static String PESO_DURACION = "duracion";
	
	public Conexion(Double pCosto, Double pDuracion, int pDia, Double pCostoAnterior){
		costo = pCosto;
		duracion = pDuracion;
		dia = pDia;
		costoAnterior = pCostoAnterior;
	}

	public Double darPeso(String tipo){
		Double resp = -1.0;
		switch (tipo){
		case PESO_COSTO:
			resp = darCosto();
			break;
		case PESO_DURACION:
		resp = darDuracion();
			break;
		}
		if (resp==-1){
			System.err.println("Peso negativo");
		}
		return resp;
	}
	
	public Double darCosto() {
		// TODO Auto-generated method stub
		return costo;
	}

	public Double darDuracion() {
		// TODO Auto-generated method stub
		return duracion;
	}
	
	public int darDia(){
		return dia;
	}

	public Double darCostoAnterior(){
		return costoAnterior;
	}
	
	@Override
	public int compareTo(Conexion o) {
		ComparadorConexion c = new ComparadorConexion();
		return c.compare(this, o).intValue();
	}
}
