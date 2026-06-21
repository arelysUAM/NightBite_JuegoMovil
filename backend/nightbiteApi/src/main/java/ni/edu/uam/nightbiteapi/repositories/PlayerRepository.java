package ni.edu.uam.nightbiteapi.repositories;

import ni.edu.uam.nightbiteapi.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByUserAccountId(Long userAccountId);

    boolean existsByUserAccountId(Long userAccountId);
}