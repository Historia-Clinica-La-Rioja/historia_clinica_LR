import { Component, Input } from '@angular/core';
import { CardModel } from '@presentation/components/card/card.component';
import { TableModel } from '@presentation/components/table/table.component';
import { InternacionService } from '@api-rest/services/internacion.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { ContextService } from '@core/services/context.service';
import { MapperService } from "@presentation/services/mapper.service";
import { DocumentsSummaryDto } from '@api-rest/api-model';
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';

@Component({
	selector: 'app-internment-patient-card',
	templateUrl: './internment-patient-card.component.html',
	styleUrls: ['./internment-patient-card.component.scss']
})

export class InternmentPatientCardComponent {
	private readonly routePrefix;
	internmentPatientTable: TableModel<InternmentPatientTableData>;
	internmentPatientCard: CardModel[] = [];

	@Input()
	set redirect(redirect: Redirect) {
		if (redirect === Redirect.patientCard) {
			this.internmentPatientService.getAllInternmentPatientsBasicData().subscribe(data => {
				this.internmentPatientCard = this.buildCard(data.map(patient => this.mapperService.toInternmentPatientTableData(patient)), redirect);
			});
		}
		else {
			this.internacionService.getAllPacientesInternados().subscribe(data => {
				this.internmentPatientCard = this.buildCard(data.map(patient => this.mapperService.toInternmentPatientTableData(patient)), redirect);
			})
		}
	}

	constructor(
		private internacionService: InternacionService,
		private readonly patientNameService: PatientNameService,
		private readonly contextService: ContextService,
		private readonly mapperService: MapperService,
		private readonly internmentPatientService: InternmentPatientService,

	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}`
	}

	private buildCard(data: InternmentPatientTableData[], redirect: Redirect): CardModel[] {
		return data?.map((person: InternmentPatientTableData) => {
			return {
				header: [{ title: "", value: person.nameSelfDetermination ? `${this.patientNameService.getPatientName(person.firstName, person.nameSelfDetermination)} ${person.lastName}` : this.getName(person) }],
				headerSimple: [{ title: "DNI", value: person.identificationNumber || "-" }],
				hasPhysicalDischarge: person.hasPhysicalDischarge,
				roomNumber: person.bedInfo.roomNumber,
				bedNumber: person.bedInfo.bedNumber,
				sectorDescription: person.bedInfo.sector,
				lastMissingDocument: person.hasPhysicalDischarge ? this.missingDocument(person.documentsSummary, person.hasMedicalDischarge) : null,
				action: {
					display: 'ambulatoria.card-patient.BUTTON',
					do: (redirect === Redirect.patientCard) ? `${this.routePrefix}/pacientes/profile/${person.patientId}` : `${this.routePrefix}/ambulatoria/paciente/${person.patientId}`
				},

			}

		})

	}

	private getName(person: InternmentPatientTableData): string {
		return person.fullName ? ` ${person.fullName}` : `internaciones.internment-patient-card.NO_INFO`
	}

	private missingDocument(document: DocumentsSummaryDto, hasMedicalDischarge: boolean): Documnet {
		if (!document.lastEvaluationNote.confirmed)
			return { matIcon: "assignment", title: 'internaciones.internment-patient-card.PENDING_EVOLUTION' }
		if (!document.epicrisis.confirmed)
			return { matIcon: "assignment_turned_in", title: 'internaciones.internment-patient-card.PENDING_EPICRISIS' }
		if (!hasMedicalDischarge)
			return { matIcon: "assignment_return", title: 'internaciones.internment-patient-card.PENDING_MEDICAL_DISCHARGE' }
		return { matIcon: "keyboard_backspace", title: 'internaciones.internment-patient-card.PENDING_ADMINISTRATIVE_DISCHARGE' }
	}
}
export interface InternmentPatientTableData {
	patientId: number;
	firstName: string;
	identificationNumber: string;
	identificationTypeId: number;
	internmentId: number;
	lastName: string;
	fullName: string;
	nameSelfDetermination: string;
	bedInfo: {
		sector: string;
		roomNumber: string;
		bedNumber: string;
	}
	hasPhysicalDischarge: boolean;
	hasMedicalDischarge?: boolean;
	documentsSummary: DocumentsSummaryDto;
}

export enum Redirect {
	HC, patientCard
}

interface Documnet {
	matIcon: string;
	title: string;
}
