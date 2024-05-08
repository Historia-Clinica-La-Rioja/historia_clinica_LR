package net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ProcedureReduced;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentObservationsDto;
import net.pladema.clinichistory.hospitalization.controller.dto.ResponsibleDoctorDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentSearchDto implements Serializable {

    private static final long serialVersionUID = 8690562999821021073L;

    private Long id;

    @Deprecated
    private DocumentObservationsDto notes;

    private String mainDiagnosis;

    private List<String> diagnosis;

    @Deprecated
    private List<ProcedureReduced> procedures;

    private ResponsibleDoctorDto creator;

    private DateTimeDto createdOn;

    private String message;

    private String documentType;

	private DateTimeDto editedOn;
    
	private boolean confirmed;

}
