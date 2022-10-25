package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Servizio;
import it.keybiz.cdp.innova.dto.ServiceSearchDTO;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
public interface ServiceService {
    List<Servizio> findAll(@NotNull ServiceSearchDTO searchDTO);

    Servizio findOne(@NotNull UUID id);

    void create(@NotNull Servizio service);

    void update(@NotNull UUID id, @NotNull Servizio service);

    void sort(@NotNull List<Servizio> services);

    void delete(@NotNull UUID id);
}
