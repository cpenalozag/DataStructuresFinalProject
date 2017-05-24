package mundo;

import estructuras.IComparator;

public class ComparadorVuelo implements IComparator<Vuelo> 
{

	public Double compare(Vuelo o1, Vuelo o2) {
		if (o1.darAerolinea().darNombre().compareTo(o2.darAerolinea().darNombre())>1.0){
			return 1.0;
		}
		return -1.0;
	}

}
