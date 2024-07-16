package net.pladema.reports.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.domain.LicenseNumberBo;

import java.util.List;

@Getter
@ToString
@Builder
public class AnnexIIProfessionalBo {

	private Integer healthcareProfessionalId;

	private String completeProfessionalName;

	private List<LicenseNumberBo> licenses;

}
