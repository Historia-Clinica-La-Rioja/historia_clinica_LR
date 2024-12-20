package net.pladema.snowstorm.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SnomedMedicationSearchDto extends SnomedSearchItemDto {

	private boolean financed;

	private List<String> auditRequiredText;

}
