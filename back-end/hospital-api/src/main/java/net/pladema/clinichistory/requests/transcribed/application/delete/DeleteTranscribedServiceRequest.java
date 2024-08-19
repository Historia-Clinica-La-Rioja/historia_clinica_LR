package net.pladema.clinichistory.requests.transcribed.application.delete;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo;
import net.pladema.clinichistory.requests.transcribed.application.port.TranscribedServiceRequestStorage;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.OrderImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteTranscribedServiceRequest {

    private final OrderImageFileRepository orderImageFileRepository;
    private final TranscribedServiceRequestStorage transcribedServiceRequestStorage;

    @Transactional
    public Integer run(Integer transcribedOrderId) {
        log.debug("Input parameters -> transcribedOrderId {}", transcribedOrderId);
        this.deleteImages(transcribedOrderId);
        this.deleteTranscribedOrder(transcribedOrderId);
        log.debug("Output -> deleted transcribed order id {}", transcribedOrderId);
        return null;
    }

    private void deleteImages(Integer orderId) {
        List<FileVo> images = orderImageFileRepository.getFilesByOrderId(orderId);
        for (FileVo image : images) {
            orderImageFileRepository.deleteById(image.getFileId());
        }
    }

    private void deleteTranscribedOrder(Integer transcribedOrderId) {
        transcribedServiceRequestStorage.getDiagnosticReports(transcribedOrderId)
                .stream()
                .map(DiagnosticReportBo::getId)
                .forEach(diagnosticReportId -> transcribedServiceRequestStorage.deleteDiagnosticReportIdRelatedTo(transcribedOrderId, diagnosticReportId));
        transcribedServiceRequestStorage.deleteById(transcribedOrderId);
    }
}
