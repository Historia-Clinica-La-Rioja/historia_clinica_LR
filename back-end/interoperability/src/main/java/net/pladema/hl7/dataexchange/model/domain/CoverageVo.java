package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CoverageVo {

	private Integer patientMedicalCoverageId;
	private Integer medicalCoverageId;
	private String affiliateNumber;
	private Boolean isActive;
	private String cuit;
	private String name;
	private Integer patientId;
	private Date startDate;
	private Date endDate;
	private Integer planId;
	private String planName;

	public CoverageVo(Integer patientMedicalCoverageId, Integer medicalCoverageId, String affiliateNumber,
					  Boolean active, Integer patientId, Date startDate, Date endDate, String cuit, String name, Integer planId, String plan) {
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.medicalCoverageId = medicalCoverageId;
		this.affiliateNumber = affiliateNumber;
		this.isActive = active;
		this.patientId = patientId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.cuit = cuit;
		this.name = name;
		this.planId = planId;
		this.planName = plan;
	}

	public CoverageVo() {

	}

	public boolean havePlan() {
		return (planId != null && planName != null);
	}

}
