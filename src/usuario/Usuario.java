package usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cancion.ICancion;

public class Usuario implements IUsuario {
    private final String nombreUsuario;
    private final List<ICancion> cancionesEscuchadas;

    public Usuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
        }
        this.nombreUsuario = nombreUsuario;
        this.cancionesEscuchadas = new ArrayList<>();
    }

    @Override
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    @Override
    public void escucharCancion(ICancion cancion) {
        if (cancion == null) {
            throw new IllegalArgumentException("La canción no puede ser nula.");
        }
        cancionesEscuchadas.add(cancion);
    }

    @Override
    public List<ICancion> getCancionesEscuchadas() {
        return Collections.unmodifiableList(cancionesEscuchadas);
    }
    
    @Override
    public int contarCancionesEscuchadas() {
        return cancionesEscuchadas.size();
    }

    @Override
    public List<ICancion> obtenerCancionesLargas(int umbralSegundos) {
        if (umbralSegundos <= 0) {
            throw new IllegalArgumentException("Umbral debe ser positivo.");
        }
        return cancionesEscuchadas.stream()
            .filter(c -> c.getDuracion() > umbralSegundos)
            .toList();
    }
    
    @Override
    public Map<String, List<String>> agruparCancionesPorNombreDeAlbum() {
        return cancionesEscuchadas.stream()
            .collect(Collectors.groupingBy(
                c -> c.getAlbum().getNombre(),
                Collectors.mapping(ICancion::getNombre, Collectors.toList())
            ));
    }


    @Override
    public boolean haEscuchadoCancionConNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido.");
        }
        return cancionesEscuchadas.stream()
            .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));
    }

    @Override
    public int getDuracionTotalEscuchada() {
        return cancionesEscuchadas.stream()
            .mapToInt(ICancion::getDuracion)
            .sum();
    }

    @Override
    public List<String> getResumenDeEscuchas() {
        return cancionesEscuchadas.stream()
            .map(c -> String.format("Canción: %s, Duración: %ds", c.getNombre(), c.getDuracion()))
            .toList();
    }
    
    @Override
    public List<String> obtenerResumenCancionesLargasConAlbum(int umbral) {
        if (umbral <= 0) throw new IllegalArgumentException("Umbral inválido.");

        return cancionesEscuchadas.stream()
            .filter(c -> c.getDuracion() > umbral)
            .map(c -> String.format(
                "Canción %s (%s) - Álbum %s",
                c.getNombre(),
                c.getDuracionFormateada(),
                c.getAlbum().getNombre()
            ))
            .toList();
    }

    @Override
    public List<ICancion> obtenerCancionesDelAutor(int idAutor) {
        return cancionesEscuchadas.stream()
            .filter(c -> c.getAlbum().getAutor().getIdAutor() == idAutor)
            .toList();
    }
}
