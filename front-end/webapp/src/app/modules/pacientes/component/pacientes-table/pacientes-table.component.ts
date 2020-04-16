import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { MatTableDataSource } from "@angular/material/table";
import { PatientService } from "@api-rest/services/patient.service";

@Component({
	selector: 'app-pacientes-table',
	templateUrl: './pacientes-table.component.html',
	styleUrls: ['./pacientes-table.component.scss']
})
export class PacientesTableComponent implements OnInit {

	displayedColumns: string[] = ['ID Paciente', 'Nro. Documento', 'Nombre', 'Apellido', 'F. Nac', 'Sexo', 'Action'];
	dataSource = new MatTableDataSource([]);

	constructor(
		private patientService: PatientService,
		private router: Router) {
	}

	ngOnInit(): void {
		this.dataSource.filterPredicate = (data, filter: string)  => {
			const accumulator = (currentTerm, key) => {
				//return key === 'person' ? currentTerm + data.person.first_name : currentTerm + data[key];
				return this.nestedFilterCheck(currentTerm, data, key);
			};
			const dataStr = Object.keys(data).reduce(accumulator, '').toLowerCase();
			// Transform the filter by converting it to lowercase and removing whitespace.
			const transformedFilter = filter.trim().toLowerCase();
			return dataStr.indexOf(transformedFilter) !== -1;
		};

		this.patientService.getAllPatients().subscribe(data => this.dataSource.data = data);
	}

	actionRow(elementId): void {

	}

	applyFilter(event: Event) {
		const filterValue = (event.target as HTMLInputElement).value;
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}

	private nestedFilterCheck(search, data, key) {
		if (typeof data[key] === 'object') {
			for (const k in data[key]) {
				if (data[key][k] !== null) {
					search = this.nestedFilterCheck(search, data[key], k);
				}
			}
		} else {
			search += data[key];
		}
		return search;
	}
}
