package escucha;

import java.time.LocalTime;

import cancion.ICancion;
import usuario.IUsuario;

public interface IEscucha {
    IUsuario getUsuario();
    ICancion getCancion();
    boolean esReciente(int minutos);
    String getResumen();
    boolean esDeCancionConDuracionSuperiorA(int segundos);
    java.time.Duration tiempoDesdeLaEscucha();
    boolean perteneceARangoHorario(LocalTime inicio, LocalTime fin);
    String getDescripcionDetallada();
    public boolean esCancionDelAutorConNombre(String nombreAutor);
}
