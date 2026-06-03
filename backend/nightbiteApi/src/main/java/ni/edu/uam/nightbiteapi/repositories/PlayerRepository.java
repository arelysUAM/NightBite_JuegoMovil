package ni.edu.uam.nightbiteapi.repositories;

import ni.edu.uam.nightbiteapi.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio encargado de gestionar las operaciones de base de datos
 * relacionadas con la ficha/personaje del repartidor.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByUserAccountId(Long userAccountId);

    boolean existsByUserAccountId(Long userAccountId);

    boolean existsByNickname(String nickname);
}