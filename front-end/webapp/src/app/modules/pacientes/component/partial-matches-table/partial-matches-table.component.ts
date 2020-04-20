import { Component, OnInit, Input } from '@angular/core';
import { MatTableDataSource } from "@angular/material/table";
import { PatientSearchDto } from '@api-rest/api-model';

@Component({
	selector: 'app-partial-matches-table',
	templateUrl: './partial-matches-table.component.html',
	styleUrls: ['./partial-matches-table.component.scss']
})
export class PartialMatchesTableComponent implements OnInit {

	@Input('matchingPatient') matchingPatient: PatientSearchDto[];

	displayedColumns: string[] = ['ID', 'Nombres', 'Apellidos', 'Sexo', 'F. Nac', 'DNI', 'Estado', 'Coincidencia'];
	dataSource = new MatTableDataSource([
		{
			id: '123',
			person: {
				name: 'Juan',
				lastName: 'Perez',
				gender: {
					description: 'masculino'
				},
				birthDate: '01/01/2020',
				identificationNumber: '12345678',
			},
			type: {
				description: 'Validado'
			},
			ranking: '80%'
		}
	]);

	constructor() {
	}

	ngOnInit(): void {
	}

	getPatient(patient): void {
		//TODO display popup
	}

}
