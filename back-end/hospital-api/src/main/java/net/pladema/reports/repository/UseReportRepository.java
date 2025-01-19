package net.pladema.reports.repository;

import java.util.List;
import java.util.Map;

public interface UseReportRepository {

	List<Map<String, Object>> getImplementedInstitutions();

	List<Map<String, Object>> getUserSpecialty(Integer domain);

	List<Map<String, Object>> getInstitutionSpecialty();

	List<Map<String, Object>> getInstitutionConsultation(String limitDate);

	List<Map<String, Object>> getGivenAppointment(String limitDate);

	List<Map<String, Object>> getConfirmedOrAttendedAppointment(String limitDate);

}
