package mundo;

import estructuras.IComparator;

public class ComparadorConexion implements IComparator<Conexion>  {

	@Override
	public Double compare(Conexion o1, Conexion o2) {
		Double resp = null;
		if (o1.darCosto().equals(o2.darCosto())){
			if (o1.darDuracion()==o2.darDuracion()){
				resp=0.0;
			}
			else{
				resp = (o1.darDuracion()-o2.darDuracion());
			}
		}
		else{
			resp = (o1.darCosto()-o2.darCosto());
		}
		return resp;
	}

}
