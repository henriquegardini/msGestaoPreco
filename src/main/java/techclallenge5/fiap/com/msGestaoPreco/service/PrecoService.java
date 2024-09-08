package techclallenge5.fiap.com.msGestaoPreco.service;

import techclallenge5.fiap.com.msGestaoPreco.dto.PrecoDto;

import java.util.List;

public interface PrecoService {

    PrecoDto cadastrarOuAtualizarPreco(PrecoDto precoDto);

    PrecoDto obterPrecoPorItemId(Long itemId);

    List<PrecoDto> obterPrecos();

    void excluirPreco(Long itemId);

}
