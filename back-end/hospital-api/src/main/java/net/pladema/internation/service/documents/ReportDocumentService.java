package net.pladema.internation.service.documents;

import net.pladema.internation.service.internment.domain.ResponsibleDoctorBo;
import net.pladema.internation.service.ips.domain.*;

import java.util.List;

public interface ReportDocumentService {

    GeneralHealthConditionBo getReportHealthConditionFromDocument(Long documentId);

    List<InmunizationBo> getReportInmunizationStateFromDocument(Long documentId);

    List<AllergyConditionBo> getReportAllergyIntoleranceStateFromDocument(Long documentId);

    List<MedicationBo> getReportMedicationStateFromDocument(Long documentId);

    AnthropometricDataBo getReportAnthropometricDataStateFromDocument(Long documentId);

    VitalSignBo getReportVitalSignStateFromDocument(Long documentId);

    ResponsibleDoctorBo getAuthor(Long documentId);

}

