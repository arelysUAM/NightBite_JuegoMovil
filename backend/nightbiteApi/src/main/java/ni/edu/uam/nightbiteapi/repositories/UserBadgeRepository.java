package ni.edu.uam.nightbiteapi.repositories;

import ni.edu.uam.nightbiteapi.model.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findByUserAccountIdOrderByLevelIdAsc(Long userAccountId);

    Optional<UserBadge> findByUserAccountIdAndLevelId(
            Long userAccountId,
            Integer levelId
    );

    boolean existsByUserAccountIdAndLevelId(
            Long userAccountId,
            Integer levelId
    );
}