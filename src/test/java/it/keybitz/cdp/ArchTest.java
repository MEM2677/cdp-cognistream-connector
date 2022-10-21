package it.keybitz.cdp;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("it.keybitz.cdp");

        noClasses()
            .that()
            .resideInAnyPackage("it.keybitz.cdp.service..")
            .or()
            .resideInAnyPackage("it.keybitz.cdp.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..it.keybitz.cdp.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
