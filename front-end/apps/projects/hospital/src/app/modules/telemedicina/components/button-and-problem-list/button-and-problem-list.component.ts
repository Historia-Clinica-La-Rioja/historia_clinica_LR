import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ProblemConceptSearchDialogComponent } from '@historia-clinica/dialogs/problem-concept-search-dialog/problem-concept-search-dialog.component';

@Component({
	selector: 'app-button-and-problem-list',
	templateUrl: './button-and-problem-list.component.html',
	styleUrls: ['./button-and-problem-list.component.scss']
})
export class ButtonAndProblemListComponent implements OnInit {
	@Input() problemLimit: number;
	@Output() selectionChange = new EventEmitter();
	problems: AmbulatoryConsultationProblem[];
	searchConceptsLocallyFFIsOn: boolean;
	canEdit = false;
	consultationProblemsService = new AmbulatoryConsultationProblemsService(this.formBuilder, this.snomedService, this.snackBarService, null, this.dialog);
	constructor(private readonly dialog: MatDialog, private readonly formBuilder: UntypedFormBuilder,
		private readonly snomedService: SnomedService, private readonly snackBarService: SnackBarService,
		private readonly featureFlagService: FeatureFlagService) { }

	ngOnInit(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
		this.consultationProblemsService.problems$.subscribe(problemesData => {
			this.problems = problemesData.map(
				(problema: AmbulatoryConsultationProblem) => {
					return {
						snomed: problema.snomed,
					};
				}
			)
			this.selectionChange.emit(this.problems)
		}
		)
	}


	addProblem(): void {
		this.consultationProblemsService.setShowInitialDate(false);
		this.dialog.open(ProblemConceptSearchDialogComponent, {
			data: {
				ambulatoryConsultationProblemsService: this.consultationProblemsService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});

	}
}
