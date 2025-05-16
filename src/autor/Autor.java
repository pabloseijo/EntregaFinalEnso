package autor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import album.IAlbum;
import cancion.ICancion;

public class Autor implements IAutor {
    private final int idAutor;
    private final String nombre;
    private final List<IAlbum> albumes;

    public Autor(int idAutor, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del autor no puede ser nulo o vacío.");
        }
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.albumes = new ArrayList<>();
    }

    @Override
    public int getIdAutor() {
        return idAutor;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void registrarAlbum(IAlbum album) {
        if (album == null) {
            throw new IllegalArgumentException("El álbum no puede ser nulo.");
        }
        if (!album.getAutor().equals(this)) {
            throw new IllegalArgumentException("El álbum no pertenece a este autor.");
        }
        albumes.add(album);
    }

    @Override
    public List<IAlbum> getAlbumes() {
        return Collections.unmodifiableList(albumes);
    }
}
