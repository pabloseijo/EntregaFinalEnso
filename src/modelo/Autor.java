package modelo;

public class Autor {
	private final int idAutor;
    private final String nombre;

    public Autor(int idAutor, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del autor no puede ser nulo o vacío.");
        }
        this.idAutor = idAutor;
        this.nombre = nombre;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public String getNombre() {
        return nombre;
    }
}
