package net.pladema.template.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DocumentTemplateBo {

	private Integer userId;
	private String name;
	private String category;
	private Integer institutionId;
	private String templateText;
	private List<ConclusionBo> conclusions;

	@Override
	public String toString() {
		return "DocumentTemplateBo{" + "userId=" + userId +
				", name='" + name + '\'' +
				", category='" + category + '\'' +
				", institutionId=" + institutionId +
				", templateText='" + "<OMITTED>" + '\'' +
				", conclusions=" + conclusions +
				'}';
	}
}
