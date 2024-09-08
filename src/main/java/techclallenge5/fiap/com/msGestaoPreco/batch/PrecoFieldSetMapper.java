package techclallenge5.fiap.com.msGestaoPreco.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import techclallenge5.fiap.com.msGestaoPreco.model.Preco;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PrecoFieldSetMapper implements FieldSetMapper<Preco> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Preco mapFieldSet(FieldSet fieldSet) throws BindException {
        Preco preco = new Preco();

        preco.setPrecoId(fieldSet.readLong("preco_id"));
        preco.setPrecoNormal(fieldSet.readBigDecimal("preco_normal"));

        String precoPromocionalStr = fieldSet.readString("preco_promocional");
        preco.setPrecoPromocional(precoPromocionalStr != null && !precoPromocionalStr.isEmpty()
                ? new BigDecimal(precoPromocionalStr)
                : null);

        String dataInicioStr = fieldSet.readString("data_inicio_promocao");
        preco.setDataInicioPromocao(dataInicioStr != null && !dataInicioStr.isEmpty()
                ? LocalDate.parse(dataInicioStr, DATE_FORMATTER)
                : null);

        String dataFimStr = fieldSet.readString("data_fim_promocao");
        preco.setDataFimPromocao(dataFimStr != null && !dataFimStr.isEmpty()
                ? LocalDate.parse(dataFimStr, DATE_FORMATTER)
                : null);

        preco.setItemId(fieldSet.readLong("item_id"));
        return preco;
    }
}
