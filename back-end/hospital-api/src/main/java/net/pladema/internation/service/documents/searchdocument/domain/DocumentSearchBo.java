package net.pladema.internation.service.documents.searchdocument.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.documents.searchdocument.DocumentSearchVo;
import net.pladema.internation.service.internment.summary.domain.ResponsibleDoctorBo;
import net.pladema.internation.service.ips.domain.DocumentObservationsBo;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchBo {

    private Long id;

    private DocumentObservationsBo notes;

    private String mainDiagnosis;

    private List<String> diagnosis;

    private ResponsibleDoctorBo creator;

    private LocalDate createdOn;

    public DocumentSearchBo(DocumentSearchVo source) {
        this.id = source.getId();
        this.notes = new DocumentObservationsBo(source.getNotes());
        this.mainDiagnosis = source.getMainDiagnosis();
        this.diagnosis = source.getDiagnosis();
        this.creator = new ResponsibleDoctorBo(source.getCreator().getFirstName(), source.getCreator().getLastName());
        this.createdOn = source.getCreatedOn();
    }
}
