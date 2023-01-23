import { DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { PatientSearchDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { DatePipeFormat } from '@core/utils/date.utils';
import { CardModel, ValueAction } from '@presentation/components/card/card.component';
const PAGE_SIZE_OPTIONS = [5, 10, 25];
const PAGE_MIN_SIZE = 5;

@Component({
	selector: 'app-card-patient',
	templateUrl: './card-patient.component.html',
	styleUrls: ['./card-patient.component.scss']
})
export class CardPatientComponent {
	pageSizeOptions = PAGE_SIZE_OPTIONS;
	patientContent = [];
	pageSlice = [];
	numberOfPatients = 0;
	@Input() patientData: PatientSearchDto[] = [];
	@Input() genderTableView: string[] = [];

	private readonly routePrefix;

	constructor(
		private readonly patientNameService: PatientNameService,
		private readonly datePipe: DatePipe,
		private readonly contextService: ContextService,
		private readonly permissionsService: PermissionsService,
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnChanges() {
		this.patientContent = this.mapToPatientContent();
		this.numberOfPatients = this.patientContent?.length;
		this.pageSlice = this.patientContent.slice(0, PAGE_MIN_SIZE);
	}
	private mapToPatientContent(): CardModel[] {
		let medicalSpecialist = false;
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => medicalSpecialist = anyMatch<ERole>(userRoles,
			[
				ERole.ESPECIALISTA_MEDICO,
				ERole.PROFESIONAL_DE_SALUD,
				ERole.ENFERMERO,
				ERole.ESPECIALISTA_EN_ODONTOLOGIA,
				ERole.PERSONAL_DE_LABORATORIO,
				ERole.PERSONAL_DE_IMAGENES,
				ERole.PERSONAL_DE_FARMACIA]));

		return this.patientData?.map((patient: PatientSearchDto) => {
			return {
				header: [{ title: " ", value: this.patientNameService.getFullName(patient.person.firstName, patient.person.nameSelfDetermination, patient.person?.middleNames) + ' ' + this.getLastNames(patient) }],
				id: patient.idPatient,
				dni: patient.person.identificationNumber || "-",
				gender: this.genderTableView[patient.person.genderId],
				date: patient.person.birthDate ? this.datePipe.transform(patient.person.birthDate, DatePipeFormat.SHORT_DATE) : '',
				ranking: patient?.ranking,
				action: this.setActionByRole(medicalSpecialist, patient.idPatient)
			}
		});
	}
	private getLastNames(patient: PatientSearchDto): string {
		return patient.person?.otherLastNames ? patient.person?.lastName + ' ' + patient.person?.otherLastNames : patient.person?.lastName;
	}
	onPageChange($event: any): void {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		this.pageSlice = this.patientContent.slice(startPage, $event.pageSize + startPage);
	}

	private setActionByRole(medicalSpecialist: boolean, idPatient: number): ValueAction {
		if (medicalSpecialist)
			return {
				display: 'ambulatoria.card-patient.BUTTON',
				do: `${this.routePrefix}ambulatoria/paciente/${idPatient}`
			}
		else
			return {
				display: 'ambulatoria.card-patient.BUTTON',
				do: `${this.routePrefix}pacientes/profile/${idPatient}`
			}
	}
}
