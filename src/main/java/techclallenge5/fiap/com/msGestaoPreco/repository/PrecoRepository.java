package techclallenge5.fiap.com.msGestaoPreco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techclallenge5.fiap.com.msGestaoPreco.model.Preco;

import java.util.Optional;

@Repository
public interface PrecoRepository extends JpaRepository<Preco, Long> {

    Optional<Preco> findByItemId(Long itemId);
    boolean existsByItemId(Long itemId);
    void deleteByItemId(Long itemId);

}
