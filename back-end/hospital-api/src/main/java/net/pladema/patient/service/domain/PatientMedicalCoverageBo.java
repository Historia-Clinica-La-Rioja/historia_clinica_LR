package net.pladema.patient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.controller.dto.EPatientMedicalCoverageCondition;
import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientMedicalCoverageBo {

    private Integer id;

    private LocalDate vigencyDate;

    private Boolean active;

    private String affiliateNumber;

    private MedicalCoverageBo medicalCoverage;

	private EPatientMedicalCoverageCondition condition;

	private LocalDate startDate;

    private LocalDate endDate;

	private Integer planId;

	private String planName;


	public PatientMedicalCoverageBo(PatientMedicalCoverageVo patientMedicalCoverageVo) {
        this.id = patientMedicalCoverageVo.getId();
        this.vigencyDate = patientMedicalCoverageVo.getVigencyDate();
        this.active = patientMedicalCoverageVo.getActive();
        this.affiliateNumber = patientMedicalCoverageVo.getAffiliateNumber();
        this.medicalCoverage = patientMedicalCoverageVo.getMedicalCoverage().newInstance();
		if(patientMedicalCoverageVo.getConditionId()!=null)
			this.condition = EPatientMedicalCoverageCondition.map(patientMedicalCoverageVo.getConditionId());
        this.startDate = patientMedicalCoverageVo.getStartDate();
		this.endDate = patientMedicalCoverageVo.getEndDate();
		this.planId = patientMedicalCoverageVo.getPlanId();
    }
}
