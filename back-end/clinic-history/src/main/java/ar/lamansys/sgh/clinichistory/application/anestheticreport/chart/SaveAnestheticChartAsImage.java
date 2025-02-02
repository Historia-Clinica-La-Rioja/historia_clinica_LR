package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EAnestheticReportException;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgx.shared.files.images.ImageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.image.GetChartImage;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.exceptions.AnestheticReportException;
import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveAnestheticChartAsImage {
    private final String RELATIVE_DIRECTORY = "/institution/{institutionId}/{encounterType}/{encounterId}/{documentType}/";

    private final GetChartImage getChartImage;
    private final ImageFileService imageFileService;

    public String run(JFreeChart chart, Integer institutionId, Integer encounterId, int width, int height, String extension) {
        log.debug("Input parameters -> chart {}, institutionId {}, encounterId {}, width {}, height {}",
                chart, institutionId, encounterId, width, height);

        String imageData = getChartImage.run(chart, width, height);
        String uuid = imageFileService.createUuid();
        String relativePath = this.getRelativeDirectory()
                .replace("{institutionId}", institutionId.toString())
                .replace("{encounterType}", ESourceType.HOSPITALIZATION.getValue())
                .replace("{encounterId}", encounterId.toString())
                .replace("{documentType}", EDocumentType.ANESTHETIC_REPORT.toString())
                .concat(uuid)
                .concat(extension);

        var path = imageFileService.buildCompletePath(relativePath);

        boolean result = imageFileService.saveImage(path, uuid, "ANESTHETIC_CHART", imageData);

        if (!result)
            throw new AnestheticReportException(EAnestheticReportException.GRAPHIC_NOT_RENDERED, "No se ha podido generar el gráfico asociado al parte anestésico");

        return relativePath;
    }

    private String getRelativeDirectory() {
        return RELATIVE_DIRECTORY;
    }
}
