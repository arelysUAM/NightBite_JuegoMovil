package ni.edu.uam.nightbiteapi.repositories;

import ni.edu.uam.nightbiteapi.model.PlayerProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerProgressRepository extends JpaRepository<PlayerProgress, Long> {

    Optional<PlayerProgress> findByUserAccountId(Long userAccountId);

    boolean existsByUserAccountId(Long userAccountId);

    void deleteByUserAccountId(Long userAccountId);
}