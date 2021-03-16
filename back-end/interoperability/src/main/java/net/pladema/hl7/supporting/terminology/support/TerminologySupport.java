package net.pladema.hl7.supporting.terminology.support;

import lombok.experimental.UtilityClass;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;

import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class TerminologySupport {

    public static CodeSystem loadCodeSystem(String url, String valueSetUrl, List<FhirCode> codes){
        return new CodeSystem().setUrl(url)
                .setContent(CodeSystem.CodeSystemContentMode.COMPLETE)
                .setValueSet(valueSetUrl)
                .setConcept(mapToConcepts(codes));
    }

    private static List<CodeSystem.ConceptDefinitionComponent> mapToConcepts(List<FhirCode> codes){
        List<CodeSystem.ConceptDefinitionComponent> concepts = new ArrayList<>();
        codes.forEach(code->
            concepts.add(new CodeSystem.ConceptDefinitionComponent().setCode(code.getTheCode()))
        );
        return concepts;
    }

    public static ValueSet loadValueSet(String url, String system, List<String> codes) {
        ValueSet.ValueSetComposeComponent compose = new ValueSet.ValueSetComposeComponent()
                .addInclude(new ValueSet.ConceptSetComponent()
                        .setSystem(system)
                        .setConcept(codes.stream()
                                .map(code -> new ValueSet.ConceptReferenceComponent().setCode(code))
                                .collect(Collectors.toList()))
                );
        return new ValueSet().setUrl(url).setCompose(compose);
    }

    public static ValueSet loadValueSet(String url, String system, String code) {
        return loadValueSet(url, system,Collections.singletonList(code));
    }

    public static ElementDefinition loadElementDefinition(String path,
                                                          Enumerations.BindingStrength strength,
                                                          String valueSet) {
        return new ElementDefinition()
                .setPath(path)
                .setBinding(new ElementDefinition.ElementDefinitionBindingComponent()
                .setStrength(strength)
                .setValueSet(valueSet)
        );
    }

    public static StructureDefinition loadStructureDefinition(String url, String baseUrl, String type){
        return new StructureDefinition()
                .setUrl(url)
                .setBaseDefinition(baseUrl)
                .setType(type)
                .setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
    }

    public static StructureDefinition loadStructureDefinitionWithDifferential(String url, String baseUrl,
                                                                        String type,
                                                                        List<ElementDefinition> elements){
        return loadStructureDefinition(url, baseUrl, type)
                .setDifferential(new StructureDefinition.StructureDefinitionDifferentialComponent()
                .setElement(elements));
    }

    public static StructureDefinition loadStructureDefinitionWithDifferential(String url, String baseUrl,
                                                                              String type,
                                                                              ElementDefinition element){
        return loadStructureDefinitionWithDifferential(
                url, baseUrl, type, Collections.singletonList(element)
        );
    }
}
