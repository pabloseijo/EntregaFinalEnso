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

    @Override
    public int getNumeroTotalDeCanciones() {
        return albumes.stream()
            .mapToInt(a -> a.getCanciones().size())
            .sum();
    }

    @Override
    public int getDuracionTotal() {
        return albumes.stream()
            .flatMap(a -> a.getCanciones().stream())
            .mapToInt(ICancion::getDuracion)
            .sum();
    }

    @Override
    public List<String> getNombresDeCancionesLargas(int umbralSegundos) {
        if (umbralSegundos <= 0) {
            throw new IllegalArgumentException("El umbral debe ser positivo.");
        }
        List<String> resultado = new ArrayList<>();
        for (IAlbum album : albumes) {
            for (ICancion cancion : album.getCanciones()) {
                if (cancion.getDuracion() > umbralSegundos) {
                    resultado.add(cancion.getNombre());
                }
            }
        }
        return resultado;
    }
    
    @Override
    public List<String> getCancionesLargasOrdenadasPorAlbum(int umbral) {
        return albumes.stream()
            .map(album -> {
                List<String> cancionesFiltradas = album.getCanciones().stream()
                    .filter(c -> c.getDuracion() > umbral)
                    .map(ICancion::getNombre)
                    .toList();

                if (cancionesFiltradas.isEmpty()) return null;

                return "Álbum: " + album.getNombre() + " → " + String.join(", ", cancionesFiltradas);
            })
            .filter(Objects::nonNull)
            .toList();
    }
    
    @Override
    public List<IAlbum> getAlbumesConDuracionMediaSuperiorA(double umbral) {
        if (umbral <= 0) throw new IllegalArgumentException("Umbral inválido.");
        return albumes.stream()
            .filter(album -> album.getDuracionMediaCanciones() > umbral)
            .toList();
    }
}
