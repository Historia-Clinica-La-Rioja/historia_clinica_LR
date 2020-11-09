package net.pladema.hl7.dataexchange.model.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.adaptor.Cast;
import net.pladema.hl7.supporting.exchange.documents.profile.PatientSummaryDocument;
import org.hl7.fhir.r4.model.Coding;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BundleVo {


    private String id;

    private Date lastUpdated;

    private Coding type = PatientSummaryDocument.TYPE;

    private PatientVo patient;

    @Getter(AccessLevel.NONE)
    private boolean hasDocuments = false;

    public BundleVo(Object[] tuple) {
        int index=0;
        PatientVo patient = new PatientVo();
        patient.setId(Cast.toString(tuple[index++]));
        patient.setFirstname(Cast.toString(tuple[index++]));
        patient.setMiddlenames(Cast.toString(tuple[index++]));
        patient.setLastname(Cast.toString(tuple[index++]));
        this.patient = patient;
        setId(patient.getId());
        setHasDocuments(Cast.toBoolean(tuple[index]));
    }

    public void setLastUpdated(LocalDate date){
        lastUpdated = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public boolean existsPatient(){
        return patient != null && patient.getId() != null;
    }

    public boolean hasDocuments(){
        return hasDocuments;
    }
}
