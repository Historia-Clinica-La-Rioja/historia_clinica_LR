package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MeasuringPointRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MeasuringPoint;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadMeasuringPoints {

    private final MeasuringPointRepository measuringPointRepository;
    private final DocumentService documentService;

    public List<MeasuringPointBo> run(Long documentId, List<MeasuringPointBo> measuringPoints) {
        log.debug("Input parameters -> documentId {} measuringPoints {}", documentId, measuringPoints);

        measuringPoints.stream()
                .filter(this::hasToSaveEntity)
                .forEach(measuringPointBo -> this.createMeasuringPoint(documentId, measuringPointBo));

        log.debug("Output -> {}", measuringPoints);
        return measuringPoints;
    }

    private boolean hasToSaveEntity(MeasuringPointBo measuringPointBo) {
        return nonNull(measuringPointBo.getBloodPressureMin())
                || nonNull(measuringPointBo.getBloodPressureMax())
                || nonNull(measuringPointBo.getBloodPulse())
                || nonNull(measuringPointBo.getO2Saturation())
                || nonNull(measuringPointBo.getCo2EndTidal());
    }

    private void createMeasuringPoint(Long documentId, MeasuringPointBo measuringPointBo) {
        if (measuringPointBo.getId() == null) {
            this.saveEntity(documentId, measuringPointBo);
        }
        documentService.createDocumentMeasuringPoint(documentId, measuringPointBo.getId());
    }

    private void saveEntity(Long documentId, MeasuringPointBo measuringPointBo) {

        LocalDate date = measuringPointBo.getDate();
        LocalTime time = measuringPointBo.getTime();
        Integer bloodPressureMin = measuringPointBo.getBloodPressureMin();
        Integer bloodPressureMax = measuringPointBo.getBloodPressureMax();
        Integer bloodPulse = measuringPointBo.getBloodPulse();
        Integer o2Saturation = measuringPointBo.getO2Saturation();
        Integer co2EndTidal = measuringPointBo.getCo2EndTidal();

        MeasuringPoint measuringPoint = measuringPointRepository.save(new MeasuringPoint(
                null, documentId, date, time, bloodPressureMin, bloodPressureMax, bloodPulse, o2Saturation, co2EndTidal));

        measuringPointBo.setId(measuringPoint.getId());
    }
}
