package techclallenge5.fiap.com.msGestaoPreco.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import techclallenge5.fiap.com.msGestaoPreco.dto.PrecoDto;
import techclallenge5.fiap.com.msGestaoPreco.exception.ItemNotFoundException;
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

        when(precoRepository.findByItemId(precoDto.itemId())).thenReturn(Optional.empty());
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
        precoExistente.setItemId(precoDto.itemId());
        precoExistente.setPrecoNormal(new BigDecimal("110.00"));

        when(precoRepository.findByItemId(precoDto.itemId())).thenReturn(Optional.of(precoExistente));
        when(precoRepository.save(any(Preco.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        PrecoDto result = precoService.cadastrarOuAtualizarPreco(precoDto);

        assertEquals(precoDto, result);
        verify(precoRepository).save(any(Preco.class));
    }

    @Test
    void deveObterPrecoPorItemId() {
        Preco preco = new Preco();
        preco.setItemId(1L);
        preco.setPrecoNormal(new BigDecimal("100.00"));

        when(precoRepository.findByItemId(1L)).thenReturn(Optional.of(preco));

        PrecoDto result = precoService.obterPrecoPorItemId(1L);

        assertNotNull(result);
        assertEquals(1L, result.itemId());
        verify(precoRepository).findByItemId(1L);
    }

    @Test
    void deveObterPrecoPorItemIdNaoEncontrado() {
        when(precoRepository.findByItemId(1L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> precoService.obterPrecoPorItemId(1L));
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
    void deveObterPrecoPorItemIdEncontrado() {
        Preco preco = new Preco();
        preco.setItemId(1L);
        preco.setPrecoNormal(BigDecimal.valueOf(100));
        preco.setPrecoPromocional(BigDecimal.valueOf(80));
        preco.setDataInicioPromocao(LocalDate.now().minusDays(1));
        preco.setDataFimPromocao(LocalDate.now().plusDays(1));

        when(precoRepository.findByItemId(1L)).thenReturn(Optional.of(preco));

        PrecoDto resultado = precoService.obterPrecoPorItemId(1L);

        assertEquals(1L, resultado.itemId());
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
