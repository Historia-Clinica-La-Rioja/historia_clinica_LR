package net.pladema.clinichistory.mocks;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.repository.entity.DocumentLab;
import net.pladema.clinichistory.documents.repository.entity.DocumentVitalSign;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationLab;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationVitalSign;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ObservationStatus;

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
