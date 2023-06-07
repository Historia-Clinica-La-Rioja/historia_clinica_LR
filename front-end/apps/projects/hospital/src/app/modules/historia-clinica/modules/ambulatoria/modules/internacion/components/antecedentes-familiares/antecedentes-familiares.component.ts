import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { HealthHistoryConditionDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SearchSnomedConceptComponent } from '@historia-clinica/modules/ambulatoria/dialogs/search-snomed-concept/search-snomed-concept.component';
import { ComponentEvaluationManagerService } from '../../../../services/component-evaluation-manager.service';

@Component({
	selector: 'app-antecedentes-familiares',
	templateUrl: './antecedentes-familiares.component.html',
	styleUrls: ['./antecedentes-familiares.component.scss']
})

export class AntecedentesFamiliaresComponent{

	@Output() familyHistoriesChange = new EventEmitter();

	@Input() familyHistories: HealthHistoryConditionDto[] = [];



	constructor(
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly dialog: MatDialog,


	) {
	}

	addSnomedConcept(snomedConcept: SnomedDto) {
		if (snomedConcept) {
			const antecedenteFamiliar: HealthHistoryConditionDto = {
				startDate: null,
				note: null,
				snomed: snomedConcept
			};
			this.add(antecedenteFamiliar);
		}
	}


	add(af: HealthHistoryConditionDto): void {
		this.familyHistories = pushTo<HealthHistoryConditionDto>(this.familyHistories, af);
		this.componentEvaluationManagerService.familyHistories = this.familyHistories;
		this.familyHistoriesChange.next(this.familyHistories);
	}

	remove(index: number): void {
		this.familyHistories = removeFrom<HealthHistoryConditionDto>(this.familyHistories, index);
		this.componentEvaluationManagerService.familyHistories = this.familyHistories;
		this.familyHistoriesChange.next(this.familyHistories);
	}

	addFamilyHistories(): void {
		const dialogConfig = new MatDialogConfig();
		dialogConfig.width = '35%';
		dialogConfig.disableClose = false;
		dialogConfig.data = {
			label: 'internaciones.anamnesis.antecedentes-familiares.FAMILY_HISTORY',
			title: 'internaciones.anamnesis.antecedentes-familiares.ADD',
			eclFilter: SnomedECL.PERSONAL_RECORD
		};

		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, dialogConfig);

		dialogRef.afterClosed().subscribe(snomedConcept => {
			if (snomedConcept)
				this.addSnomedConcept(snomedConcept)
		});
	}

}
