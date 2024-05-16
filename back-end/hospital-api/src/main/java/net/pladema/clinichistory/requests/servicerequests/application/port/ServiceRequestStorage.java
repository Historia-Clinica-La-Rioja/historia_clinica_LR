package net.pladema.clinichistory.requests.servicerequests.application.port;

import java.util.List;
import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;
import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;

public interface ServiceRequestStorage {

	List<ServiceRequestProcedureInfoBo> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds);

	void cancelServiceRequest(Integer serviceRequestId);

    List<String> getDiagnosticReportsFrom(EquipmentAppointmentBo equipmentAppointmentBo);
}
