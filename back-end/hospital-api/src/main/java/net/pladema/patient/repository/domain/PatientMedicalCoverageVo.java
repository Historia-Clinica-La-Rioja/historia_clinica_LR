package net.pladema.patient.repository.domain;


import lombok.*;
import net.pladema.patient.controller.dto.EPatientMedicalCoverageCondition;
import net.pladema.patient.repository.entity.PrivateHealthInsuranceDetails;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientMedicalCoverageVo {

    private Integer id;

    private String affiliateNumber;

    private LocalDate vigencyDate;

    private Boolean active;

    private MedicalCoverageVo medicalCoverage;

	private LocalDate startDate;

	private LocalDate endDate;

	private Integer planId;

	private Short conditionId;

    public PatientMedicalCoverageVo(Integer id, String affiliateNumber, LocalDate vigencyDate, Boolean active, Integer medicalCoverageId, String name,
                                    String cuit, Integer rnos, String acronym, Short conditionId, LocalDate startDate, LocalDate endDate, Integer planId, Integer healthInsuranceId) {
        this.id = id;
        this.affiliateNumber = affiliateNumber;
        this.vigencyDate = vigencyDate;
        this.active = active;
		this.startDate = startDate;
		this.endDate = endDate;
		this.planId = planId;
        if (healthInsuranceId != null)
            this.medicalCoverage = new HealthInsuranceVo(medicalCoverageId, name,cuit, rnos, acronym);
        else{
            this.medicalCoverage = new PrivateHealthInsuranceVo(medicalCoverageId, name,cuit);
        }
        this.conditionId = conditionId;
    }

       public PatientMedicalCoverageVo(Integer id, String affiliateNumber, LocalDate vigencyDate, Boolean active, Integer medicalCoverageId, String name, String cuit, Integer rnos, String acronym, Short conditionId, Integer planId){
        this.id = id;
        this.affiliateNumber = affiliateNumber;
        this.vigencyDate = vigencyDate;
        this.active = active;
        this.medicalCoverage = new HealthInsuranceVo(medicalCoverageId, name,cuit, rnos, acronym);
		this.conditionId = conditionId;
        this.planId = planId;
    }

    public PatientMedicalCoverageVo(Integer id, String affiliateNumber, LocalDate vigencyDate, Boolean active, Integer medicalCoverageId, String name, String cuit, Short conditionId, LocalDate startDate, LocalDate endDate, Integer planId){
        this.id = id;
        this.affiliateNumber = affiliateNumber;
        this.vigencyDate = vigencyDate;
        this.active = active;
        this.medicalCoverage = new PrivateHealthInsuranceVo(medicalCoverageId, name, cuit);
		this.conditionId = conditionId;
        this.startDate = startDate;
		this.endDate = endDate;
		this.planId = planId;
    }
}
