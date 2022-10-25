package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.domain.Servizio;
import it.keybiz.cdp.innova.dto.DragDropSortDTO;
import it.keybiz.cdp.innova.dto.ServiceDTO;
import it.keybiz.cdp.innova.dto.ServiceEditDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper
public abstract class ServiceMapper {
    public abstract ServiceDTO serviceToDto(Servizio service);

    @Named("serviceToDtoWithoutID")
    @Mapping(target = "id", ignore = true)
    public abstract ServiceDTO serviceToDtoWithoutID(Servizio service);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void copyValues(Servizio source, @MappingTarget Servizio target);

    public abstract List<ServiceDTO> servicesToDtoBackList(List<Servizio> servizi);

    @IterableMapping(qualifiedByName = "serviceToDtoWithoutID")
    public abstract List<ServiceDTO> servicesToDtoFrontList(List<Servizio> servizi);

    public abstract List<Servizio> servicesToEntityList(List<DragDropSortDTO> dragDropSortDTOList);

    public abstract Servizio serviceEditDtoToEntity(ServiceEditDTO serviceEditDTO);
}
