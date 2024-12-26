package hexlet.code.app.Service;

import hexlet.code.app.dto.label.CreateLabelDTO;
import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.dto.label.UpdateLabelDTO;
import hexlet.code.app.Mapper.LabelMapper;
import hexlet.code.app.Repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class LabelService {

    private LabelRepository repository;
    private LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        var labels = repository.findAll();
        return labels.stream()
                .map(labelMapper::map)
                .toList();
    }

    public LabelDTO getById(Long id) {
        var label = repository.findById(id)
                .orElseThrow();
        return labelMapper.map(label);
    }

    public LabelDTO create(CreateLabelDTO labelData) {
        var label = labelMapper.map(labelData);
        repository.save(label);
        return labelMapper.map(label);
    }

    public LabelDTO update(UpdateLabelDTO labelData, Long id) {
        var label = repository.findById(id)
                .orElseThrow();
        labelMapper.update(labelData, label);
        repository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
