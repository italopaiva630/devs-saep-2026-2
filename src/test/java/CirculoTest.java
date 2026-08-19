import org.example.Circulo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CirculoTest {

    @Test
    void numeroDeRaioDeveSerMaiorQueZero() {

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> new Circulo(0)
        );

        assertEquals(
                "O valor do raio deve ser maior que zero.",
                excecao.getMessage()
        );
    }

    @Test
    void verificarCalculoDaAreaDoCirculo() {

        Circulo circulo = new Circulo(15);

        double calculo = circulo.calcularArea();

        assertEquals(
                Math.PI * Math.pow(15, 2),
                calculo,
                0.0001
        );
    }

    @Test
    void verificandoNumeroDoRaioCerto() {

        Circulo circulo = new Circulo(10);

        assertFalse(circulo.raio < 0);
    }

    @Test
    void deveInicializarCirculoComRaioValidoEPositivo() {

        Circulo circulo = new Circulo(10);

        assertAll(
                () -> assertEquals(10, circulo.raio),
                () -> assertTrue(circulo.raio > 0),
                () -> assertFalse(circulo.raio < 0)
        );
    }
}