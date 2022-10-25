package it.keybiz.cdp.innova.web.rest.back;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.OrganizationUnit;
import it.keybiz.cdp.innova.dto.OrganizationUnitDTO;
import it.keybiz.cdp.innova.dto.OrganizationUnitEditDTO;
import it.keybiz.cdp.innova.dto.OrganizationUnitSearchDTO;
import it.keybiz.cdp.innova.mapper.OrganizationUnitMapper;
import it.keybiz.cdp.innova.service.OrganizationUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/organization-unit")
@RequiredArgsConstructor
@Api(tags = "Organization Unit")
@Tag(name = "Organization Unit")
public class OrganizationUnitController {
    private final OrganizationUnitService organizationUnitService;

    private final OrganizationUnitMapper organizationUnitMapper;

    @GetMapping
    public List<OrganizationUnitDTO> findAll(OrganizationUnitSearchDTO searchDTO) {
        List<OrganizationUnit> organizationUnits = organizationUnitService.findAll(searchDTO);
        return organizationUnitMapper.organizationUnitsToDtoList(organizationUnits);
    }

    @GetMapping("/{id}")
    public OrganizationUnitDTO findOne(@PathVariable @NotNull UUID id) {
        OrganizationUnit organizationUnit = organizationUnitService.findOne(id);
        return organizationUnitMapper.organizationUnitToDTO(organizationUnit);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid OrganizationUnitEditDTO organizationUnitEditDTO) {
        OrganizationUnit organizationUnit = organizationUnitMapper.organizationUnitEditDtoToEntity(organizationUnitEditDTO);
        organizationUnitService.create(organizationUnit);
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable @NotNull UUID id, @RequestBody @Valid OrganizationUnitEditDTO organizationUnitEditDTO) {
        OrganizationUnit organizationUnit = organizationUnitMapper.organizationUnitEditDtoToEntity(organizationUnitEditDTO);
        organizationUnitService.update(id, organizationUnit);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NotNull UUID id) {
        organizationUnitService.delete(id);
    }
}
