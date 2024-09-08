package techclallenge5.fiap.com.msGestaoPreco.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techclallenge5.fiap.com.msGestaoPreco.dto.PrecoDto;
import techclallenge5.fiap.com.msGestaoPreco.exception.ProdutoNotFoundException;
import techclallenge5.fiap.com.msGestaoPreco.exception.PrecoInvalidoException;
import techclallenge5.fiap.com.msGestaoPreco.model.Preco;
import techclallenge5.fiap.com.msGestaoPreco.repository.PrecoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrecoServiceImplTest {

    @InjectMocks
    private PrecoServiceImpl precoService;

    @Mock
    private PrecoRepository precoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarPrecoNovo() {
        PrecoDto precoDto = new PrecoDto(
                1L,
                new BigDecimal("100.00"),
                new BigDecimal("90.00"),
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2024, 12, 31),
                new BigDecimal("90.00")
        );

        when(precoRepository.findByProdutoId(precoDto.produtoId())).thenReturn(Optional.empty());
        when(precoRepository.save(any(Preco.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        PrecoDto result = precoService.cadastrarOuAtualizarPreco(precoDto);

        assertEquals(precoDto, result);
        verify(precoRepository).save(any(Preco.class));
    }

    @Test
    void deveAtualizarPrecoExistente() {
        PrecoDto precoDto = new PrecoDto(
                1L,
                new BigDecimal("100.00"),
                new BigDecimal("90.00"),
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2024, 12, 31),
                new BigDecimal("90.00")
        );

        Preco precoExistente = new Preco();
        precoExistente.setProdutoId(precoDto.produtoId());
        precoExistente.setPrecoNormal(new BigDecimal("110.00"));

        when(precoRepository.findByProdutoId(precoDto.produtoId())).thenReturn(Optional.of(precoExistente));
        when(precoRepository.save(any(Preco.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        PrecoDto result = precoService.cadastrarOuAtualizarPreco(precoDto);

        assertEquals(precoDto, result);
        verify(precoRepository).save(any(Preco.class));
    }

    @Test
    void deveObterPrecoPorProdutoId() {
        Preco preco = new Preco();
        preco.setProdutoId(1L);
        preco.setPrecoNormal(new BigDecimal("100.00"));

        when(precoRepository.findByProdutoId(1L)).thenReturn(Optional.of(preco));

        PrecoDto result = precoService.obterPrecoPorProdutoId(1L);

        assertNotNull(result);
        assertEquals(1L, result.produtoId());
        verify(precoRepository).findByProdutoId(1L);
    }

    @Test
    void deveObterPrecoPorProdutoIdNaoEncontrado() {
        when(precoRepository.findByProdutoId(1L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNotFoundException.class, () -> precoService.obterPrecoPorProdutoId(1L));
    }

    @Test
    void deveValidarPrecoInvalido() {
        assertThrows(PrecoInvalidoException.class, () -> precoService.cadastrarOuAtualizarPreco(
                new PrecoDto(
                        1L,
                        new BigDecimal("0.00"),
                        null,
                        null,
                        null,
                        null
                )
        ));
    }

    @Test
    void deveObterPrecoPorProdutoIdEncontrado() {
        Preco preco = new Preco();
        preco.setProdutoId(1L);
        preco.setPrecoNormal(BigDecimal.valueOf(100));
        preco.setPrecoPromocional(BigDecimal.valueOf(80));
        preco.setDataInicioPromocao(LocalDate.now().minusDays(1));
        preco.setDataFimPromocao(LocalDate.now().plusDays(1));

        when(precoRepository.findByProdutoId(1L)).thenReturn(Optional.of(preco));

        PrecoDto resultado = precoService.obterPrecoPorProdutoId(1L);

        assertEquals(1L, resultado.produtoId());
        assertEquals(BigDecimal.valueOf(100), resultado.precoNormal());
        assertEquals(BigDecimal.valueOf(80), resultado.precoPromocional());
        assertEquals(LocalDate.now().minusDays(1), resultado.dataInicioPromocao());
        assertEquals(LocalDate.now().plusDays(1), resultado.dataFimPromocao());
    }

    @Test
    void deveLacarExceptionPrecoNormalInvalido() {
        assertThrows(PrecoInvalidoException.class, () -> precoService.validarPreco(BigDecimal.ZERO, BigDecimal.valueOf(80), LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)));
    }

    @Test
    void deveLacarExceptionPrecoPromocionalInvalido() {
        assertThrows(PrecoInvalidoException.class, () -> precoService.validarPreco(BigDecimal.valueOf(100), BigDecimal.ZERO, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)));
    }

    @Test
    void deveLacarExceptionDatasNulas() {
        assertThrows(PrecoInvalidoException.class, () -> precoService.validarPreco(BigDecimal.valueOf(100), BigDecimal.valueOf(80), null, LocalDate.now().plusDays(1)));
    }

    @Test
    void deveLacarExceptionDataFimAntesDataInicio() {
        assertThrows(PrecoInvalidoException.class, () -> precoService.validarPreco(BigDecimal.valueOf(100), BigDecimal.valueOf(80), LocalDate.now().plusDays(1), LocalDate.now().minusDays(1)));
    }
}
