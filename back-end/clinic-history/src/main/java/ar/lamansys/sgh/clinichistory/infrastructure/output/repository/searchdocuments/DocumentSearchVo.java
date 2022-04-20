package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ProcedureReduced;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.DocumentObservationsVo;

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

    private List<ProcedureReduced> procedures = new ArrayList<>();

    private String mainDiagnosis;

    private String documentType;


    public DocumentSearchVo(Long id, DocumentObservationsVo notes, LocalDateTime createdOn,
                            String firstName, String lastName, List<String> diagnosis, String mainDiagnosis,
							String documentType){
        this.id = id;
        this.notes = notes;
        this.createdOn = createdOn.toLocalDate();
        this.creator = new PersonNameVo(firstName, lastName);
        this.diagnosis = diagnosis;
        this.mainDiagnosis = mainDiagnosis;
        this.documentType = documentType;

    }
}
