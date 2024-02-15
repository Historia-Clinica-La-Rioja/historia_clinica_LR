package net.pladema.violencereport.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportActorDto<T> {

	private ViolenceReportPersonDto actorPersonalData;

	private T relationshipWithVictim;

	private String otherRelationshipWithVictim;

}
