package net.pladema.clinichistory.mocks;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.repository.entity.DocumentLab;
import net.pladema.clinichistory.documents.repository.entity.DocumentVitalSign;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationLab;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationVitalSign;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ObservationStatus;

import java.time.LocalDateTime;

public class ClinicalObservationTestMocks {

    public static ObservationLab createObservationLab(String sctId, String error, LocalDateTime now) {
        ObservationLab result = new ObservationLab();
        result.setPatientId(1);
        result.setSctidCode(sctId);
        result.setStatusId(error);
        result.setCategoryId("category");
        result.setValue("Lab Value");
        result.setEffectiveTime(now);
        return result;
    }

    public static ObservationVitalSign createObservationVitalSign(String sctId, String error, LocalDateTime now) {
        ObservationVitalSign observationVitalSign = new ObservationVitalSign();
        observationVitalSign.setPatientId(1);
        observationVitalSign.setSctidCode(sctId);
        observationVitalSign.setStatusId(error);
        observationVitalSign.setCategoryId("category");
        observationVitalSign.setValue("Vital sign Value");
        observationVitalSign.setEffectiveTime(now);
        return observationVitalSign;
    }

    public static DocumentVitalSign createDocumentVitalSign(Document doc, ObservationVitalSign obs){
        return new DocumentVitalSign(doc.getId(), obs.getId());
    }

    public static ObservationVitalSign createFinalObservationVitalSign(String sctId, LocalDateTime now) {
        ObservationVitalSign observationVitalSign = createObservationVitalSign(sctId, ObservationStatus.FINAL, now);
        return observationVitalSign;
    }

    public static ObservationVitalSign createErrorObservationVitalSign(String sctId, LocalDateTime now) {
        ObservationVitalSign observationVitalSign = createObservationVitalSign(sctId, ObservationStatus.ERROR, now);
        return observationVitalSign;
    }

    public static DocumentLab createDocumentLab(Document doc, ObservationLab obs){
        return new DocumentLab(doc.getId(), obs.getId());
    }

    public static ObservationLab createFinalObservationLab(String sctId, LocalDateTime now) {
        ObservationLab observationLab = createObservationLab(sctId, ObservationStatus.FINAL, now);
        return observationLab;
    }
}
