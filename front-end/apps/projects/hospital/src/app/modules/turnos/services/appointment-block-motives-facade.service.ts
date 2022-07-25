import { Injectable } from "@angular/core";
import { MasterDataInterface } from "@api-rest/api-model";
import { MedicalConsultationMasterdataService } from "@api-rest/services/medical-consultation-masterdata.service";

@Injectable({
	providedIn: 'root'
})
export class AppointmentBlockMotivesFacadeService {

	private appointmentBlockMotives: MasterDataInterface<number>[];

	constructor(
		private readonly medicalConsultationMasterdataService: MedicalConsultationMasterdataService,
	) {
		this.medicalConsultationMasterdataService.getAppointmentBlockMotives().subscribe(response => this.appointmentBlockMotives = response);
	}

	getAllAppointmentBlockMotives(): MasterDataInterface<number>[] {
		return this.appointmentBlockMotives;
	}

	getAppointmentBlockMotiveById(id: number): string {
		if (id === undefined) {
			return 'sin motivo';
		}
		return this.appointmentBlockMotives.find(appointmentBlockMotive => appointmentBlockMotive.id === id).description;
	}

}
