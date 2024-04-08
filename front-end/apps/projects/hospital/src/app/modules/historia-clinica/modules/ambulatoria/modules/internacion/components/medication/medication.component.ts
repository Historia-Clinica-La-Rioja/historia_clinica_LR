import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MedicationDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model'
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { SearchSnomedConceptComponent } from '@historia-clinica/modules/ambulatoria/dialogs/search-snomed-concept/search-snomed-concept.component';
import { Color } from "@presentation/colored-label/colored-label.component";
import { FormMedicationComponent } from '../../dialogs/form-medication/form-medication.component';

@Component({
	selector: 'app-medication',
	templateUrl: './medication.component.html',
	styleUrls: ['./medication.component.scss']
})
export class MedicationComponent {
	@Output() medicationsChange = new EventEmitter();
	@Input() medications: MedicationDto[] = [];
	@Input() hideSuspended = false;
	@Input() title = '';

	protected readonly Color = Color;


	constructor(
		private readonly dialog: MatDialog,

	) { }

	addToList(medicacion: MedicationDto) {
		if (medicacion) {
			this.add(medicacion);
		}
	}

	add(medicacion: MedicationDto) {
		const lenght = this.medications?.length;
		this.medications = pushIfNotExists<MedicationDto>(this.medications, medicacion, this.compare);
		if (this.medications.length > lenght) {
			this.medicationsChange.next(this.medications);
		}
	}

	compare(concept1: MedicationDto, concept2: MedicationDto): boolean {
		return concept1.snomed.sctid === concept2.snomed.sctid
	}

	remove(index: number) {
		this.medications = removeFrom<MedicationDto>(this.medications, index);
		this.medicationsChange.next(this.medications);
	}

	addMedication() {
		const dialogConfig = new MatDialogConfig();
		dialogConfig.width = '35%';
		dialogConfig.disableClose = false;
		dialogConfig.data = {
			label: 'historia-clinica.new-consultation-medication-form.CONCEPT_LABEL',
			title: 'ambulatoria.paciente.nueva-consulta.medicaciones.ADD',
			eclFilter: SnomedECL.MEDICINE
		};

		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, dialogConfig);

		dialogRef.afterClosed().subscribe(snomedConcept => {
			if (snomedConcept) {
				const dialog = this.dialog.open(FormMedicationComponent, {
					data: {
						title: 'ambulatoria.paciente.nueva-consulta.medicaciones.ADD',
						hideSuspended: this.hideSuspended,
						snomedConcept: snomedConcept
					}
				});
				dialog.afterClosed().subscribe(medicacion => {
					this.addToList(medicacion)
				});
			}
		});
	}

}
