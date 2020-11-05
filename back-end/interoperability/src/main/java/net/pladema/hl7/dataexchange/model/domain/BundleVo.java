package net.pladema.hl7.dataexchange.model.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.model.adaptor.Cast;
import org.hl7.fhir.r4.model.Coding;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BundleVo {

    @Setter
    private String id;

    private Date lastUpdated;

    @Setter
    private Coding type;

    @Setter
    private PatientVo patient;

    @Getter(AccessLevel.NONE)
    private boolean hasDocuments = false;

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
