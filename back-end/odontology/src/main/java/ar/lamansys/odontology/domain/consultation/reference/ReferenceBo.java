package ar.lamansys.odontology.domain.consultation.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ReferenceBo {

	private String note;

	private Boolean consultation;

	private Boolean procedure;

	private Integer careLineId;

	private List<Integer> clinicalSpecialtyIds;

	private List<ReferenceProblemBo> problems;

	private List<Integer> fileIds;

	private Integer destinationInstitutionId;

	private String phoneNumber;

	private String phonePrefix;

	private Integer priority;

	private ReferenceStudyBo study;

}
