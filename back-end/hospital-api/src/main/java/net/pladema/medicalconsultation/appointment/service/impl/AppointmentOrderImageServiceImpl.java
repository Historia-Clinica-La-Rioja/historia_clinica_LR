package net.pladema.medicalconsultation.appointment.service.impl;


import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedDiagnosticImagingOrder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.TranscribedServiceRequestRepository;
import net.pladema.imagenetwork.application.exception.StudyException;
import net.pladema.imagenetwork.domain.exception.EStudyException;
import net.pladema.medicalconsultation.appointment.repository.AppointmentDetailOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.DetailsOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentOrderDetailImageBO;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentOrderImage;
import net.pladema.medicalconsultation.appointment.repository.entity.DetailsOrderImage;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentOrderImageBo;
import net.pladema.medicalconsultation.appointment.service.domain.DetailsOrderImageBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentOrderImageServiceImpl implements AppointmentOrderImageService, SharedDiagnosticImagingOrder {

	private final AppointmentOrderImageRepository appointmentOrderImageRepository;
	private final DetailsOrderImageRepository detailsOrderImageRepository;
	private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;
	private final AppointmentDetailOrderImageRepository appointmentDetailOrderImageRepository;

	@Override
	public boolean isAlreadyCompleted(Integer appointmentId) {
		return appointmentOrderImageRepository.isAlreadyCompleted(appointmentId).isPresent();
	}

	@Override
	@Transactional
	public void updateCompleted(DetailsOrderImageBo detailsOrderImageBo) {
		log.debug("Input parameters -> DetailsOrderImageBo '{}'", detailsOrderImageBo);
		Integer appointmentId = detailsOrderImageBo.getAppointmentId();
		Boolean isReportRequired = detailsOrderImageBo.getIsReportRequired();

		if (isAlreadyCompleted(appointmentId)) {
			throw new StudyException(EStudyException.STUDY_ALREADY_FINISHED, "appointment.study.already.finished");
		}

		DetailsOrderImage doi = detailsOrderImageRepository.save(new DetailsOrderImage(appointmentId,
				detailsOrderImageBo.getObservations(), detailsOrderImageBo.getCompletedOn(),
				detailsOrderImageBo.getProfessionalId(), isReportRequired));
		appointmentOrderImageRepository.updateCompleted(detailsOrderImageBo.getAppointmentId(), true);
		if (!isReportRequired)
			this.setReportStatusId(doi.getAppointmentId(), EDiagnosticImageReportStatus.NOT_REQUIRED.getId());
		log.debug("Output -> appointmentId {} study finished by technician", appointmentId);
	}

	@Override
	public Optional <String> getImageId(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<String> imageId = appointmentOrderImageRepository.getIdImage(appointmentId);
		log.debug("Output -> imageId {}", imageId);
		return imageId;
	}

	@Override
	public void save(AppointmentOrderImageBo appointmentOrderImageBo) {
		log.debug("Input parameters -> appointmentOrderImageBo {}", appointmentOrderImageBo);
		AppointmentOrderImage entity = new AppointmentOrderImage(appointmentOrderImageBo.getAppointmentId(),appointmentOrderImageBo.getOrderId(),
				appointmentOrderImageBo.getStudyId(),appointmentOrderImageBo.getImageId(),appointmentOrderImageBo.isCompleted(),
				appointmentOrderImageBo.getTranscribedOrderId(), appointmentOrderImageBo.getDestInstitutionId(),
				appointmentOrderImageBo.getReportStatusId(), true);
		appointmentOrderImageRepository.save(entity);
		log.debug("Output -> AppointmentOrderImage {}", entity);
	}

	@Override
	public void setImageId(Integer appointmentId, String imageId) {
		log.debug("Input parameters -> appointmentId {}, imageId {} ", appointmentId, imageId);
		appointmentOrderImageRepository.updateImageId(appointmentId, imageId);
	}

	@Override
	public void setDestInstitutionId(Integer destInstitutionId, Integer appointmentId) {
		log.debug("Input parameters ->  destInstitutionId {}, appointmentId {}",  destInstitutionId , appointmentId);
		appointmentOrderImageRepository.updateDestInstitution(destInstitutionId, appointmentId);
	}

	@Override
	public void setReportStatusId(Integer appointmentId, Short reportStatusId) {
		log.debug("Input parameters -> appointmentId {}, reportStatusId {} ", appointmentId, reportStatusId);
		appointmentOrderImageRepository.updateReportStatusId(appointmentId, reportStatusId);
	}

	@Override
	public boolean updateOrderId(Integer appointmentId, Integer orderId, Boolean transcribed, Integer studyId) {
		log.debug("Input parameters -> appointmentId '{}', orderId '{}', transcribed '{}'", appointmentId, orderId, transcribed);
		if (transcribed) {
			appointmentOrderImageRepository.updateTranscribedOrderId(appointmentId, orderId);
			return Boolean.TRUE;
		}
		appointmentOrderImageRepository.updateOrderId(appointmentId, studyId, orderId);
		return Boolean.TRUE;
	}

	@Override
	public Optional<Integer> getDiagnosticImagingOrderAuthorId(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId '{}'", appointmentId);
		return appointmentOrderImageRepository.getDiagnosticImagingOrderAuthorId(appointmentId);
	}

	@Override
	public Optional<String> getDiagnosticImagingTranscribedOrderAuthor(Integer transcribedOrderId) {
		log.debug("Input parameters -> transcribedOrderId '{}'", transcribedOrderId);
		return transcribedServiceRequestRepository.getHealthcareProfessionalName(transcribedOrderId);
	}

	@Override
	public List<Integer> getAppointmentIdByOrderId(Integer orderId) {
		log.debug("Input parameters -> orderId '{}'", orderId);
		return appointmentOrderImageRepository.getAppointmentIdsByOrderId(orderId);
	}

	@Override
	public AppointmentOrderDetailImageBO getDetailOrdenImageTechnical(Integer appointmentId, boolean isTranscribed) {
		log.debug("Input parameters -> appointmentId '{}' isTranscribed '{}'", appointmentId, isTranscribed);
		AppointmentOrderDetailImageBO result;

		if (isTranscribed) {
			result = this.appointmentDetailOrderImageRepository.getOrderTranscribedDetailImage(appointmentId);
			result.setHealthCondition(transcribedServiceRequestRepository.getDiagnosticReports(result.getIdServiceRequest())
					.get(0)
					.getSnomedPt());
		} else
			result = this.appointmentDetailOrderImageRepository.getOrderDetailImage(appointmentId);

		return result;
	}
}
