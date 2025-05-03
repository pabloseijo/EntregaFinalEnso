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
    
    @Override
    public boolean esLarga(int umbralSegundos) {
        if (umbralSegundos <= 0) {
            throw new IllegalArgumentException("El umbral debe ser positivo.");
        }
        return this.duracion > umbralSegundos;
    }

    
    @Override
    public String getDuracionFormateada() {
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    @Override
    public boolean perteneceAlAlbumConNombre(String nombreAlbum) {
        if (nombreAlbum == null || nombreAlbum.isBlank()) {
            throw new IllegalArgumentException("El nombre del álbum no puede ser nulo.");
        }
        return album.getNombre().equalsIgnoreCase(nombreAlbum);
    }

    @Override
    public int compararDuracion(ICancion otra) {
        if (otra == null) {
            throw new IllegalArgumentException("La canción a comparar no puede ser nula.");
        }
        return Integer.compare(this.duracion, otra.getDuracion());
    }

    @Override
    public boolean perteneceAMismoAutor(ICancion otra) {
        if (otra == null) throw new IllegalArgumentException("La canción a comparar no puede ser nula.");
        return this.getAlbum().getAutor().getIdAutor() == otra.getAlbum().getAutor().getIdAutor();
    }

}
