package ar.lamansys.sgh.clinichistory.mocks;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;

import java.time.LocalDateTime;

public class ClinicalObservationTestMocks {

    public static ObservationLab createObservationLab(Integer snomedId, String error, LocalDateTime now) {
        ObservationLab result = new ObservationLab();
        result.setPatientId(1);
        result.setSnomedId(snomedId);
        result.setStatusId(error);
        result.setCategoryId("category");
        result.setValue("Lab Value");
        result.setEffectiveTime(now);
        return result;
    }

    public static ObservationRiskFactor createObservationRiskFactor(Integer snomedId, String error, LocalDateTime now) {
        ObservationRiskFactor observationRiskFactor = new ObservationRiskFactor();
        observationRiskFactor.setPatientId(1);
        observationRiskFactor.setSnomedId(snomedId);
        observationRiskFactor.setStatusId(error);
        observationRiskFactor.setCategoryId("category");
        observationRiskFactor.setValue("Risk factor Value");
        observationRiskFactor.setEffectiveTime(now);
        return observationRiskFactor;
    }

    public static DocumentRiskFactor createDocumentRiskFactor(Document doc, ObservationRiskFactor obs){
        return new DocumentRiskFactor(doc.getId(), obs.getId());
    }

    public static ObservationRiskFactor createFinalObservationRiskFactor(Integer snomedId, LocalDateTime now) {
        ObservationRiskFactor observationRiskFactor = createObservationRiskFactor(snomedId, ObservationStatus.FINAL, now);
        return observationRiskFactor;
    }

    public static ObservationRiskFactor createErrorObservationRiskFactor(Integer snomedId, LocalDateTime now) {
        ObservationRiskFactor observationRiskFactor = createObservationRiskFactor(snomedId, ObservationStatus.ERROR, now);
        return observationRiskFactor;
    }

    public static DocumentLab createDocumentLab(Document doc, ObservationLab obs){
        return new DocumentLab(doc.getId(), obs.getId());
    }

    public static ObservationLab createFinalObservationLab(Integer snomedId, LocalDateTime now) {
        ObservationLab observationLab = createObservationLab(snomedId, ObservationStatus.FINAL, now);
        return observationLab;
    }
}
