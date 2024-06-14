package net.pladema.medicalconsultation.appointment.application;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.application.port.AppointmentPatientIdentityAccreditationStatusPort;
import net.pladema.medicalconsultation.appointment.application.port.AppointmentPort;
import net.pladema.medicalconsultation.appointment.application.port.HistoricAppointmentStatePort;
import net.pladema.medicalconsultation.appointment.domain.UpdateAppointmentStateBo;
import net.pladema.medicalconsultation.appointment.infraestructure.output.internal.EncryptPatientIdentificationCode;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.domain.enums.EPatientIdentityAccreditationStatus;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;

import net.pladema.medicalconsultation.appointment.service.impl.AppointmentServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChangeAppointmentState {

	private final AppointmentPort appointmentPort;

	private final FeatureFlagsService featureFlagsService;

	private final HistoricAppointmentStatePort historicAppointmentStatePort;

	//No me gusta pero tiene un método utilitario de los que ya sabemos
	private final AppointmentServiceImpl appointmentService;

	private final AppointmentPatientIdentityAccreditationStatusPort appointmentPatientIdentityAccreditationStatusPort;

	private final EncryptPatientIdentificationCode encryptPatientIdentificationCode;

	@Transactional
	public boolean run(UpdateAppointmentStateBo updateAppointmentStateBo) {
		log.debug("Input parameters -> updateAppointmentStatusBo {}", updateAppointmentStateBo);
		checkAndHandleOnSiteAttention(updateAppointmentStateBo);
		checkAndHandleRecurringAppointment(updateAppointmentStateBo);
		appointmentPort.updateAppointmentState(updateAppointmentStateBo.getAppointmentId(), updateAppointmentStateBo.getAppointmentStateId());
		historicAppointmentStatePort.save(updateAppointmentStateBo);
		log.debug("Output -> {}", Boolean.TRUE);
		return Boolean.TRUE;
	}

	private void checkAndHandleOnSiteAttention(UpdateAppointmentStateBo updateAppointmentStateBo) {
		Short appointmentModalityId = appointmentPort.getAppointmentModalityById(updateAppointmentStateBo.getAppointmentId());
		if (appointmentModalityId.equals(EAppointmentModality.ON_SITE_ATTENTION.getId()))
			handleOnSiteAttention(updateAppointmentStateBo);
	}

	private void handleOnSiteAttention(UpdateAppointmentStateBo updateAppointmentStateBo) {
		checkAndHandleIfPreviousStateWasConfirmed(updateAppointmentStateBo);
		checkAndHandleIfNewStateIsConfirmed(updateAppointmentStateBo);
	}

	private void checkAndHandleRecurringAppointment(UpdateAppointmentStateBo updateAppointmentStateBo) {
		if (featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO))
			handleRecurringAppointment(updateAppointmentStateBo);
	}

	private void handleRecurringAppointment(UpdateAppointmentStateBo updateAppointmentStateBo) {
		Integer appointmentParentId = appointmentPort.getAppointmentParentId(updateAppointmentStateBo.getAppointmentId());
		if (appointmentParentId != null)
			appointmentService.checkRemainingChildAppointments(appointmentParentId);
	}

	private void checkAndHandleIfNewStateIsConfirmed(UpdateAppointmentStateBo updateAppointmentStateBo) {
		if (mustModifyPatientIdentificationStatusInfo(updateAppointmentStateBo.getAppointmentStateId()))
			savePatientIdentificationStatusInfo(updateAppointmentStateBo);
	}

	private boolean mustModifyPatientIdentificationStatusInfo(Short appointmentStateId) {
		return appointmentStateId.equals(AppointmentState.CONFIRMED) && featureFlagsService.isOn(AppFeature.HABILITAR_ANEXO_II_MENDOZA);
	}

	private void savePatientIdentificationStatusInfo(UpdateAppointmentStateBo updateAppointmentStateBo) {
		String encryptedPatientIdentificationHash = null;
		short patientIdentityAccreditationStatusId = EPatientIdentityAccreditationStatus.NOT_GIVEN.getId();
		if (updateAppointmentStateBo.getPatientIdentificationBarCode() != null) {
			encryptedPatientIdentificationHash = encryptPatientIdentificationCode.run(updateAppointmentStateBo.getPatientIdentificationBarCode());
			patientIdentityAccreditationStatusId = EPatientIdentityAccreditationStatus.VALID.getId();
		}
		appointmentPatientIdentityAccreditationStatusPort.save(updateAppointmentStateBo.getAppointmentId(), patientIdentityAccreditationStatusId, encryptedPatientIdentificationHash);
	}

	private void checkAndHandleIfPreviousStateWasConfirmed(UpdateAppointmentStateBo updateAppointmentStateBo) {
		Short previousAppointmentState = appointmentPort.getAppointmentStateIdByAppointmentId(updateAppointmentStateBo.getAppointmentId());
		if (mustModifyPatientIdentificationStatusInfo(previousAppointmentState))
			appointmentPatientIdentityAccreditationStatusPort.clearAppointmentPatientPreviousIdentificationHashByAppointmentId(updateAppointmentStateBo.getAppointmentId());
	}

}
