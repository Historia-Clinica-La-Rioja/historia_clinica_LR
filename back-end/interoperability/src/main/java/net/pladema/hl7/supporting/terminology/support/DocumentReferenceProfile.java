package net.pladema.hl7.supporting.terminology.support;

import lombok.experimental.UtilityClass;
import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import org.hl7.fhir.r4.model.StructureDefinition;

@UtilityClass
public final class DocumentReferenceProfile {

    public static StructureDefinition structureDefinition(){
        return TerminologySupport.loadStructureDefinition(
                CodingProfile.DocumentReference.URL,
                CodingProfile.DocumentReference.BASEURL,
                CodingProfile.DocumentReference.TYPE
        );
    }
}
