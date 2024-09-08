package techclallenge5.fiap.com.msGestaoPreco.model;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
class PrecoTest {

    @Test
    void deveObterPrecoAtualUnitarioSemPromocao() {
        Preco preco = new Preco();
        preco.setPrecoNormal(new BigDecimal("100.00"));
        preco.setPrecoPromocional(null);
        preco.setDataInicioPromocao(null);
        preco.setDataFimPromocao(null);

        BigDecimal precoAtual = preco.getPrecoAtualUnitario();

        assertEquals(new BigDecimal("100.00"), precoAtual);
    }

    @Test
    void deveObterPrecoAtualUnitarioComPromocaoEmVigencia() {
        Preco preco = new Preco();
        preco.setPrecoNormal(new BigDecimal("100.00"));
        preco.setPrecoPromocional(new BigDecimal("80.00"));
        preco.setDataInicioPromocao(LocalDate.of(2024, 9, 1));
        preco.setDataFimPromocao(LocalDate.of(2024, 12, 31));

        BigDecimal precoAtual = preco.getPrecoAtualUnitario();

        assertEquals(new BigDecimal("80.00"), precoAtual);
    }

    @Test
    void deveObterPrecoAtualUnitarioComPromocaoForaDoPeriodo() {
        Preco preco = new Preco();
        preco.setPrecoNormal(new BigDecimal("100.00"));
        preco.setPrecoPromocional(new BigDecimal("80.00"));
        preco.setDataInicioPromocao(LocalDate.of(2024, 1, 1));
        preco.setDataFimPromocao(LocalDate.of(2024, 6, 30));

        BigDecimal precoAtual = preco.getPrecoAtualUnitario();

        assertEquals(new BigDecimal("100.00"), precoAtual);
    }

    @Test
    void deveObterPrecoAtualUnitarioSemPrecoPromocional() {
        Preco preco = new Preco();
        preco.setPrecoNormal(new BigDecimal("100.00"));
        preco.setPrecoPromocional(null);
        preco.setDataInicioPromocao(LocalDate.of(2024, 9, 1));
        preco.setDataFimPromocao(LocalDate.of(2024, 12, 31));

        BigDecimal precoAtual = preco.getPrecoAtualUnitario();

        assertEquals(new BigDecimal("100.00"), precoAtual);
    }

}
