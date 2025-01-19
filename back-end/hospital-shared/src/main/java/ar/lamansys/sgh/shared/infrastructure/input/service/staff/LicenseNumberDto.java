package ar.lamansys.sgh.shared.infrastructure.input.service.staff;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LicenseNumberDto {

	private Integer id;
	private String number;
	private String type;

	public String getInfo() {
		return (type) + "-" + number;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LicenseNumberDto)) return false;
		LicenseNumberDto that = (LicenseNumberDto) o;
		return Objects.equals(getId(), that.getId()) && Objects.equals(getNumber(), that.getNumber()) && Objects.equals(getType(), that.getType());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getNumber(), getType());
	}

	@JsonIgnore
	public boolean hasMN() {
		return "MN".equals(getType());
	}

	@JsonIgnore
	public boolean hasMP() {
		return "MP".equals(getType());
	}
}
