package net.pladema.medicalconsultation.appointment.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.DocumentAppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.DocumentAppointment;
import net.pladema.medicalconsultation.appointment.service.DocumentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.DocumentAppointmentBo;

@Service
@AllArgsConstructor
public class DocumentAppointmentServiceImpl implements DocumentAppointmentService {

	private DocumentAppointmentRepository documentAppointmentRepository;

	@Override
	public void save(DocumentAppointmentBo documentAppointmentBo) {
		this.documentAppointmentRepository.save(new DocumentAppointment(
				documentAppointmentBo.getDocumentId(),
				documentAppointmentBo.getAppointmentId()
		));
	}

	@Override
	public void delete(DocumentAppointmentBo documentAppointmentBo) {
		this.documentAppointmentRepository.delete(new DocumentAppointment(
				documentAppointmentBo.getDocumentId(),
				documentAppointmentBo.getAppointmentId()
		));
	}

	@Override
	public Optional<DocumentAppointmentBo> getDocumentAppointmentForAppointment(Integer appointmentId) {
		return this.documentAppointmentRepository.getDocumentAppointmentByAppointmentId(appointmentId);
	}

	@Override
	public Optional<DocumentAppointmentBo> getDocumentAppointmentForDocument(Long documentId) {
		return this.documentAppointmentRepository.getDocumentAppointmentByDocumentId(documentId);
	}
}
