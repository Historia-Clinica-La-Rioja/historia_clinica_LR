package net.pladema.hl7.dataexchange.mock;

import net.pladema.hl7.dataexchange.model.domain.BundleVo;
import net.pladema.hl7.dataexchange.model.domain.PatientVo;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.hl7.fhir.r4.model.Coding;

import java.time.LocalDate;

public class MockBundle {

    public MockBundle(){
        super();
    }

    public static BundleVo mockSearchDocument(){
        BundleVo resource = new BundleVo();
        resource.setId("ID-documentReference");
        resource.setLastUpdated(LocalDate.of(2020, 10, 1));
        PatientVo patient = new PatientVo();
        patient.setId("x999698");
        patient.setFirstname("VERONICA");
        patient.setMiddlenames("SILVANA");
        patient.setLastname("OJEDA");
        resource.setPatient(patient);
        resource.setType(new Coding()
                .setSystem(CodingSystem.LOINC)
                .setCode("60591-5")
                .setDisplay("Patient summary Document"));

        resource.setHasDocuments(true);
        return resource;
    }

}
