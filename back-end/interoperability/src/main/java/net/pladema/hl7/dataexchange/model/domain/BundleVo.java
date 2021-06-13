package net.pladema.hl7.dataexchange.model.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.adaptor.Cast;
import net.pladema.hl7.supporting.exchange.documents.ips.PatientSummaryDocument;
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

    public BundleVo(Integer id, String firstName, String middleNames,
                    String lastName, boolean hasDocuments) {
        PatientVo patientData = new PatientVo();
        patientData.setId(Cast.toString(id));
        patientData.setFirstname(firstName);
        patientData.setMiddlenames(middleNames);
        patientData.setLastname(lastName);
        this.patient = patientData;
        setId(patientData.getId());
        setHasDocuments(hasDocuments);
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
