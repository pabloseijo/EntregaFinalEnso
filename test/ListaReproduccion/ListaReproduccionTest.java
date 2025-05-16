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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
            Arguments.of("1:00", 60),
            Arguments.of("02:30", 150),
            Arguments.of("10:00", 600),
            Arguments.of("59:59", 3599),
            Arguments.of("90:00", 5400),
            Arguments.of("120:00", 7200)
        );
    }
    
    @ParameterizedTest(name = "Formato inválido: \"{0}\"")
    @MethodSource("casosInvalidos")
    @Tag("Sprint1")
    @DisplayName("Subprueba 1.1: Manejo de formatos inválidos")
    void testStringASegundos_Invalido(String input) {
        IListaReproduccion lista = new ListaReproduccion();
        assertThrows(Exception.class, () -> {
            lista.stringASegundos(input);
        }, "Se esperaba una excepción para la entrada inválida: " + input);
    }

    static Stream<String> casosInvalidos() {
        return Stream.of(
            "",              // vacío
            "abc",           // texto sin número
            "12",            // sin segundos
            "12:xx",         // segundos inválidos
            "12:60",         // segundos fuera de rango (aunque lo acepta como int)
            "12:30:45",      // más de dos partes
            null             // null pointer
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
    @DisplayName("Prueba 2: Duración total de una lista de canciones")
    void testDuracionMaxima(List<Integer> duraciones, int duracionEsperada) {
        IListaReproduccion lista = new ListaReproduccion();

        List<ICancion> canciones = new ArrayList<>();
        for (Integer dur : duraciones) {
            ICancion c = mock(ICancion.class);
            when(c.getDuracion()).thenReturn(dur);
            canciones.add(c);
        }
        
        int duracionTotal = lista.calcularDuracion(canciones); 

        assertEquals(duracionEsperada, duracionTotal, "La duración total calculada no coincide con la esperada");
    }
    static Stream<Arguments> casosDuracion() {
        return Stream.of(
            Arguments.of(Arrays.asList(1200, 1200, 1199), 3599),  		// 59:59
            Arguments.of(Arrays.asList(1800, 1800, 1800), 5400),        // 90:00
            Arguments.of(Arrays.asList(0, 0, 0), 0),        			// 00:00, canciones sin duración
            Arguments.of(Arrays.asList(), 0)                           	// Lista vacía
        );
    }
    
    @ParameterizedTest
    @MethodSource("casosDeIntercalado")
    @Tag("Sprint1")
    @DisplayName("Prueba 3: Intercalado correcto de canciones")
    void testIntercalado(Map<IUsuario, List<ICancion>> listasUsuarios, Set<IUsuario> usuariosEnCoche, List<Integer> listaEsperada) {
        
    	IListaReproduccion lista = new ListaReproduccion();
        List<ICancion> cancionesGeneradas = lista.intercalarCanciones(listasUsuarios, usuariosEnCoche);

        List<Integer> listaGenerada = cancionesGeneradas.stream()
                .map(ICancion::getIdCancion)
                .collect(Collectors.toList());

        assertEquals(listaEsperada, listaGenerada, "La lista generada no es la esperada");
    }

    static Stream<Arguments> casosDeIntercalado() {
        // Usuarios
        IUsuario u1 = mock(IUsuario.class);
        IUsuario u2 = mock(IUsuario.class);
        IUsuario u3 = mock(IUsuario.class);
        IUsuario u4 = mock(IUsuario.class);

        // Canciones
        ICancion c1 = mock(ICancion.class); when(c1.getIdCancion()).thenReturn(1); when(c1.getDuracion()).thenReturn(60); 
        ICancion c2 = mock(ICancion.class); when(c2.getIdCancion()).thenReturn(6); when(c2.getDuracion()).thenReturn(60); 
        ICancion c3 = mock(ICancion.class); when(c3.getIdCancion()).thenReturn(21); when(c3.getDuracion()).thenReturn(60);
        ICancion c4 = mock(ICancion.class); when(c4.getIdCancion()).thenReturn(36); when(c4.getDuracion()).thenReturn(60);
        ICancion c5 = mock(ICancion.class); when(c5.getIdCancion()).thenReturn(2);  when(c5.getDuracion()).thenReturn(60);
        ICancion c6 = mock(ICancion.class); when(c6.getIdCancion()).thenReturn(7);  when(c6.getDuracion()).thenReturn(60);
        ICancion c7 = mock(ICancion.class); when(c7.getIdCancion()).thenReturn(22); when(c7.getDuracion()).thenReturn(60);
        ICancion c8 = mock(ICancion.class); when(c8.getIdCancion()).thenReturn(37); when(c8.getDuracion()).thenReturn(60);

        return Stream.of(
                // 1. Usuarios con una canción cada uno
                Arguments.of(
                    new LinkedHashMap<IUsuario, List<ICancion>>() {{
                        put(u1, List.of(c1));
                        put(u2, List.of(c2));
                        put(u3, List.of(c3));
                    }},
                    new LinkedHashSet<>(List.of(u1, u2, u3)),                    
                    List.of(1, 6, 21)
                ),
                // 2. Usuarios con dos canciones cada uno
                Arguments.of(
                    new LinkedHashMap<IUsuario, List<ICancion>>() {{
                        put(u1, List.of(c1, c5));
                        put(u2, List.of(c2, c6));
                        put(u3, List.of(c3, c7));
                        put(u4, List.of(c4, c8));
                    }},
                    new LinkedHashSet<>(List.of(u1, u2, u3, u4)),
                    List.of(1, 6, 21, 36, 2, 7, 22, 37)
                ),
                // 3. Usuarios con diferentes numeros de canciones
                Arguments.of(
	                new LinkedHashMap<IUsuario, List<ICancion>>() {{
	                    put(u1, List.of(c1, c4, c7));
	                    put(u2, List.of(c2));
	                    put(u3, List.of(c3, c6, c8)); // Solo una canción
	                }},
	                new LinkedHashSet<>(List.of(u1, u2, u3)),
	                List.of(1, 6, 21, 36, 7, 22, 37)
                ),
                // 4. Usuarios sin canciones
                Arguments.of(
                    new LinkedHashMap<IUsuario, List<ICancion>>() {{
                        put(u1, List.of());
                        put(u2, List.of());
                        put(u3, List.of());
                    }},
                    new LinkedHashSet<>(List.of(u1, u2, u3)),
                    List.of()
                ),
                // 5. Usuarios con y sin canciones
                Arguments.of(
                    new LinkedHashMap<IUsuario, List<ICancion>>() {{
                        put(u1, List.of(c1));
                        put(u2, List.of());
                        put(u3, List.of(c3));
                    }},
                    new LinkedHashSet<>(List.of(u1, u2, u3)),
                    List.of(1, 21)
                ),
                // 6. Uno de los usuarios no está en el coche
                Arguments.of(
                    new LinkedHashMap<IUsuario, List<ICancion>>() {{
                        put(u1, List.of(c1, c5));
                        put(u2, List.of(c2, c6));
                        put(u3, List.of(c3, c7));
                    }},
                    new LinkedHashSet<>(List.of(u1, u2)), // u3 no está en el coche
                    List.of(1, 6, 2, 7)
                ),
                // 7. usuariosEnCoche == null (se deben usar todos)
                Arguments.of(
                    new LinkedHashMap<IUsuario, List<ICancion>>() {{
                        put(u1, List.of(c1));
                        put(u2, List.of(c2));
                    }},
                    null,
                    List.of(1, 6)
                ),
                // 8. Lista vacía de usuarios
                Arguments.of(
                    new LinkedHashMap<IUsuario, List<ICancion>>() {{
                        // vacío
                    }},
                    new LinkedHashSet<>(List.of()),
                    List.of()
                ),
                // 9. listasUsuarios == null
                Arguments.of(
                    null,
                    null,
                    List.of()
                )
            );
    }

    @ParameterizedTest
    @MethodSource("casosDeDuracion")
    @Tag("Sprint1")
    @DisplayName("Prueba 4: Reinicio del ciclo cuando se acaban las canciones")
    void testCicloDuracion(Map<IUsuario, List<ICancion>> listasUsuarios, int maxTiempo, int cancionesEscuchadas, int llamadaPorCancion) {

    	IListaReproduccion lista = new ListaReproduccion();
        
        Set<IUsuario> usuarios = listasUsuarios.keySet();

        // Se simula la lectura del csv manualmente aquí
        List<ICancion> listaFinal = new ArrayList<>();
        while (lista.calcularDuracion(listaFinal) < maxTiempo) {
            List<ICancion> intercalado = lista.intercalarCanciones(listasUsuarios, usuarios);
            for (ICancion c : intercalado) {
                listaFinal.add(c);
                if (lista.calcularDuracion(listaFinal) > maxTiempo)
                	break;
            }
        }
        
        // Esperamos dos vueltas completas: 3 canciones por vuelta
        assertEquals(listaFinal.size(), cancionesEscuchadas, "Se esperaban más vueltas completas en la lista de reproducción.");
        
        // Verificar que getDuracion() se llamó al menos dos veces por canción
        for (List<ICancion> canciones : listasUsuarios.values()) {
            for (ICancion cancion : canciones) {
                verify(cancion, atLeast(llamadaPorCancion)).getDuracion();
            }
        }
    }
    
    static Stream<Arguments> casosDeDuracion() {
        IUsuario u1 = mock(IUsuario.class);
        IUsuario u2 = mock(IUsuario.class);
        IUsuario u3 = mock(IUsuario.class);

        ICancion c1 = mock(ICancion.class); when(c1.getDuracion()).thenReturn(30); when(c1.getIdCancion()).thenReturn(1);
        ICancion c2 = mock(ICancion.class); when(c2.getDuracion()).thenReturn(30); when(c2.getIdCancion()).thenReturn(2);
        ICancion c3 = mock(ICancion.class); when(c3.getDuracion()).thenReturn(30); when(c3.getIdCancion()).thenReturn(3);

        return Stream.of(
            // Caso 1: duración total menor a tiempo máximo (no se reinician)
            Arguments.of(
                new LinkedHashMap<IUsuario, List<ICancion>>() {{
                    put(u1, List.of(c1));
                    put(u2, List.of(c2));
                    put(u3, List.of(c3));
                }},
                80, // maxTiempo
                3,   // canciones en la lista final (incluyendo repetidas)
                1    // llamadas a getDuracion() de cada cancion (no repite)
            ),

            // Caso 2: duración ligeramente mayor (una vuelta y algunas repeticiones)
            Arguments.of(
                new LinkedHashMap<IUsuario, List<ICancion>>() {{
                    put(u1, List.of(c1));
                    put(u2, List.of(c2));
                    put(u3, List.of(c3));
                }},
                180,
                6,
                2 
            ),

            // Caso 3: duración mucho mayor (varias repeticiones)
            Arguments.of(
                new LinkedHashMap<IUsuario, List<ICancion>>() {{
                    put(u1, List.of(c1));
                    put(u2, List.of(c2));
                    put(u3, List.of(c3));
                }},
                450,
                15,
                5 
            )
        );
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

    // ————————————————
	// Pruebas parametrizadas Sprint2
	// ————————————————
	
	@ParameterizedTest(name = "Filtrar: rango [{1}-{2}] seg → {3}")
	@MethodSource("casosFiltrarPorDuracion")
	@Tag("Sprint2")
	@DisplayName("Prueba FiltrarPorDuracion (parametrizada)")
	void testFiltrarPorDuracion_Param(
	        List<ICancion> input,
	        int minSeg,
	        int maxSeg,
	        List<ICancion> esperado) {
	    IListaReproduccion lista = new ListaReproduccion();
	    List<ICancion> actual = lista.filtrarPorDuracion(input, minSeg, maxSeg);
	    assertEquals(esperado, actual);
	}

	static Stream<Arguments> casosFiltrarPorDuracion() {
	    // mocks para reuse
	    ICancion a = mock(ICancion.class); when(a.getDuracion()).thenReturn( 50);
	    ICancion b = mock(ICancion.class); when(b.getDuracion()).thenReturn( 90);
	    ICancion c = mock(ICancion.class); when(c.getDuracion()).thenReturn(150);
	    ICancion d = mock(ICancion.class); when(d.getDuracion()).thenReturn(180);
	    ICancion e = mock(ICancion.class); when(e.getDuracion()).thenReturn(200);

	    List<ICancion> listaBase = Arrays.asList(a, b, c, d, e);

	    return Stream.of(
	        // caso null
	        Arguments.of(null,  0, 100, List.of()),
	        // todo fuera de rango
	        Arguments.of(listaBase, 300, 400, List.of()),
	        // algunos dentro: [minSeg, maxSeg]
	        Arguments.of(listaBase,  90, 180, Arrays.asList(b, c, d)),
	        // incluir exactos en los límites
	        Arguments.of(listaBase,  50, 200, Arrays.asList(a, b, c, d, e))
	    );
	}


	@ParameterizedTest(name = "Ordenar {1} sobre {0} → {2}")
	@MethodSource("casosOrdenarPorDuracion")
	@Tag("Sprint2")
	@DisplayName("Prueba ordenarPorDuracion (parametrizada)")
	void testOrdenarPorDuracion_Param(
	        List<ICancion> input,
	        String orden,
	        List<ICancion> esperado) {
	    IListaReproduccion lista = new ListaReproduccion();
	    List<ICancion> actual = lista.ordenarPorDuracion(input, orden);
	    assertEquals(esperado, actual);
	}

	static Stream<Arguments> casosOrdenarPorDuracion() {
	    // mocks de nuevo
	    ICancion a = mock(ICancion.class); when(a.getDuracion()).thenReturn(100);
	    ICancion b = mock(ICancion.class); when(b.getDuracion()).thenReturn(200);
	    ICancion c = mock(ICancion.class); when(c.getDuracion()).thenReturn(300);

	    List<ICancion> base = Arrays.asList(a, b, c);
	    List<ICancion> inversa = Arrays.asList(c, b, a);
	    List<ICancion> desorden = Arrays.asList(b, c, a);

	    return Stream.of(
	        // null → vacío
	        Arguments.of(null,       "asc",  List.of()),
	        // ascendente
	        Arguments.of(desorden,   "asc",  base),
	        // descendente
	        Arguments.of(desorden,   "desc", inversa),
	        // orden no reconocido → misma lista
	        Arguments.of(desorden,   "foo",  desorden)
	    );
	}
	
    @Test
    @Tag("Sprint2")
    @DisplayName("Integración: CSV → filtrarPorDuracion → ordenarPorDuracion (asc)")
    void testIntegracion_FiltrarYOrdenarAsc() {
        IListaReproduccion lista = new ListaReproduccion();

        // 1) Cargo el CSV real
        Map<IUsuario, List<ICancion>> porUsuario =
            lista.generarListasPorUsuario("src/resources/discos_usuarios.csv");

        // 2) Aplano en una única lista
        List<ICancion> todas = porUsuario.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        // 3) Filtro entre 170s (2:50) y 180s (3:00)
        int minSeg = 170, maxSeg = 180;
        List<ICancion> filtradas = lista.filtrarPorDuracion(todas, minSeg, maxSeg);

        // Compruebo que todas las duraciones estén en el rango
        assertFalse(filtradas.isEmpty(), "Debe haber al menos una canción en ese rango");
        for (ICancion c : filtradas) {
            int d = c.getDuracion();
            assertTrue(d >= minSeg && d <= maxSeg,
                "Duración fuera de rango: " + d + "s");
        }

        // 4) Ordeno ascendente
        List<ICancion> asc = lista.ordenarPorDuracion(filtradas, "asc");

        // Verifico que quede estrictamente no decreciente
        for (int i = 0; i < asc.size() - 1; i++) {
            int d1 = asc.get(i).getDuracion();
            int d2 = asc.get(i + 1).getDuracion();
            assertTrue(d1 <= d2,
                String.format("No está ascendente: %ds > %ds en %d→%d", d1, d2, i, i + 1));
        }
    }

    @Test
    @Tag("Sprint2")
    @DisplayName("Integración: CSV → ordenarPorDuracion (desc)")
    void testIntegracion_SoloOrdenarDesc() {
        IListaReproduccion lista = new ListaReproduccion();

        // Cargo y aplano
        Map<IUsuario, List<ICancion>> porUsuario =
            lista.generarListasPorUsuario("src/resources/discos_usuarios.csv");
        List<ICancion> todas = porUsuario.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        // Ordeno descendente
        List<ICancion> desc = lista.ordenarPorDuracion(todas, "desc");

        // Verifico que quede no creciente
        for (int i = 0; i < desc.size() - 1; i++) {
            int d1 = desc.get(i).getDuracion();
            int d2 = desc.get(i + 1).getDuracion();
            assertTrue(d1 >= d2,
                String.format("No está descendente: %ds < %ds en %d→%d", d1, d2, i, i + 1));
        }
    }

	
}
