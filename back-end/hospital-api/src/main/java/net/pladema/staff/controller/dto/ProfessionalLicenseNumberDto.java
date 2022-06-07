package net.pladema.staff.controller.dto;

import java.io.Serializable;

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
public class ProfessionalLicenseNumberDto implements Serializable {

	private static final long serialVersionUID = 7788785116152807155L;

	private Integer id;

	private String licenseNumber;

	private Short typeId;

	private Integer healthcareProfessionalId;

	private Integer healthcareProfessionalSpecialtyId;

}
