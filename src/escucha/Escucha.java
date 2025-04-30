package escucha;

import java.util.Objects;

import cancion.ICancion;
import usuario.IUsuario;

public class Escucha implements IEscucha {
    private final IUsuario usuario;
    private final ICancion cancion;

    public Escucha(IUsuario usuario, ICancion cancion) {
        if (usuario == null || cancion == null) {
            throw new IllegalArgumentException("Ni el usuario ni la canción pueden ser nulos.");
        }
        this.usuario = usuario;
        this.cancion = cancion;
    }

    @Override
    public IUsuario getUsuario() {
        return usuario;
    }

    @Override
    public ICancion getCancion() {
        return cancion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IEscucha)) return false;
        IEscucha other = (IEscucha) obj;
        return usuario.getNombreUsuario().equals(other.getUsuario().getNombreUsuario()) &&
               cancion.getIdCancion() == other.getCancion().getIdCancion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario.getNombreUsuario(), cancion.getIdCancion());
    }
}
