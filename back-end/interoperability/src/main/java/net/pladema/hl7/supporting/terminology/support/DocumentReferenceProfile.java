package net.pladema.hl7.supporting.terminology.support;

import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import org.hl7.fhir.r4.model.StructureDefinition;

public class DocumentReferenceProfile {

    public DocumentReferenceProfile(){
        super();
    }

    public static StructureDefinition structureDefinition(){
        return TerminologySupport.loadStructureDefinition(
                CodingProfile.DocumentReference.URL,
                CodingProfile.DocumentReference.BASEURL,
                CodingProfile.DocumentReference.TYPE
        );
    }
}
