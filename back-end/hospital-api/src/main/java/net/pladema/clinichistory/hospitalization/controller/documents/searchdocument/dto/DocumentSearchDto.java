package net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ProcedureReduced;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentObservationsDto;
import net.pladema.clinichistory.hospitalization.controller.dto.ResponsibleDoctorDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class DocumentSearchDto implements Serializable {

    private Long id;

    private DocumentObservationsDto notes;

    private String mainDiagnosis;

    private List<String> diagnosis;

    private List<ProcedureReduced> procedures;

    private ResponsibleDoctorDto creator;

    private DateTimeDto createdOn;

    private String message;

    private String documentType;

	private DateTimeDto editedOn;
    
	private boolean confirmed;

}
