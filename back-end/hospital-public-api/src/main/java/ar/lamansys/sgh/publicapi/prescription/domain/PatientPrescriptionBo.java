package ar.lamansys.sgh.publicapi.prescription.domain;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PatientPrescriptionBo {
	String name;
	String lastName;
	String selfPerceivedName;
	String dniSex;
	String gender;
	LocalDate birthDate;
	String identificationType;
	String identificationNumber;
	String medicalCoverage;
	String medicalCoverageCuit;
	String medicalCoveragePlan;
	String affiliateNumber;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PatientPrescriptionBo)) return false;
		PatientPrescriptionBo that = (PatientPrescriptionBo) o;
		return Objects.equals(getName(), that.getName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getSelfPerceivedName(), that.getSelfPerceivedName()) && Objects.equals(getDniSex(), that.getDniSex()) && Objects.equals(getGender(), that.getGender()) && Objects.equals(getBirthDate(), that.getBirthDate()) && Objects.equals(getIdentificationType(), that.getIdentificationType()) && Objects.equals(getIdentificationNumber(), that.getIdentificationNumber()) && Objects.equals(getMedicalCoverage(), that.getMedicalCoverage()) && Objects.equals(getMedicalCoverageCuit(), that.getMedicalCoverageCuit()) && Objects.equals(getMedicalCoveragePlan(), that.getMedicalCoveragePlan()) && Objects.equals(getAffiliateNumber(), that.getAffiliateNumber());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getLastName(), getSelfPerceivedName(), getDniSex(), getGender(), getBirthDate(), getIdentificationType(), getIdentificationNumber(), getMedicalCoverage(), getMedicalCoverageCuit(), getMedicalCoveragePlan(), getAffiliateNumber());
	}
}
