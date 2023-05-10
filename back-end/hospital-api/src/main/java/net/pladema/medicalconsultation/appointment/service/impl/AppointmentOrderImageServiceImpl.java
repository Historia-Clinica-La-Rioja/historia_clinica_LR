package net.pladema.medicalconsultation.appointment.service.impl;


import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentOrderImage;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentOrderImageBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentOrderImageServiceImpl implements AppointmentOrderImageService {

	private static final Logger LOG = LoggerFactory.getLogger(AppointmentOrderImageServiceImpl.class);

	private final AppointmentOrderImageRepository appointmentOrderImageRepository;


	@Override
	public void updateCompleted(Integer appointmentId, boolean completed) {
		LOG.debug("Input parameters -> appointmentId {}, completed {} ", appointmentId, completed);
		appointmentOrderImageRepository.updateCompleted(appointmentId, completed);
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
		AppointmentOrderImage entity =new AppointmentOrderImage(appointmentOrderImageBo.getAppointmentId(),appointmentOrderImageBo.getOrderId(),
				appointmentOrderImageBo.getStudyId(),appointmentOrderImageBo.getImageId(),appointmentOrderImageBo.isCompleted());
		appointmentOrderImageRepository.save(entity);
		LOG.debug("Output -> AppointmentOrderImage {}", entity);
	}

	@Override
	public void setImageId(Integer appointmentId, String imageId) {
		LOG.debug("Input parameters -> appointmentId {}, imageId {} ", appointmentId, imageId);
		appointmentOrderImageRepository.updateImageId(appointmentId, imageId);
	}
}
