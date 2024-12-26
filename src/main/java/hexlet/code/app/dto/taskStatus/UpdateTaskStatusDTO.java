package hexlet.code.app.dto.taskStatus;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UpdateTaskStatusDTO {
    @NotBlank
    @Size(min = 1)
    private JsonNullable<String> name;
    @NotBlank
    @Column(unique = true)
    private JsonNullable<String> slug;
}
