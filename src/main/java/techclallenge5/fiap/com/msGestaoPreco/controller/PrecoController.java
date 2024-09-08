package techclallenge5.fiap.com.msGestaoPreco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techclallenge5.fiap.com.msGestaoPreco.dto.PrecoDto;
import techclallenge5.fiap.com.msGestaoPreco.service.PrecoService;

import java.util.List;

@RestController
@RequestMapping("/precos")
public class PrecoController {

    private final PrecoService precoService;

    public PrecoController(PrecoService precoService) {
        this.precoService = precoService;
    }

    @PostMapping("/precificar")
    public ResponseEntity<PrecoDto> cadastrarOuAtualizarPreco(@RequestBody PrecoDto precoDto) {
        PrecoDto preco = precoService.cadastrarOuAtualizarPreco(precoDto);
        return ResponseEntity.ok(preco);
    }

    @GetMapping("/obterPreco/{produtoId}")
    public ResponseEntity<PrecoDto> obterPrecoPorProdutoId(@PathVariable Long produtoId) {
        PrecoDto preco = precoService.obterPrecoPorProdutoId(produtoId);
        return ResponseEntity.ok(preco);
    }

    @GetMapping("/obterPrecos")
    public ResponseEntity<List<PrecoDto>> obterPrecos() {
        List<PrecoDto> precoDtos = precoService.obterPrecos();
        return ResponseEntity.ok(precoDtos);
    }

    @DeleteMapping("/excluirPreco/{produtoId}")
    public ResponseEntity<String> excluirPreco(@PathVariable Long produtoId) {
        precoService.excluirPreco(produtoId);
        return ResponseEntity.ok(String.format("Preço excluído com sucesso para o produto com ID %d", produtoId));
    }

}
