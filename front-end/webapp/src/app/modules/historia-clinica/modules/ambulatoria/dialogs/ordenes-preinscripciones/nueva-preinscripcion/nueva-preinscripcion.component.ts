import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientMedicalCoverage } from '@core/dialogs/medical-coverage/medical-coverage.component';
import { MapperService } from '@core/services/mapper.service';
import { map } from 'rxjs/operators';
import { AgregarPreinscripcionItemComponent, NewPrescriptionItem } from './../agregar-preinscripcion-item/agregar-preinscripcion-item.component';

@Component({
  selector: 'app-nueva-preinscripcion',
  templateUrl: './nueva-preinscripcion.component.html',
  styleUrls: ['./nueva-preinscripcion.component.scss']
})
export class NuevaPreinscripcionComponent implements OnInit {

	prescriptionItems: NewPrescriptionItem[];
	patientMedicalCoverages: PatientMedicalCoverage[];
	prescriptionForm: FormGroup;
	itemCount = 0;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly dialog: MatDialog,
		public dialogRef: MatDialogRef<NuevaPreinscripcionComponent>,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionData) { }

	ngOnInit(): void {
		this.prescriptionForm = this.formBuilder.group({
			patientMedicalCoverage: [null],
			withRecipe: [null],
		});

		this.prescriptionItems = this.data.prescriptionItemList ? this.data.prescriptionItemList : [];
		this.setMedicalCoverages();

	}

	closeModal(withRecipe: boolean) {
		this.dialogRef.close(withRecipe);
	}

	openPrescriptionItemDialog(item?: NewPrescriptionItem) {
		const newPrescriptionItemDialog = this.dialog.open(AgregarPreinscripcionItemComponent,
		{
			data: {
				patientId: this.data.patientId,
				titleLabel: this.data.childData.titleLabel,
				searchSnomedLabel: this.data.childData.searchSnomedLabel,
				showDosage: this.data.childData.showDosage,
				eclTerm: this.data.childData.eclTerm,
				item: item,
			},
			width: '35%',
		});

		newPrescriptionItemDialog.afterClosed().subscribe((prescriptionItem: NewPrescriptionItem) => {
			if (prescriptionItem) {
				if (!prescriptionItem.id) {
					prescriptionItem.id = ++this.itemCount;
					this.prescriptionItems.push(prescriptionItem);
				} else {
					let editPrescriptionItem = this.prescriptionItems.find(pi => pi.id === prescriptionItem.id);
					editPrescriptionItem.healthProblem = prescriptionItem.healthProblem;
					editPrescriptionItem.administrationTimeDays = prescriptionItem.administrationTimeDays;
					editPrescriptionItem.intervalHours = prescriptionItem.intervalHours;
					editPrescriptionItem.isChronicAdministrationTime = prescriptionItem.isChronicAdministrationTime;
					editPrescriptionItem.isDailyInterval = prescriptionItem.isDailyInterval;
					editPrescriptionItem.observations = prescriptionItem.observations;
					editPrescriptionItem.snomed = prescriptionItem.snomed;
				}
			}
		});
	}

	confirmPrescription() {
		this.closeModal(true);
	}

	deletePrescriptionItem(prescriptionItem: NewPrescriptionItem): void {
		this.prescriptionItems.splice(this.prescriptionItems.findIndex(item => item.id === prescriptionItem.id) , 1);
	}

	getDosage(prescriptionItem: NewPrescriptionItem): string {
		const intervalText = prescriptionItem.isDailyInterval ? 'Diario - ' : `Cada ${prescriptionItem.intervalHours} hs - `;
		const administrationTimeText = prescriptionItem.isChronicAdministrationTime ? 'Habitual' : `Durante ${prescriptionItem.administrationTimeDays} días`;
		return (intervalText + administrationTimeText);
	}

	getFullMedicalCoverageText(patientMedicalCoverage): string {
		const medicalCoverageText = [patientMedicalCoverage.medicalCoverage.acronym, patientMedicalCoverage.medicalCoverage.name]
			.filter(Boolean).join(' - ');
		return [medicalCoverageText, patientMedicalCoverage.affiliateNumber].filter(Boolean).join(' / ');
	}

	private setMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(Number(this.data.patientId))
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => this.patientMedicalCoverages = patientMedicalCoverages);
	}

}

export class NewPrescriptionData {
	patientId: string;
	titleLabel: string;
	addLabel: string;
	canRecipe: boolean;
	prescriptionItemList: any[];
	childData: {
		titleLabel: string;
		searchSnomedLabel: string;
		showDosage: boolean;
		eclTerm: string;
	}
}