package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Usuario {
    private final String nombreUsuario;
    private final List<Cancion> cancionesEscuchadas;

    public Usuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
        }
        this.nombreUsuario = nombreUsuario;
        this.cancionesEscuchadas = new ArrayList<>();
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void escucharCancion(Cancion cancion) {
        if (cancion == null) {
            throw new IllegalArgumentException("La canción no puede ser nula.");
        }
        cancionesEscuchadas.add(cancion);
    }

    public List<Cancion> getCancionesEscuchadas() {
        return Collections.unmodifiableList(cancionesEscuchadas);
    }
}
