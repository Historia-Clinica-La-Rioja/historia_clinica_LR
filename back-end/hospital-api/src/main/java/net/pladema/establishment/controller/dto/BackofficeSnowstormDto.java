package net.pladema.establishment.controller.dto;

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
public class BackofficeSnowstormDto {

	private String id;
	private String conceptId;
	private String term;

	public BackofficeSnowstormDto(String term) {
		this.term = term;
	}
}
