package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Album {
	private final int idAlbum;
    private final String nombre;
    private final Autor autor;
    private final List<Cancion> canciones;

    public Album(int idAlbum, String nombre, Autor autor) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del álbum no puede ser nulo o vacío.");
        }
        if (autor == null) {
            throw new IllegalArgumentException("El álbum debe tener un autor.");
        }
        this.idAlbum = idAlbum;
        this.nombre = nombre;
        this.autor = autor;
        this.canciones = new ArrayList<>();
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public String getNombre() {
        return nombre;
    }

    public Autor getAutor() {
        return autor;
    }

    public void agregarCancion(Cancion cancion) {
        if (cancion == null) {
            throw new IllegalArgumentException("La canción no puede ser nula.");
        }
        if (!cancion.getAlbum().equals(this)) {
            throw new IllegalArgumentException("La canción no pertenece a este álbum.");
        }
        canciones.add(cancion);
    }

    public List<Cancion> getCanciones() {
        return Collections.unmodifiableList(canciones);
    }
}
