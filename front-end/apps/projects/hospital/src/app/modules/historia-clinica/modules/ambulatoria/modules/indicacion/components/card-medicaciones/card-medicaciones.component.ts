import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HCEDocumentDataDto, ApiErrorMessageDto, AppFeature, MedicationInfoDto, ProfessionalLicenseNumberValidationResponseDto, ApiErrorDto } from '@api-rest/api-model.d';
import { SnomedECL } from '@api-rest/api-model';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ORDENES_MEDICACION } from '@historia-clinica/constants/summaries';
import { ConfirmarPrescripcionComponent } from '../../../../dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import {
	NewPrescription,
	NuevaPrescripcionComponent
} from '../../dialogs/nueva-prescripcion/nueva-prescripcion.component';
import { PrescripcionesService, PrescriptionTypes } from '../../../../services/prescripciones.service';
import { MedicationStatusChange, PRESCRIPTION_STATES } from '../../../../constants/prescripciones-masterdata';
import { SuspenderMedicacionComponent } from '../../dialogs/suspender-medicacion/suspender-medicacion.component';
import { UntypedFormBuilder, UntypedFormGroup, UntypedFormArray, AbstractControl } from '@angular/forms';
import { PrescriptionItemData } from '../item-prescripciones/item-prescripciones.component';
import { MEDICATION_STATUS } from '../../../../constants/prescripciones-masterdata';
import { PermissionsService } from '@core/services/permissions.service';
import { MedicacionesService } from '../../../../services/medicaciones.service';
import { ERole } from '@api-rest/api-model';
import { PrescripcionValidatorPopupComponent } from '../../dialogs/prescripcion-validator-popup/prescripcion-validator-popup.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { EnviarRecetaDigitalPorEmailComponent } from '@historia-clinica/modules/ambulatoria/dialogs/enviar-receta-digital-por-email/enviar-receta-digital-por-email.component';
import { anyMatch } from '@core/utils/array.utils';
import { processErrors } from '@core/utils/form.utils';
import { DocumentService } from '@api-rest/services/document.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { forkJoin, take } from 'rxjs';

const ROLES_TO_EDIT: ERole[] = [ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PRESCRIPTOR];

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
	public medicationCheckboxes: UntypedFormGroup;
	public hideFilterPanel = false;
	public formFilter: UntypedFormGroup;
	private hasRoleToEdit: boolean;
	isHabilitarRecetaDigitalEnabled: boolean = false;
	canOnlyViewSelfAddedProblems = false;
	rolesThatCanOnlyViewSelfAddedProblems = [ERole.PRESCRIPTOR];
	DISPENSED_STATE_ID: number = PRESCRIPTION_STATES.DISPENSADA.id;
	CANCELED_STATE_ID: number = PRESCRIPTION_STATES.ANULADA.id;
	PROVISIONAL_DISPENSED_STATE_ID: number = PRESCRIPTION_STATES.PROVISORIO_DISPENSADA.id;

	HABILITAR_RELACIONES_SNOMED = false;
	HABILITAR_DISPENSA = false;
	canDispense = false;

	@Input() patientId: number;
	@Input() personId?: number;
	@Input()
	set medicamentStatus(medicamentStatus: any[]) {
		this._medicamentStatus = medicamentStatus;
	}
	get medicamentStatus(): any[] {
		return this._medicamentStatus;
	}
	private _medicamentStatus: any[];

	constructor(
		private readonly dialog: MatDialog,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly permissionsService: PermissionsService,
		private prescripcionesService: PrescripcionesService,
		private snackBarService: SnackBarService,
		private medicacionesService: MedicacionesService,
		private readonly featureFlagService: FeatureFlagService,
		private documentService: DocumentService
	) {
		this.setFeatureFlags();
	}

	ngOnInit(): void {
		this.formFilter = this.formBuilder.group({
			statusId: [null],
			medicationStatement: [null],
			healthCondition: [null],
		});

		this.setPermissions();

		this.medicationCheckboxes = this.formBuilder.group({
			checkboxArray: this.formBuilder.array([])
		});

		this.medicacionesService.medicaments$.subscribe(response => {
			this.medicationsInfo = response;
			const checkboxArray = this.medicationCheckboxes.controls.checkboxArray as UntypedFormArray;

			this.medicationsInfo.forEach(m => {
				checkboxArray.push(this.formBuilder.group({ checked: false }));
			});
		});

		this.formFilter.controls.statusId.setValue(MEDICATION_STATUS.ACTIVE.id);

		this.getMedication();


		this.permissionsService.hasContextAssignments$(ROLES_TO_EDIT).subscribe(hasRole => this.hasRoleToEdit = hasRole);

	}

	private setPermissions(): void {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.canOnlyViewSelfAddedProblems = anyMatch<ERole>(userRoles, this.rolesThatCanOnlyViewSelfAddedProblems);
		});
	}

	private getMedication(): void {
		if (this.canOnlyViewSelfAddedProblems) {
			this.medicacionesService.updateMedicationFilterByRoles(this.patientId,
				this.formFilter.controls.statusId.value,
				this.formFilter.controls.medicationStatement.value,
				this.formFilter.controls.healthCondition.value);
		} else {
			this.medicacionesService.updateMedicationFilter(this.patientId,
				this.formFilter.controls.statusId.value,
				this.formFilter.controls.medicationStatement.value,
				this.formFilter.controls.healthCondition.value);
		}
	}

	private getMedicationList(medication?: MedicationInfoDto) {
		return medication ? [medication] : this.selectedMedicationList.length ? this.selectedMedicationList : null;
	}

	private openDailogPrescriptionValidator(result: ProfessionalLicenseNumberValidationResponseDto) {
		this.dialog.open(PrescripcionValidatorPopupComponent, {
			data: {
				twoFactorAuthenticationEnabled: result.twoFactorAuthenticationEnabled,
				healthcareProfessionalLicenseNumberValid: result.healthcareProfessionalLicenseNumberValid,
				healthcareProfessionalCompleteContactData: result.healthcareProfessionalCompleteContactData,
				healthcareProfessionalHasLicenses: result.healthcareProfessionalHasLicenses
			},
			width: '35%',
		});
	}

	private openNuevaPrescripcion(isNewMedication: boolean, medication?: MedicationInfoDto, patientEmail?: string) {
		const medicationList = isNewMedication ? null : this.getMedicationList(medication);
		const dialogTitle = this.isHabilitarRecetaDigitalEnabled ? 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.MEDICATION_TITLE_FF' : 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.MEDICATION_TITLE';
		const newMedicationDialog = this.dialog.open(NuevaPrescripcionComponent,
			{
				data: {
					patientId: this.patientId,
					personId: this.personId,
					titleLabel: dialogTitle,
					addLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.ADD_MEDICATION_LABEL',
					prescriptionType: PrescriptionTypes.MEDICATION,
					prescriptionItemList: medicationList && medicationList.map(m => this.prescripcionesService.toNewPrescriptionItem(PrescriptionTypes.MEDICATION, m)),
					addPrescriptionItemDialogData: {
						titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.MEDICATION_TITLE',
						searchSnomedLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.MEDICATION',
						showDosage: true,
						showStudyCategory: false,
						eclTerm: this.isHabilitarRecetaDigitalEnabled ? SnomedECL.MEDICINE_WITH_UNIT_OF_PRESENTATION : SnomedECL.MEDICINE,
					},
				},
				width: '760px',
			});

			newMedicationDialog.afterClosed().subscribe((newPrescription: NewPrescription) => {
					if (newPrescription?.prescriptionDto.hasRecipe) {
					this.dialog.open(ConfirmarPrescripcionComponent,
						{
							disableClose: true,
							data: {
								titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.MEDICATION_TITLE',
								downloadButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.DOWNLOAD_BUTTON_MEDICATION',
								sendEmail: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.SEND_EMAIL',
								successLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_MEDICATION_SUCCESS',
								prescriptionType: PrescriptionTypes.MEDICATION,
								patientId: this.patientId,
								prescriptionRequest: newPrescription.prescriptionRequestResponse,
								patientEmail,
								identificationNumber: newPrescription.identificationNumber
							},
							width: '35%'
						});
				} else if (newPrescription) {
					this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_MEDICATION_SUCCESS');
				}
			this.getMedication();
			this.cleanSelectedMedicationList();
		});
	}

	openDialogNewMedication(isNewMedication: boolean, medication?: MedicationInfoDto) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL)
			.subscribe((isFFActive: boolean) => {
				if (isFFActive) {
					this.validateProfessional(isNewMedication, medication);
					return;
				}

				this.openNuevaPrescripcion(isNewMedication, medication);
			})
	}

	private validateProfessional(isNewMedication: boolean, medication?: MedicationInfoDto) {
		this.prescripcionesService.validateProfessional(this.patientId)
			.subscribe((result: ProfessionalLicenseNumberValidationResponseDto) => {
				if (! result.healthcareProfessionalCompleteContactData
					|| ! result.healthcareProfessionalLicenseNumberValid
					|| ! result.twoFactorAuthenticationEnabled
					|| ! result.healthcareProfessionalHasLicenses) {
						this.openDailogPrescriptionValidator(result);
						return;
					}

				this.openNuevaPrescripcion(isNewMedication, medication, result?.patientEmail);
			}, (error: ApiErrorMessageDto) => {
				processErrors(error, (msg) => this.snackBarService.showError(msg));
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
				this.cleanSelectedMedicationList();
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
					this.cleanSelectedMedicationList();
				},
				_ => {
					this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.MEDICATION_CHANGE_ERROR');
				}
			);
		}
	}

	downloadRecipe(documentData: HCEDocumentDataDto) {
		this.documentService.downloadFile(documentData);
	}

	openSendEmailDialog(medicationInfo: MedicationInfoDto) {
		this.dialog.open(EnviarRecetaDigitalPorEmailComponent, {
			width: '35%',
			data: {
				patientId: this.patientId,
				prescriptionRequest: [{
					requestId: medicationInfo.id,
					documentId: medicationInfo.hceDocumentData.id
				}]
			}
		})
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
		const checkboxArray = this.medicationCheckboxes.controls.checkboxArray as UntypedFormArray;

		checkboxArray.controls.forEach(control => {
			control.setValue({ checked });
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

	clearFilterField(control: AbstractControl): void {
		control.reset();
	}

	prescriptionItemDataBuilder(medication: MedicationInfoDto): PrescriptionItemData {
		return {
			prescriptionStatus: this.prescripcionesService.renderStatusDescription(PrescriptionTypes.MEDICATION, medication.statusId),
			prescriptionPt: medication.snomed.pt,
			problemPt: medication.healthCondition.snomed.pt,
			doctor: medication.doctor,
			totalDays: medication.totalDays,
			observation: medication.observations ? medication.observations.trim() : '',
			prescriptionLineState: this.prescripcionesService.renderPrescriptionLineState(medication.prescriptionLineState),
			isDigital: medication.isDigital,
		};
	}

	hasActionsMenu(medicationInfo: MedicationInfoDto): boolean {
		if (!this.isHabilitarRecetaDigitalEnabled
			&& (medicationInfo.prescriptionLineState == this.DISPENSED_STATE_ID
			|| medicationInfo.prescriptionLineState == this.PROVISIONAL_DISPENSED_STATE_ID
			|| medicationInfo.prescriptionLineState == this.CANCELED_STATE_ID))
			return false;

		if (this.hasRoleToEdit) {
			return true;
		}
		return this.hasRecipe(medicationInfo);
	}

	hasRecipe(medicationInfo: MedicationInfoDto): boolean {
		return (medicationInfo.hasRecipe && medicationInfo.medicationRequestId !== null);
	}

	cleanSelectedMedicationList() {
		this.selectedMedicationList = [];
		const checkboxArray = this.medicationCheckboxes.controls.checkboxArray as UntypedFormArray;
		checkboxArray.controls.forEach(control => {
			control.setValue({ checked: false });
		});
	}

	openCancelDialog(id: number) {
		this.dialog.open(DiscardWarningComponent, {
			data: {
				title: 'ambulatoria.paciente.ordenes_prescripciones.menu_items.cancel.dialog.TITLE',
				content: 'ambulatoria.paciente.ordenes_prescripciones.menu_items.cancel.dialog.CONTENT',
				contentBold: 'ambulatoria.paciente.ordenes_prescripciones.menu_items.cancel.dialog.QUESTION',
				cancelButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.menu_items.cancel.dialog.DISAGREE',
				okButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.menu_items.cancel.dialog.AGREE',
				okBottonColor: 'warn'
			}
		}).afterClosed().subscribe((result: boolean) => {
			if (result) {
				this.prescripcionesService.cancelPrescriptionLineState(id, this.patientId)
					.subscribe(() => {
						this.getMedication();
						this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.menu_items.cancel.dialog.SUCCESS')
					}, (error: ApiErrorDto) => {
						this.snackBarService.showError(error.errors[0]);
					});
			}
		});
	}

	private setFeatureFlags = () => {
		forkJoin([
			this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL).pipe(take(1)),
			this.featureFlagService.isActive(AppFeature.HABILITAR_DISPENSA).pipe(take(1)),
			this.featureFlagService.isActive(AppFeature.HABILITAR_RELACIONES_SNOMED).pipe(take(1))
		]).subscribe(([isHabilitarRecetaDigital, isHabilitarDispensa, isHabilitarRelacionesSnomed]: [boolean, boolean, boolean]) => {
			this.isHabilitarRecetaDigitalEnabled = isHabilitarRecetaDigital;
			this.HABILITAR_DISPENSA = isHabilitarDispensa;
			this.HABILITAR_RELACIONES_SNOMED = isHabilitarRelacionesSnomed;
			this.setCanDispense();
		})
	}	

	private setCanDispense = () => {
		this.canDispense = this.isHabilitarRecetaDigitalEnabled && this.HABILITAR_DISPENSA && this.HABILITAR_RELACIONES_SNOMED;
	}
}
