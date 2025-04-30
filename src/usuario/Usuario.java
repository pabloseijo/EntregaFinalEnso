package usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
