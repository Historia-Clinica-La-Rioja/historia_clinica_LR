package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.Cast;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.foundation.lifecycle.ResourceStatus;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class ImmunizationVo {

    public ImmunizationVo(Integer id, String immunizationCode, String immunizationTerm, String statusId,
                          Date administrationDate, Date createdOn, Date expirationDate){
        setId(Cast.toString(id));
        setImmunizationCode(immunizationCode);
        setImmunizationTerm(immunizationTerm);
        setStatus(Cast.toString(statusId));
        setAdministrationDate(administrationDate, createdOn);
        setExpirationDate(FhirDateMapper.toLocalDate(expirationDate));
    }

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
    private String batchNumber;

    public FhirCode get(){
        return new FhirCode(immunizationCode, immunizationTerm);
    }

    public FhirCode getVaccine(){
        return new FhirCode(vaccineCode, vaccineTerm);
    }

    public String getStatus(){
        return ResourceStatus.getStatus(status);
    }

    private void setAdministrationDate(Date administrationDate, Date createdOn){
        if(administrationDate != null)
            this.administrationDate = FhirDateMapper.toLocalDate(administrationDate);
        else
            this.administrationDate = FhirDateMapper.toLocalDate(createdOn);
    }
}
