package net.pladema.internation.repository.ips.generalstate;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.Snomed;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InmunizationVo extends ClinicalTermVo{

    private LocalDate administrationDate;

    private Long noteId;

    private String note;

    public InmunizationVo(Integer id, Snomed snomed, String statusId, LocalDate administrationDate, Long noteId,
                              String note) {
        super(id, snomed, statusId);
        this.administrationDate = administrationDate;
        this.noteId = noteId;
        this.note = note;
    }
}
