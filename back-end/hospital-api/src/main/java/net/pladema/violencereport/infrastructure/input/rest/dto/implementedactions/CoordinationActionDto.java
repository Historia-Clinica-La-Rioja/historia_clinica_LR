package net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Schema(name = "net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.CoordinationActionDto", implementation = CoordinationActionDto.class)
public class CoordinationActionDto<T> {

	private Boolean within;

	private List<T> organizations;

	private String other;

}
