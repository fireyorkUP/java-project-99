package hexlet.code.app.Service;

import hexlet.code.app.Exception.ResourceNotFoundException;
import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskParamsDTO;
import hexlet.code.app.dto.task.UpdateTaskDTO;
import hexlet.code.app.Mapper.TaskMapper;
import hexlet.code.app.Repository.TaskRepository;
import hexlet.code.app.Component.TaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskRepository repository;
    private TaskMapper taskMapper;
    private TaskSpecification taskSpecification;

    public List<TaskDTO> getAll(TaskParamsDTO params) {
        var spec = taskSpecification.build(params);
        var tasks = repository.findAll(spec);
        return tasks.stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO getById(Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO taskData) {
        var task = taskMapper.map(taskData);
        repository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO update(UpdateTaskDTO taskData, Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskMapper.update(taskData, task);
        repository.save(task);
        return taskMapper.map(task);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
