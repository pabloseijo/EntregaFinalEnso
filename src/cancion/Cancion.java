package cancion;

import album.IAlbum;

public class Cancion implements ICancion {
    private final int idCancion;
    private final String nombre;
    private final int duracion; // en segundos
    private final IAlbum album;

    public Cancion(int idCancion, String nombre, int duracion, IAlbum album) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la canción no puede ser nulo o vacío.");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser un entero positivo.");
        }
        if (album == null) {
            throw new IllegalArgumentException("La canción debe pertenecer a un álbum.");
        }
        this.idCancion = idCancion;
        this.nombre = nombre;
        this.duracion = duracion;
        this.album = album;
    }

    @Override
    public int getIdCancion() {
        return idCancion;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getDuracion() {
        return duracion;
    }

    @Override
    public IAlbum getAlbum() {
        return album;
    }

}
