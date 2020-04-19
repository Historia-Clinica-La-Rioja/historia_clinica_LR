import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from "@angular/material/table";
import { PatientService } from "@api-rest/services/patient.service";
import { TableService } from '@core/services/table.service';

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
		private tableService: TableService,
	) {	}

	ngOnInit(): void {
		this.dataSource.filterPredicate = this.tableService.predicateFilter;
		this.patientService.getAllPatients().subscribe(data => this.dataSource.data = data);
	}

	actionRow(): void {
		//TODO redirect
	}

	applyFilter(event: Event) {
		const filterValue = (event.target as HTMLInputElement).value;
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}

}
