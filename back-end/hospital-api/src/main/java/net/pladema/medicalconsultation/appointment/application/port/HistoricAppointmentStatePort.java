package net.pladema.medicalconsultation.appointment.application.port;

import net.pladema.medicalconsultation.appointment.domain.UpdateAppointmentStateBo;

public interface HistoricAppointmentStatePort {

	void save(UpdateAppointmentStateBo updateAppointmentStateBo);

}
