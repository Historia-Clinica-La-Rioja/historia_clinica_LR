package net.pladema.hl7.dataexchange.mock;

import net.pladema.hl7.dataexchange.model.domain.CompositionVo;
import net.pladema.hl7.supporting.exchange.documents.profile.PatientSummaryDocument;

import java.time.LocalDate;
import java.util.Calendar;

public class MockComposition {

    public static CompositionVo mockIPSAR(){
        CompositionVo resource = new CompositionVo(PatientSummaryDocument.TYPE);
        resource.setCreatedOn(LocalDate.of(2020, Calendar.AUGUST, 19));
        return resource;
    }
}
