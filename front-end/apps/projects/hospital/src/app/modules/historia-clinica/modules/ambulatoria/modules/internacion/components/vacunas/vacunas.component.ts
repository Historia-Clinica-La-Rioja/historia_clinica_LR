import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ImmunizationDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { ComponentEvaluationManagerService } from '../../../../services/component-evaluation-manager.service';
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
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly dialog: MatDialog,

	) { }


	add(vacuna: ImmunizationDto): void {
		this.immunizations = pushTo<ImmunizationDto>(this.immunizations, vacuna);
		this.componentEvaluationManagerService.vaccines = this.immunizations;
		this.immunizationsChange.next(this.immunizations);
	}

	remove(index: number): void {
		this.immunizations = removeFrom<ImmunizationDto>(this.immunizations, index);
		this.componentEvaluationManagerService.vaccines = this.immunizations;
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
