package net.pladema.patient.repository.domain;


import lombok.*;
import net.pladema.patient.controller.dto.EMedicalCoverageType;

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
                                    String cuit, Short type, Integer rnos, String acronym, Short conditionId, LocalDate startDate, LocalDate endDate, Integer planId) {
        this.id = id;
        this.affiliateNumber = affiliateNumber;
        this.vigencyDate = vigencyDate;
        this.active = active;
		this.startDate = startDate;
		this.endDate = endDate;
		this.planId = planId;
		switch (EMedicalCoverageType.map(type)){
			case PREPAGA: this.medicalCoverage =  new PrivateHealthInsuranceVo(medicalCoverageId, name, cuit, type);
				break;
			case OBRASOCIAL: this.medicalCoverage = new HealthInsuranceVo(medicalCoverageId, name,cuit, rnos, acronym, type);
				break;
			case ART: this.medicalCoverage = new ARTCoverageVo(medicalCoverageId, name,cuit, type);
		}
        this.conditionId = conditionId;
    }

       public PatientMedicalCoverageVo(Integer id, String affiliateNumber, LocalDate vigencyDate, Boolean active, Integer medicalCoverageId, String name, String cuit, Short type, Integer rnos, String acronym, Short conditionId, Integer planId){
        this.id = id;
        this.affiliateNumber = affiliateNumber;
        this.vigencyDate = vigencyDate;
        this.active = active;
        this.medicalCoverage = new HealthInsuranceVo(medicalCoverageId, name,cuit, rnos, acronym, type);
		this.conditionId = conditionId;
        this.planId = planId;
    }

    public PatientMedicalCoverageVo(Integer id, String affiliateNumber, LocalDate vigencyDate, Boolean active, Integer medicalCoverageId, String name, String cuit, Short type, Short conditionId, LocalDate startDate, LocalDate endDate, Integer planId){
        this.id = id;
        this.affiliateNumber = affiliateNumber;
        this.vigencyDate = vigencyDate;
        this.active = active;
        this.medicalCoverage = new PrivateHealthInsuranceVo(medicalCoverageId, name, cuit, type);
		this.conditionId = conditionId;
        this.startDate = startDate;
		this.endDate = endDate;
		this.planId = planId;
    }
}
