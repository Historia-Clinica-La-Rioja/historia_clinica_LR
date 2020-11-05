package net.pladema.hl7.dataexchange.mock;

import net.pladema.hl7.dataexchange.model.domain.PatientVo;
import org.hl7.fhir.r4.model.codesystems.AdministrativeGender;

public class MockPatient {

    public static PatientVo mock(){
        PatientVo resource = new PatientVo();
        resource.setId("ID-Patient");
        resource.setGender(AdministrativeGender.FEMALE.getDisplay());
        return resource;
    }
}
