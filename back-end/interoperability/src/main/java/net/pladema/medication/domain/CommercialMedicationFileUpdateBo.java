package net.pladema.medication.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommercialMedicationFileUpdateBo {

	private Long logId;

	private String filePath;

}
