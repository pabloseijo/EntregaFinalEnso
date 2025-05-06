package usuario;

import java.util.List;
import java.util.Map;

import cancion.ICancion;

public interface IUsuario {
    String getNombreUsuario();
    void escucharCancion(ICancion cancion);
    List<ICancion> getCancionesEscuchadas();
    int contarCancionesEscuchadas();
    List<ICancion> obtenerCancionesLargas(int umbralSegundos);
    boolean haEscuchadoCancionConNombre(String nombre);
    int getDuracionTotalEscuchada();
    List<String> getResumenDeEscuchas();
    Map<String, List<String>> agruparCancionesPorNombreDeAlbum();
    List<String> obtenerResumenCancionesLargasConAlbum(int umbral);
    public List<ICancion> obtenerCancionesDelAutor(int idAutor);
}
