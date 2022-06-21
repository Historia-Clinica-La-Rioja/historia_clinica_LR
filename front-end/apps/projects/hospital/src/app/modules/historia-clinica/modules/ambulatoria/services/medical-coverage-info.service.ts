import { SummaryCoverageInformation } from '@historia-clinica/modules/ambulatoria/components/medical-coverage-summary-view/medical-coverage-summary-view.component';
import { Injectable } from '@angular/core';
import { ExternalPatientCoverageDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';

@Injectable({
	providedIn: 'root'
})
export class MedicalCoverageInfoService {

	internmentEpisodeCoverageInfo: ExternalPatientCoverageDto;
	appointmentConfirmedCoverageInfo: ExternalPatientCoverageDto;
	summaryCoverageInfo: SummaryCoverageInformation;

	constructor(
		private readonly appointmentsService: AppointmentsService,
		private readonly hceGeneralStateService: HceGeneralStateService,
	) { }

	setAppointmentMCoverage(summaryCoverage: SummaryCoverageInformation) {
		this.summaryCoverageInfo = summaryCoverage;
	}

	setInternmentMCoverage(patientId: number, internmentEpisodeId: number) {
		this.hceGeneralStateService.getInternmentEpisodeMedicalCoverage(patientId, internmentEpisodeId).subscribe(
			(data: ExternalPatientCoverageDto) => this.internmentEpisodeCoverageInfo = data
		);
	}

	setAppointmentConfirmedCoverageInfo(patientId: number) {
		this.appointmentsService.getCurrentAppointmentMedicalCoverage(patientId).subscribe(
			(info: ExternalPatientCoverageDto) => this.appointmentConfirmedCoverageInfo = info
		);
	}

	clearAll() {
		this.internmentEpisodeCoverageInfo = null;
		this.summaryCoverageInfo = null;
		this.appointmentConfirmedCoverageInfo = null;
	}
}
