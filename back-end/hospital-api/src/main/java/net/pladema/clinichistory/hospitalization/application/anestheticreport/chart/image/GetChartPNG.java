package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.exceptions.AnestheticReportException;
import net.pladema.clinichistory.hospitalization.domain.exceptions.AnestheticReportEnumException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GetChartPNG implements GetChartImage {

    public String getFileExtension() {
        return ".png";
    }

    public String run(JFreeChart chart, int width, int height) {
        log.debug("Input parameters -> chart {}, width {}, height {}", chart, width, height);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(outputStream, chart, width, height);
            byte[] pngBytes = outputStream.toByteArray();

            String base64String = Base64.getEncoder().encodeToString(pngBytes);

            log.debug("Output -> chart encoded to base64");
            return String.format("data:image/png;base64,%s", base64String);

        } catch (IOException e) {
            throw new AnestheticReportException(AnestheticReportEnumException.GRAPHIC_NOT_RENDERED, "No se ha podido generar el gráfico asociado al parte anestésico");
        }
    }
}
