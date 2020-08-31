package net.pladema.clinichistory.documents.repository.searchdocument;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.DocumentObservationsVo;
import net.pladema.person.repository.domain.PersonNameVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentSearchVo {

    private Long id;

    private DocumentObservationsVo notes;

    private LocalDate createdOn;

    private PersonNameVo creator;

    private List<String> diagnosis = new ArrayList<>();

    private List<String> procedures = new ArrayList<>();

    private String mainDiagnosis;


    public DocumentSearchVo(Long id, DocumentObservationsVo notes, LocalDateTime createdOn,
                            String firstName, String lastName, List<String> diagnosis, String mainDiagnosis){
        this.id = id;
        this.notes = notes;
        this.createdOn = createdOn.toLocalDate();
        this.creator = new PersonNameVo(firstName, lastName);
        this.diagnosis = diagnosis;
        this.mainDiagnosis = mainDiagnosis;

    }
}
