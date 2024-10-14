package net.pladema.violencereport.infrastructure.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Schema(name = "net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportActorDto", implementation = ViolenceReportActorDto.class)
public class ViolenceReportActorDto<T> {

	private ViolenceReportPersonDto actorPersonalData;

	private T relationshipWithVictim;

	private String otherRelationshipWithVictim;

}
