package album;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import album.Album;
import autor.IAutor;
import cancion.ICancion;

@Tag("Sprint1")
@DisplayName("Prueba 6: Agregar canción al álbum")
class AlbumTest {

    @ParameterizedTest(name = "Subprueba 6.1.{index}: canción = {0}")
    @MethodSource("casosEntradasInvalidas")
    @DisplayName("Subprueba 6.1: Manejo de entradas inválidas")
    void subprueba_6_1_entradasInvalidas(ICancion cancion, String mensajeEsperado) {
        IAutor autor = mock(IAutor.class);
        Album album = new Album(1, "NombreEjemplo", autor);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> album.agregarCancion(cancion)
        );
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    static Stream<Arguments> casosEntradasInvalidas() {
        IAutor autor = mock(IAutor.class);
        Album album = new Album(1, "NombreEjemplo", autor);
        Album otroAlbum = new Album(2, "OtroAlbum", autor);

        ICancion cancionNull = null;

        ICancion cancionAjena = mock(ICancion.class);
        when(cancionAjena.getAlbum()).thenReturn(otroAlbum);

        return Stream.of(
            Arguments.of(cancionNull,      "La canción no puede ser nula."),
            Arguments.of(cancionAjena,     "La canción no pertenece a este álbum.")
        );
    }

    @ParameterizedTest(name = "Subprueba 6.2.{index}: distintos={0}, duplicados={1} → total={2}")
    @MethodSource("casosRegistroValido")
    @DisplayName("Subprueba 6.2: Agregar canciones válidas")
    void subprueba_6_2_registroValido(int distintos, int duplicados, int esperado) {
        IAutor autor = mock(IAutor.class);
        Album album = new Album(1, "NombreEjemplo", autor);

        List<ICancion> canciones =
            IntStream.range(0, distintos)
                     .mapToObj(i -> {
                         ICancion c = mock(ICancion.class);
                         when(c.getAlbum()).thenReturn(album);
                         return c;
                     })
                     .collect(Collectors.toList());

        // Agregar canciones distintas
        canciones.forEach(album::agregarCancion);

        // Agregar duplicados del primer elemento
        for (int i = 0; i < duplicados; i++) {
            album.agregarCancion(canciones.get(0));
        }

        assertEquals(esperado, album.getCanciones().size(),
            "El número total de canciones en el álbum no coincide con el esperado");
    }

    static Stream<Arguments> casosRegistroValido() {
        return Stream.of(
            Arguments.of(1, 0, 1),   // 1 distinta, 0 duplicados → total 1
            Arguments.of(2, 0, 2),   // 2 distintas, 0 duplicados → total 2
            Arguments.of(1, 2, 3)    // 1 distinta, 2 duplicados → total 3
        );
    }
}
