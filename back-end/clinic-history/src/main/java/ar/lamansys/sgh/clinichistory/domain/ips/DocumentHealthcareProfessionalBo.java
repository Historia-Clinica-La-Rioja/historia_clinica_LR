package ar.lamansys.sgh.clinichistory.domain.ips;


import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.EProfessionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentHealthcareProfessionalBo {

	@Nullable
	private Integer id;

	@NotNull
	private HealthcareProfessionalBo healthcareProfessional;

	@NotNull
	private EProfessionType professionType;

	private String otherProfessionTypeDescription;

	@Nullable
	private String comments;

	public DocumentHealthcareProfessionalBo(Integer id, Integer healthcareProfessionalId, EProfessionType type, String comments){
		this.id = id;
		this.healthcareProfessional = new HealthcareProfessionalBo(healthcareProfessionalId);
		this.professionType = type;
		this.comments = comments;
	}

	public DocumentHealthcareProfessionalBo(Integer id, Integer healthcareProfessionalId, String licenceNumber,
											Integer personId, String firstName, String lastName, String identificationNumber,
											String nameSelfDetermination,String middleNames,String otherLastNames, Short type,
											String otherProfessionTypeDescription, String comments){
		this.id = id;
		this.healthcareProfessional = new HealthcareProfessionalBo(healthcareProfessionalId, licenceNumber, personId, firstName, lastName, identificationNumber, nameSelfDetermination, middleNames, otherLastNames);
		this.professionType = EProfessionType.map(type);
		this.otherProfessionTypeDescription = otherProfessionTypeDescription;
		this.comments = comments;
	}

}
