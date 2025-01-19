package hexlet.code.Service;

import hexlet.code.Exception.ResourceNotFoundException;
import hexlet.code.dto.taskStatus.CreateTaskStatusDTO;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.dto.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.Mapper.TaskStatusMapper;
import hexlet.code.Repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository repository;

    @Autowired
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
