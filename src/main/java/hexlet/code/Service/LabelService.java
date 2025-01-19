package hexlet.code.Service;

import hexlet.code.Exception.ResourceNotFoundException;
import hexlet.code.dto.label.CreateLabelDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.UpdateLabelDTO;
import hexlet.code.Mapper.LabelMapper;
import hexlet.code.Repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class LabelService {

    @Autowired
    private LabelRepository repository;

    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        return repository.findAll().stream().map(labelMapper::map).toList();
    }

    public LabelDTO getById(Long id) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.map(label);
    }

    public LabelDTO create(CreateLabelDTO labelData) {
        var label = labelMapper.map(labelData);
        repository.save(label);
        return labelMapper.map(label);
    }

    public LabelDTO update(UpdateLabelDTO labelData, Long id) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        labelMapper.update(labelData, label);
        repository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
