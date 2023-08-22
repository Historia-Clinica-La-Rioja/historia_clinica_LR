package net.pladema.medicalconsultation.appointment.service.impl;


import java.util.Optional;

import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.DetailsOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentOrderImage;
import net.pladema.medicalconsultation.appointment.repository.entity.DetailsOrderImage;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentOrderImageBo;
import net.pladema.medicalconsultation.appointment.service.domain.DetailsOrderImageBo;

@Service
@RequiredArgsConstructor
public class AppointmentOrderImageServiceImpl implements AppointmentOrderImageService {

	private static final Logger LOG = LoggerFactory.getLogger(AppointmentOrderImageServiceImpl.class);

	private final AppointmentOrderImageRepository appointmentOrderImageRepository;
	private final DetailsOrderImageRepository detailsOrderImageRepository;


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
}
