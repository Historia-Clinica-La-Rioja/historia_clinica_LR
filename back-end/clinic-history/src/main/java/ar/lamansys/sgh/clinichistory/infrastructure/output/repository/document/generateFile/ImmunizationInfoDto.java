package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineConditionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineDoseInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineSchemeInfoDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImmunizationInfoDto {

    private Integer id;

    private SnomedDto snomed;

    private String administrationDate;

    private String institutionInfo;

    private String doctorInfo;

    private VaccineDoseInfoDto dose;

    private VaccineConditionDto condition;

    private VaccineSchemeInfoDto scheme;

    private String lotNumber;

    private boolean billable = false;

    private String note;
}
