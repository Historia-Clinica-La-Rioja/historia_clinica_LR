import { Component, OnInit } from '@angular/core';
import { InternacionService } from '@api-rest/services/internacion.service';
import { InternmentEpisodeDto } from '@api-rest/api-model';
import { Router } from '@angular/router';
import { TableModel } from '@core/components/table/table.component';

@Component({
	selector: 'app-internaciones-table',
	templateUrl: './internaciones-table.component.html',
	styleUrls: ['./internaciones-table.component.scss']
})
export class InternacionesTableComponent implements OnInit {

	internacionesTable: TableModel<InternmentEpisodeDto>;

	constructor(
		private internacionService: InternacionService,
		private router: Router,
	) { }

	private buildTable(data: InternmentEpisodeDto[]): TableModel<InternmentEpisodeDto> {
		return {
			columns: [
				{
					columnDef: 'patiendId',
					header: 'ID Paciente',
					text: (row) => row.patient.id
				},
				{
					columnDef: 'patientName',
					header: 'Nombre Paciente',
					text: (row) => row.patient.firstName
				},
				{
					columnDef: 'patientLastName',
					header: 'Apellido Paciente',
					text: (row) => row.patient.lastName
				},
				{
					columnDef: 'sectorName',
					header: 'Sector',
					text: (row) => row.bed.room.sector.description
				},
				{
					columnDef: 'specialityName',
					header: 'Especialidad',
					text: (row) => row.specialty.name
				},
				{
					columnDef: 'roomNumber',
					header: 'Nro. Habitación',
					text: (row) => row.bed.room.roomNumber
				},
				{
					columnDef: 'bedNumber',
					header: 'Nro. Cama',
					text: (row) => row.bed.bedNumber
				},
				{
					columnDef: 'action',
					action: {
						text: 'Ver',
						do: (internacion) => {
							let url = `internaciones/internacion/${internacion.id}/paciente/${internacion.patient.id}`;
							this.router.navigate([url]);
						}
					}
				},
			],
			data,
			enableFilter: true
		};
	}

	ngOnInit(): void {
		this.internacionService.getAllPacientesInternados<InternmentEpisodeDto>().subscribe(
			data => this.internacionesTable = this.buildTable(data)
		);
	}

}
