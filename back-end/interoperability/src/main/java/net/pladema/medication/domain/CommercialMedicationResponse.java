package net.pladema.medication.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medication.domain.decodedResponse.CommercialMedicationDecodedResponse;

@Getter
@Setter
@NoArgsConstructor
public class CommercialMedicationResponse {

	private CommercialMedicationDecodedResponse commercialMedicationDecodedResponse;

	private String filePath;

}
