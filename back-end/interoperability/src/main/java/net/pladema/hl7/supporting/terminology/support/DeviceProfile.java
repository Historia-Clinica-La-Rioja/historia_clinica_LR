package net.pladema.hl7.supporting.terminology.support;

import lombok.experimental.UtilityClass;
import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.Collections;
import java.util.List;

@UtilityClass
public final class DeviceProfile {

    public static StructureDefinition structureDefinition(){
        return TerminologySupport.loadStructureDefinition(
                CodingProfile.Device.URL,
                CodingProfile.Device.BASEURL,
                CodingProfile.Device.TYPE
        );
    }

    public static List<ValueSet> allValueSet(){
        return Collections.emptyList();
    }
}
