package ListaReproduccion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cancion.ICancion;
import usuario.IUsuario;

class ListaReproduccionTest {

	@ParameterizedTest(name = "Duración máxima: {0}")
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
		
		assertEquals(listaEsperada, listaFinal);
	}
	static Stream<Arguments> casosDePrueba() {
        return Stream.of(
            Arguments.of("60:00", Arrays.asList(1, 6, 21, 36, 2, 7, 22, 37, 3, 8, 23, 38, 4, 9, 24, 39, 5)),
            Arguments.of("90:00", Arrays.asList(1, 6, 21, 36, 2, 7, 22, 37, 3, 8, 23, 38, 4, 9, 24, 39, 5, 10, 25, 40, 11, 26, 41, 12, 27, 42))
        );
    }
}
