package techclallenge5.fiap.com.msGestaoPreco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techclallenge5.fiap.com.msGestaoPreco.dto.PrecoDto;
import techclallenge5.fiap.com.msGestaoPreco.exception.ItemNotFoundException;
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

        Optional<Preco> precoOptional = precoRepository.findByItemId(precoDto.itemId());
        Preco preco;

        if (precoOptional.isPresent()) {
            preco = precoOptional.get();
            atualizarPreco(preco, precoDto.precoNormal(), precoDto.precoPromocional(), precoDto.dataInicioPromocao(), precoDto.dataFimPromocao());
        } else {
            preco = criarNovoPreco(precoDto.itemId(), precoDto.precoNormal(), precoDto.precoPromocional(), precoDto.dataInicioPromocao(), precoDto.dataFimPromocao());
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

    private Preco criarNovoPreco(Long itemId, BigDecimal precoNormal, BigDecimal precoPromocional,
                                 LocalDate dataInicioPromocao, LocalDate dataFimPromocao) {
        Preco novoPreco = new Preco();
        novoPreco.setItemId(itemId);
        novoPreco.setPrecoNormal(precoNormal);
        novoPreco.setPrecoPromocional(precoPromocional);
        novoPreco.setDataInicioPromocao(dataInicioPromocao);
        novoPreco.setDataFimPromocao(dataFimPromocao);
        return novoPreco;
    }

    @Override
    public PrecoDto obterPrecoPorItemId(Long itemId) {
        Optional<Preco> precoOptional = precoRepository.findByItemId(itemId);
        if (precoOptional.isEmpty()) {
            throw new ItemNotFoundException();
        }
        return toPrecoDto(precoOptional.get());
    }

    @Override
    public List<PrecoDto> obterPrecos() {
        List<Preco> precos = precoRepository.findAll();
        return precos.stream()
                .sorted(Comparator.comparing(Preco::getItemId))
                .map(this::toPrecoDto)
                .toList();
    }

    @Override
    @Transactional
    public void excluirPreco(Long itemId) {
        if (!precoRepository.existsByItemId(itemId)) {
            throw new ItemNotFoundException("Preço não encontrado para o item com ID " + itemId);
        }
        precoRepository.deleteByItemId(itemId);
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
                preco.getItemId(),
                preco.getPrecoNormal(),
                preco.getPrecoPromocional(),
                preco.getDataInicioPromocao(),
                preco.getDataFimPromocao(),
                preco.getPrecoAtualUnitario()
        );
    }

}
