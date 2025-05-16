package album;

import java.util.List;
import autor.IAutor;
import cancion.ICancion;

public interface IAlbum {
    int getIdAlbum();
    String getNombre();
    IAutor getAutor();
    void agregarCancion(ICancion cancion);
    List<ICancion> getCanciones();
}
