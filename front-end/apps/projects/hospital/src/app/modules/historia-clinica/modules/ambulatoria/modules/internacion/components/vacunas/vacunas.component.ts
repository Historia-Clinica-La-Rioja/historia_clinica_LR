import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ImmunizationDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { SearchSnomedConceptComponent } from '@historia-clinica/modules/ambulatoria/dialogs/search-snomed-concept/search-snomed-concept.component';
import { Concept, ConceptDateFormComponent } from '../../dialogs/concept-date-form/concept-date-form.component';

@Component({
	selector: 'app-vacunas',
	templateUrl: './vacunas.component.html',
	styleUrls: ['./vacunas.component.scss']
})
export class VacunasComponent {
	@Output() immunizationsChange = new EventEmitter();

	@Input() immunizations: ImmunizationDto[] = [];

	constructor(
		private readonly dialog: MatDialog,

	) { }


	add(vacuna: ImmunizationDto) {
		const lenght = this.immunizations?.length;
		this.immunizations = pushIfNotExists<ImmunizationDto>(this.immunizations, vacuna, this.compare);
		if (this.immunizations.length > lenght) {
			this.immunizationsChange.next(this.immunizations);
		}
	}

	compare(concept1: ImmunizationDto, concept2: ImmunizationDto): boolean {
		return concept1.snomed.sctid === concept2.snomed.sctid
	}

	remove(index: number): void {
		this.immunizations = removeFrom<ImmunizationDto>(this.immunizations, index);
		this.immunizationsChange.next(this.immunizations);
	}


	addSnomedConcept(vaccine: Concept) {
		if (vaccine) {
			const vacuna: ImmunizationDto = {
				administrationDate: vaccine.data,
				note: null,
				snomed: vaccine.snomedConcept
			};
			this.add(vacuna);
		}
	}

	addVaccine(): void {
		const dialogConfig = new MatDialogConfig();
		dialogConfig.width = '35%';
		dialogConfig.disableClose = false;
		dialogConfig.data = {
			label: 'internaciones.anamnesis.vacunas.INMUNIZATION',
			title: 'internaciones.anamnesis.vacunas.ADD',
			eclFilter: SnomedECL.VACCINE
		};

		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, dialogConfig);

		dialogRef.afterClosed().subscribe(snomedConcept => {
			if (snomedConcept) {
				const dialog = new MatDialogConfig();
				dialog.width = '35%';
				dialogConfig.disableClose = false;
				dialog.data = {
					add: 'internaciones.anamnesis.vacunas.ADD',
					label: 'internaciones.anamnesis.vacunas.INMUNIZATION',
					title: 'internaciones.anamnesis.vacunas.INMUNIZATION',
					snomedConcept: snomedConcept
				};

				const dialogRef = this.dialog.open(ConceptDateFormComponent, dialog);

				dialogRef.afterClosed().subscribe((concept: Concept) => {
					if (concept)
						this.addSnomedConcept(concept);

				});
			}
		});

	}

}
