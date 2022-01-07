package net.pladema.staff.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Integer professionalSpecialtyId;

    private Integer clinicalSpecialtyId;

    @Nullable
    private Boolean deleted;

    public HealthcareProfessionalSpecialtyBo(Integer id,
                                             Integer healthcareProfessionalId,
                                             Integer professionalSpecialtyId,
                                             Integer clinicalSpecialtyId) {
        this.id = id;
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.professionalSpecialtyId = professionalSpecialtyId;
        this.clinicalSpecialtyId = clinicalSpecialtyId;
    }

    public HealthcareProfessionalSpecialtyBo(Integer healthcareProfessionalId,
                                             Integer professionalSpecialtyId,
                                             Integer clinicalSpecialtyId) {
        this(null, healthcareProfessionalId, professionalSpecialtyId, clinicalSpecialtyId);
    }

    public boolean hasToBeDeleted(Integer id){
        return (this.id!=null&&!Objects.equals(id, this.id));
    }
}
