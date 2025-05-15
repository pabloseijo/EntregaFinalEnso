package ListaReproduccion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import autor.IAutor;
import autor.Autor;

import album.IAlbum;
import album.Album;

import cancion.ICancion;
import cancion.Cancion;

import usuario.IUsuario;
import usuario.Usuario;

public class ListaReproduccion implements IListaReproduccion {

	@Override
	public int stringASegundos(String duracion) {
		// Solo admite tiempo en formato "mm:ss"
        try {
            if (duracion == null || !duracion.matches("\\d{1,}:\\d{2}")) {
                throw new IllegalArgumentException("Formato inválido");
            }
            String[] partes = duracion.split(":");
            int minutos = Integer.parseInt(partes[0]);
            int segundos = Integer.parseInt(partes[1]);
            if (segundos >= 60) {
                throw new IllegalArgumentException("Segundos fuera de rango");
            }
            return minutos * 60 + segundos;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error en formato de duración", e);
        }
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
	public int calcularDuracion(List<ICancion> canciones) {
	    if (canciones == null) {
	        throw new IllegalArgumentException("La lista de canciones no puede ser null");
	    }
		
		int duracion = 0;
		for(ICancion cancion : canciones) {
			duracion += cancion.getDuracion();
		}
		
		return duracion;
	}

	@Override
	public List<ICancion> intercalarCanciones(Map<IUsuario, List<ICancion>> listasUsuario, Set<IUsuario> usuariosEnCoche){
	    // Lista que almacenará el resultado de una "ronda" de intercalación
		List<ICancion> cancionesIntercaladas = new ArrayList<>();
		
		// Si no se pasan listas de canciones de cada usuario, se devuelve una lista vacía.
		if(listasUsuario == null || listasUsuario.isEmpty()) {
			return cancionesIntercaladas;
		}

	    // Mapa que mantiene el índice actual de cada usuario (cuántas canciones suyas ya se han intercalado)
		Map<IUsuario, Integer> indicesPorUsuario = new HashMap<>();

	    // Si no se especifican los usuarios, se usan todos los del CSV
	    if(usuariosEnCoche == null || usuariosEnCoche.isEmpty()) {
			usuariosEnCoche = listasUsuario.keySet();
	    }
	
		// Inicializar índices por usuario
	    for (IUsuario usuario : usuariosEnCoche) {
	        indicesPorUsuario.put(usuario, 0);
	    }

	    boolean cancionesPendientes = true;

	    // Mientras haya al menos una canción pendiente para cualquier usuario
	    while (cancionesPendientes) {
	        cancionesPendientes = false;

		    // Esta parte realiza una única ronda de intercalación:
		    // Añade la siguiente canción disponible (si existe) de cada usuario
		    for (IUsuario usuario : usuariosEnCoche) {
		        List<ICancion> cancionesUsuario = listasUsuario.getOrDefault(usuario, List.of());
		        int indice = indicesPorUsuario.get(usuario);
		        
		        // Si aún quedan canciones por intercalar para este usuario...
		        if (indice < cancionesUsuario.size()) {
		            ICancion cancion = cancionesUsuario.get(indice);
	
		            cancionesIntercaladas.add(cancion);
	
		            indicesPorUsuario.put(usuario, indice + 1);	 
		            
		            cancionesPendientes = true;
		        }
		    }	
	    }
	    return cancionesIntercaladas;
	}
	
	@Override
    public List<Integer> generarListaReproduccionIntercalada(
    	    String rutaCSV,
    	    Set<IUsuario> usuariosEnCoche,
    	    int maxTiempo
    	) {
    	    List<Integer> listaGlobal = new ArrayList<>();
    	    List<ICancion> cancionesResultado = new ArrayList<>();
    	    List<ICancion> cancionesIntercaladas = new ArrayList<>();
    	    
    	    Map<IUsuario, List<ICancion>> cancionesPorUsuario = generarListasPorUsuario(rutaCSV);

    	    // Si no se especifican los usuarios, se usan todos los del CSV
    	    if(usuariosEnCoche == null || usuariosEnCoche.isEmpty()) {
    			usuariosEnCoche = cancionesPorUsuario.keySet();
    	    }
    	    
    	    // Este while se ejecuta cada vez que se recorren todas las canciones y hay que volver a empezar
    	    while (this.calcularDuracion(cancionesResultado) < maxTiempo) {
        	    cancionesIntercaladas = intercalarCanciones(cancionesPorUsuario, usuariosEnCoche);
        	    
        	    for(ICancion cancion : cancionesIntercaladas) {
    	    		cancionesResultado.add(cancion);
    	    		// Se añade una cancion aunque no de tiempo a acabarla
        	    	if(this.calcularDuracion(cancionesResultado) > maxTiempo)
        	    		break;
        	    }
    	    }
    	    
    	    // Lista con el ID de las canciones que da tiempo a poner
    	    for(ICancion cancion : cancionesResultado) {
    	    	listaGlobal.add(cancion.getIdCancion());
    	    }
    	    return listaGlobal;
    	}
}
