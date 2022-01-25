package net.pladema.snvs.infrastructure.input.rest;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.snvs.application.ports.event.exceptions.SnvsStorageException;
import net.pladema.snvs.application.ports.report.exceptions.ReportPortException;
import net.pladema.snvs.application.reportproblems.ReportProblems;
import net.pladema.snvs.application.reportproblems.RetryReport;
import net.pladema.snvs.application.reportproblems.exceptions.ReportProblemException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.problem.SnvsProblemBo;
import net.pladema.snvs.domain.problem.exceptions.SnvsProblemBoException;
import net.pladema.snvs.domain.report.ReportCommandBo;
import net.pladema.snvs.domain.report.SnvsReportBo;
import net.pladema.snvs.domain.report.exceptions.ReportCommandBoException;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import net.pladema.snvs.infrastructure.input.rest.dto.SnvsReportDto;
import net.pladema.snvs.infrastructure.input.rest.dto.SnvsSnomedDto;
import net.pladema.snvs.infrastructure.input.rest.dto.SnvsToReportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/snvs")
@Tag(name = "Snvs", description = "Snvs")
@Conditional(SnvsCondition.class)
public class SnvsController {

    public static final String OUTPUT = "Output -> {}";

    private final Logger logger;

    private final ReportProblems reportProblems;

    private final RetryReport retryReport;

    public SnvsController(ReportProblems reportProblems, RetryReport retryReport){
        this.reportProblems = reportProblems;
        this.retryReport = retryReport;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostMapping(value = "/report")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SnvsReportDto> reportSnvs(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestBody List<SnvsToReportDto> toReportList) throws ReportProblemException,
            SnvsProblemBoException, ReportCommandBoException, SnvsEventInfoBoException, SnvsStorageException, ReportPortException {
        List<SnvsReportBo> resultService = reportProblems.run(buildCommand(toReportList, institutionId, patientId));
        logger.debug(OUTPUT, resultService);
        return mapReportsSnvsResponse(resultService);
    }
    private List<SnvsReportDto> mapReportsSnvsResponse(List<SnvsReportBo> resultService) {
        return resultService.stream().map(this::buildSnvsReportDto).collect(Collectors.toList());
    }

    private SnvsReportDto buildSnvsReportDto(SnvsReportBo snvsReportBo) {
        var result = new SnvsReportDto();
        result.setSisaRegisteredId(snvsReportBo.getSisaRegisteredId());
        result.setEventId(snvsReportBo.getEventId());
        result.setGroupEventId(snvsReportBo.getGroupEventId());
        result.setManualClassificationId(snvsReportBo.getManualClassificationId());
        result.setProblem(buildSnomedDto(snvsReportBo.getProblemBo()));
        result.setLastUpdate(buildDate(snvsReportBo.getLastUpdate()));
        result.setResponseCode(snvsReportBo.getResponseCode());
        result.setStatus(snvsReportBo.getStatus());
        return result;
    }

    private SnvsSnomedDto buildSnomedDto(SnvsProblemBo problemBo) {
        if (problemBo == null)
            return null;
        return new SnvsSnomedDto(problemBo.getSctid(), problemBo.getPt());
    }

    private DateDto buildDate(LocalDate lastUpdate) {
        if (lastUpdate == null)
            return null;
        return new DateDto(lastUpdate.getYear(), lastUpdate.getMonthValue(), lastUpdate.getDayOfMonth());

    }

    private List<ReportCommandBo> buildCommand(List<SnvsToReportDto> toReportList, Integer institutionId, Integer patientId) throws SnvsProblemBoException, ReportCommandBoException {
        List<ReportCommandBo> result = new ArrayList<>();
        for (SnvsToReportDto reportDto: toReportList)
            result.add(new ReportCommandBo(patientId, institutionId, reportDto.getManualClassificationId(),
                    reportDto.getGroupEventId(), reportDto.getEventId() , buildProblem(reportDto.getProblem())));
        return result;
    }

    private SnvsProblemBo buildProblem(SnvsSnomedDto problem) throws SnvsProblemBoException {
        return problem == null ? null : new SnvsProblemBo(problem.getSctid(), problem.getPt());
    }
}
