package hexlet.code.app.Service;

import hexlet.code.app.Exception.ResourceNotFoundException;
import hexlet.code.app.dto.taskStatus.CreateTaskStatusDTO;
import hexlet.code.app.dto.taskStatus.TaskStatusDTO;
import hexlet.code.app.dto.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.app.Mapper.TaskStatusMapper;
import hexlet.code.app.Repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusService {

    private TaskStatusRepository repository;
    private TaskStatusMapper statusMapper;

    public List<TaskStatusDTO> getAll() {
        var statuses = repository.findAll();
        return statusMapper.map(statuses);
    }

    public TaskStatusDTO getById(Long id) {
        var status = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));
        return statusMapper.map(status);
    }

    public TaskStatusDTO create(CreateTaskStatusDTO statusData) {
        var status = statusMapper.map(statusData);
        repository.save(status);
        return statusMapper.map(status);
    }

    public TaskStatusDTO update(UpdateTaskStatusDTO statusData, Long id) {
        var status = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));
        statusMapper.update(statusData, status);
        repository.save(status);
        return statusMapper.map(status);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
