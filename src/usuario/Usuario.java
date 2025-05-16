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


}
