package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.CreateAnestheticReportDocument;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.GetAnestheticReport;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.input.AnestheticReportAccessPort;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto.AnestheticReportDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.mapper.AnestheticReportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnestheticReportPortAccessImpl implements AnestheticReportAccessPort {

    private final GetAnestheticReport getAnestheticReport;
    private final CreateAnestheticReportDocument createAnestheticReportDocument;
    private final AnestheticReportMapper anestheticReportMapper;

    @Override
    public Integer createAnestheticReportDocument(AnestheticReportDto anestheticReportDto) {
        log.debug("Input parameters -> AnestheticReportDto {}", anestheticReportDto);
        AnestheticReportBo anestheticReportBo = anestheticReportMapper.fromAnestheticReportDto(anestheticReportDto);
        var result = createAnestheticReportDocument.run(anestheticReportBo);
        log.debug("Ouput -> {}", result);
        return result;
    }

    @Override
    public Optional<AnestheticReportDto> get(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        var result = Optional.ofNullable(getAnestheticReport.run(documentId))
                .map(anestheticReportMapper::fromAnestheticReportBo);
        log.debug("Ouput -> {}", result);
        return result;
    }
}
