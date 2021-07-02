package ar.lamansys.immunization.domain.doctor;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class DoctorInfoBo {

    private final Integer id;

    private final List<ClinicalSpecialtyBo> specialties;

    public DoctorInfoBo(Integer id) {
        this.id = id;
        this.specialties = Collections.emptyList();
    }

    public DoctorInfoBo(Integer id, List<ClinicalSpecialtyBo> specialties) {
        this.id = id;
        this.specialties = specialties;
    }

    public boolean hasSpecialty(Integer clinicalSpecialtyId){
        return specialties.stream().anyMatch(clinicalSpecialtyBo -> clinicalSpecialtyBo.getId().equals(clinicalSpecialtyId));
    }
}
