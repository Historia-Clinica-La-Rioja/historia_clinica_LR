package ar.lamansys.sgh.shared.infrastructure.input.service.rule;

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
public class SharedRuleDto {

	private Integer id;

	private Integer clinicalSpecialtyId;

	private Integer snomedId;

	private String name;

	private Short level;


}
