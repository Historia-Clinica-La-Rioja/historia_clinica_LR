import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosticReportInfoDto, MedicationInfoDto, PrescriptionDto } from '@api-rest/api-model';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ESTUDIOS, INDICACIONES, ORDENES_MEDICACION } from 'src/app/modules/historia-clinica/constants/summaries';
import { STUDY_STATUS } from '../../constants/orders-constant';
import { ConfirmarPrescripcionComponent } from '../../dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { NuevaPrescripcionComponent } from '../../dialogs/ordenes-prescripciones/nueva-prescripcion/nueva-prescripcion.component';
import { PrescripcionesService, PrescriptionTypes } from '../../services/prescripciones.service';
import { MedicationStatus, MedicationStatusChange } from '../../constants/prescripciones-masterdata';
import { SuspenderMedicacionComponent } from '../../dialogs/ordenes-prescripciones/suspender-medicacion/suspender-medicacion.component';
import { FormBuilder, FormGroup, FormArray } from '@angular/forms';

@Component({
	selector: 'app-ordenes',
	templateUrl: './ordenes.component.html',
	styleUrls: ['./ordenes.component.scss']
})
export class OrdenesComponent implements OnInit {

	public readonly medicacion = ORDENES_MEDICACION;
	public readonly estudios = ESTUDIOS;
	public readonly indicaciones = INDICACIONES;
	public readonly STUDY_STATUS = STUDY_STATUS;
	public readonly medicationStatus = MedicationStatus;
	public readonly medicationStatusChange = MedicationStatusChange;
    public medicationsInfo : MedicationInfoDto[];
	public diagnosticReportsInfo : DiagnosticReportInfoDto[];
	public selectedMedicationList: MedicationInfoDto[] = [];
	public medicationCheckboxes: FormGroup;

	@Input('patientId') patientId: number;

	constructor(
		private readonly dialog: MatDialog,
		private readonly formBuilder: FormBuilder,
		private prescripcionesService: PrescripcionesService,
		private snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.medicationCheckboxes = this.formBuilder.group({
			checkboxArray: this.formBuilder.array([])
		});

		this.getMedication();
		this.getStudy();
	}

	private getMedication(): void {
		this.prescripcionesService.getPrescription(PrescriptionTypes.MEDICATION, this.patientId, null, null, null).subscribe(
			response => {
				this.medicationsInfo = response;
				const checkboxArray = this.medicationCheckboxes.controls['checkboxArray'] as FormArray;

				this.medicationsInfo.forEach(m => {
					checkboxArray.push(this.formBuilder.group({checked: false}));
				});
			});
	}

	private getStudy(): void {
		this.prescripcionesService.getPrescription(PrescriptionTypes.STUDY, this.patientId, null, null, null).subscribe(
			response => {
				this.diagnosticReportsInfo = response;
			});
	}

	openDialogNewMedication(medication?: MedicationInfoDto) {
		const medicationList = this.getMedicationList(medication);

		if (medicationList) {
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
				if(newPrescription) {
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
						}, _ => {this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_MEDICATION_ERROR')});
					}
				}
			});
		}
	}

	openDialogNewStudy() {
		const newStudyDialog = this.dialog.open(NuevaPrescripcionComponent,
			{
				data: {
					patientId: this.patientId,
					titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.STUDY_TITLE',
					addLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.ADD_STUDY_LABEL',
					prescriptionType: PrescriptionTypes.STUDY,
					prescriptionItemList: undefined,
					addPrescriptionItemDialogData: {
						titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.STUDY_TITLE',
						searchSnomedLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.STUDY',
						showDosage: false,
						showStudyCategory: true,
						eclTerm: 'procedure',
					}
				},
				width: '35%',
			});

		newStudyDialog.afterClosed().subscribe((newPrescription: PrescriptionDto) => {
			if (newPrescription) {
				const newServiceRequest$ = this.prescripcionesService.createPrescription(PrescriptionTypes.STUDY, newPrescription, this.patientId);
				const confirmPrescriptionDialog = this.dialog.open(ConfirmarPrescripcionComponent,
					{
						disableClose: true,
						data: {
							titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.STUDY_TITLE',
							downloadButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.DOWNLOAD_BUTTON_STUDY',
							successLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_STUDY_SUCCESS',
							errorLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_STUDY_ERROR',
							prescriptionRequest: newServiceRequest$,
						},
						width: '35%',
					});

				confirmPrescriptionDialog.afterClosed().subscribe( (hasError: boolean) => {
					if (!hasError) {
						this.getStudy();
					}
				});
			}
		});
	}

	openDialogNewRecommendation() {
		//TODO completar con pop-up nueva recomendacion
		console.log("Nueva recomendacion");
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
	
			});
		}
	}

	changeMedicationStatus(statusChange: string, medication?: MedicationInfoDto) {
		const medicationList = this.getMedicationList(medication);

		if (medicationList) {
			this.prescripcionesService.changeMedicationStatus(statusChange, this.patientId, medicationList.map(m => m.id)).subscribe(() => {
				this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.MEDICATION_CHANGE_SUCCESS')
			}, _ => {this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.MEDICATION_CHANGE_ERROR')});
		}
	}

	downloadRecipe(medicationId: number) {
		this.prescripcionesService.downloadRecipe(this.patientId, medicationId).subscribe(() => {
			this.snackBarService.showSuccess('La receta se descargo correctamente')
		});
	}

	checkMedication(checked: boolean, medicationInfo: MedicationInfoDto) {
		if (checked) {
			this.selectedMedicationList.push(medicationInfo);
		} else {
			let selectedMedicationListCopy = [...this.selectedMedicationList];
			this.selectedMedicationList = selectedMedicationListCopy.filter(m => m.id !== medicationInfo.id);
		}	
	}

	selectAllMedication(checked: boolean) {
		const checkboxArray = this.medicationCheckboxes.controls['checkboxArray'] as FormArray;

		checkboxArray.controls.forEach(control => {
			control.setValue({checked: checked});
		});

		this.selectedMedicationList = checked ? this.medicationsInfo : []; 
	}

	checkMedicationStatus(statusId: string) {
		return this.selectedMedicationList.every(m => m.statusId === statusId);
	}

	renderStatusDescription(statusId: string): string {
		switch (statusId) {
			case this.medicationStatus.ACTIVE: 
				return 'Activa';
			case this.medicationStatus.STOPPED:
				return 'Finalizada';
			case this.medicationStatus.SUSPENDED:
				return 'Suspendida';
		}
	}

	private getMedicationList(medication?: MedicationInfoDto) {
		return medication ? [medication] : this.selectedMedicationList.length ? this.selectedMedicationList : null;
	}

}
