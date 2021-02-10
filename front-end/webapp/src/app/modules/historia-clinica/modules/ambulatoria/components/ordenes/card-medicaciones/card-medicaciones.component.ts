import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MedicationInfoDto, PrescriptionDto } from '@api-rest/api-model';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ORDENES_MEDICACION } from 'src/app/modules/historia-clinica/constants/summaries';
import { ConfirmarPrescripcionComponent } from '../../../dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { NuevaPrescripcionComponent } from '../../../dialogs/ordenes-prescripciones/nueva-prescripcion/nueva-prescripcion.component';
import {PrescripcionesService, PrescriptionTypes} from '../../../services/prescripciones.service';
import { MedicationStatusChange } from '../../../constants/prescripciones-masterdata';
import { SuspenderMedicacionComponent } from '../../../dialogs/ordenes-prescripciones/suspender-medicacion/suspender-medicacion.component';
import { FormBuilder, FormGroup, FormArray } from '@angular/forms';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { PrescriptionItemData } from '../item-prescripciones/item-prescripciones.component';
import { MEDICATION_STATUS } from './../../../constants/prescripciones-masterdata';
import { PermissionsService } from '@core/services/permissions.service';
import { MedicacionesService } from '../../../services/medicaciones.service';
import { ERole } from '@api-rest/api-model';

const ROLES_TO_VIEW: ERole[] = [ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO];

@Component({
  selector: 'app-card-medicaciones',
  templateUrl: './card-medicaciones.component.html',
  styleUrls: ['./card-medicaciones.component.scss']
})
export class CardMedicacionesComponent implements OnInit {

	public readonly medicacion = ORDENES_MEDICACION;
	public readonly MEDICATION_STATUS = MEDICATION_STATUS;
	public readonly medicationStatusChange = MedicationStatusChange;
	public medicationsInfo: MedicationInfoDto[];
	public selectedMedicationList: MedicationInfoDto[] = [];
	public medicationCheckboxes: FormGroup;
	public hideFilterPanel = false;
	public formFilter: FormGroup;
	public medicamentStatus = [];
	private hasRoleToView: boolean;

	@Input() patientId: number;

	constructor(
		private readonly dialog: MatDialog,
		private readonly formBuilder: FormBuilder,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly permissionsService: PermissionsService,
		private prescripcionesService: PrescripcionesService,
		private snackBarService: SnackBarService,
		private medicacionesService: MedicacionesService,
	) { }

	ngOnInit(): void {
		this.formFilter = this.formBuilder.group({
			statusId: [null],
			medicationStatement: [null],
			healthCondition: [null],
		});

		this.medicationCheckboxes = this.formBuilder.group({
			checkboxArray: this.formBuilder.array([])
		});

		this.medicacionesService.medicaments$.subscribe(response => {
			this.medicationsInfo = response;
			const checkboxArray = this.medicationCheckboxes.controls.checkboxArray as FormArray;

			this.medicationsInfo.forEach(m => {
				checkboxArray.push(this.formBuilder.group({checked: false}));
			});
		});

		this.requestMasterDataService.medicationStatus().subscribe(status => {
			this.medicamentStatus = status;
		});

		this.getMedication();

		this.formFilter.controls.statusId.setValue(MEDICATION_STATUS.ACTIVE.id);

		this.permissionsService.hasContextAssignments$(ROLES_TO_VIEW).subscribe(hasRole => this.hasRoleToView = hasRole);

	}

	private getMedication(): void {
		this.medicacionesService.updateMedicationFilter( this.patientId,
																  this.formFilter.controls.statusId.value,
																  this.formFilter.controls.medicationStatement.value,
																  this.formFilter.controls.healthCondition.value);
	}

	private getMedicationList(medication?: MedicationInfoDto) {
		return medication ? [medication] : this.selectedMedicationList.length ? this.selectedMedicationList : null;
	}

	openDialogNewMedication(isNewMedication: boolean, medication?: MedicationInfoDto) {
		const medicationList = isNewMedication ? null : this.getMedicationList(medication);

		const newMedicationDialog = this.dialog.open(NuevaPrescripcionComponent,
			{
				data: {
					patientId: this.patientId,
					titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.MEDICATION_TITLE',
					addLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.ADD_MEDICATION_LABEL',
					prescriptionType: PrescriptionTypes.MEDICATION,
					prescriptionItemList: medicationList && medicationList.map(m => this.prescripcionesService.toNewPrescriptionItem(PrescriptionTypes.MEDICATION, m)),
					addPrescriptionItemDialogData: {
						titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.MEDICATION_TITLE',
						searchSnomedLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.MEDICATION',
						showDosage: true,
						showStudyCategory: false,
						eclTerm: 'medicine',
					}
				},
				width: '35%',
			});

		newMedicationDialog.afterClosed().subscribe((newPrescription: PrescriptionDto) => {
			if (newPrescription) {
				const newMedicationRequest$ = this.prescripcionesService.createPrescription(PrescriptionTypes.MEDICATION, newPrescription, this.patientId);
				if (newPrescription.hasRecipe) {
					const confirmPrescriptionDialog = this.dialog.open(ConfirmarPrescripcionComponent,
						{
							disableClose: true,
							data: {
								titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.MEDICATION_TITLE',
								downloadButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.DOWNLOAD_BUTTON_MEDICATION',
								successLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_MEDICATION_SUCCESS',
								errorLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_MEDICATION_ERROR',
								prescriptionType: PrescriptionTypes.MEDICATION,
								patientId: this.patientId,
								prescriptionRequest: newMedicationRequest$,
							},
							width: '35%'
						});

					confirmPrescriptionDialog.afterClosed().subscribe((hasError: boolean) => {
						if (!hasError) {
							this.getMedication();
						}
					});
				} else {
					newMedicationRequest$.subscribe((newRecipe: number) => {
						this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_MEDICATION_SUCCESS');
						this.getMedication();
					}, err => {
						this.snackBarService.showError(err.errors[0]);
					});
				}
			}
		});
	}

	openSuspendMedicationDialog(medication?: MedicationInfoDto) {
		const medicationList = this.getMedicationList(medication);

		if (medicationList) {
			const newSuspendMedicationDialog = this.dialog.open(SuspenderMedicacionComponent, {
				data: {
					medications: medicationList,
					patientId: this.patientId,
				},
				width: '35%',
			});

			newSuspendMedicationDialog.afterClosed().subscribe(() => {
				this.getMedication();
			});
		}
	}

	changeMedicationStatus(statusChange: string, medication?: MedicationInfoDto) {
		const medicationList = this.getMedicationList(medication);

		if (medicationList) {
			this.prescripcionesService.changeMedicationStatus(statusChange, this.patientId, medicationList.map(m => m.id)).subscribe(
				() => {
					this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.MEDICATION_CHANGE_SUCCESS');
					this.getMedication();
				},
				_ => {
					this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.MEDICATION_CHANGE_ERROR');
				}
			);
		}
	}

	downloadRecipe(medicationRequestId: number) {
		this.prescripcionesService.downloadPrescriptionPdf(this.patientId, [medicationRequestId], PrescriptionTypes.MEDICATION);
	}

	checkMedication(checked: boolean, medicationInfo: MedicationInfoDto) {
		if (checked) {
			this.selectedMedicationList.push(medicationInfo);
		} else {
			const selectedMedicationListCopy = [...this.selectedMedicationList];
			this.selectedMedicationList = selectedMedicationListCopy.filter(m => m.id !== medicationInfo.id);
		}
	}

	selectAllMedication(checked: boolean) {
		const checkboxArray = this.medicationCheckboxes.controls.checkboxArray as FormArray;

		checkboxArray.controls.forEach(control => {
			control.setValue({checked});
		});

		this.selectedMedicationList = checked ? this.medicationsInfo : [];
	}

	checkMedicationStatus(statusId: string): boolean {
		switch (statusId) {
			case this.MEDICATION_STATUS.ACTIVE.id:
				return this.selectedMedicationList.every(m => m.statusId === statusId);
			case this.MEDICATION_STATUS.STOPPED.id:
				return !this.selectedMedicationList.some(m => m.statusId === statusId);
		}
	}

	hideFilters() {
		this.hideFilterPanel = !this.hideFilterPanel;
	}

	filter(): void {
		this.getMedication();
	}

	clear(): void {
		this.formFilter.reset();
		this.formFilter.controls.statusId.setValue(MEDICATION_STATUS.ACTIVE.id);
		if (this.hideFilterPanel === false) {
			this.getMedication();
		}
	}

	prescriptionItemDataBuilder(medication: MedicationInfoDto): PrescriptionItemData {
		return {
			prescriptionStatus: this.prescripcionesService.renderStatusDescription(PrescriptionTypes.MEDICATION, medication.statusId),
			prescriptionPt: medication.snomed.pt,
			problemPt: medication.healthCondition.snomed.pt,
			doctor: medication.doctor,
			totalDays: medication.totalDays
		};
	}

	hasActionsMenu(medicationInfo: MedicationInfoDto): boolean {
		if (this.hasRoleToView) {
			return (medicationInfo.hasRecipe && medicationInfo.medicationRequestId !== null);
		}
		return true;
	}

}
