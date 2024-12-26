package hexlet.code.app.Repository;

import hexlet.code.app.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Optional<Task> findByAssigneeId(Long id);
    Optional<Task> findByName(String name);
}
