import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AllergyConditionDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { ComponentEvaluationManagerService } from '../../../../services/component-evaluation-manager.service';
import { SearchSnomedConceptComponent } from '@historia-clinica/modules/ambulatoria/dialogs/search-snomed-concept/search-snomed-concept.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';

@Component({
	selector: 'app-alergias',
	templateUrl: './alergias.component.html',
	styleUrls: ['./alergias.component.scss']
})
export class AlergiasComponent {


	@Output() allergiesChange = new EventEmitter();
	@Input() allergies: AllergyConditionDto[] = [];



	constructor(
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly dialog: MatDialog,

	) {	}

	addSnomedConcept(snomedConcept: SnomedDto) {
		if (snomedConcept) {
			const alergia: AllergyConditionDto = {
				categoryId: null,
				date: null,
				verificationId: null,
				id: null,
				snomed: snomedConcept,
				criticalityId: null,
				statusId: null
			};
			this.add(alergia);
		}
	}



	add(a: AllergyConditionDto) {
		this.allergies = pushTo<AllergyConditionDto>(this.allergies, a);
		this.componentEvaluationManagerService.allergies = this.allergies;
		this.allergiesChange.emit(this.allergies);
	}

	remove(index: number) {
		this.allergies = removeFrom<AllergyConditionDto>(this.allergies, index);
		this.componentEvaluationManagerService.allergies = this.allergies;
		this.allergiesChange.emit(this.allergies);
	}

	addAlergies() {
		const dialogConfig = new MatDialogConfig();
		dialogConfig.width = '35%';
		dialogConfig.disableClose = false;
		dialogConfig.data = {
			label: 'internaciones.anamnesis.alergias.ALLERGY',
			title: 'internaciones.anamnesis.alergias.ADD',
			eclFilter: SnomedECL.ALLERGY
		};

		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, dialogConfig);

		dialogRef.afterClosed().subscribe(snomedConcept => {
			if (snomedConcept)
				this.addSnomedConcept(snomedConcept)
		});
	}

}
