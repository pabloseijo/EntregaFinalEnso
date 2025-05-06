package ListaReproduccion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import cancion.ICancion;
import usuario.IUsuario;

class ListaReproduccionTest {

	/*@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}*/

	@ParameterizedTest(name = "Duración máxima: {0}")
    //@ValueSource(strings = { "60:00", "90:00" })
    @MethodSource("casosDePrueba")
    @Tag("Sprint1")
    @DisplayName("Canciones escuchadas en diferentes duraciones")
    void testGenerarListaReproduccionIntercalada(String tiempoMaximoStr, List<Integer> listaEsperada) {	
		
		// Obtenemos las canciones de cada usuario
		IListaReproduccion lista = new ListaReproduccion();
		Map<IUsuario, List<ICancion>> listasUsuarios = lista.generarListasPorUsuario("src/resources/discos_usuarios.csv");
		
		List<IUsuario> usuariosEnCoche = new ArrayList<>();
		usuariosEnCoche.addAll(listasUsuarios.keySet());
		int maxTiempo = lista.stringASegundos(tiempoMaximoStr);
		List<Integer> listaFinal = lista.generarListaReproduccionIntercalada(listasUsuarios, usuariosEnCoche, maxTiempo);
		
		/*System.out.println("IDs de canciones en "+ tiempoMaximoStr +":");
		for(int id : listaFinal) {
			System.out.println("\tID: " + id);
		}*/
		assertEquals(listaEsperada, listaFinal);
	}
	static Stream<Arguments> casosDePrueba() {
        return Stream.of(
            Arguments.of("60:00", Arrays.asList(1, 6, 21, 36, 2, 7, 22, 37, 3, 8, 23, 38, 4, 9, 24, 39)),
            Arguments.of("90:00", Arrays.asList(1, 6, 21, 36, 2, 7, 22, 37, 3, 8, 23, 38, 4, 9, 24, 39, 5, 10, 25, 40, 11, 26, 41))
        );
    }
}
