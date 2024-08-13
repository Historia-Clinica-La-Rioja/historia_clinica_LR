package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EAnestheticReportException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.exceptions.AnestheticReportException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Primary
@Component
public class GetChartPNG implements GetChartImage {

    public String run(JFreeChart chart, int width, int height) {
        log.debug("Input parameters -> chart {}, width {}, height {}", chart, width, height);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(outputStream, chart, width, height);
            byte[] pngBytes = outputStream.toByteArray();

            String base64String = Base64.getEncoder().encodeToString(pngBytes);

            log.debug("Output -> chart encoded to base64");
            return String.format("data:image/png;base64,%s", base64String);

        } catch (IOException e) {
            throw new AnestheticReportException(EAnestheticReportException.GRAPHIC_NOT_RENDERED, "No se ha podido generar el gráfico asociado al parte anestésico");
        }
    }
}
