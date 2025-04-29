package modelo;

public class Cancion {
	private final int idCancion;
    private final String nombre;
    private final int duracion; // en segundos
    private final Album album;

    public Cancion(int idCancion, String nombre, int duracion, Album album) {
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

    public int getIdCancion() {
        return idCancion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public Album getAlbum() {
        return album;
    }
}
