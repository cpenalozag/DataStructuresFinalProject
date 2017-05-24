package estructuras;

public interface IComparator<T>
{
	/**
	 * Compara dos objetos.
	 * @param o1 primer objeto
	 * @param o2 segundo objeto
	 * @return un numero positivo si el primer objeto el mayor al segundo, un numero negativo
	 * de lo contrario o 0 si son iguales.
	 */
	public Double compare(T o1, T o2);
}
