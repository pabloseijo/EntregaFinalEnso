package ListaReproduccion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import autor.IAutor;
import autor.Autor;

import album.IAlbum;
import album.Album;

import cancion.ICancion;
import cancion.Cancion;

import usuario.IUsuario;
import usuario.Usuario;

import escucha.IEscucha;
import escucha.Escucha;

public class ListaReproduccion {

    public Map<IUsuario, List<ICancion>> generarListasPorUsuario(String rutaCSV, List<IEscucha> escuchas) {
        Map<String, IAutor> autores = new HashMap<>();
        Map<String, IAlbum> albumes = new HashMap<>();
        Map<String, IUsuario> usuarios = new HashMap<>();
        Map<IUsuario, List<ICancion>> resultado = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
            String linea = br.readLine(); // encabezado

            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split(",");

                if (columnas.length != 7) {
                    System.err.println("Línea con formato inválido: " + linea);
                    continue;
                }

                int id = Integer.parseInt(columnas[0].trim());
                String nombre = columnas[1].trim();
                String apellido = columnas[2].trim();
                String artista = columnas[3].trim();
                String album = columnas[4].trim();
                String cancion = columnas[5].trim();
                String duracion = columnas[6].trim();

                String nombreCompleto = nombre + " " + apellido;
                String claveAlbum = artista + "-" + album;

                String[] partesDuracion = duracion.split(":");
                int minutos = Integer.parseInt(partesDuracion[0]);
                int segundos = Integer.parseInt(partesDuracion[1]);
                int duracionSegundos = minutos * 60 + segundos;

                // Obtener o crear autor
                IAutor au = autores.computeIfAbsent(artista, k -> new Autor(k.hashCode(), k));

                // Obtener o crear álbum
                IAlbum al = albumes.computeIfAbsent(claveAlbum, k -> {
                    IAlbum nuevo = new Album(album.hashCode(), album, au);
                    au.registrarAlbum(nuevo);
                    return nuevo;
                });

                // Crear canción y asociarla al álbum
                ICancion c = new Cancion(id, cancion, duracionSegundos, al);
                al.agregarCancion(c);

                // Obtener o crear usuario
                IUsuario u = usuarios.computeIfAbsent(nombreCompleto, k -> new Usuario(k));

                // Agregar canción a la lista del usuario
                resultado.computeIfAbsent(u, k -> new ArrayList<>()).add(c);

                // Crear escucha
                IEscucha e = new Escucha(u, c, LocalDateTime.now());
                escuchas.add(e);
            }

        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error al procesar el archivo CSV.");
            e.printStackTrace();
        }

        return resultado;
    }
    
    public List<Integer> generarListaReproduccionIntercalada(
    	    Map<IUsuario, List<ICancion>> cancionesPorUsuario,
    	    List<IUsuario> usuariosEnCoche,
    	    int maxTiempo
    	) {
    	    List<Integer> listaGlobal = new ArrayList<>();
    	    Map<IUsuario, Integer> indicesPorUsuario = new HashMap<>();
    	    int tiempoAcumulado = 0;

    	    // Inicializar índices por usuario
    	    for (IUsuario usuario : usuariosEnCoche) {
    	        indicesPorUsuario.put(usuario, 0);
    	    }

    	    boolean cancionesDisponibles = true;

    	    while (tiempoAcumulado < maxTiempo && cancionesDisponibles) {
    	        cancionesDisponibles = false;

    	        for (IUsuario usuario : usuariosEnCoche) {
    	            List<ICancion> cancionesUsuario = cancionesPorUsuario.getOrDefault(usuario, List.of());
    	            int indice = indicesPorUsuario.get(usuario);

    	            if (indice < cancionesUsuario.size()) {
    	                ICancion cancion = cancionesUsuario.get(indice);
    	                int duracion = cancion.getDuracion();

    	                if (tiempoAcumulado + duracion > maxTiempo) {
    	                    return listaGlobal;
    	                }

    	                listaGlobal.add(cancion.getIdCancion());
    	                tiempoAcumulado += duracion;

    	                indicesPorUsuario.put(usuario, indice + 1);
    	                cancionesDisponibles = true;
    	            }
    	        }
    	    }

    	    return listaGlobal;
    	}
a
}
