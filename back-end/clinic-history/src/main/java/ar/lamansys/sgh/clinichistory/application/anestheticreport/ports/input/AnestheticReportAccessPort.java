package ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.input;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto.AnestheticReportDto;

import java.util.Optional;

public interface AnestheticReportAccessPort {

    Integer createAnestheticReportDocument(AnestheticReportDto anestheticReportDto);

    Optional<AnestheticReportDto> get(Long documentId);

}
