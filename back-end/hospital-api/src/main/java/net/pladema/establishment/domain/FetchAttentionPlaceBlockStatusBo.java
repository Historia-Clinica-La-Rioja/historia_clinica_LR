package net.pladema.establishment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pladema.staff.domain.ProfessionalCompleteBo;

import javax.persistence.Column;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class FetchAttentionPlaceBlockStatusBo {
	private Integer id;
	private Boolean isBlocked;
	private Integer userId;
	private ZonedDateTime createdOn;
	private Short reasonId;
	private EBlockAttentionPlaceReason reasonEnum;
	private String reasonEnumDescription;
	private String reason;
	private ProfessionalCompleteBo blockedBy;
}
