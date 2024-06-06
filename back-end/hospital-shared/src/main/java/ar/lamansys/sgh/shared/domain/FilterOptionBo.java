package ar.lamansys.sgh.shared.domain;

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
public class FilterOptionBo {

	private Number id;

	private String description;

	public FilterOptionBo(Number id, Number description) {
		this.id = id;
		this.description = description.toString();
	}
}
