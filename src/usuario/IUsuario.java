package usuario;

import java.util.List;
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
}
