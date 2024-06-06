package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InstitutionPrescriptionBo {
	String name;
	String sisaCode;
	String provinceCode;
	String address;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof InstitutionPrescriptionBo)) return false;
		InstitutionPrescriptionBo that = (InstitutionPrescriptionBo) o;
		return Objects.equals(getName(), that.getName()) && Objects.equals(getSisaCode(), that.getSisaCode()) && Objects.equals(getProvinceCode(), that.getProvinceCode()) && Objects.equals(getAddress(), that.getAddress());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getSisaCode(), getProvinceCode(), getAddress());
	}
}
