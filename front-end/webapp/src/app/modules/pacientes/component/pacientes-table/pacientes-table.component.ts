import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { MatTableDataSource } from "@angular/material/table";

const ELEMENT_DATA = [
	{id: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
	{id: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
	{id: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
	{id: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
	{id: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
];

@Component({
	selector: 'app-pacientes-table',
	templateUrl: './pacientes-table.component.html',
	styleUrls: ['./pacientes-table.component.scss']
})
export class PacientesTableComponent implements OnInit {

	displayedColumns: string[] = ['ID Paciente', 'Nro. Documento', 'Nombres', 'Apellidos', 'F. Nac', 'Sexo', 'Action'];
	dataSource = new MatTableDataSource(ELEMENT_DATA);

	constructor(
		private router: Router) {
	}

	ngOnInit(): void {

	}

	actionRow(elementId): void {

	}

	applyFilter(event: Event) {
		const filterValue = (event.target as HTMLInputElement).value;
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}
}
