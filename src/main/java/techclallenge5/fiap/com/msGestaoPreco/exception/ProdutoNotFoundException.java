package techclallenge5.fiap.com.msGestaoPreco.exception;

public class ProdutoNotFoundException extends RuntimeException {
    public ProdutoNotFoundException() {
        super("Não foi encontrado produto para aplicar o preço.");
    }

    public ProdutoNotFoundException(String mensagem) {
        super(mensagem);
    }
}
