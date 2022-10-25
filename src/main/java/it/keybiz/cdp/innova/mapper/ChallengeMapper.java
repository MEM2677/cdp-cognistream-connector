package it.keybiz.cdp.innova.mapper;

import it.keybiz.cdp.innova.client.cognistreamer.models.ChallengeResponse;
import it.keybiz.cdp.innova.domain.Challenge;
import it.keybiz.cdp.innova.domain.Contribution;
import it.keybiz.cdp.innova.dto.ChallengeDTO;
import it.keybiz.cdp.innova.dto.ChallengeListDTO;
import it.keybiz.cdp.innova.dto.ContributionDTO;
import it.keybiz.cdp.innova.enums.ImageSizeEnum;
import it.keybiz.cdp.innova.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.nio.file.Path;
import java.util.List;

@Slf4j
@Mapper
public abstract class ChallengeMapper {
    @Autowired
    private FileStorageService fileStorageService;

    public abstract List<Challenge> challengesResponseToEntities(List<ChallengeResponse> challengesResponse);

    @IterableMapping(qualifiedByName = "challengeListDTOMapping")
    public abstract List<ChallengeListDTO> challengesToDtoList(List<Challenge> challenges);

    public Page<ChallengeListDTO> challengeToDtoPage(Page<Challenge> challenges, Pageable pageable) {
        List<Challenge> content = challenges.getContent();
        List<ChallengeListDTO> challengeListDTOS = challengesToDtoList(content);
        return new PageImpl<>(challengeListDTOS, pageable, challenges.getTotalElements());
    }

    @ChallengeMappings
    public abstract ChallengeDTO challengeToDto(Challenge challenge);

    @Named("challengeListDTOMapping")
    @ChallengeMappings
    public abstract ChallengeListDTO challengeToListDto(Challenge challenge);

    @Mappings({
        @Mapping(source = "dateCreatedUtc", target = "dateCreated"),
    })
    public abstract ContributionDTO map(Contribution contribution);

    @AfterMapping
    public void convertDTOImageToBase64(Contribution contribution, @MappingTarget ContributionDTO contributionDTO) {
        Path imagePath = Path.of(contribution.getClass().getSimpleName().toLowerCase(), contribution.getId().toString(), ImageSizeEnum.SMALL.getSize());
        contributionDTO.setImageUrl(fileStorageService.getImageUrl(imagePath));
    }

    @AfterMapping
    public void setDTOImagePath(Challenge challenge, @MappingTarget ChallengeDTO challengeDTO) {
        Path imagePath = Path.of(challenge.getClass().getSimpleName().toLowerCase(), challenge.getId().toString(), ImageSizeEnum.LARGE.getSize());
        challengeDTO.setImageUrl(fileStorageService.getImageUrl(imagePath));
    }

    @AfterMapping
    public void setListDTOImagePath(Challenge challenge, @MappingTarget ChallengeListDTO challengeListDTO) {
        Path imagePath = Path.of(challenge.getClass().getSimpleName().toLowerCase(), challenge.getId().toString(), ImageSizeEnum.SMALL.getSize());
        challengeListDTO.setImageUrl(fileStorageService.getImageUrl(imagePath));
    }
}
