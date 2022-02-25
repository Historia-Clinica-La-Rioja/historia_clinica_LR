import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { DIAGNOSTICO_PRINCIPAL } from '../../../../constants/summaries';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { HealthConditionDto } from '@api-rest/api-model';
import { Observable, of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { InternmentFields, InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { DockPopupService } from "@presentation/services/dock-popup.service";
import { ChangeMainDiagnosisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/change-main-diagnosis-dock-popup/change-main-diagnosis-dock-popup.component";
import { DiagnosisClinicalEvaluationDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-clinical-evaluation-dock-popup/diagnosis-clinical-evaluation-dock-popup.component";

@Component({
	selector: 'app-main-diagnosis-summary',
	templateUrl: './main-diagnosis-summary.component.html',
	styleUrls: ['./main-diagnosis-summary.component.scss']
})
export class MainDiagnosisSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() editable = true;
	@Input() mainDiagnosis: HealthConditionDto;

	mainDiagnosticosSummary = DIAGNOSTICO_PRINCIPAL;
	mainDiagnosis$: Observable<HealthConditionDto>;
	dialogRef: DockPopupRef;
	private routePrefix;

	constructor(
		private internmentStateService: InternmentStateService,
		private router: Router,
		private contextService: ContextService,
		private readonly route: ActivatedRoute,
		private readonly dockPopupService: DockPopupService,
		public readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
	) { }

	ngOnInit(): void {
		this.mainDiagnosis$ = this.internmentStateService.getMainDiagnosis(this.internmentEpisodeId);
		this.route.paramMap.subscribe(
			(params) => {
				const patientId = Number(params.get('idPaciente'));
				this.routePrefix = `institucion/${this.contextService.institutionId}/internaciones/internacion/${this.internmentEpisodeId}/paciente/${patientId}`;
			});
	}

	ngOnChanges() {
		if (this.mainDiagnosis) {
			this.mainDiagnosis$ = of(this.mainDiagnosis);
		}
	}

	openClinicalEvaluation(id: number): void {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(DiagnosisClinicalEvaluationDockPopupComponent, {
				diagnosisInfo: {
					internmentEpisodeId: this.internmentEpisodeId,
					diagnosisId: id,
				},
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate) {
					this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openChangeMainDiagnosis(): void {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(ChangeMainDiagnosisDockPopupComponent, {
				internmentEpisodeId: this.internmentEpisodeId,
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate) {
					this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

}
