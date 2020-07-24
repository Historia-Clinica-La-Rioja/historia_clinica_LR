import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ImmunizationDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AplicarVacunaComponent } from '../../dialogs/aplicar-vacuna/aplicar-vacuna.component';

@Component({
	selector: 'app-vacunas',
	templateUrl: './vacunas.component.html',
	styleUrls: ['./vacunas.component.scss']
})
export class VacunasComponent implements OnInit {

	public immunizations$: Observable<ImmunizationDto[]>;
	public patientId: number;

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly route: ActivatedRoute,
		public dialog: MatDialog,
	) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.immunizations$ = this.hceGeneralStateService.getImmunizations(this.patientId);
			});
	}

	goToAplicarVacuna() {
		const dialogRef = this.dialog.open(AplicarVacunaComponent, {
			disableClose: true,
			width: '45%',
			data: {
				patientId : this.patientId
			}
		});

		dialogRef.afterClosed().subscribe(_ => {
			}
		);
	}

}
