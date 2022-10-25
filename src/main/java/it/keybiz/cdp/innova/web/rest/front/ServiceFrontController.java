package it.keybiz.cdp.innova.web.rest.front;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.domain.Servizio;
import it.keybiz.cdp.innova.dto.ServiceDTO;
import it.keybiz.cdp.innova.dto.ServiceSearchDTO;
import it.keybiz.cdp.innova.mapper.ServiceMapper;
import it.keybiz.cdp.innova.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/front/service")
@RequiredArgsConstructor
@Api(tags = "Service")
@Tag(name = "Service")
public class ServiceFrontController {
    private final ServiceService serviceService;

    private final ServiceMapper serviceMapper;

    @GetMapping
    public List<ServiceDTO> findAll() {
        List<Servizio> servizi = serviceService.findAll(new ServiceSearchDTO());
        return serviceMapper.servicesToDtoFrontList(servizi);
    }
}

