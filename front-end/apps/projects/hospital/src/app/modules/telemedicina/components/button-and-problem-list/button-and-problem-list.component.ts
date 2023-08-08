import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { momentFormat, DateFormat } from '@core/utils/moment.utils';
import { NewConsultationAddProblemFormComponent } from '@historia-clinica/dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { Problema } from '@historia-clinica/services/problemas.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-button-and-problem-list',
	templateUrl: './button-and-problem-list.component.html',
	styleUrls: ['./button-and-problem-list.component.scss']
})
export class ButtonAndProblemListComponent implements OnInit {
	@Input() problemLimit: number;
	@Output() selectionChange = new EventEmitter();
	problems: AmbulatoryConsultationProblem[];
	reportFFIsOn: boolean;
	severityTypes: any[];
	searchConceptsLocallyFFIsOn = false;
	consultationProblemsService = new AmbulatoryConsultationProblemsService(this.formBuilder, this.snomedService, this.snackBarService, null, this.dialog);
	constructor(private readonly dialog: MatDialog, private readonly formBuilder: UntypedFormBuilder,
		private readonly snomedService: SnomedService, private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly snackBarService: SnackBarService, private readonly featureFlagService: FeatureFlagService) { }

	ngOnInit(): void {
		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.consultationProblemsService.setSeverityTypes(healthConditionSeverities);
		});
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
		this.consultationProblemsService.problems$.subscribe(problemesData => {
			this.problems = problemesData.map(
				(problema: Problema) => {
					return {
						severity: problema.codigoSeveridad,
						chronic: problema.cronico,
						endDate: problema.fechaFin ? momentFormat(problema.fechaFin, DateFormat.API_DATE) : undefined,
						snomed: problema.snomed,
						startDate: problema.fechaInicio ? momentFormat(problema.fechaInicio, DateFormat.API_DATE) : undefined
					};
				}
			)
			this.selectionChange.emit(this.problems )
		}
		)
	}


	addProblem(): void {
		this.dialog.open(NewConsultationAddProblemFormComponent, {
			data: {
				ambulatoryConsultationProblemsService: this.consultationProblemsService,
				severityTypes: this.severityTypes,
				epidemiologicalReportFF: this.reportFFIsOn,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}
}
