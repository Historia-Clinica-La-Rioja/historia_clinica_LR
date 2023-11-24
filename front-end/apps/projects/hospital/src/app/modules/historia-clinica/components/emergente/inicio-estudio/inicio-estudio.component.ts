import { Component, OnInit } from '@angular/core';
import { EstudiosPopupComponent } from '../pop-up/estudios-popup.component';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'app-adulto-mayor',
	templateUrl: './inicio-estudio.component.html',
	styleUrls: ['./inicio-estudio.component.scss'],
})
export class AdultoMayorComponent implements OnInit {
	patientId: number;

	constructor(
		private readonly route: ActivatedRoute,
		private dialog: MatDialog
	) {
		this.route.paramMap.subscribe((params) => {
			this.patientId = Number(params.get('idPaciente'));
		});
	}

	ngOnInit(): void {}

	mostrarPopup(): void {
		const dialogRef = this.dialog.open(EstudiosPopupComponent, {
			width: '800px',
			data: {
				patientId: this.patientId,
			},
		});

		dialogRef.afterClosed().subscribe((result) => {});
	}
}
