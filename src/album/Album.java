package album;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import autor.IAutor;
import cancion.ICancion;

public class Album implements IAlbum {
    private final int idAlbum;
    private final String nombre;
    private final IAutor autor;
    private final List<ICancion> canciones;

    public Album(int idAlbum, String nombre, IAutor autor) {
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

    @Override
    public int getIdAlbum() {
        return idAlbum;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public IAutor getAutor() {
        return autor;
    }

    @Override
    public void agregarCancion(ICancion cancion) {
        if (cancion == null) {
            throw new IllegalArgumentException("La canción no puede ser nula.");
        }
        if (!cancion.getAlbum().equals(this)) {
            throw new IllegalArgumentException("La canción no pertenece a este álbum.");
        }
        canciones.add(cancion);
    }

    @Override
    public List<ICancion> getCanciones() {
        return Collections.unmodifiableList(canciones);
    }
    
    @Override
    public void ordenarCancionesPorDuracionAscendente() {
        canciones.sort(Comparator.comparingInt(ICancion::getDuracion));
    }

    @Override
    public boolean eliminarCancionPorId(int idCancion) {
        return canciones.removeIf(c -> c.getIdCancion() == idCancion);
    }

    @Override
    public int contarCancionesPorDuracionMayorA(int segundos) {
        if (segundos < 0) {
            throw new IllegalArgumentException("El tiempo debe ser no negativo.");
        }
        return (int) canciones.stream()
                .filter(c -> c.getDuracion() > segundos)
                .count();
    }

    @Override
    public List<ICancion> obtenerCancionesDelAutor(String nombreAutor) {
        if (!this.autor.getNombre().equalsIgnoreCase(nombreAutor)) {
            return List.of(); 
        }
        return getCanciones();
    }

    @Override
    public double getDuracionMediaCanciones() {
        if (canciones.isEmpty()) return 0.0;
        return canciones.stream()
            .mapToInt(ICancion::getDuracion)
            .average()
            .orElse(0.0);
    }
    
}
