package net.pladema.nomivac.domain.immunization;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class ImmunizationBo {

    private Integer id;

    private NomivacPatientBo patient;

    private NomivacVaccineBo vaccine;

    private String statusId;

    private LocalDate administrationDate;

    private LocalDate expirationDate;

    private NomivacInstitutionBo institution;

    private NomivacConditionInfoBo condition;

    private NomivacSchemeInfoBo scheme;

    private NomivacDoseInfoBo dose;

    private String lotNumber;

    private String note;

    private final boolean deleteable;

    public String getVaccineSctid() {
        return vaccine != null ? vaccine.getVaccineSctid() : null;
    }

    public String getVaccinePt() {
        return vaccine != null ? vaccine.getVaccinePt() : null;
    }

    public String getPatientIdentificationNumber() {
        return patient != null ? patient.getIdentificationNumber() : null;
    }

    public String getPatientName() {
        return patient != null ? patient.getCompleteName() : null;
    }

    public String getLocationName() {
        return institution != null ? institution.getName() : null;
    }

    public String getLocationSisaCode() {
        return institution != null ? institution.getSisaCode() : null;
    }

    public Short getConditionId() {
        return condition != null ? condition.getId() : null;
    }

    public String getConditionDescription() {
        return condition != null ? condition.getDescription() : null;
    }

    public Short getSchemeId() {
        return scheme != null ? scheme.getId() : null;
    }

    public String getSchemeDescription() {
        return scheme != null ? scheme.getDescription() : null;
    }

    public Short getDoseOrder() {
        return dose.getDoseOrder();
    }
}
