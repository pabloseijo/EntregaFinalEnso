package ListaReproduccion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import cancion.ICancion;
import usuario.IUsuario;

class ListaReproduccionTest {

	@ParameterizedTest(name = "Caso {index}: \"{0}\" → {1} segundos")
    @MethodSource("casosValidos")
    @Tag("Sprint1")
    @DisplayName("Subrueba 1.1: Conversión correcta de duraciones válidas")
    void testStringASegundos_Valido(String input, int esperado) {
        IListaReproduccion lista = new ListaReproduccion();
        int resultado = lista.stringASegundos(input);
        assertEquals(esperado, resultado, "La conversión de " + input + " no es correcta.");
    }

    static Stream<Arguments> casosValidos() {
        return Stream.of(
            Arguments.of("00:00", 0),
            Arguments.of("00:01", 1),
            Arguments.of("01:00", 60),
            Arguments.of("02:30", 150),
            Arguments.of("10:00", 600),
            Arguments.of("59:59", 3599),
            Arguments.of("90:00", 5400)
        );
    }

    @Test
    @Tag("Sprint1")
    @DisplayName("Prueba 1: Carga correcta desde el CSV")
    void testCargaDesdeCSV() {
        IListaReproduccion lista = new ListaReproduccion();
        Map<IUsuario, List<ICancion>> usuarios = lista.generarListasPorUsuario("src/resources/discos_usuarios.csv");

        assertEquals(4, usuarios.size(), "El CSV debe contener 4 usuarios."); // número esperado de usuarios
        for (Map.Entry<IUsuario, List<ICancion>> entry : usuarios.entrySet()) {
            IUsuario usuario = entry.getKey();
            List<ICancion> canciones = entry.getValue();
            assertNotNull(usuario.getNombreUsuario(), "No se reconocio el nombre de todos los usuarios");
            for (ICancion cancion : canciones) {
                assertNotNull(cancion.getAlbum(), "Las canciones deben estar asociadas a albumes");
            }
        }
    }

    @ParameterizedTest(name = "Caso {index}: duración total = {2} <= {1}")
    @MethodSource("casosDuracion")
    @Tag("Sprint1")
    @DisplayName("Prueba 2: Duración total no excede el máximo")
    void testDuracionMaxima(List<Integer> duraciones, String tiempoMaximoStr, int duracionEsperada) {
        IListaReproduccion lista = new ListaReproduccion();

        List<ICancion> canciones = new ArrayList<>();
        for (Integer dur : duraciones) {
            ICancion c = mock(ICancion.class);
            when(c.getDuracion()).thenReturn(dur);
            canciones.add(c);
        }
        
        int maxTiempo = lista.stringASegundos(tiempoMaximoStr);

        int duracionTotal = lista.calcularDuracion(canciones); 

        assertEquals(duracionEsperada, duracionTotal, "La duración total calculada no coincide con la esperada");
        assertTrue(duracionTotal <= maxTiempo, "La duración total no debe exceder el tiempo máximo");
    }
    static Stream<Arguments> casosDuracion() {
        return Stream.of(
            Arguments.of(Arrays.asList(1200, 1200, 1199), "60:00", 3599),  // 59:59
            Arguments.of(Arrays.asList(1800, 1800, 1800), "90:00", 5400),        // 90:00 exactos
            Arguments.of(Arrays.asList(), "60:00", 0)                            // Lista vacía
        );
    }

    @Test
    @Tag("Sprint1")
    @DisplayName("Prueba 3: Orden correcto de intercalado")
    void testOrdenIntercalado() {
        IListaReproduccion lista = new ListaReproduccion();
        
        IUsuario u1 = mock(IUsuario.class);
        IUsuario u2 = mock(IUsuario.class);
        IUsuario u3 = mock(IUsuario.class);
        IUsuario u4 = mock(IUsuario.class);

        ICancion c1 = mock(ICancion.class); when(c1.getIdCancion()).thenReturn(1);  when(c1.getDuracion()).thenReturn(60);
        ICancion c2 = mock(ICancion.class); when(c2.getIdCancion()).thenReturn(6);  when(c2.getDuracion()).thenReturn(60);
        ICancion c3 = mock(ICancion.class); when(c3.getIdCancion()).thenReturn(21); when(c3.getDuracion()).thenReturn(60);
        ICancion c4 = mock(ICancion.class); when(c4.getIdCancion()).thenReturn(36); when(c4.getDuracion()).thenReturn(60);
        ICancion c5 = mock(ICancion.class); when(c5.getIdCancion()).thenReturn(2);  when(c5.getDuracion()).thenReturn(60);
        ICancion c6 = mock(ICancion.class); when(c6.getIdCancion()).thenReturn(7);  when(c6.getDuracion()).thenReturn(60);
        ICancion c7 = mock(ICancion.class); when(c7.getIdCancion()).thenReturn(22); when(c7.getDuracion()).thenReturn(60);
        ICancion c8 = mock(ICancion.class); when(c8.getIdCancion()).thenReturn(37); when(c8.getDuracion()).thenReturn(60);

        Map<IUsuario, List<ICancion>> listasUsuarios = new LinkedHashMap<>(); // Linked para mantener el orden
        listasUsuarios.put(u1, List.of(c1, c5));
        listasUsuarios.put(u2, List.of(c2, c6));
        listasUsuarios.put(u3, List.of(c3, c7));
        listasUsuarios.put(u4, List.of(c4, c8));
        
        Set<IUsuario> usuarios = listasUsuarios.keySet();

        List<ICancion> cancionesGeneradas = lista.intercalarCanciones(listasUsuarios, usuarios);
        List<Integer> listaGenerada = new ArrayList<>();
        for(ICancion cancion : cancionesGeneradas) {
        	listaGenerada.add(cancion.getIdCancion());
        }

        List<Integer> listaEsperada = Arrays.asList(1, 6, 21, 36, 2, 7, 22, 37);

        assertEquals(listaEsperada, listaGenerada, "La lista de IDs no intercala las canciones por usuarios");
      
    }

    @Test
    @Tag("Sprint1")
    @DisplayName("Prueba 4: Reinicio del ciclo cuando se acaban las canciones")
    void testReinicioCiclo() {
        IListaReproduccion lista = new ListaReproduccion();
        Map<IUsuario, List<ICancion>> listasUsuarios = new HashMap<>();

        // Mocks con solo 1 canción por usuario
        for (int i = 0; i < 3; i++) {
            IUsuario usuario =mock(IUsuario.class);
            ICancion cancion = mock(ICancion.class);
            when(cancion.getDuracion()).thenReturn(30);
            when(cancion.getIdCancion()).thenReturn(i + 1);
            listasUsuarios.put(usuario, List.of(cancion));
        }
        Set<IUsuario> usuarios = listasUsuarios.keySet();
        int maxTiempo = 180;

        // Se simula la lectura del csv manualmente aquí
        List<ICancion> listaFinal = new ArrayList<>();
        while (lista.calcularDuracion(listaFinal) < maxTiempo) {
            List<ICancion> intercalado = lista.intercalarCanciones(listasUsuarios, usuarios);
            for (ICancion c : intercalado) {
                if (lista.calcularDuracion(listaFinal) + c.getDuracion() <= maxTiempo) {
                	listaFinal.add(c);
                }
            }
        }
        
        // Esperamos al menos dos vueltas completas: 3 canciones por vuelta
        assertTrue(listaFinal.size() >= 6, "Se esperaban al menos dos vueltas completas en la lista de reproducción.");
        
        // Verificar que getDuracion() se llamó al menos dos veces por canción
        for (List<ICancion> canciones : listasUsuarios.values()) {
            for (ICancion cancion : canciones) {
                verify(cancion, atLeast(2)).getDuracion();
            }
        }
    }

	@ParameterizedTest(name = "Duración máxima: {0}")
    @MethodSource("casosDePrueba")
    @Tag("Sprint1")
    @DisplayName("Prueba de aceptación: Canciones escuchadas en diferentes duraciones")
    void testGenerarListaReproduccionIntercalada(String tiempoMaximoStr, List<Integer> listaEsperada) {	
		IListaReproduccion lista = new ListaReproduccion();
		int maxTiempo = lista.stringASegundos(tiempoMaximoStr);
		List<Integer> listaFinal = lista.generarListaReproduccionIntercalada("src/resources/discos_usuarios.csv", null, maxTiempo);
		
		assertEquals(listaEsperada, listaFinal, "La lista de IDs de canciones no es correcta");
	}
	static Stream<Arguments> casosDePrueba() {
        return Stream.of(
            Arguments.of("60:00", Arrays.asList(1, 6, 21, 36, 2, 7, 22, 37, 3, 8, 23, 38, 4, 9, 24, 39, 5, 10)),
            Arguments.of("90:00", Arrays.asList(1, 6, 21, 36, 2, 7, 22, 37, 3, 8, 23, 38, 4, 9, 24, 39, 5, 10, 25, 40, 11, 26, 41, 12, 27, 42, 13))
        );
    }
	
}
