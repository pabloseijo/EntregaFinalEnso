package cancion;

import album.IAlbum;

public interface ICancion {
    int getIdCancion();
    String getNombre();
    int getDuracion();
    IAlbum getAlbum();
}
