import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MedicationRequestService } from '@api-rest/services/medication-request.service';
import { SnomedDto } from '@api-rest/api-model';
import { NewPrescriptionData } from '../../dialogs/nueva-prescripcion/nueva-prescripcion.component';
import { AgregarPrescripcionItemComponent, NewPrescriptionItem } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { PharmacosFrequentComponent } from '../../dialogs/pharmacos-frequent/pharmacos-frequent.component';
import { mapToNewPrescriptionItem } from '../../utils/prescripcion-mapper';
import { PrescriptionForm, StatePrescripcionService } from '../../services/state-prescripcion.service';

@Component({
    selector: 'app-medication-information',
    templateUrl: './medication-information.component.html',
    styleUrls: ['./medication-information.component.scss']
})
export class MedicationInformationComponent implements OnInit {

    @Input() prescriptionData: NewPrescriptionData;
    @Input() isHabilitarRecetaDigitalEnabled: boolean;
    @Input() submitted: boolean;

    @Output() showMedicationErrorEmitter = new EventEmitter<boolean>();
	@Output() prescriptionItemsEmmiter = new EventEmitter<NewPrescriptionItem[]>();

	prescriptionForm: FormGroup<PrescriptionForm>;
    prescriptionItems: NewPrescriptionItem[];
    itemCount = 0;
    showAddMedicationError = false;
    isAddMedicationLoading = false;

    constructor(
        private readonly dialog: MatDialog,
        private readonly medicationRequestService: MedicationRequestService,
		private statePrescripcionService: StatePrescripcionService,
    ) { }

    ngOnInit(): void {
		this.prescriptionForm = this.statePrescripcionService.getForm();
        this.prescriptionItems = this.prescriptionData.prescriptionItemList ? this.prescriptionData.prescriptionItemList : [];
		this.prescriptionItemsEmmiter.emit(this.prescriptionItems);
    }

    openPrescriptionItemDialog(item?: NewPrescriptionItem): void {
		const newPrescriptionItemDialog = this.dialog.open(AgregarPrescripcionItemComponent,
		{
			data: {
				patientId: this.prescriptionData.patientId,
				titleLabel: this.prescriptionData.addPrescriptionItemDialogData.titleLabel,
				searchSnomedLabel: this.prescriptionData.addPrescriptionItemDialogData.searchSnomedLabel,
				showDosage: this.prescriptionData.addPrescriptionItemDialogData.showDosage,
				showStudyCategory: this.prescriptionData.addPrescriptionItemDialogData.showStudyCategory,
				eclTerm: this.prescriptionData.addPrescriptionItemDialogData.eclTerm,
				item,
			},
			width: '35%',
		});

		newPrescriptionItemDialog.afterClosed().subscribe((prescriptionItem: NewPrescriptionItem) => {
			if (prescriptionItem) {
				if (!prescriptionItem.id) {
					prescriptionItem.id = ++this.itemCount;
					this.prescriptionItems.push(prescriptionItem);
					this.prescriptionItemsEmmiter.emit(this.prescriptionItems);
					this.setShowAddMedicationError();
				} else {
					this.editPrescriptionItem(prescriptionItem);
				}
			}
		});
	}

    deletePrescriptionItem(prescriptionItem: NewPrescriptionItem): void {
		this.prescriptionItems.splice(this.prescriptionItems.findIndex(item => item.id === prescriptionItem.id), 1);
		this.prescriptionItemsEmmiter.emit(this.prescriptionItems);
		this.setShowAddMedicationError();
	}

    openPharmacosFrequestDialog() {
		this.isAddMedicationLoading = true;
		this.medicationRequestService.mostFrequentPharmacosPreinscription(this.prescriptionData.patientId).subscribe((pharmacos: SnomedDto[]) => {
			this.isAddMedicationLoading = false;
			this.dialog.open(PharmacosFrequentComponent, {
				width: '50%',
				data: { pharmacos }
			}).afterClosed().subscribe(result => {
				if (!result || !result.openFormPharmaco) return;
				if (!result.pharmaco && result.openFormPharmaco) return this.openPrescriptionItemDialog();

				this.openPrescriptionItemDialog(mapToNewPrescriptionItem(result.pharmaco));
			});
		})
	}

    private setShowAddMedicationError() {
		this.showAddMedicationError = this.prescriptionItems.length == 0;
	}

    private editPrescriptionItem(prescriptionItem: NewPrescriptionItem): void {
		const editPrescriptionItem = this.prescriptionItems.find(pi => pi.id === prescriptionItem.id);

		editPrescriptionItem.snomed = prescriptionItem.snomed;
		editPrescriptionItem.healthProblem = prescriptionItem.healthProblem;
		editPrescriptionItem.unitDose = prescriptionItem.unitDose;
		editPrescriptionItem.dayDose = prescriptionItem.dayDose;
		editPrescriptionItem.quantity = prescriptionItem.quantity;
		editPrescriptionItem.administrationTimeDays = prescriptionItem.administrationTimeDays;
		editPrescriptionItem.isChronicAdministrationTime = prescriptionItem.isChronicAdministrationTime;
		editPrescriptionItem.intervalHours = prescriptionItem.intervalHours;
		editPrescriptionItem.isDailyInterval = prescriptionItem.isDailyInterval;
		editPrescriptionItem.studyCategory = prescriptionItem.studyCategory;
		editPrescriptionItem.observations = prescriptionItem.observations;
	}

}