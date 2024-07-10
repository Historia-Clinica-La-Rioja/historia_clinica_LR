package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

    private String mainDiagnosis;

    private List<String> diagnosis;

    private ResponsibleDoctorDto creator;

    private DateTimeDto createdOn;

    private String documentType;

	private DateTimeDto editedOn;
    
	private boolean confirmed;

}
