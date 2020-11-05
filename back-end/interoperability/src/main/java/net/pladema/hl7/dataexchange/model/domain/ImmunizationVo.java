package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.foundation.lifecycle.ResourceStatus;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ImmunizationVo {

        private String id;

    private String vaccineCode;
    private String vaccineTerm;

    private String status;

    //Immunization info
    private String immunizationCode;
    private String immunizationTerm;

    private String series;

    private LocalDate administrationDate;
    private LocalDate expirationDate;

    private Integer doseNumber;
    private boolean primarySource;
    private String loteNumber;

    public FhirCode get(){
        return new FhirCode(immunizationCode, immunizationTerm);
    }

    public FhirCode getVaccine(){
        return new FhirCode(vaccineCode, vaccineTerm);
    }

    public String getStatus(){
        return ResourceStatus.getStatus(status);
    }

    private void setAdministrationDate(java.sql.Date administrationDate, java.sql.Timestamp createdOn){
        if(administrationDate != null)
            this.administrationDate = FhirDateMapper.toLocalDate(administrationDate);
        else
            this.administrationDate = FhirDateMapper.toLocalDate(createdOn);
    }
}
