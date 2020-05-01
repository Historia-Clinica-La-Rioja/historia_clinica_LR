package net.pladema.internation.repository.ips.generalstate;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.Snomed;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicationVo extends ClinicalTermVo{

    private Long noteId;

    private String note;

    public MedicationVo(Integer id, Snomed snomed, String statusId, Long noteId,
                          String note) {
        super(id, snomed, statusId);
        this.noteId = noteId;
        this.note = note;
    }
}
