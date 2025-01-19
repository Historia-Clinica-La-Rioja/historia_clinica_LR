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
	String country;
	String province;
	String department;
	String city;
	String street;
	String streetNumber;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PatientPrescriptionBo)) return false;
		PatientPrescriptionBo that = (PatientPrescriptionBo) o;
		return Objects.equals(getName(), that.getName()) &&
				Objects.equals(getLastName(), that.getLastName()) &&
				Objects.equals(getSelfPerceivedName(), that.getSelfPerceivedName()) &&
				Objects.equals(getDniSex(), that.getDniSex()) &&
				Objects.equals(getGender(), that.getGender()) &&
				Objects.equals(getBirthDate(), that.getBirthDate()) &&
				Objects.equals(getIdentificationType(), that.getIdentificationType()) &&
				Objects.equals(getIdentificationNumber(), that.getIdentificationNumber()) &&
				Objects.equals(getMedicalCoverage(), that.getMedicalCoverage()) &&
				Objects.equals(getMedicalCoverageCuit(), that.getMedicalCoverageCuit()) &&
				Objects.equals(getMedicalCoveragePlan(), that.getMedicalCoveragePlan()) &&
				Objects.equals(getAffiliateNumber(), that.getAffiliateNumber()) &&
				Objects.equals(getCountry(), that.getCountry()) &&
				Objects.equals(getProvince(), that.getProvince()) &&
				Objects.equals(getDepartment(), that.getDepartment()) &&
				Objects.equals(getCity(), that.getCity()) &&
				Objects.equals(getStreet(), that.getStreet()) &&
				Objects.equals(getStreetNumber(), that.getStreetNumber());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getLastName(), getSelfPerceivedName(), getDniSex(), getGender(), getBirthDate(), getIdentificationType(), getIdentificationNumber(), getMedicalCoverage(), getMedicalCoverageCuit(), getMedicalCoveragePlan(), getAffiliateNumber(), getCountry(), getProvince(), getDepartment(), getCity(), getStreet(), getStreetNumber());
	}

}
