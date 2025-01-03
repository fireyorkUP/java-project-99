package hexlet.code.app.Controller;

import hexlet.code.app.dto.label.CreateLabelDTO;
import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.dto.label.UpdateLabelDTO;
import hexlet.code.app.Service.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LabelsController {

    private final LabelService labelService;

    @GetMapping(path = "/labels")
    public ResponseEntity<List<LabelDTO>> index() {
        var labels = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @GetMapping(path = "/labels/{id}")
    public LabelDTO show(@PathVariable Long id) {
        return labelService.getById(id);
    }

    @PostMapping(path = "/labels")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody CreateLabelDTO dataDTO) {
        return labelService.create(dataDTO);
    }

    @PutMapping(path = "/labels/{id}")
    public LabelDTO update(@Valid @RequestBody UpdateLabelDTO dataDTO, @PathVariable Long id) {
        return labelService.update(dataDTO, id);
    }

    @DeleteMapping(path = "/labels/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        labelService.delete(id);
    }
}
