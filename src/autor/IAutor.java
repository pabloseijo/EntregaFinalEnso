package autor;

import java.util.List;
import album.IAlbum;

public interface IAutor {
    int getIdAutor();
    String getNombre();

    void registrarAlbum(IAlbum album);
    List<IAlbum> getAlbumes();
}
