package Autor;

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

import album.IAlbum;
import autor.Autor;

@Tag("Sprint1")
@DisplayName("Prueba 4: Registro de nuevos autores")
class AutorTest {

    @ParameterizedTest(name = "Subprueba 4.1.{index}: álbum = {0}")
    @MethodSource("casosEntradasInvalidas")
    @DisplayName("Subprueba 4.1: Manejo de entradas inválidas")
    void subprueba_4_1_entradasInvalidas(IAlbum album, String mensajeEsperado) {
        Autor autor = new Autor(1, "Autor Ejemplo");
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> autor.registrarAlbum(album)
        );
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    static Stream<Arguments> casosEntradasInvalidas() {
        // Álbum ajeno a este autor
        Autor otroAutor = new Autor(2, "Autor Distinto");
        IAlbum albumAjeno = mock(IAlbum.class);
        when(albumAjeno.getAutor()).thenReturn(otroAutor);

        return Stream.of(
            Arguments.of(null, "El álbum no puede ser nulo."),
            Arguments.of(albumAjeno, "El álbum no pertenece a este autor.")
        );
    }

    @ParameterizedTest(name = "Subprueba 4.2.{index}: distintos={0}, duplicados={1} → total={2}")
    @MethodSource("casosRegistroValido")
    @DisplayName("Subprueba 4.2: Registro de álbumes válidos")
    void subprueba_4_2_registroValido(int distintos, int duplicados, int esperado) {
        Autor autor = new Autor(1, "Autor Ejemplo");

        // Crear 'distintos' mocks que devuelven este autor
        List<IAlbum> discos =
            IntStream.range(0, distintos)
                     .mapToObj(i -> {
                         IAlbum a = mock(IAlbum.class);
                         when(a.getAutor()).thenReturn(autor);
                         return a;
                     })
                     .collect(Collectors.toList());

        // Registrar cada uno
        discos.forEach(autor::registrarAlbum);

        // Registrar duplicados del primer álbum
        for (int i = 0; i < duplicados; i++) {
            autor.registrarAlbum(discos.get(0));
        }

        // Verificar tamaño final
        assertEquals(esperado, autor.getAlbumes().size());
    }

    static Stream<Arguments> casosRegistroValido() {
        return Stream.of(
            // 1 álbum distinto, 0 duplicados → total 1
            Arguments.of(1, 0, 1),
            // 2 álbumes distintos, 0 duplicados → total 2
            Arguments.of(2, 0, 2),
            // 1 álbum distinto, 2 duplicados  → total 3
            Arguments.of(1, 2, 3)
        );
    }
}
