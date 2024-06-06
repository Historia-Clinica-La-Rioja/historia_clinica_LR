package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEClinicalObservationBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnthropometricValueBo {

	private String weight;
	private String height;
	private String headCircumference;
	public boolean hasValues(){
		return (height != null || weight != null || headCircumference != null ||
				!height.isEmpty() || !weight.isEmpty() || !headCircumference.isEmpty());
	}

	public String getBmi(){
		if (height == null || weight == null)
			return null;
		if (height.isEmpty() || weight.isEmpty())
			return null;
		if (Float.parseFloat(height) <= 0)
			return null;
		try {
			Double bmi = Float.parseFloat(weight) / Math.pow((Float.parseFloat(height)/100),2);
			return bmi.toString();
		} catch (Exception e) {
			return null;
		}
	}

}
