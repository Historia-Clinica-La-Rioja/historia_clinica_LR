package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnestheticSubstanceType;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnitsOfTimeBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.GenerateAnestheticChart;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.ParsePointsToTimeSeries;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.SaveAnestheticChartAsImage;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.image.GetChartImage;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.image.enums.EImageFileExtension;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.AnestheticReportValidator;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PatientNotFoundException;
import net.pladema.patient.controller.service.PatientExternalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateAnestheticReportDocument {

    private final int DEFAULT_WIDTH = 700;
    private final int DEFAULT_HEIGHT = 420;
    private final boolean SAVE_CHART_IMAGE = false;
    private final EImageFileExtension IMAGE_FILE_EXTENSION = EImageFileExtension.PNG;

    private final GetChartImage getChartImage;
    private final PatientExternalService patientExternalService;
    private final InternmentEpisodeService internmentEpisodeService;
    private final AnestheticReportValidator anestheticReportValidator;
    private final DocumentFactory documentFactory;
    private final AnestheticStorage anestheticStorage;
    private final ParsePointsToTimeSeries parsePointsToTimeSeries;
    private final GenerateAnestheticChart generateAnestheticChart;
    private final SaveAnestheticChartAsImage saveAnestheticChartAsImage;

    @Transactional
    public Integer run(IDocumentBo document) {
        AnestheticReportBo anestheticReport = (AnestheticReportBo) document;
        log.debug("Input parameter -> anestheticReport {}", anestheticReport);

        this.completeValuesAnestheticReport(anestheticReport);

        anestheticReportValidator.assertContextValid(anestheticReport);

        this.generateChart(anestheticReport);

        documentFactory.run(anestheticReport, anestheticReport.isConfirmed());

        Integer result = anestheticStorage.save(anestheticReport);

        log.debug("Output -> saved anestheticReport id {}", result);
        return result;
    }

    private void generateChart(AnestheticReportBo anestheticReport) {
        var points = anestheticReport.getMeasuringPoints();

        if (!anestheticReport.isConfirmed()) {
            log.debug("Output -> chart not generated because document is not confirmed");
            return;
        }

        if (points.isEmpty()) {
            log.debug("Output -> empty chart");
            return;
        }
        var datasets = parsePointsToTimeSeries.run(anestheticReport.getMeasuringPoints());
        var jFreechart = generateAnestheticChart.run(datasets);
        var encodedChart = getChartImage.run(jFreechart, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        anestheticReport.setAnestheticChart(encodedChart);

        if (this.SAVE_CHART_IMAGE)
            saveAnestheticChartAsImage.run(jFreechart, anestheticReport.getInstitutionId(), anestheticReport.getEncounterId(), DEFAULT_WIDTH, DEFAULT_HEIGHT, IMAGE_FILE_EXTENSION.getDescription());
    }

    private void completeValuesAnestheticReport(AnestheticReportBo anestheticReport) {
        Integer encounterId = anestheticReport.getEncounterId();

        internmentEpisodeService.getPatient(encounterId)
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresentOrElse(patientInfo -> {
                    anestheticReport.setPatientInfo(patientInfo);
                    anestheticReport.setPatientId(patientInfo.getId());
                }, PatientNotFoundException::new);

        LocalDate entryDate = internmentEpisodeService.getEntryDate(encounterId).toLocalDate();
        anestheticReport.setPatientInternmentAge(entryDate);

        anestheticReport.setPerformedDate(LocalDateTime.now());

        this.setTypeSurgicalProcedures(anestheticReport.getSurgeryProcedures());

        this.setAnestheticSubstanceValues(anestheticReport.getPreMedications(), EAnestheticSubstanceType.PRE_MEDICATION.getId());

        this.setAnestheticSubstanceValues(anestheticReport.getAnestheticPlans(), EAnestheticSubstanceType.ANESTHETIC_PLAN.getId());

        this.setAnestheticSubstanceValues(anestheticReport.getAnalgesicTechniques(), EAnestheticSubstanceType.ANALGESIC_TECHNIQUE.getId());

        this.setAnestheticSubstanceValues(anestheticReport.getFluidAdministrations(), EAnestheticSubstanceType.FLUID_ADMINISTRATION.getId());

        this.setAnestheticSubstanceValues(anestheticReport.getAnestheticAgents(), EAnestheticSubstanceType.ANESTHETIC_AGENT.getId());

        this.setAnestheticSubstanceValues(anestheticReport.getNonAnestheticDrugs(), EAnestheticSubstanceType.NON_ANESTHETIC_DRUG.getId());

        this.setAnestheticSubstanceValues(anestheticReport.getAntibioticProphylaxis(), EAnestheticSubstanceType.ANTIBIOTIC_PROPHYLAXIS.getId());
    }

    private void setTypeSurgicalProcedures(List<ProcedureBo> surgicalProcedures) {
        surgicalProcedures
                .stream()
                .filter(Objects::nonNull)
                .forEach(surgicalProcedure -> surgicalProcedure.setType(ProcedureTypeEnum.SURGICAL_PROCEDURE));
    }

    private void setAnestheticSubstanceValues(List<? extends AnestheticSubstanceBo> substances, Short typeId) {
        substances.stream()
                .peek(substance -> substance.setTypeId(typeId))
                .map(AnestheticSubstanceBo::getDosage)
                .filter(Objects::nonNull)
                .forEach(dosageBo -> dosageBo.setPeriodUnit(EUnitsOfTimeBo.EVENT));
    }

}
