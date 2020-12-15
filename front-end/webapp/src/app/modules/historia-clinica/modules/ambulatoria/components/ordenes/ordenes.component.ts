import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ESTUDIOS, INDICACIONES, ORDENES_MEDICACION } from 'src/app/modules/historia-clinica/constants/summaries';
import { ConfirmarPreinscripcionComponent } from '../../dialogs/ordenes-preinscripciones/confirmar-preinscripcion/confirmar-preinscripcion.component';
import { NuevaPreinscripcionComponent } from '../../dialogs/ordenes-preinscripciones/nueva-preinscripcion/nueva-preinscripcion.component';

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
	) { }

	ngOnInit(): void {
	}

	openDialogNewMedication() {
		const newMedicationDialog = this.dialog.open(NuevaPreinscripcionComponent,
			{
				data: {
					patientId: this.patientId,
					titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.MEDICATION_TITLE',
					addLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.ADD_MEDICATION_LABEL',
					hasMedicalCoverage: false,
					prescriptionItemList: undefined,
					childData: {
						titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.MEDICATION_TITLE',
						searchSnomedLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.MEDICATION',
						showDosage: true,
						eclTerm: 'medicine',
					}
				}
			});

		newMedicationDialog.afterClosed().subscribe(data => {
			if (data) {
				const confirmPrescriptionDialog = this.dialog.open(ConfirmarPreinscripcionComponent,
					{
						disableClose: true,
						data: {
							titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.MEDICATION_TITLE',
						}
					});
			}
		});
	}

	openDialogNewStudy() {
		const newStudyDialog = this.dialog.open(NuevaPreinscripcionComponent,
			{
				data: {
					patientId: this.patientId,
					titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.STUDY_TITLE',
					addLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.ADD_STUDY_LABEL',
					hasMedicalCoverage: false,
					prescriptionItemList: undefined,
					childData: {
						titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.STUDY_TITLE',
						searchSnomedLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.STUDY',
						showDosage: false,
						eclTerm: 'procedure',
					}
				}
			});

		newStudyDialog.afterClosed().subscribe(data => {
			if (data) {
				const confirmPrescriptionDialog = this.dialog.open(ConfirmarPreinscripcionComponent,
					{
						disableClose: true,
						data: {
							titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.STUDY_TITLE',
						}
					});
			}
		});
	}

	openDialogNewRecommendation() {
	//TODO completar con pop-up nueva recomendacion
	console.log("Nueva recomendacion");
	}

}
