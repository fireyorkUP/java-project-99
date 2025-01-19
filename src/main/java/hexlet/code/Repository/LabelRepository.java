package hexlet.code.Repository;

import hexlet.code.Model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {

    Optional<Label> findByName(String name);
}
