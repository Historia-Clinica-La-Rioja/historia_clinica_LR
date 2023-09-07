package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentOrderImageExistCheckBo;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentOrderImageExistCheckVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.clinichistory.requests.servicerequests.service.ExistCheckDiagnosticReportService;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;

import java.util.List;

@Service
public class ExistCheckDiagnosticReportServiceImpl implements ExistCheckDiagnosticReportService {

    private final AppointmentOrderImageRepository appointmentOrderImageRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ExistCheckDiagnosticReportServiceImpl.class);
    private final String OUTPUT = "Output -> {}";

    public ExistCheckDiagnosticReportServiceImpl(AppointmentOrderImageRepository appointmentOrderImageRepository){
        this.appointmentOrderImageRepository = appointmentOrderImageRepository;
    }
    @Override
    public AppointmentOrderImageExistCheckBo execute(Integer orderId) {
        LOG.debug("Input: diagnosticReportId: {}", orderId);
		List<AppointmentOrderImageExistCheckVo> findAppointmentIdAndReportByOrderId = appointmentOrderImageRepository.findAppointmentIdAndReportByOrderId(orderId);
		AppointmentOrderImageExistCheckBo result;
		if (findAppointmentIdAndReportByOrderId != null && !findAppointmentIdAndReportByOrderId.isEmpty()){
			result = new AppointmentOrderImageExistCheckBo(findAppointmentIdAndReportByOrderId.get(0));
		}
		else {
			result =new AppointmentOrderImageExistCheckBo(null);
		}
		return result;
    }
}
