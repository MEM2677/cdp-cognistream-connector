package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Idea;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface IdeaService {
    void submit(@NotNull Idea idea);
}
