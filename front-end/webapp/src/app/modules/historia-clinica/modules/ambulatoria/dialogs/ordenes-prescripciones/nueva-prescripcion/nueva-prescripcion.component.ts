import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientMedicalCoverage } from '@core/dialogs/medical-coverage/medical-coverage.component';
import { MapperService } from '@core/services/mapper.service';
import { map } from 'rxjs/operators';
import { AgregarPrescripcionItemComponent, NewPrescriptionItem } from '../agregar-prescripcion-item/agregar-prescripcion-item.component';
import { PrescriptionDto } from '@api-rest/api-model';

@Component({
  selector: 'app-nueva-prescripcion',
  templateUrl: './nueva-prescripcion.component.html',
  styleUrls: ['./nueva-prescripcion.component.scss']
})
export class NuevaPrescripcionComponent implements OnInit {

	prescriptionItems: NewPrescriptionItem[];
	patientMedicalCoverages: PatientMedicalCoverage[];
	prescriptionForm: FormGroup;
	itemCount = 0;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly dialog: MatDialog,
		public dialogRef: MatDialogRef<NuevaPrescripcionComponent>,
		@Inject(MAT_DIALOG_DATA) public data: NewPrescriptionData) { }

	ngOnInit(): void {
		this.prescriptionForm = this.formBuilder.group({
			patientMedicalCoverage: [null],
			withRecipe: [false],
		});

		this.prescriptionItems = this.data.prescriptionItemList ? this.data.prescriptionItemList : [];
		this.setMedicalCoverages();

	}

	closeModal(newPrescription?: PrescriptionDto) {
		this.dialogRef.close(newPrescription);
	}

	openPrescriptionItemDialog(item?: NewPrescriptionItem) {
		const newPrescriptionItemDialog = this.dialog.open(AgregarPrescripcionItemComponent,
		{
			data: {
				patientId: this.data.patientId,
				titleLabel: this.data.addPrescriptionItemDialogData.titleLabel,
				searchSnomedLabel: this.data.addPrescriptionItemDialogData.searchSnomedLabel,
				showDosage: this.data.addPrescriptionItemDialogData.showDosage,
				eclTerm: this.data.addPrescriptionItemDialogData.eclTerm,
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
		const newPrescription: PrescriptionDto = {
			hasRecipe: this.data.canRecipe ? this.prescriptionForm.controls.withRecipe.value : true,
			medicalCoverageId: this.prescriptionForm.controls.patientMedicalCoverage.value.id,
			items: this.prescriptionItems.map(pi => {
				return {
					healthConditionId: pi.healthProblem.id,
					observations: pi.observations,
					snomed: {
						id: pi.snomed.id,
						parentFsn: null,
						parentId: null,
						pt: pi.snomed.pt
					},
					dosage: {
						chronic: pi.isChronicAdministrationTime,
						diary: pi.isDailyInterval,
						duration: Number(pi.administrationTimeDays),
						frequency: Number(pi.intervalHours)
					}
				}
			})
		}

		this.closeModal(newPrescription);
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
	addPrescriptionItemDialogData: {
		titleLabel: string;
		searchSnomedLabel: string;
		showDosage: boolean;
		eclTerm: string;
	}
}