package it.keybiz.cdp.innova.mapper;


import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mappings({
    @Mapping(source = "descriptionHtml", target = "description"),
    @Mapping(source = "contributionCount", target = "totalContributionCount"),
    @Mapping(source = "program.title", target = "programTitle"),
    @Mapping(source = "program.community.name", target = "communityTitle"),
})
public @interface ChallengeMappings {
}
