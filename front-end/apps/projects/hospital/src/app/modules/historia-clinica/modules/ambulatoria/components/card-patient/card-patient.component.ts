import { DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { PatientSearchDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { DatePipeFormat } from '@core/utils/date.utils';
import { CardModel } from '@presentation/components/card/card.component';

@Component({
	selector: 'app-card-patient',
	templateUrl: './card-patient.component.html',
	styleUrls: ['./card-patient.component.scss']
})
export class CardPatientComponent {
	patientContent: CardModel[] = [];
	@Input() patientData: PatientSearchDto[] = [];
	@Input() genderTableView: string[] = [];

	private readonly routePrefix;

	constructor(
		private readonly patientNameService: PatientNameService,
		private readonly datePipe: DatePipe,
		private readonly contextService: ContextService,


	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnChanges() {
		this.patientContent = this.mapToPatientContent();
	}

	mapToPatientContent(): CardModel[] {
		return this.patientData?.map((patient: PatientSearchDto) => {
			return {
				header: [{ title: " ", value: this.patientNameService.getPatientName(patient.person.firstName, patient.person.nameSelfDetermination) }],

				id: patient.idPatient,
				dni: patient.person.identificationNumber,
				gender: this.genderTableView[patient.person.genderId],
				date: patient.person.birthDate ? this.datePipe.transform(patient.person.birthDate, DatePipeFormat.SHORT_DATE) : '' ,

				action: {
					display: 'ambulatoria.card-patient.BUTTON',
					do: `${this.routePrefix}ambulatoria/paciente/${patient.idPatient}`
				}
			}
		});
	}

}
