package it.keybiz.cdp.innova.web.rest.back;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.Servizio;
import it.keybiz.cdp.innova.dto.DragDropSortDTO;
import it.keybiz.cdp.innova.dto.ServiceDTO;
import it.keybiz.cdp.innova.dto.ServiceEditDTO;
import it.keybiz.cdp.innova.dto.ServiceSearchDTO;
import it.keybiz.cdp.innova.mapper.ServiceMapper;
import it.keybiz.cdp.innova.service.ServiceService;
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
@RequestMapping("/api/service")
@RequiredArgsConstructor
@Api(tags = "Service")
@Tag(name = "Service")
public class ServiceBackController {
    private final ServiceService serviceService;

    private final ServiceMapper serviceMapper;

    @GetMapping
    public List<ServiceDTO> findAll(ServiceSearchDTO searchDTO) {
        List<Servizio> servizi = serviceService.findAll(searchDTO);
        return serviceMapper.servicesToDtoBackList(servizi);
    }

    @GetMapping("/{id}")
    public ServiceDTO findOne(@PathVariable @NotNull UUID id) {
        Servizio service = serviceService.findOne(id);
        return serviceMapper.serviceToDto(service);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ServiceEditDTO serviceEditDTO) {
        Servizio servizio = serviceMapper.serviceEditDtoToEntity(serviceEditDTO);
        serviceService.create(servizio);
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable @NotNull UUID id, @RequestBody @Valid ServiceEditDTO serviceEditDTO) {
        Servizio service = serviceMapper.serviceEditDtoToEntity(serviceEditDTO);
        serviceService.update(id, service);
    }

    @PostMapping("/sort")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sort(@RequestBody @Valid @NotNull List<DragDropSortDTO> dragDropSortDTOList) {
        List<Servizio> services = serviceMapper.servicesToEntityList(dragDropSortDTOList);
        serviceService.sort(services);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NotNull UUID id) {
        serviceService.delete(id);
    }
}
