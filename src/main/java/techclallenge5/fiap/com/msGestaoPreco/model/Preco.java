package techclallenge5.fiap.com.msGestaoPreco.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;


import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRECO")
public class Preco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long precoId;
    private BigDecimal precoNormal;
    private BigDecimal precoPromocional;
    private LocalDate dataInicioPromocao;
    private LocalDate dataFimPromocao;
    private Long produtoId;

    public BigDecimal getPrecoAtualUnitario() {
        LocalDate hoje = LocalDate.now();
        if (precoPromocional != null &&
                dataInicioPromocao != null &&
                dataFimPromocao != null &&
                hoje.isAfter(dataInicioPromocao.minusDays(1)) &&
                hoje.isBefore(dataFimPromocao.plusDays(1))) {
            return precoPromocional;
        }
        return precoNormal;
    }

}
