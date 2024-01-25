package net.pladema.violencereport.domain;

import ar.lamansys.sgh.shared.domain.FilterOptionBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ViolenceReportFilterOptionBo {

	private List<FilterOptionBo> situations;

	private List<FilterOptionBo> modalities;

	private List<FilterOptionBo> types;

	private List<FilterOptionBo> institutions;

}
