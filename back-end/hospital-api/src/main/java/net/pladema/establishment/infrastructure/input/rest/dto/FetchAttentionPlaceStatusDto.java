package net.pladema.establishment.infrastructure.input.rest.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalPersonDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.establishment.domain.EBlockAttentionPlaceReason;

@NoArgsConstructor
@Setter
@Getter
public class FetchAttentionPlaceStatusDto {
	private Integer id;
	private Boolean isBlocked;
	private Integer userId;
	private DateTimeDto createdOn;
	private Short reasonId;
	private EBlockAttentionPlaceReason reasonEnum;
	private String reasonEnumDescription;
	private String reason;
	private ProfessionalPersonDto blockedBy;
}
