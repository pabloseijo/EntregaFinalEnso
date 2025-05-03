package cancion;

import album.IAlbum;

public interface ICancion {
    int getIdCancion();
    String getNombre();
    int getDuracion();
    IAlbum getAlbum();
    boolean esLarga(int umbralSegundos);
    String getDuracionFormateada();
    boolean perteneceAlAlbumConNombre(String nombreAlbum);
    int compararDuracion(ICancion otra);
    public boolean perteneceAMismoAutor(ICancion otra);
}
