import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MIN_DATE } from '@core/utils/date.utils';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';

@Component({
	selector: 'app-anesthesia-form',
	templateUrl: './anesthesia-form.component.html',
	styleUrls: ['./anesthesia-form.component.scss']
})
export class AnesthesiaFormComponent {

	minDate = MIN_DATE;

	constructor(
		public dialogRef: MatDialogRef<AnesthesiaFormComponent>,
		@Inject(MAT_DIALOG_DATA) public readonly data: AnesthesiaData,
	) { }

	addAnesthesia(): void {
		if (this.data.anesthesiaService.addToList()) {
			this.dialogRef.close();
		}
	}

	close(): void {
		this.data.anesthesiaService.resetForm();
		this.dialogRef.close()
	}

}

interface AnesthesiaData {
	anesthesiaService: ProcedimientosService,
	searchConceptsLocallyFF: boolean
}
