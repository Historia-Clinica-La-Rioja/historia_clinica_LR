package net.pladema.reports.application.fetchnominalappointmentdetail;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.reports.application.ports.NominalAppointmentDetailStorage;

import net.pladema.reports.domain.ReportSearchFilterBo;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FetchNominalAppointmentDetail {

	private final NominalAppointmentDetailStorage nominalAppointmentDetailStorage;

	public IWorkbook run(String title, ReportSearchFilterBo filter){
		log.debug("Fetch Nominal Appointments Detail Report by filter {} ", filter);
		return nominalAppointmentDetailStorage.buildNominalAppointmentsDetailExcelReport(title, filter);
	}

}
