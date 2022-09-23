package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.InstitutionBo;
import ar.lamansys.refcounterref.domain.professionalperson.ProfessionalPersonBo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReferenceSummaryBo {

	private Integer referenceId;

	private Integer careLineId;

	private InstitutionBo institution;

	private LocalDate date;

	private ProfessionalPersonBo professional;

	public ReferenceSummaryBo(Integer referenceId, Integer institutionId, String institutionName,
							  LocalDate date, String firstName, String lastName,
							  String nameSelfDetermination, Integer careLineId) {
		this.referenceId = referenceId;
		this.institution = new InstitutionBo(institutionId, institutionName);
		this.date = date;
		this.professional = new ProfessionalPersonBo(firstName, nameSelfDetermination, lastName);
		this.careLineId = careLineId;
	}

}
