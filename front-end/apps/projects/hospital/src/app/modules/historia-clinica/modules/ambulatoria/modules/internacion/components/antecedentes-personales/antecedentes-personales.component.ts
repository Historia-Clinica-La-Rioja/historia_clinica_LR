import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { HealthHistoryConditionDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SearchSnomedConceptComponent } from '@historia-clinica/modules/ambulatoria/dialogs/search-snomed-concept/search-snomed-concept.component';
import { ComponentEvaluationManagerService } from '../../../../services/component-evaluation-manager.service';

@Component({
	selector: 'app-antecedentes-personales',
	templateUrl: './antecedentes-personales.component.html',
	styleUrls: ['./antecedentes-personales.component.scss']
})
export class AntecedentesPersonalesComponent {


	@Output() personalHistoriesChange = new EventEmitter();

	@Input() personalHistories: HealthHistoryConditionDto[] = [];





	constructor(
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly dialog: MatDialog,


	) {
	}


	addSnomedConcept(snomedConcept: SnomedDto) {
		if (snomedConcept) {
			const antecedentePersonal: HealthHistoryConditionDto = {
				startDate: null,
				note: null,
				snomed: snomedConcept
			};
			this.add(antecedentePersonal);
		}
	}


	add(ap: HealthHistoryConditionDto): void {
		this.personalHistories = pushTo<HealthHistoryConditionDto>(this.personalHistories, ap);
		this.componentEvaluationManagerService.personalHistories = this.personalHistories;
		this.personalHistoriesChange.next(this.personalHistories);
	}

	remove(index: number): void {
		this.personalHistories = removeFrom<HealthHistoryConditionDto>(this.personalHistories, index);
		this.componentEvaluationManagerService.personalHistories = this.personalHistories;
		this.personalHistoriesChange.next(this.personalHistories);
	}

	addPersonalHistory(): void {
		const dialogConfig = new MatDialogConfig();
		dialogConfig.width = '35%';
		dialogConfig.disableClose = false;
		dialogConfig.data = {
			label: 'internaciones.anamnesis.antecedentes-personales.PERSONAL_HISTORY',
			title: 'internaciones.anamnesis.antecedentes-personales.ADD',
			eclFilter: SnomedECL.FAMILY_RECORD
		};

		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, dialogConfig);

		dialogRef.afterClosed().subscribe(snomedConcept =>
			this.addSnomedConcept(snomedConcept));
	}

}
