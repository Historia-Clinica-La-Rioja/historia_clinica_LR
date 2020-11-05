package net.pladema.hl7.supporting.terminology.support;

import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.supporting.terminology.coding.CodingValueSet;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionProfile {

    public ConditionProfile(){
        super();
    }

    public static StructureDefinition structureDefinition(){
        List<ElementDefinition> elements = new ArrayList<>(){{
            add(TerminologySupport.loadElementDefinition(CodingProfile.Condition.PATH.CATEGORY,
                    Enumerations.BindingStrength.EXTENSIBLE,
                    CodingValueSet.Condition.CATEGORY
            ));
            add(TerminologySupport.loadElementDefinition(CodingProfile.Condition.PATH.SEVERITY,
                    Enumerations.BindingStrength.PREFERRED,
                    CodingValueSet.Condition.SEVERITY
            ));
        }};

        return TerminologySupport.loadStructureDefinitionWithDifferential(
                CodingProfile.Condition.URL,
                CodingProfile.Condition.BASEURL,
                CodingProfile.Condition.TYPE,
                elements
        );
    }

    public static List<ValueSet> allValueSet(){
        return new ArrayList<>(){{
            add(TerminologySupport.loadValueSet(
                    CodingValueSet.Condition.CATEGORY, CodingSystem.LOINC, "75326-9"));
            add(TerminologySupport.loadValueSet(
                    CodingValueSet.Condition.SEVERITY, CodingSystem.LOINC, Arrays.asList("LA6752-5", "LA6751-7", "LA6750-9")));
        }};
    }
}
