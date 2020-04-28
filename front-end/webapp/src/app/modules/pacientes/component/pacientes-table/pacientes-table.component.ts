import { Component, OnInit } from '@angular/core';
import { PatientService } from "@api-rest/services/patient.service";
import { TableService } from '@core/services/table.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { BMPatientDto } from '@api-rest/api-model';
import { TableModel } from 'src/app/modules/presentation/components/table/table.component';
import { Router } from '@angular/router';
import { momentFormatDate, DateFormat } from '@core/utils/moment.utils';

@Component({
	selector: 'app-pacientes-table',
	templateUrl: './pacientes-table.component.html',
	styleUrls: ['./pacientes-table.component.scss']
})
export class PacientesTableComponent implements OnInit {

	public displayedColumns: string[] = ['ID Paciente', 'Nro. Documento', 'Nombre', 'Apellido', 'F. Nac', 'Sexo', 'Action'];
	public allPatient: TableModel<BMPatientDto>;
	public genderOptions={};

	constructor(
		private patientService: PatientService,
		private tableService: TableService,
		private personMasterDataService: PersonMasterDataService,
		private router: Router,
	) {	}

	ngOnInit(): void {
		this.personMasterDataService.getGenders().subscribe(
			genders => {
				genders.forEach(gender => {
					this.genderOptions[gender.id]=gender.description
				});
		});
		this.patientService.getAllPatients().subscribe(data => {
				this.allPatient = this.buildTable(data);
		});
	}

	private buildTable(data: BMPatientDto[]): TableModel<BMPatientDto> {
		return {
			columns: [
				{
					columnDef: 'patiendId',
					header: 'ID Paciente',
					text: (row) => row.id
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
					text: (row) =>  momentFormatDate(new Date(row.birthDate),DateFormat.VIEW_DATE)
				},
				{
					columnDef: 'gender',
					header: 'Sexo',
					text: (row) => this.genderOptions[row.genderId]
				},
				{
					columnDef: 'action',
					action: {
						text: 'Ver',
						do: (internacion) => {
							let url = `pacientes`;
							this.router.navigate([url]);
						}
					}
				},
			],
			data,
			enableFilter: true
		};
	}

	actionRow(): void {
		//TODO redirect
	}

	applyFilter(event: Event) {
		const filterValue = (event.target as HTMLInputElement).value;
	}

}
