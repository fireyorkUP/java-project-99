package hexlet.code.app.Mapper;

import hexlet.code.app.dto.taskStatus.CreateTaskStatusDTO;
import hexlet.code.app.dto.taskStatus.TaskStatusDTO;
import hexlet.code.app.dto.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.app.Model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

public abstract class TaskStatusMapper {
    public abstract List<TaskStatusDTO> map(List<TaskStatus> taskStatuses);
    public abstract TaskStatus map(CreateTaskStatusDTO taskStatusCreateDTO);

    public abstract TaskStatusDTO map(TaskStatus taskStatus);

    public abstract void update(UpdateTaskStatusDTO taskStatusUpdateDTO, @MappingTarget TaskStatus taskStatus);
}
