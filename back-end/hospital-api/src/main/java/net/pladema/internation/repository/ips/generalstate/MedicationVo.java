package net.pladema.internation.repository.ips.generalstate;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.Snomed;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicationVo {

    public Integer id;

    private String statusId;

    private Snomed snomed;

    private String note;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        MedicationVo medicationVo = (MedicationVo)o;
        return Objects.equals(id, medicationVo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
