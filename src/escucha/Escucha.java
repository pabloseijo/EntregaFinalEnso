package escucha;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import cancion.ICancion;
import usuario.IUsuario;

public class Escucha implements IEscucha {
    private final IUsuario usuario;
    private final ICancion cancion;
    private final LocalDateTime fechaEscucha;

    public Escucha(IUsuario usuario, ICancion cancion, LocalDateTime fechaEscucha) {
        if (usuario == null || cancion == null || fechaEscucha == null) {
            throw new IllegalArgumentException("Ningún parámetro puede ser nulo.");
        }
        this.usuario = usuario;
        this.cancion = cancion;
        this.fechaEscucha = fechaEscucha;
    }

    @Override
    public IUsuario getUsuario() {
        return usuario;
    }

    @Override
    public ICancion getCancion() {
        return cancion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IEscucha)) return false;
        IEscucha other = (IEscucha) obj;
        return usuario.getNombreUsuario().equals(other.getUsuario().getNombreUsuario()) &&
               cancion.getIdCancion() == other.getCancion().getIdCancion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario.getNombreUsuario(), cancion.getIdCancion());
    }
    
    @Override
    public boolean esReciente(int minutos) {
        if (minutos <= 0) throw new IllegalArgumentException("Minutos debe ser positivo.");
        return fechaEscucha.isAfter(LocalDateTime.now().minusMinutes(minutos));
    }

    @Override
    public String getResumen() {
        return String.format("Usuario %s escuchó '%s' a las %s",
            usuario.getNombreUsuario(),
            cancion.getNombre(),
            fechaEscucha.toLocalTime().toString());
    }

    @Override
    public boolean esDeCancionConDuracionSuperiorA(int segundos) {
        return cancion.getDuracion() > segundos;
    }
    
    @Override
    public Duration tiempoDesdeLaEscucha() {
        return Duration.between(fechaEscucha, LocalDateTime.now());
    }
    
    @Override
    public boolean perteneceARangoHorario(LocalTime inicio, LocalTime fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("El rango horario no puede ser nulo.");
        }
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la de fin.");
        }

        LocalTime horaEscucha = fechaEscucha.toLocalTime();
        return !horaEscucha.isBefore(inicio) && !horaEscucha.isAfter(fin);
    }
    
    @Override
    public String getDescripcionDetallada() {
        return String.format("%s escuchó '%s' (%s) del álbum '%s'",
            usuario.getNombreUsuario(),
            cancion.getNombre(),
            cancion.getDuracionFormateada(),
            cancion.getAlbum().getNombre()
        );
    }
    
    @Override
    public boolean esCancionDelAutorConNombre(String nombreAutor) {
        if (nombreAutor == null || nombreAutor.isBlank()) {
            throw new IllegalArgumentException("Nombre inválido.");
        }
        return cancion.getAlbum().getAutor().getNombre().equalsIgnoreCase(nombreAutor);
    }

}
