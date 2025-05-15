package Autor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import album.IAlbum;
import autor.Autor;

@Tag("Sprint1")
@DisplayName("Prueba 4: Registro de nuevos autores")
class AutorTest {

    @Test
    @DisplayName("Subprueba 4.1: Manejo de entradas inválidas")
    void subprueba_4_1_entradasInvalidas() {
        Autor autor = new Autor(1, "Autor Ejemplo");

        // Álbum nulo
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> autor.registrarAlbum(null));
        assertEquals("El álbum no puede ser nulo.", ex1.getMessage());

        // Álbum con autor distinto
        Autor autorDistinto = new Autor(2, "Otro Autor");
        IAlbum albumAjeno = mock(IAlbum.class);
        when(albumAjeno.getAutor()).thenReturn(autorDistinto);
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> autor.registrarAlbum(albumAjeno));
        assertEquals("El álbum no pertenece a este autor.", ex2.getMessage());
    }

    @Test
    @DisplayName("Subprueba 4.2: Registro de álbumes válidos (único, múltiples y duplicados)")
    void subprueba_4_2_registroValido() {
        Autor autor = new Autor(1, "Autor Ejemplo");
        IAlbum albumA = mock(IAlbum.class);
        IAlbum albumB = mock(IAlbum.class);
        when(albumA.getAutor()).thenReturn(autor);
        when(albumB.getAutor()).thenReturn(autor);

        // Registro único
        autor.registrarAlbum(albumA);
        List<IAlbum> lista1 = autor.getAlbumes();
        assertEquals(1, lista1.size(), "Debe contener exactamente un álbum");
        assertSame(albumA, lista1.get(0), "El álbum registrado debe ser albumA");

        // Registro múltiple
        autor.registrarAlbum(albumB);
        List<IAlbum> lista2 = autor.getAlbumes();
        assertEquals(2, lista2.size(), "Debe contener dos álbumes");
        assertSame(albumA, lista2.get(0), "El primer álbum debe ser albumA");
        assertSame(albumB, lista2.get(1), "El segundo álbum debe ser albumB");

        // Registro duplicado
        autor.registrarAlbum(albumA);
        List<IAlbum> lista3 = autor.getAlbumes();
        assertEquals(3, lista3.size(), "Debe permitir duplicados");
        assertSame(albumA, lista3.get(2), "El tercer elemento debe ser el duplicado de albumA");
    }
}
