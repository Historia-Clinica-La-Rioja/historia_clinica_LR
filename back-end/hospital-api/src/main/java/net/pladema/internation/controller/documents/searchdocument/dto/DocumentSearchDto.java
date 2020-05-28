package net.pladema.internation.controller.documents.searchdocument.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.internment.dto.DocumentObservationsDto;
import net.pladema.internation.controller.internment.dto.ResponsibleDoctorDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class DocumentSearchDto implements Serializable {

    private Long id;

    private DocumentObservationsDto notes;

    private String mainDiagnosis;

    private List<String> diagnosis;

    private ResponsibleDoctorDto creator;

    private LocalDate createdOn;

    private String message;

}
