import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PrescriptionDto } from '@api-rest/api-model';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ESTUDIOS, INDICACIONES, ORDENES_MEDICACION } from 'src/app/modules/historia-clinica/constants/summaries';
import { ConfirmarPrescripcionComponent } from '../../dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { NuevaPrescripcionComponent } from '../../dialogs/ordenes-prescripciones/nueva-prescripcion/nueva-prescripcion.component';
import { PrescripcionesService, PrescriptionTypes } from '../../services/prescripciones.service';

@Component({
	selector: 'app-ordenes',
	templateUrl: './ordenes.component.html',
	styleUrls: ['./ordenes.component.scss']
})
export class OrdenesComponent implements OnInit {

	public readonly medicacion = ORDENES_MEDICACION;
	public readonly estudios = ESTUDIOS;
	public readonly indicaciones = INDICACIONES;

	@Input('patientId') patientId: number;

	constructor(
		private readonly dialog: MatDialog,
		private prescripcionesService: PrescripcionesService,
		private snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
	}

	openDialogNewMedication() {
		const newMedicationDialog = this.dialog.open(NuevaPrescripcionComponent,
			{
				data: {
					patientId: this.patientId,
					titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.MEDICATION_TITLE',
					addLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.ADD_MEDICATION_LABEL',
					prescriptionType: PrescriptionTypes.MEDICATION,
					prescriptionItemList: undefined,
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
				} else {
					newMedicationRequest$.subscribe((newRecipe: number) => {
						this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_MEDICATION_SUCCESS');
					}, _ => {this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_MEDICATION_ERROR')});
				}
			}

		});
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
			}
		});
	}

	openDialogNewRecommendation() {
	//TODO completar con pop-up nueva recomendacion
	console.log("Nueva recomendacion");
	}

}
