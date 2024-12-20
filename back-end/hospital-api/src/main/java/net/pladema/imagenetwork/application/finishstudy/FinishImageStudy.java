package net.pladema.imagenetwork.application.finishstudy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportRDIService;
import net.pladema.imagenetwork.application.equipmentchangestate.EquipmentChangeState;
import net.pladema.imagenetwork.derivedstudies.service.MoveStudiesService;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.domain.DetailsOrderImageBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FinishImageStudy {

    private final AppointmentOrderImageService appointmentOrderImageService;
    private final MoveStudiesService moveStudiesService;
    private final EquipmentChangeState equipmentChangeState;
    private final CompleteDiagnosticReportRDIService completeDiagnosticReportRDIService;

    @Transactional
    public boolean run(Integer institutionId, DetailsOrderImageBo detailsOrderImageBo) {
        log.debug("Input parameters -> detailsOrderImageBo {}", detailsOrderImageBo);
        Integer appointmentId = detailsOrderImageBo.getAppointmentId();
        String reason = "Técnico finaliza el estudio";
        Integer patientId = detailsOrderImageBo.getPatientId();

        appointmentOrderImageService.updateCompleted(detailsOrderImageBo);
        Integer idMove = moveStudiesService.create(appointmentId, institutionId);
        moveStudiesService.publishSizeFromOrchestrator(idMove);

        equipmentChangeState.run(institutionId, appointmentId, AppointmentState.SERVED, reason);

        completeDiagnosticReportRDIService.run(patientId, appointmentId);

        return true;
    }
}
