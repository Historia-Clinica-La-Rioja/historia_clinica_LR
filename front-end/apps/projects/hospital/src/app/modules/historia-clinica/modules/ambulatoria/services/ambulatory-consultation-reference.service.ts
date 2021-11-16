import { Inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { ReferenceComponent } from '../dialogs/reference/reference.component';

@Injectable({
	providedIn: 'root'
})
export class AmbulatoryConsultationReferenceService {

		constructor(
				private readonly dialog: MatDialog,
				@Inject(OVERLAY_DATA) public informationData: any,
				private readonly ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService,
		) { }

		openReferenceDialog(): void {
				const dialogRef = this.dialog.open(ReferenceComponent, {
					autoFocus: false,
					data: {
							newConsultationProblems: this.ambulatoryConsultationProblemsService.getProblemas(),
							idPatient: this.informationData.idPaciente,
					}
				});
		}
}
