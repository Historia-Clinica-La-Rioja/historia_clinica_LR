package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentOrderImageExistCheckBo;

public interface ExistCheckDiagnosticReportService {
    AppointmentOrderImageExistCheckBo execute(Integer diagnosticReportId);
}
