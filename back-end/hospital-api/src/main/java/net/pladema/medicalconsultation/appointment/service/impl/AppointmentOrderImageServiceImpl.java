package net.pladema.medicalconsultation.appointment.service.impl;


import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedDiagnosticImagingOrder;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;
import net.pladema.clinichistory.requests.servicerequests.repository.TranscribedServiceRequestRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.DetailsOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentOrderImage;
import net.pladema.medicalconsultation.appointment.repository.entity.DetailsOrderImage;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentOrderImageBo;
import net.pladema.medicalconsultation.appointment.service.domain.DetailsOrderImageBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentOrderImageServiceImpl implements AppointmentOrderImageService, SharedDiagnosticImagingOrder {

	private static final Logger LOG = LoggerFactory.getLogger(AppointmentOrderImageServiceImpl.class);

	private final AppointmentOrderImageRepository appointmentOrderImageRepository;
	private final DetailsOrderImageRepository detailsOrderImageRepository;
	private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;

	@Override
	public boolean isAlreadyCompleted(Integer appointmentId) {
		return appointmentOrderImageRepository.isAlreadyCompleted(appointmentId).isPresent();
	}

	@Override
	public void updateCompleted(DetailsOrderImageBo detailsOrderImageBo) {
		LOG.debug("Input parameters -> DetailsOrderImageBo '{}'", detailsOrderImageBo);
		Integer appointmentId = detailsOrderImageBo.getAppointmentId();
		Boolean isReportRequired = detailsOrderImageBo.getIsReportRequired();
		DetailsOrderImage doi = detailsOrderImageRepository.save(new DetailsOrderImage(appointmentId,
				detailsOrderImageBo.getObservations(), detailsOrderImageBo.getCompletedOn(),
				detailsOrderImageBo.getProfessionalId(), isReportRequired));
		appointmentOrderImageRepository.updateCompleted(detailsOrderImageBo.getAppointmentId(), true);
		if (!isReportRequired)
			this.setReportStatusId(doi.getAppointmentId(), EDiagnosticImageReportStatus.NOT_REQUIRED.getId());
		LOG.debug("Output -> appointmentId {} study finished by technician", appointmentId);
	}

	@Override
	public Optional <String> getImageId(Integer appointmentId) {
		LOG.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<String> imageId = appointmentOrderImageRepository.getIdImage(appointmentId);
		LOG.debug("Output -> imageId {}", imageId);
		return imageId;
	}

	@Override
	public void save(AppointmentOrderImageBo appointmentOrderImageBo) {
		LOG.debug("Input parameters -> appointmentOrderImageBo {}", appointmentOrderImageBo);
		AppointmentOrderImage entity = new AppointmentOrderImage(appointmentOrderImageBo.getAppointmentId(),appointmentOrderImageBo.getOrderId(),
				appointmentOrderImageBo.getStudyId(),appointmentOrderImageBo.getImageId(),appointmentOrderImageBo.isCompleted(),
				appointmentOrderImageBo.getTranscribedOrderId(), appointmentOrderImageBo.getDestInstitutionId(),
				appointmentOrderImageBo.getReportStatusId(), true);
		appointmentOrderImageRepository.save(entity);
		LOG.debug("Output -> AppointmentOrderImage {}", entity);
	}

	@Override
	public void setImageId(Integer appointmentId, String imageId) {
		LOG.debug("Input parameters -> appointmentId {}, imageId {} ", appointmentId, imageId);
		appointmentOrderImageRepository.updateImageId(appointmentId, imageId);
	}

	@Override
	public void setDestInstitutionId(Integer destInstitutionId, Integer appointmentId) {
		LOG.debug("Input parameters ->  destInstitutionId {}, appointmentId {}",  destInstitutionId , appointmentId);
		appointmentOrderImageRepository.updateDestInstitution(destInstitutionId, appointmentId);
	}

	@Override
	public void setReportStatusId(Integer appointmentId, Short reportStatusId) {
		LOG.debug("Input parameters -> appointmentId {}, reportStatusId {} ", appointmentId, reportStatusId);
		appointmentOrderImageRepository.updateReportStatusId(appointmentId, reportStatusId);
	}

	@Override
	public boolean updateOrderId(Integer appointmentId, Integer orderId, Boolean transcribed, Integer studyId) {
		LOG.debug("Input parameters -> appointmentId '{}', orderId '{}', transcribed '{}'", appointmentId, orderId, transcribed);
		if (transcribed) {
			appointmentOrderImageRepository.updateTranscribedOrderId(appointmentId, orderId);
			return Boolean.TRUE;
		}
		appointmentOrderImageRepository.updateOrderId(appointmentId, studyId, orderId);
		return Boolean.TRUE;
	}

	@Override
	public Optional<Integer> getDiagnosticImagingOrderAuthorId(Integer appointmentId) {
		LOG.debug("Input parameters -> appointmentId '{}'", appointmentId);
		return appointmentOrderImageRepository.getDiagnosticImagingOrderAuthorId(appointmentId);
	}

	@Override
	public Optional<String> getDiagnosticImagingTranscribedOrderAuthor(Integer transcribedOrderId) {
		LOG.debug("Input parameters -> transcribedOrderId '{}'", transcribedOrderId);
		return transcribedServiceRequestRepository.getHealthcareProfessionalName(transcribedOrderId);
	}

	@Override
	public List<Integer> getAppointmentIdByOrderId(Integer orderId) {
		LOG.debug("Input parameters -> orderId '{}'", orderId);
		return appointmentOrderImageRepository.getAppointmentIdsByOrderId(orderId);
	}
}
