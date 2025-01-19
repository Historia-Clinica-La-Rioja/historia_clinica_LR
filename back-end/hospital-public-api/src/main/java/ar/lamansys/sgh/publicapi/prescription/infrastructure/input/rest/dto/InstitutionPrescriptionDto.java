package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionPrescriptionDto {
	String name;
	String sisaCode;
	String provinceCode;
	String address;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof InstitutionPrescriptionDto)) return false;
		InstitutionPrescriptionDto that = (InstitutionPrescriptionDto) o;
		return Objects.equals(getName(), that.getName()) && Objects.equals(getSisaCode(), that.getSisaCode()) && Objects.equals(getProvinceCode(), that.getProvinceCode()) && Objects.equals(getAddress(), that.getAddress());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getSisaCode(), getProvinceCode(), getAddress());
	}
}
