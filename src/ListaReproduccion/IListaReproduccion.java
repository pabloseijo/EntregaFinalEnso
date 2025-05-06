package ListaReproduccion;

import java.util.List;
import java.util.Map;

import cancion.ICancion;
import usuario.IUsuario;

public interface IListaReproduccion {
	public int stringASegundos(String duracion);
	public Map<IUsuario, List<ICancion>> generarListasPorUsuario(String rutaCSV);
	public List<Integer> generarListaReproduccionIntercalada(Map<IUsuario, List<ICancion>> cancionesPorUsuario, List<IUsuario> usuariosEnCoche, int maxTiempo) ;
}
