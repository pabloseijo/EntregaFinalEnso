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

public class ListaReproduccion implements IListaReproduccion {

	@Override
	public int stringASegundos(String duracion) {
		// Solo admite tiempo en formato "mm:ss"
		String[] partesDuracion = duracion.split(":");
        int minutos = Integer.parseInt(partesDuracion[0]);
        int segundos = Integer.parseInt(partesDuracion[1]);
        return minutos * 60 + segundos;
	}

	@Override
	public Map<IUsuario, List<ICancion>> generarListasPorUsuario(String rutaCSV) {
        Map<String, IAutor> autores = new HashMap<>();
        Map<String, IAlbum> albumes = new HashMap<>();
        Map<String, IUsuario> usuarios = new HashMap<>();
        Map<IUsuario, List<ICancion>> resultado = new LinkedHashMap<>();	// Linked para que mantenga el orden de los usuarios

        try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
            String linea = br.readLine(); // encabezado

            while ((linea = br.readLine()) != null) {
            	if (linea.trim().isEmpty()) continue; // ignorar línea vacía final
            	
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

                int duracionSegundos = stringASegundos(duracion);

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

                // Crear escucha
                IEscucha e = new Escucha(u, c, LocalDateTime.now());
                
                // Agregar canción a la lista del usuario
                resultado.computeIfAbsent(u, k -> new ArrayList<>()).add(c);
            }

        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error al procesar el archivo CSV.");
            e.printStackTrace();
        }

        return resultado;
    }

	@Override
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

    	    while (tiempoAcumulado < maxTiempo) {
    	        boolean alguienTieneCanciones = false;
    	        
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
    	                
    	                alguienTieneCanciones = true;
    	            }
    	        }
    	        // Si todos los índices han llegado al final, reiniciamos
    	        if (!alguienTieneCanciones) {
    	            for (IUsuario usuario : usuariosEnCoche) {
    	                indicesPorUsuario.put(usuario, 0);
    	            }
    	        }
    	        
    	    }
    	    // Lista con el ID de las canciones que da tiempo a poner
    	    return listaGlobal;
    	}
}
