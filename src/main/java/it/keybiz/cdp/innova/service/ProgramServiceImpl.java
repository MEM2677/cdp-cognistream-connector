package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Program;
import it.keybiz.cdp.innova.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {
    private final ProgramRepository programRepository;

    @Override
    public List<Program> findAll() {
        return programRepository.findAll();
    }
}
