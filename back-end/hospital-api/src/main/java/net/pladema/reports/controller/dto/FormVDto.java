package net.pladema.reports.controller.dto;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FormVDto {

    private String establishment;

    private String completePatientName;

    private String address;

    private LocalDate reportDate;

    private String patientGender;

    private Short patientAge;

    private String documentType;

    private String documentNumber;

    private String medicalCoverage;

    private String affiliateNumber;

    private LocalDate consultationDate;

    private String problems;

    private String sisaCode;

    private String cie10Codes;

	private String medicalCoverageCondition;

	private String establishmentProvinceCode;

	private Integer hcnId;

	private String completeProfessionalName;

	private List<String> licenses;

	private String bedNumber;

	private String roomNumber;

	public FormVDto(String establishment, String completePatientName,
					ContactInfoBo contactInfo, LocalDate reportDate,
					String medicalCoverage, String problems,
					String medicalCoverageCondition, String establishmentProvinceCode,
					Integer hcnId, String completeProfessionalName,
					List<String> licenses, String bedNumber,
					String roomNumber, String affiliateNumber) {
		this.establishment = establishment;
		this.completePatientName = completePatientName;
		this.address = contactInfo.getAddress().getCompleteAddress();
		this.reportDate = reportDate;
		this.medicalCoverage = medicalCoverage;
		this.problems = problems;
		this.medicalCoverageCondition = medicalCoverageCondition;
		this.establishmentProvinceCode = establishmentProvinceCode;
		this.hcnId = hcnId;
		this.completeProfessionalName = completeProfessionalName;
		this.licenses = licenses;
		this.bedNumber = bedNumber;
		this.roomNumber = roomNumber;
		this.affiliateNumber = affiliateNumber;
	}
}
