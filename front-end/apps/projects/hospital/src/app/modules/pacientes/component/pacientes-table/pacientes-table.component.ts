import { Component, OnInit } from '@angular/core';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { TableModel, ActionDisplays } from '@presentation/components/table/table.component';
import { Router } from '@angular/router';
import { momentFormatDate, DateFormat } from '@core/utils/moment.utils';
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';
import { ContextService } from '@core/services/context.service';
import { MapperService } from './../../../presentation/services/mapper.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { AppFeature } from "@api-rest/api-model";

const ROUTE_PROFILE = 'pacientes/profile/';

@Component({
	selector: 'app-pacientes-table',
	templateUrl: './pacientes-table.component.html',
	styleUrls: ['./pacientes-table.component.scss']
})
export class PacientesTableComponent implements OnInit {

	public displayedColumns: string[] = ['ID Paciente', 'Nro. Documento', 'Nombre', 'Apellido', 'F. Nac', 'Sexo', 'Action'];
	public allPatient: TableModel<PatientTableData>;
	public genderOptions = {};
	private readonly routePrefix;
	nameSelfDeterminationEnabled: boolean;

	constructor(
		private mapperService: MapperService,
		private internmentPatientService: InternmentPatientService,
		private personMasterDataService: PersonMasterDataService,
		private contextService: ContextService,
		private router: Router,
		private patientNameService: PatientNameService,
		private featureFlagService: FeatureFlagService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isEnabled => this.nameSelfDeterminationEnabled = isEnabled);
	}

	ngOnInit(): void {
		this.personMasterDataService.getGenders().subscribe(
			genders => {
				genders.forEach(gender => {
					this.genderOptions[gender.id] = gender.description;
				});
			});

		this.internmentPatientService.getAllInternmentPatientsBasicData().subscribe(data => {
			this.allPatient = this.buildTable(data.map(patient => this.mapperService.toPatientTableData(patient)));
		});
	}

	private buildTable(data: PatientTableData[]): TableModel<PatientTableData> {
		if(this.nameSelfDeterminationEnabled) {
			return {
				columns: [
					{
						columnDef: 'patiendId',
						header: 'pacientes.search.ROW_TABLE',
						text: (row) => row.patientId
					},
					{
						columnDef: 'numberDni',
						header: 'Nro. Documento',
						text: (row) => row.identificationNumber
					},
					{
						columnDef: 'firstName',
						header: 'Nombre',
						text: (row) => this.patientNameService.getPatientName(row.firstName, row.nameSelfDetermination)
					},
					{
						columnDef: 'lastName',
						header: 'Apellido',
						text: (row) => row.lastName
					},
					{
						columnDef: 'birthDate',
						header: 'F. Nacimiento',
						text: (row) => (row.birthDate == undefined) ? '' : momentFormatDate(new Date(row.birthDate), DateFormat.VIEW_DATE)
					},
					{
						columnDef: 'action',
						action: {
							displayType: ActionDisplays.BUTTON,
							display: 'Ver',
							matColor: 'primary',
							do: (internacion) => {
								const url = this.routePrefix + ROUTE_PROFILE + internacion.patientId;
								this.router.navigate([url]);
							}
						}
					},
				],
				data,
				enableFilter: true,
				enablePagination: true
			};
		} else {
			return {
				columns: [
					{
						columnDef: 'patiendId',
						header: 'pacientes.search.ROW_TABLE',
						text: (row) => row.patientId
					},
					{
						columnDef: 'numberDni',
						header: 'Nro. Documento',
						text: (row) => row.identificationNumber
					},
					{
						columnDef: 'firstName',
						header: 'Nombre',
						text: (row) => row.firstName
					},
					{
						columnDef: 'lastName',
						header: 'Apellido',
						text: (row) => row.lastName
					},
					{
						columnDef: 'birthDate',
						header: 'F. Nac',
						text: (row) => (row.birthDate == undefined) ? '' : momentFormatDate(new Date(row.birthDate), DateFormat.VIEW_DATE)
					},
					{
						columnDef: 'gender',
						header: 'Sexo',
						text: (row) => this.genderOptions[row.genderId]
					},
					{
						columnDef: 'action',
						action: {
							displayType: ActionDisplays.BUTTON,
							display: 'Ver',
							matColor: 'primary',
							do: (internacion) => {
								const url = this.routePrefix + ROUTE_PROFILE + internacion.patientId;
								this.router.navigate([url]);
							}
						}
					},
				],
				data,
				enableFilter: true,
				enablePagination: true
			};
		}
	}

	actionRow(): void {
		// TODO redirect
	}

}

export interface PatientTableData {
	birthDate: Date;
	firstName: string;
	genderId: number;
	identificationNumber: string;
	identificationTypeId: number;
	internmentId: number;
	lastName: string;
	patientId: number;
	fullName: string;
	nameSelfDetermination: string;
}
