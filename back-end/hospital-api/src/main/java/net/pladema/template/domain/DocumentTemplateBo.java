package net.pladema.template.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTemplateBo {

	private Integer userId;
	private String name;
	private String category;
	private Integer institutionId;
	private String templateText;

	@Override
	public String toString() {
		return "DocumentTemplateBo{" + "userId=" + userId +
				", name='" + name + '\'' +
				", category='" + category + '\'' +
				", institutionId=" + institutionId +
				", templateText='" + "<OMITTED>" + '\'' +
				'}';
	}
}
