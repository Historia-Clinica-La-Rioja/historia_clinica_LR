package ar.lamansys.sgh.clinichistory.mocks;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentVitalSign;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationVitalSign;
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

    public static ObservationVitalSign createObservationVitalSign(Integer snomedId, String error, LocalDateTime now) {
        ObservationVitalSign observationVitalSign = new ObservationVitalSign();
        observationVitalSign.setPatientId(1);
        observationVitalSign.setSnomedId(snomedId);
        observationVitalSign.setStatusId(error);
        observationVitalSign.setCategoryId("category");
        observationVitalSign.setValue("Vital sign Value");
        observationVitalSign.setEffectiveTime(now);
        return observationVitalSign;
    }

    public static DocumentVitalSign createDocumentVitalSign(Document doc, ObservationVitalSign obs){
        return new DocumentVitalSign(doc.getId(), obs.getId());
    }

    public static ObservationVitalSign createFinalObservationVitalSign(Integer snomedId, LocalDateTime now) {
        ObservationVitalSign observationVitalSign = createObservationVitalSign(snomedId, ObservationStatus.FINAL, now);
        return observationVitalSign;
    }

    public static ObservationVitalSign createErrorObservationVitalSign(Integer snomedId, LocalDateTime now) {
        ObservationVitalSign observationVitalSign = createObservationVitalSign(snomedId, ObservationStatus.ERROR, now);
        return observationVitalSign;
    }

    public static DocumentLab createDocumentLab(Document doc, ObservationLab obs){
        return new DocumentLab(doc.getId(), obs.getId());
    }

    public static ObservationLab createFinalObservationLab(Integer snomedId, LocalDateTime now) {
        ObservationLab observationLab = createObservationLab(snomedId, ObservationStatus.FINAL, now);
        return observationLab;
    }
}
