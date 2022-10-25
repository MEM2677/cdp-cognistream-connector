package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.EvangelistProcessStep;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepSearchDTO;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
public interface EvangelistProcessStepService {
    List<EvangelistProcessStep> findAll(@NotNull EvangelistProcessStepSearchDTO searchDTO);

    EvangelistProcessStep findOne(@NotNull UUID id);

    void create(@NotNull EvangelistProcessStep evangelistProcessStep, @Nullable String icon);

    void update(@NotNull UUID id, @NotNull EvangelistProcessStep evangelistProcessStep, @Nullable String icon);

    void sort(@NotNull List<EvangelistProcessStep> evangelistProcessSteps);

    void delete(@NotNull UUID id);
}
