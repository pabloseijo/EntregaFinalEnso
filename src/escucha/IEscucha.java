package escucha;

import cancion.ICancion;
import usuario.IUsuario;

public interface IEscucha {
    IUsuario getUsuario();
    ICancion getCancion();
}
