package ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork;

import java.util.Optional;

public interface SharedDiagnosticImagingOrder {
    Optional<Integer> getDiagnosticImagingOrderAuthorId(Integer appointmentId);
    Optional<String> getDiagnosticImagingTranscribedOrderAuthor(Integer transcribedOrderId);
}
