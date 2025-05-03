package autor;

import java.util.List;
import album.IAlbum;

public interface IAutor {
    int getIdAutor();
    String getNombre();

    void registrarAlbum(IAlbum album);
    List<IAlbum> getAlbumes();

    int getNumeroTotalDeCanciones();
    int getDuracionTotal();
    List<String> getNombresDeCancionesLargas(int umbralSegundos);
    List<String> getCancionesLargasOrdenadasPorAlbum(int umbral);
    public List<IAlbum> getAlbumesConDuracionMediaSuperiorA(double umbral);
}
