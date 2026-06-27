package ni.edu.uam.nightbiteapi.repositories;

import ni.edu.uam.nightbiteapi.model.LevelResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LevelResultRepository extends JpaRepository<LevelResult, Long> {

    List<LevelResult> findByUserAccountIdOrderByLevelIdAsc(Long userAccountId);

    Optional<LevelResult> findByUserAccountIdAndLevelId(
            Long userAccountId,
            Integer levelId
    );

    void deleteByUserAccountId(Long userAccountId);
}