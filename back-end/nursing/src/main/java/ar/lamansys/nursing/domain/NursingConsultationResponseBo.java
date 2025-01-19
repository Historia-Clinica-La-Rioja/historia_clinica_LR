package ar.lamansys.nursing.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class NursingConsultationResponseBo {
	private Integer encounterId;

	private List<Integer> orderIds;
}
