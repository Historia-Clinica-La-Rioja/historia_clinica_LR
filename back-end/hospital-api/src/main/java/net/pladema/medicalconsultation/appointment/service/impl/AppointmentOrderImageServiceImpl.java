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
	public boolean updateCompleted(DetailsOrderImageBo detailsOrderImageBo, boolean completed) {
		LOG.debug("Input parameters -> details {} to finish with '{}'", detailsOrderImageBo, completed);
		DetailsOrderImage doi = detailsOrderImageRepository.save(new DetailsOrderImage(detailsOrderImageBo.getAppointmentId(),
				detailsOrderImageBo.getObservations(), detailsOrderImageBo.getCompletedOn(),
				detailsOrderImageBo.getProfessionalId(), detailsOrderImageBo.getRoleId(), detailsOrderImageBo.getIsReportRequired()));
		appointmentOrderImageRepository.updateCompleted(detailsOrderImageBo.getAppointmentId(), completed);
		LOG.debug("Output -> appointmentId {} study finished", doi.getAppointmentId());
		return true;
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
				appointmentOrderImageBo.getReportStatusId());
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
	public void updateReportStatusId(Integer appointmentId, boolean isReportRequired) {
		LOG.debug("Input parameters -> appointmentId {}, isReportRequired {} ", appointmentId, isReportRequired);
		if (!isReportRequired)
			appointmentOrderImageRepository.updateReportStatusId(appointmentId, EDiagnosticImageReportStatus.NOT_REQUIRED.getId());
	}
}
