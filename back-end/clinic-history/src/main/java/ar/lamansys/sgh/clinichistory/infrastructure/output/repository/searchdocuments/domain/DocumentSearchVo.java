package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentSearchVo {

    private Long id;

    private LocalDateTime createdOn;

    private PersonDataVo creator;

    private List<String> diagnosis = new ArrayList<>();

    private String mainDiagnosis;

    private String documentType;

	private Long initDocumentId;

	private String statusId;


    public DocumentSearchVo(Long id, LocalDateTime createdOn, Integer creatorUserId,
                            String firstName, String lastName, List<String> diagnosis, String mainDiagnosis,
							String documentType, String nameSelfDetermination, Long initDocumentId, String statusId){
        this.id = id;
        this.createdOn = createdOn;
        this.creator = new PersonDataVo(creatorUserId, firstName, lastName, nameSelfDetermination);
        this.diagnosis = diagnosis;
        this.mainDiagnosis = mainDiagnosis;
        this.documentType = documentType;
		this.initDocumentId = initDocumentId;
		this.statusId = statusId;
    }

	public boolean isFinal() {
		return statusId.equals(DocumentStatus.FINAL);
	}

}
