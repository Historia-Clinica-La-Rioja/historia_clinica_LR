import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AgregarPreinscripcionItemComponent, NewPrescriptionItem } from './../agregar-preinscripcion-item/agregar-preinscripcion-item.component';

@Component({
  selector: 'app-nueva-preinscripcion',
  templateUrl: './nueva-preinscripcion.component.html',
  styleUrls: ['./nueva-preinscripcion.component.scss']
})
export class NuevaPreinscripcionComponent implements OnInit {

	constructor(
		private readonly dialog: MatDialog,
		public dialogRef: MatDialogRef<NuevaPreinscripcionComponent>,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionData) { }

	ngOnInit(): void {
	}

	closeModal(withRecipe: boolean) {
		this.dialogRef.close(withRecipe);
	}

	addPrescriptionItem() {
		const newPrescriptionItemDialog = this.dialog.open(AgregarPreinscripcionItemComponent,
		{
			data: {
				patientId: this.data.patientId,
				titleLabel: this.data.childData.titleLabel,
				searchSnomedLabel: this.data.childData.searchSnomedLabel,
				showDosage: this.data.childData.showDosage,
				eclTerm: this.data.childData.eclTerm,
			},
			width: '35%',
		});

		newPrescriptionItemDialog.afterClosed().subscribe((prescriptionItem: NewPrescriptionItem) => {
			if (prescriptionItem) {
				console.log(prescriptionItem);
			}
		});
	}

	confirmPrescription() {
		this.closeModal(true);
	}

}

export class NewPrescriptionData {
	patientId: string;
	titleLabel: string;
	addLabel: string;
	hasMedicalCoverage: boolean;
	prescriptionItemList: any[];
	childData: {
		titleLabel: string;
		searchSnomedLabel: string;
		showDosage: boolean;
		eclTerm: string;
	}
}