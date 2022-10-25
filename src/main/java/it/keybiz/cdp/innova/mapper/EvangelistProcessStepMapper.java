package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.domain.EvangelistProcessStep;
import it.keybiz.cdp.innova.dto.DragDropSortDTO;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepDTO;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepEditDTO;
import it.keybiz.cdp.innova.service.FileStorageService;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.List;

@Mapper
public abstract class EvangelistProcessStepMapper {
    @Autowired
    private FileStorageService fileStorageService;

    public abstract EvangelistProcessStepDTO processToDTO(EvangelistProcessStep evangelistProcessStep);

    @Named("processToDTOWithoutId")
    @Mapping(target = "id", ignore = true)
    public abstract EvangelistProcessStepDTO processToDTOWithoutId(EvangelistProcessStep evangelistProcessStep);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void copyValues(EvangelistProcessStep source, @MappingTarget EvangelistProcessStep target);

    public abstract List<EvangelistProcessStepDTO> processsToDtoBackList(List<EvangelistProcessStep> processses);

    @IterableMapping(qualifiedByName = "processToDTOWithoutId")
    public abstract List<EvangelistProcessStepDTO> processsToDtoFrontList(List<EvangelistProcessStep> processses);

    public abstract List<EvangelistProcessStep> processOrderToEntityList(List<DragDropSortDTO> dragDropSortDTOList);

    public abstract EvangelistProcessStep processEditDtoToEntity(EvangelistProcessStepEditDTO evangelistProcessStepEditDTO);

    @AfterMapping
    public void setIconUrl(EvangelistProcessStep evangelistProcessStep, @MappingTarget EvangelistProcessStepDTO evangelistProcessStepDTO) {
        Path iconPath = Path.of(evangelistProcessStep.getClass().getSimpleName().toLowerCase(), evangelistProcessStep.getId().toString());
        evangelistProcessStepDTO.setIconUrl(fileStorageService.getImageUrl(iconPath));
    }
}
