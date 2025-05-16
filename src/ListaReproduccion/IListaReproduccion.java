package ListaReproduccion;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cancion.ICancion;
import usuario.IUsuario;

public interface IListaReproduccion {
	public int stringASegundos(String duracion);

	public Map<IUsuario, List<ICancion>> generarListasPorUsuario(String rutaCSV);

	public int calcularDuracion(List<ICancion> canciones);

	public List<ICancion> intercalarCanciones(Map<IUsuario, List<ICancion>> listasUsuario,
			Set<IUsuario> usuariosEnCoche);

	public List<Integer> generarListaReproduccionIntercalada(String rutaCSV, Set<IUsuario> usuariosEnCoche,
			int maxTiempo);

	// ***** Nuevas operaciones Sprint 2 *****
	List<ICancion> filtrarPorDuracion(List<ICancion> canciones, int minSeg, int maxSeg);

	List<ICancion> ordenarPorDuracion(List<ICancion> canciones, String orden);
}
