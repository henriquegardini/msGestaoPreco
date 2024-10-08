package techclallenge5.fiap.com.msGestaoPreco.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public record PrecoDto(Long produtoId,
                       BigDecimal precoNormal,
                       BigDecimal precoPromocional,
                       LocalDate dataInicioPromocao,
                       LocalDate dataFimPromocao,
                       BigDecimal precoAtualUnitario) {

}
