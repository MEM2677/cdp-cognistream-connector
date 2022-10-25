package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.domain.Evangelist;
import it.keybiz.cdp.innova.domain.OrganizationUnit;
import it.keybiz.cdp.innova.dto.EvangelistBackDTO;
import it.keybiz.cdp.innova.dto.EvangelistEditDTO;
import it.keybiz.cdp.innova.dto.EvangelistFrontDTO;
import it.keybiz.cdp.innova.dto.EvangelistSubscriptionDTO;
import it.keybiz.cdp.innova.dto.OrganizationUnitDTO;
import it.keybiz.cdp.innova.event.EvangelistSubscribedEvent;
import it.keybiz.cdp.innova.service.FileStorageService;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Mapper
public abstract class EvangelistMapper {
    @Autowired
    private FileStorageService fileStorageService;

    public abstract EvangelistBackDTO evangelistToBackDto(Evangelist evangelist);

    @Mapping(source = "role.name", target = "role")
    public abstract EvangelistFrontDTO evangelistToFrontDto(Evangelist evangelist);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void copyValues(Evangelist source, @MappingTarget Evangelist target);

    public Page<EvangelistBackDTO> evangelistsToDtoPage(Page<Evangelist> evangelists, Pageable pageable) {
        List<Evangelist> content = evangelists.getContent();
        List<EvangelistBackDTO> contentDTO = evangelistsToBackDtoList(content);
        return new PageImpl<>(contentDTO, pageable, evangelists.getTotalElements());
    }

    public abstract List<EvangelistBackDTO> evangelistsToBackDtoList(List<Evangelist> evangelists);

    public Map<String, List<EvangelistFrontDTO>> evangelistsGroupedDtos(List<Evangelist> evangelists) {
        Map<String, List<EvangelistFrontDTO>> result = new TreeMap<>();

        evangelists.forEach(evangelist -> {
            String organizationUnitName = evangelist.getRole().getOrganizationUnit().getName();
            List<EvangelistFrontDTO> organizationEvangelists = result.getOrDefault(organizationUnitName, new ArrayList<>());
            organizationEvangelists.add(evangelistToFrontDto(evangelist));
            result.put(organizationUnitName, organizationEvangelists);
        });

        // ordinamento valori per nome
        result.values().forEach(list -> list.sort(Comparator.comparing(EvangelistFrontDTO::getName)));

        return result;
    }

    @Mapping(source = "roleId", target = "role.id")
    public abstract Evangelist evangelistEditDtoToEntity(EvangelistEditDTO evangelistEditDTO);

    @Mapping(target = "roles", ignore = true)
    public abstract OrganizationUnitDTO mapOrganizationUnit(OrganizationUnit organizationUnit);

    public abstract Evangelist evangelistSubscriptionDtoToEntity(EvangelistSubscriptionDTO evangelistSubscriptionDTO);

    public abstract EvangelistSubscribedEvent evangelistToSubscribedEvent(Evangelist evangelist);

    @AfterMapping
    public void setImageUrl(Evangelist evangelist, @MappingTarget EvangelistFrontDTO evangelistFrontDTO) {
        Path iconPath = Path.of(evangelist.getClass().getSimpleName().toLowerCase(), evangelist.getId().toString());
        evangelistFrontDTO.setImageUrl(fileStorageService.getImageUrl(iconPath));
    }

    @AfterMapping
    public void setImageUrl(Evangelist evangelist, @MappingTarget EvangelistBackDTO evangelistBackDTO) {
        Path iconPath = Path.of(evangelist.getClass().getSimpleName().toLowerCase(), evangelist.getId().toString());
        evangelistBackDTO.setImageUrl(fileStorageService.getImageUrl(iconPath));
    }
}
