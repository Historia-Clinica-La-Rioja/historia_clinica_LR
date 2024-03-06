package net.pladema.cipres.domain;


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
public class CipresEncounterBo {

	private Integer encounterId;

	private Integer encounterApiId;

	private String status;

	private Short responseCode;

	public CipresEncounterBo(Integer encounterId, String status, Short responseCode) {
		this.encounterId = encounterId;
		this.status = status;
		this.responseCode = responseCode;
	}
}
