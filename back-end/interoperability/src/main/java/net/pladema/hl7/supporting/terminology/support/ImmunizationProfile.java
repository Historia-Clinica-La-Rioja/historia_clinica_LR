package net.pladema.hl7.supporting.terminology.support;

import lombok.experimental.UtilityClass;
import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.Collections;
import java.util.List;

@UtilityClass
public final class ImmunizationProfile {

    public static StructureDefinition structureDefinition(){

        ElementDefinition element = new ElementDefinition();
        element.setPath(CodingProfile.Immunization.NOMIVAC.PATH);
        element.addType()
                .setCode(CodingProfile.Immunization.NOMIVAC.CODE)
                .addProfile(CodingProfile.Immunization.NOMIVAC.URL);

        return TerminologySupport.loadStructureDefinitionWithDifferential(
                CodingProfile.Immunization.URL,
                CodingProfile.Immunization.BASEURL,
                CodingProfile.Immunization.TYPE,
                element);
    }

    public static List<ValueSet> allValueSet() {
        return Collections.emptyList();
    }
}
