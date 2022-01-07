package ar.lamansys.refcounterref.domain.doctor;

import ar.lamansys.refcounterref.domain.clinicalspecialty.ClinicalSpecialtyBo;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class CounterReferenceDoctorInfoBo {

    private final Integer id;

    private final List<ClinicalSpecialtyBo> specialties;

    public CounterReferenceDoctorInfoBo(Integer id) {
        this.id = id;
        this.specialties = Collections.emptyList();
    }

    public CounterReferenceDoctorInfoBo(Integer id, List<ClinicalSpecialtyBo> specialties) {
        this.id = id;
        this.specialties = specialties;
    }

    public boolean hasSpecialty(Integer clinicalSpecialtyId){
        return specialties.stream().anyMatch(clinicalSpecialtyBo -> clinicalSpecialtyBo.getId().equals(clinicalSpecialtyId));
    }
}