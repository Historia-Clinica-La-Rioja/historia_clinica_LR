package net.pladema.establishment.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.establishment.domain.EBlockAttentionPlaceReason;

@Getter
@Setter
@NoArgsConstructor
public class BlockAttentionPlaceCommandDto {
	Short reasonId;
	String reason;
}
