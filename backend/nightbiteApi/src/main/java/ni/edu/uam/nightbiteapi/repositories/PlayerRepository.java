package ni.edu.uam.nightbiteapi.repositories;

import ni.edu.uam.nightbiteapi.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio encargado de gestionar las operaciones de base de datos
 * relacionadas con la entidad Player.
 *
 * Al extender JpaRepository, Spring Data JPA proporciona automáticamente
 * métodos como findAll, findById, save, deleteById y existsById.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}