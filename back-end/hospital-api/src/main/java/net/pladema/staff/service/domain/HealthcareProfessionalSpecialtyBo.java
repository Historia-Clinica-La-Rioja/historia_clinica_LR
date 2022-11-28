package net.pladema.staff.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.staff.repository.domain.HealthcareProfessionalSpecialtyVo;

import javax.annotation.Nullable;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareProfessionalSpecialtyBo {

    @Nullable
    private Integer id;

    private Integer healthcareProfessionalId;

    private Integer professionalProfessionId;

    private ClinicalSpecialtyBo clinicalSpecialty;

    @Nullable
    private Boolean deleted;

    public HealthcareProfessionalSpecialtyBo(Integer id,
                                             Integer healthcareProfessionalId,
                                             Integer professionalProfessionId,
											 ClinicalSpecialtyBo clinicalSpecialty) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.professionalProfessionId = professionalProfessionId;
        this.clinicalSpecialty = clinicalSpecialty;
    }

    public HealthcareProfessionalSpecialtyBo(Integer healthcareProfessionalId,
                                             Integer professionalProfessionId,
											 ClinicalSpecialtyBo clinicalSpecialty) {
        this(null, healthcareProfessionalId, professionalProfessionId, clinicalSpecialty);
    }

	public boolean equalsClinicalSpecialty(HealthcareProfessionalSpecialtyBo bo){
		return this.clinicalSpecialty.getId().equals(bo.getClinicalSpecialty().getId());
	}
    
	public HealthcareProfessionalSpecialtyBo(HealthcareProfessionalSpecialtyVo vo){
		this.id = vo.getId();
		this.healthcareProfessionalId = vo.getHealthcareProfessionalId();
		this.professionalProfessionId = vo.getProfessionalProfessionId();
		this.clinicalSpecialty = new ClinicalSpecialtyBo(vo.getClinicalSpecialtyId(), vo.getClinicalSpecialtyName());
	}

}
