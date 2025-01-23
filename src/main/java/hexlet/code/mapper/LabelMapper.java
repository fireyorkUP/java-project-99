package hexlet.code.mapper;

import hexlet.code.dto.label.CreateLabelDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.UpdateLabelDTO;
import hexlet.code.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {

    public abstract List<LabelDTO> map(List<Label> labels);

    public abstract Label map(CreateLabelDTO labelCreateDTO);

    public abstract LabelDTO map(Label label);

    public abstract void update(UpdateLabelDTO labelUpdateDTO, @MappingTarget Label label);

    public abstract Label map(LabelDTO labelDTO);
}
