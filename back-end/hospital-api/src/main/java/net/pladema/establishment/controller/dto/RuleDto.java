package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RuleDto {

	private Integer id;

	private Integer clinicalSpecialtyId;

	private Integer snomedId;

	private String name;

	private Short level;

}
