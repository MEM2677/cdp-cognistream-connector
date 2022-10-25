package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Evangelist;
import it.keybiz.cdp.innova.dto.EvangelistSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
public interface EvangelistService {
    Page<Evangelist> findAll(@NotNull Pageable pageable, EvangelistSearchDTO searchDTO);

    List<Evangelist> findAllActive();

    Evangelist findOne(@NotNull UUID id);

    void create(@NotNull Evangelist evangelist, @Nullable String image);

    void subscribe(@NotNull Evangelist evangelist);

    void update(@NotNull UUID id, @NotNull Evangelist evangelist, @Nullable String image);

    void delete(@NotNull UUID id);
}
