package net.pladema.medicine.infrastructure.input.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MedicineGroupAuditRequiredDto {

	 private String requiredDocumentation;
	 private String name;
}
