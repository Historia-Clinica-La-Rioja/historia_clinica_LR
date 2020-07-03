import { Component, OnInit } from '@angular/core';
import { InternacionService } from '@api-rest/services/internacion.service';
import { InternmentEpisodeDto } from '@api-rest/api-model';
import { Router } from '@angular/router';
import { TableModel, ActionDisplays } from '@presentation/components/table/table.component';

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
					header: 'pacientes.search.ROW_TABLE',
					text: (row) => row.patient.id
				},
				{
					columnDef: 'patientName',
					header: 'Nombre',
					text: (row) => row.patient.firstName
				},
				{
					columnDef: 'patientLastName',
					header: 'Apellido',
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
						displayType: ActionDisplays.BUTTON,
						display: 'Ver',
						matColor: 'primary',
						do: (internacion) => {
							let url = `${this.router.url}/internacion/${internacion.id}/paciente/${internacion.patient.id}`;
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

	ngOnInit(): void {
		this.internacionService.getAllPacientesInternados().subscribe(
			(data: InternmentEpisodeDto[]) => this.internacionesTable = this.buildTable(data)
		);
	}

}
