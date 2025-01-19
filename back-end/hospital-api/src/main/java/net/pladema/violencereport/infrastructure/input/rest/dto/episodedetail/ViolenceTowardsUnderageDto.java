package net.pladema.violencereport.infrastructure.input.rest.dto.episodedetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.ESchoolLevel;
import net.pladema.violencereport.domain.enums.EViolenceTowardsUnderageType;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceTowardsUnderageDto {

	private EViolenceTowardsUnderageType type;

	private Boolean schooled;

	private ESchoolLevel schoolLevel;

}
