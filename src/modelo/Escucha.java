package modelo;

import java.util.Objects;

public class Escucha {
	private final Usuario usuario;
    private final Cancion cancion;

    public Escucha(Usuario usuario, Cancion cancion) {
        if (usuario == null || cancion == null) {
            throw new IllegalArgumentException("Ni el usuario ni la canción pueden ser nulos.");
        }
        this.usuario = usuario;
        this.cancion = cancion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Cancion getCancion() {
        return cancion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Escucha)) return false;
        Escucha other = (Escucha) obj;
        return usuario.getNombreUsuario().equals(other.usuario.getNombreUsuario()) &&
               cancion.getIdCancion() == other.cancion.getIdCancion();
    }

    /** Se usa para poder utilizar la clase en colecciones que dependan de unicidad **/
    @Override
    public int hashCode() { 
        return Objects.hash(usuario.getNombreUsuario(), cancion.getIdCancion());
    }
}
