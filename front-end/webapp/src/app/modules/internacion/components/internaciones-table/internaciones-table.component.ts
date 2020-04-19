import { Component, OnInit } from '@angular/core';
import { InternacionService } from '@api-rest/services/internacion.service';
import { TableService } from '@core/services/table.service';
import { MatTableDataSource } from '@angular/material/table';
import { InternmentEpisodeDto } from '@api-rest/api-model';

@Component({
	selector: 'app-internaciones-table',
	templateUrl: './internaciones-table.component.html',
	styleUrls: ['./internaciones-table.component.scss']
})
export class InternacionesTableComponent implements OnInit {

	displayedColumns: string[] = ['ID Paciente', 'Médico', 'Sector', 'Especialidad', 'Nro Habitación' , 'Nro Cama', 'Action'];
	dataSource: MatTableDataSource<InternmentEpisodeDto> = new MatTableDataSource([]);

	constructor(
		private internacionService: InternacionService,
		private tableService: TableService,
	) { }

	ngOnInit(): void {
		this.dataSource.filterPredicate = this.tableService.predicateFilter;
		this.internacionService.getAllPacientesInternados<InternmentEpisodeDto>().subscribe(data => this.dataSource.data = data);
	}

	actionRow(pacienteInternado: InternmentEpisodeDto): void {
		//TODO redirect
		console.log(pacienteInternado);
	}

	applyFilter(event: Event) {
		const filterValue = (event.target as HTMLInputElement).value;
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}

}
