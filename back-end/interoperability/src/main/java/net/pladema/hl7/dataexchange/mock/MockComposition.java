package net.pladema.hl7.dataexchange.mock;

import net.pladema.hl7.dataexchange.model.domain.CompositionVo;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.hl7.fhir.r4.model.Coding;

import java.time.LocalDate;
import java.util.Calendar;

public class MockComposition {

    public static CompositionVo mockIPSAR(){
        CompositionVo resource = new CompositionVo(new Coding()
                .setSystem(CodingSystem.LOINC)
                .setCode("60591-5")
                .setDisplay("Patient summary Document"));
        resource.setCreatedOn(LocalDate.of(2020, Calendar.AUGUST, 19));
        return resource;
    }
}
