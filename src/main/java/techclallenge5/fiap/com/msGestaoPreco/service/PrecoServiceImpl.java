package techclallenge5.fiap.com.msGestaoPreco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techclallenge5.fiap.com.msGestaoPreco.dto.PrecoDto;
import techclallenge5.fiap.com.msGestaoPreco.exception.ProdutoNotFoundException;
import techclallenge5.fiap.com.msGestaoPreco.exception.PrecoInvalidoException;
import techclallenge5.fiap.com.msGestaoPreco.model.Preco;
import techclallenge5.fiap.com.msGestaoPreco.repository.PrecoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PrecoServiceImpl implements PrecoService {

    @Autowired
    private PrecoRepository precoRepository;

    @Override
    @Transactional
    public PrecoDto cadastrarOuAtualizarPreco(PrecoDto precoDto) {
        validarPreco(precoDto.precoNormal(), precoDto.precoPromocional(), precoDto.dataInicioPromocao(), precoDto.dataFimPromocao());

        Optional<Preco> precoOptional = precoRepository.findByProdutoId(precoDto.produtoId());
        Preco preco;

        if (precoOptional.isPresent()) {
            preco = precoOptional.get();
            atualizarPreco(preco, precoDto.precoNormal(), precoDto.precoPromocional(), precoDto.dataInicioPromocao(), precoDto.dataFimPromocao());
        } else {
            preco = criarNovoPreco(precoDto.produtoId(), precoDto.precoNormal(), precoDto.precoPromocional(), precoDto.dataInicioPromocao(), precoDto.dataFimPromocao());
        }

        Preco precoSalvo = precoRepository.save(preco);
        return toPrecoDto(precoSalvo);
    }

    private void atualizarPreco(Preco preco, BigDecimal precoNormal, BigDecimal precoPromocional,
                                LocalDate dataInicioPromocao, LocalDate dataFimPromocao) {
        preco.setPrecoNormal(precoNormal);
        preco.setPrecoPromocional(precoPromocional);
        preco.setDataInicioPromocao(dataInicioPromocao);
        preco.setDataFimPromocao(dataFimPromocao);
    }

    private Preco criarNovoPreco(Long produtoId, BigDecimal precoNormal, BigDecimal precoPromocional,
                                 LocalDate dataInicioPromocao, LocalDate dataFimPromocao) {
        Preco novoPreco = new Preco();
        novoPreco.setProdutoId(produtoId);
        novoPreco.setPrecoNormal(precoNormal);
        novoPreco.setPrecoPromocional(precoPromocional);
        novoPreco.setDataInicioPromocao(dataInicioPromocao);
        novoPreco.setDataFimPromocao(dataFimPromocao);
        return novoPreco;
    }

    @Override
    public PrecoDto obterPrecoPorProdutoId(Long produtoId) {
        Optional<Preco> precoOptional = precoRepository.findByProdutoId(produtoId);
        if (precoOptional.isEmpty()) {
            throw new ProdutoNotFoundException();
        }
        return toPrecoDto(precoOptional.get());
    }

    @Override
    public List<PrecoDto> obterPrecos() {
        List<Preco> precos = precoRepository.findAll();
        return precos.stream()
                .sorted(Comparator.comparing(Preco::getProdutoId))
                .map(this::toPrecoDto)
                .toList();
    }

    @Override
    @Transactional
    public void excluirPreco(Long produtoId) {
        if (!precoRepository.existsByProdutoId(produtoId)) {
            throw new ProdutoNotFoundException("Preço não encontrado para o produto com ID " + produtoId);
        }
        precoRepository.deleteByProdutoId(produtoId);
    }

    public void validarPreco(BigDecimal precoNormal, BigDecimal precoPromocional, LocalDate dataInicio, LocalDate dataFim) {
        if (precoNormal == null || precoNormal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PrecoInvalidoException("O preço normal deve ser maior que zero.");
        }
        if (precoPromocional != null && precoPromocional.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PrecoInvalidoException("O preço promocional deve ser maior que zero.");
        }
        if (precoPromocional != null && (dataInicio == null || dataFim == null)) {
            throw new PrecoInvalidoException("As datas de início e fim não podem ser nulas.");
        }
        if (precoPromocional != null && dataFim.isBefore(dataInicio)) {
            throw new PrecoInvalidoException("A data de fim deve ser depois da data de início.");
        }
    }

    public PrecoDto toPrecoDto(Preco preco) {
        return new PrecoDto(
                preco.getProdutoId(),
                preco.getPrecoNormal(),
                preco.getPrecoPromocional(),
                preco.getDataInicioPromocao(),
                preco.getDataFimPromocao(),
                preco.getPrecoAtualUnitario()
        );
    }

}
