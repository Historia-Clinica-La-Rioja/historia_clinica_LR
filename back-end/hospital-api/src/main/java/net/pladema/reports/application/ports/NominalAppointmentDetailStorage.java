package net.pladema.reports.application.ports;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.domain.NominalAppointmentDetailFiterlBo;

public interface NominalAppointmentDetailStorage {

	IWorkbook buildNominalAppointmentsDetailExcelReport(String title, NominalAppointmentDetailFiterlBo filter);

}
