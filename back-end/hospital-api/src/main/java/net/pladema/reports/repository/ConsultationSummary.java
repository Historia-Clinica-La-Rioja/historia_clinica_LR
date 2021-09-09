package net.pladema.reports.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.repository.entity.ClinicalSpecialtyType;

@Getter
@NoArgsConstructor
public class ConsultationSummary {

    private Integer professionalId;
    private Integer specialtyId;
    private String specialty;
    private Integer age;
    private Short genderId;
    private boolean coverage;

    public ConsultationSummary(Integer professionalId, Integer specialtyId, String specialty,
                               Short specialtyType, Integer age, Short genderId, boolean coverage){
        this.professionalId = professionalId;
        this.specialtyId = specialtyId != null ? specialtyId : 0;
        fixSpecialtyName(specialty, specialtyType);
        this.age = age;
        this.genderId = genderId;
        this.coverage = coverage;
    }

    private void fixSpecialtyName(String name, Short type){
        if(name != null){
            if(type.equals(ClinicalSpecialtyType.Service) && !name.contains(ClinicalSpecialty.FIX_NAME))
                name = ClinicalSpecialty.FIX_NAME.concat(name);
            this.specialty = name.toUpperCase();
        }
    }

    public boolean hasGender(){
        return genderId != null;
    }

    public boolean hasBirthdate(){
        return age != null;
    }

    public int getAgeRange(){
        int index=4;
        if(age < 1)
            return index;
        if(age < 5)
            return index+2;
        if(age < 10)
            return index+4;
        if(age < 15)
            return index+6;
        if(age < 20)
            return index+8;
        if(age < 35)
            return index+10;
        if(age < 50)
            return index+12;
        if(age < 65)
            return index+14;
        return index+16;
    }
}
