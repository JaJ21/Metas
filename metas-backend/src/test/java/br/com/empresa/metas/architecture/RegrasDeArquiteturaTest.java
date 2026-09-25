package br.com.empresa.metas.architecture;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Testes que fiscalizam a arquitetura em si — não testam comportamento,
 * testam se as camadas continuam respeitando a direção de dependência
 * exigida pelo padrão (ex: "o core não pode depender do Spring"). Se
 * alguém acidentalmente importar @Autowired dentro de um pacote "core",
 * esse teste FALHA na hora do build, travando o problema antes de ele
 * virar um hábito no time.
 */
class RegrasDeArquiteturaTest {

    private static final com.tngtech.archunit.core.domain.JavaClasses TODAS_AS_CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("br.com.empresa.metas");

    @Test
    void core_nao_pode_depender_de_spring() {
        ArchRule regra = noClasses()
                .that().resideInAPackage("..core..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.springframework..", "jakarta.persistence..", "org.hibernate.."
                );
        regra.check(TODAS_AS_CLASSES);
    }

    @Test
    void core_nao_pode_depender_de_infra() {
        ArchRule regra = noClasses()
                .that().resideInAPackage("..core..")
                .should().dependOnClassesThat().resideInAPackage("..infra..");
        regra.check(TODAS_AS_CLASSES);
    }

    @Test
    void dominio_nao_pode_depender_de_lombok() {
        ArchRule regra = noClasses()
                .that().resideInAPackage("..core..")
                .should().dependOnClassesThat().resideInAPackage("lombok..");
        regra.check(TODAS_AS_CLASSES);
    }
}
