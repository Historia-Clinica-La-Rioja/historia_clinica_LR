import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MedicationRequestService } from '@api-rest/services/medication-request.service';
import { ApiErrorDto, AppFeature, SnomedDto } from '@api-rest/api-model';
import { NewPrescriptionData } from '../../dialogs/nueva-prescripcion/nueva-prescripcion.component';
import { AgregarPrescripcionItemComponent, NewPrescriptionItem } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { PharmacosFrequentComponent } from '../../dialogs/pharmacos-frequent/pharmacos-frequent.component';
import { mapToNewPrescriptionItem } from '../../utils/prescripcion-mapper';
import { PrescriptionForm, StatePrescripcionService } from '../../services/state-prescripcion.service';
import { PharmacoDetail, PharmacoDetailTitle } from '@hsi-components/pharmaco-detail/pharmaco-detail.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { AddDigitalPrescriptionItemComponent } from '@historia-clinica/modules/ambulatoria/dialogs/add-digital-prescription-item/add-digital-prescription-item.component';
import { TranslateService } from '@ngx-translate/core';
import { OldDigitalPrescriptionItemComponent } from '@historia-clinica/modules/ambulatoria/dialogs/old-digital-prescription-item/old-digital-prescription-item.component';
import { processErrors } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { DigitalPrescriptionUnitPluralizeService } from '@historia-clinica/modules/ambulatoria/services/digital-prescription-unit-pluralize.service';

@Component({
    selector: 'app-medication-information',
    templateUrl: './medication-information.component.html',
    styleUrls: ['./medication-information.component.scss']
})
export class MedicationInformationComponent implements OnInit {

    @Input() prescriptionData: NewPrescriptionData;
    @Input() isHabilitarRecetaDigital: boolean;
    @Input() submitted: boolean;
	@Input() hasSelectedCoverage = false;

    @Output() showMedicationErrorEmitter = new EventEmitter<boolean>();
	@Output() prescriptionItemsEmmiter = new EventEmitter<NewPrescriptionItem[]>();

	prescriptionForm: FormGroup<PrescriptionForm>;
    prescriptionItems: NewPrescriptionItem[];
	pharmacosDetail: PharmacoDetail[] = [];
    itemCount = 0;
    showAddMedicationError = false;
    isAddMedicationLoading = false;
	isEnabledFinancedPharmaco = false;
	HABILITAR_RECETA_DIGITAL_ACTUALIZADA = false;

    constructor(
        private readonly dialog: MatDialog,
        private readonly medicationRequestService: MedicationRequestService,
		private statePrescripcionService: StatePrescripcionService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly translate: TranslateService,
		private readonly oldDigital: DialogService<OldDigitalPrescriptionItemComponent>,
		private readonly newDigital: DialogService<AddDigitalPrescriptionItemComponent>,
		private readonly snackBar: SnackBarService,
		private readonly digitalPrescriptionUnitPluralize: DigitalPrescriptionUnitPluralizeService
    ) { }

    ngOnInit(): void {
		this.prescriptionForm = this.statePrescripcionService.getForm();
        this.prescriptionItems = this.prescriptionData.prescriptionItemList ? this.prescriptionData.prescriptionItemList : [];
		this.prescriptionItems.forEach(prescriptionItem => {
			const newPharmacoDetail = this.buildPharmacoDetail(prescriptionItem);
			if (!this.pharmacosDetail.find(pharmaco => pharmaco.id === newPharmacoDetail.id)) {
				this.pharmacosDetail.push(newPharmacoDetail);
			}
		});
		this.prescriptionItemsEmmiter.emit(this.prescriptionItems);
		this.featureFlagService.isActive(AppFeature.HABILITAR_FINANCIACION_DE_MEDICAMENTOS).subscribe(isOn => this.isEnabledFinancedPharmaco = isOn);
		this.setFeatureFlags();
    }

    openPrescriptionItemDialog(item?: NewPrescriptionItem): void {
		if (this.HABILITAR_RECETA_DIGITAL_ACTUALIZADA) return this.openAddDigitalPrescriptionItemDialog(item);
		if (this.isHabilitarRecetaDigital) return this.openOldAddDigitalPrescriptionItemDialog(item);

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
				hasSelectedCoverage: this.hasSelectedCoverage
			},
			width: '35%',
		});
		newPrescriptionItemDialog.afterClosed().subscribe((prescriptionItem: NewPrescriptionItem) => this.addPrescriptionItem(prescriptionItem))
	}

    editMedication(id: number): void {
		this.openPrescriptionItemDialog(this.prescriptionItems.find(item => item.id === id));
	}

	deleteMedication(id: number): void {
		this.pharmacosDetail.splice(this.pharmacosDetail.findIndex(item => item.id === id), 1);
		this.prescriptionItems.splice(this.prescriptionItems.findIndex(item => item.id === id), 1);
		this.prescriptionItemsEmmiter.emit(this.prescriptionItems);
		this.setShowAddMedicationError();
	}

    openPharmacosFrequestDialog() {
		this.isAddMedicationLoading = true;
		const hasToSearchInFoundedPharmaco = this.isEnabledFinancedPharmaco && this.isHabilitarRecetaDigital && !this.hasSelectedCoverage;
		if (hasToSearchInFoundedPharmaco) {
			this.isAddMedicationLoading = false;
			this.openPrescriptionItemDialog();
			return;
		}

		this.medicationRequestService.mostFrequentPharmacosPreinscription(this.prescriptionData.patientId).subscribe({
			next: (pharmacos: SnomedDto[]) => {
				this.isAddMedicationLoading = false;
				this.openPharmacosFrequent(pharmacos);
			},
			error: (error: ApiErrorDto) => {
				processErrors(error, (msg) => this.snackBar.showError(msg));
				this.isAddMedicationLoading = false;
			}
		})
	}

	private openPharmacosFrequent = (pharmacos: SnomedDto[]) => {
		this.dialog.open(PharmacosFrequentComponent, {
			width: '50%',
			data: { pharmacos }
		}).afterClosed().subscribe(result => {
			if (!result || !result.openFormPharmaco) return;
			if (!result.pharmaco && result.openFormPharmaco) return this.openPrescriptionItemDialog();
			this.openPrescriptionItemDialog(mapToNewPrescriptionItem(result.pharmaco));
		});
	}

	private setFeatureFlags = () => {
		this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL_ACTUALIZADA).subscribe((value: boolean) => this.HABILITAR_RECETA_DIGITAL_ACTUALIZADA = value);
	}

	private openAddDigitalPrescriptionItemDialog = (item?: NewPrescriptionItem) => {
		this.newDigital.open(
			AddDigitalPrescriptionItemComponent,
			{dialogWidth: DialogWidth.LARGE},
			this.getAddDigitalPrescriptionItemDialogData(item)
		).afterClosed().subscribe((prescriptionItem: NewPrescriptionItem) => this.addPrescriptionItem(prescriptionItem));
	}

	private openOldAddDigitalPrescriptionItemDialog = (item?: NewPrescriptionItem) => {
		this.oldDigital.open(
			OldDigitalPrescriptionItemComponent,
			{dialogWidth: DialogWidth.LARGE},
			this.getAddDigitalPrescriptionItemDialogData(item)
		).afterClosed().subscribe((prescriptionItem: NewPrescriptionItem) => this.addPrescriptionItem(prescriptionItem));
	}

	private addPrescriptionItem = (prescriptionItem: NewPrescriptionItem) => {
		if (!prescriptionItem) return;
		if (!prescriptionItem.id) {
			this.addPrescriptionItemsToPharmacosDetail(prescriptionItem)
			this.setShowAddMedicationError();
		} else {
			this.editPrescriptionItem(prescriptionItem);
			this.editPharmacoDetail(prescriptionItem);
		}
	}

	private addPrescriptionItemsToPharmacosDetail(prescriptionItem: NewPrescriptionItem) {
		prescriptionItem.id = ++this.itemCount;
		this.prescriptionItems.push(prescriptionItem);
		this.pharmacosDetail.push(this.buildPharmacoDetail(prescriptionItem));
		this.prescriptionItemsEmmiter.emit(this.prescriptionItems);
	}

	private getAddDigitalPrescriptionItemDialogData = (item?: NewPrescriptionItem) => {
		return {
			patientId: this.prescriptionData.patientId,
			titleLabel: this.prescriptionData.addPrescriptionItemDialogData.titleLabel,
			searchSnomedLabel: this.prescriptionData.addPrescriptionItemDialogData.searchSnomedLabel,
			showDosage: this.prescriptionData.addPrescriptionItemDialogData.showDosage,
			showStudyCategory: this.prescriptionData.addPrescriptionItemDialogData.showStudyCategory,
			eclTerm: this.prescriptionData.addPrescriptionItemDialogData.eclTerm,
			item,
			hasSelectedCoverage: this.hasSelectedCoverage
		}
	}

	private buildPharmacoDetail(prescriptionItem: NewPrescriptionItem): PharmacoDetail {
		return {
			id: prescriptionItem.id,
			pt: prescriptionItem.snomed.pt,
			dayDose: this.buildDayDose(prescriptionItem),
			quantity: this.buildQuantity(prescriptionItem),
			treatmentDays: prescriptionItem.administrationTimeDays,
			unitDose: prescriptionItem.unitDose,
			commercialMedicationPrescription: prescriptionItem.commercialMedicationPrescription,
			commercialPt: prescriptionItem.suggestedCommercialMedication?.pt,
			interval: this.buildInterval(prescriptionItem),
			observations: prescriptionItem.observations,
			healthProblem: prescriptionItem.healthProblem.description,
			titles: this.buildTitles()
		}
	}

	private buildQuantity = (prescriptionItem: NewPrescriptionItem): string => {
		let unit = prescriptionItem.quantity?.unit || '';
		unit = this.digitalPrescriptionUnitPluralize.run(unit);

		return prescriptionItem.quantity?.value ? `${prescriptionItem.quantity.value} ${unit}`: '';
	}

	private buildTitles = (): PharmacoDetailTitle => {
		if (this.HABILITAR_RECETA_DIGITAL_ACTUALIZADA) {
			return {
				unitDose: 'ambulatoria.paciente.ordenes_prescripciones.add_digital_prescription_item_dialog.QUANTITY',
				dayDose: 'ambulatoria.paciente.ordenes_prescripciones.add_digital_prescription_item_dialog.FREQUENCY',
				quantity: 'ambulatoria.paciente.ordenes_prescripciones.add_digital_prescription_item_dialog.TOTAL_QUANTITY'
			}
		}
	}

	private buildDayDose = (prescriptionItem: NewPrescriptionItem) => {
		if (prescriptionItem.dayDose && prescriptionItem.frequencyType && this.HABILITAR_RECETA_DIGITAL_ACTUALIZADA) {
			return this.translate.instant('ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.INTERVAL', {
				frequency: prescriptionItem.dayDose,
				frequencyType: prescriptionItem.frequencyType
			});
		}

		if (prescriptionItem.dayDose)
			return `${prescriptionItem.dayDose}`;
	}

	private buildInterval = (prescriptionItem: NewPrescriptionItem): string | null => {
		if (prescriptionItem.isDailyInterval) return 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.DAILY';
		if (prescriptionItem.intervalHours) return `${prescriptionItem.intervalHours} hs`;
		return null;
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
		editPrescriptionItem.commercialMedicationPrescription = prescriptionItem.commercialMedicationPrescription;
		editPrescriptionItem.suggestedCommercialMedication = prescriptionItem.suggestedCommercialMedication;
		editPrescriptionItem.frequencyType = prescriptionItem.frequencyType;
	}

	private editPharmacoDetail(prescriptionItem: NewPrescriptionItem): void {
		const editPharmacoDetail = this.pharmacosDetail.find(p => p.id === prescriptionItem.id);
		const pharmacoDetail = this.buildPharmacoDetail(prescriptionItem);

		editPharmacoDetail.id = pharmacoDetail.id;
		editPharmacoDetail.pt = pharmacoDetail.pt;
		editPharmacoDetail.treatmentDays = pharmacoDetail.treatmentDays;
		editPharmacoDetail.unitDose = pharmacoDetail.unitDose;
		editPharmacoDetail.quantity = pharmacoDetail.quantity;
		editPharmacoDetail.observations = pharmacoDetail.observations;
		editPharmacoDetail.interval = pharmacoDetail.interval;
		editPharmacoDetail.healthProblem = pharmacoDetail.healthProblem;
		editPharmacoDetail.dayDose = pharmacoDetail.dayDose;
		editPharmacoDetail.commercialPt = pharmacoDetail.commercialPt;
		editPharmacoDetail.commercialMedicationPrescription = pharmacoDetail.commercialMedicationPrescription;
	}
}
