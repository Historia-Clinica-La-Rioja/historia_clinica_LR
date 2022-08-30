import { Component, Input } from '@angular/core';
import { InternacionService } from '@api-rest/services/internacion.service';
import { Router } from '@angular/router';
import { TableModel, ActionDisplays } from '@presentation/components/table/table.component';
import { PatientNameService } from "@core/services/patient-name.service";
import { ContextService } from '@core/services/context.service';
import { InternmentPatientService } from "@api-rest/services/internment-patient.service";
import { MapperService } from "@presentation/services/mapper.service";
import { DocumentsSummaryDto, DocumentSummaryDto } from '@api-rest/api-model';

@Component({
	selector: 'app-internment-patient-table',
	templateUrl: './internment-patient-table.component.html',
	styleUrls: ['./internment-patient-table.component.scss']
})
export class InternmentPatientTableComponent {

	private readonly routePrefix;
	internmentPatientTable: TableModel<InternmentPatientTableData>;

	@Input()
	set redirect(redirect: Redirect) {
		if (redirect === Redirect.patientCard)
			this.internmentPatientService.getAllInternmentPatientsBasicData().subscribe(data => {
				this.internmentPatientTable = this.buildTable(data.map(patient => this.mapperService.toInternmentPatientTableData(patient)), redirect);
			});
		else
			this.internacionService.getAllPacientesInternados().subscribe(data => {
				this.internmentPatientTable = this.buildTable(data.map(patient => this.mapperService.toInternmentPatientTableData(patient)), redirect);
			})
	}

	constructor(
		private internacionService: InternacionService,
		private router: Router,
		private readonly patientNameService: PatientNameService,
		private readonly contextService: ContextService,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly mapperService: MapperService
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}`
	}

	private buildTable(data: InternmentPatientTableData[], redirect: Redirect): TableModel<InternmentPatientTableData> {
		return {
			columns: [
				{
					columnDef: 'numberDni',
					header: 'Nro. Documento',
					text: (row) => row.identificationNumber
				},
				{
					columnDef: 'patientName',
					header: 'Nombres',
					text: (row) => this.patientNameService.getPatientName(row.firstName, row.nameSelfDetermination)
				},
				{
					columnDef: 'patientLastName',
					header: 'Apellidos',
					text: (row) => row.lastName
				},
				{
					columnDef: 'sectorName',
					header: 'Sector',
					text: (row) => row.hasPhysicalDischarge ? '-' : row.bedInfo.sector
				},
				{
					columnDef: 'roomNumber',
					header: 'Nro. Habitación',
					text: (row) => row.hasPhysicalDischarge ? '-' : row.bedInfo.roomNumber
				},
				{
					columnDef: 'bedNumber',
					header: 'Nro. Cama',
					text: (row) => row.hasPhysicalDischarge ? '-' : row.bedInfo.bedNumber
				},
				{
					columnDef: 'physicalDischarge',
					header: 'Alta física',
					text: (row) => row.hasPhysicalDischarge ? 'Si' : 'No'
				},
				{
					columnDef: 'missingDocuments',
					header: 'Doc. Pendiente',
					text: (row) => row.hasPhysicalDischarge ? this.missingDocument(row.documentsSummary, row.hasMedicalDischarge) : null
				},
				{
					columnDef: 'action',
					action: {
						displayType: ActionDisplays.BUTTON,
						display: 'Ver',
						matColor: 'primary',
						do: (internacion) => {
							if (redirect === Redirect.patientCard) {
								const url = `${this.routePrefix}/pacientes/profile/${internacion.patientId}`;
								this.router.navigate([url]);
							}
							else {
								const url = `${this.routePrefix}/ambulatoria/paciente/${internacion.patientId}`;
								this.router.navigate([url]);
							}
						}
					}
				},
			],
			data,
			enableFilter: true,
			enablePagination: true
		};
	}

	private missingDocument(document: DocumentsSummaryDto, hasMedicalDischarge: boolean): string {
		if (!document.anamnesis.confirmed)
			return "Evaluación de ingreso";
		if (!document.lastEvaluationNote.confirmed)
			return "Nota de evolución";
		if (!document.epicrisis.confirmed)
			return "Epicrisis";
		if (!hasMedicalDischarge)
			return "Alta médica"
		return "Alta administrativa"
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
