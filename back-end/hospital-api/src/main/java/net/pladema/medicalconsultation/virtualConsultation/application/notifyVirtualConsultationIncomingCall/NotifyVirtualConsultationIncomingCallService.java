package net.pladema.medicalconsultation.virtualConsultation.application.notifyVirtualConsultationIncomingCall;

public interface NotifyVirtualConsultationIncomingCallService {

	void run(Integer virtualConsultationId, Integer healthcareProfessionalId);

}
