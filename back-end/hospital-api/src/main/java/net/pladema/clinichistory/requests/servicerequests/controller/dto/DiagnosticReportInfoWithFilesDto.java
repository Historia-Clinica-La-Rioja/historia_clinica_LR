package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.FileDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiagnosticReportInfoWithFilesDto extends DiagnosticReportInfoDto {
    private List<FileDto> files;

    public DiagnosticReportInfoWithFilesDto(DiagnosticReportInfoDto diagnosticReportInfoDto, List<FileDto> files) {
        super(diagnosticReportInfoDto);
        this.setFiles(files);
    }
}
