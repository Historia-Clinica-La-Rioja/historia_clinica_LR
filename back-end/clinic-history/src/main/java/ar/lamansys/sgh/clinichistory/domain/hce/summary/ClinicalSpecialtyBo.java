package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClinicalSpecialtyBo {

    private Integer id;

    private String name;

    private ECLinicalSpecialtyTypeBo clinicalSpecialtyType;

    private static final String FIX_NAME = "ERRÓNEA-";

    public ClinicalSpecialtyBo(Integer id, String name, Short clinicalSpecialtyTypeId) {
        this.id = id;
        this.name = name;
        this.clinicalSpecialtyType = ECLinicalSpecialtyTypeBo.map(clinicalSpecialtyTypeId);
        fixSpecialtyType();
    }

    public boolean isSpecialty(){
        return clinicalSpecialtyType.equals(ECLinicalSpecialtyTypeBo.SPECIALTY);
    }

    private void fixSpecialtyType(){
        if(!isSpecialty() && !name.contains(FIX_NAME))
            setName(FIX_NAME.concat(getName()));
    }
}
