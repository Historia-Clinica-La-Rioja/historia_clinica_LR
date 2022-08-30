import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SelectionModel } from '@angular/cdk/collections';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ContextService } from '@core/services/context.service';
import { TableService } from '@core/services/table.service';
import { PermissionsService } from '@core/services/permissions.service';

import { MapperService } from '@presentation/services/mapper.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';

import {
	DiagnosesGeneralStateDto,
	EvolutionDiagnosisDto,
	InternmentSummaryDto,
	MasterDataInterface,
} from '@api-rest/api-model';
import { InternacionService } from '@api-rest/services/internacion.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { TableCheckbox } from '@material/model/table.model';
import { HEALTH_CLINICAL_STATUS } from '../../constants/ids';
import { ROLES_FOR_ACCESS_MAIN } from '../../constants/permissions';
import { OVERLAY_DATA } from "@presentation/presentation-model";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { InternmentEpisodeSummary } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-episode-summary/internment-episode-summary.component";

@Component({
	selector: 'app-diagnosis-clinical-evaluation-dock-popup',
	templateUrl: './diagnosis-clinical-evaluation-dock-popup.component.html',
	styleUrls: ['./diagnosis-clinical-evaluation-dock-popup.component.scss']
})
export class DiagnosisClinicalEvaluationDockPopupComponent implements OnInit {

	private healthClinicalStatus: MasterDataInterface<string>[];

	verifications: MasterDataInterface<string>[];
	form: FormGroup;
	internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;
	diagnostics: TableCheckbox<DiagnosesGeneralStateDto> = {
		data: [],
		columns: [
			{
				def: 'diagnosis',
				header: 'internaciones.clinical-assessment-diagnosis.diagnostics.table.columns.DIAGNOSIS',
				display: ap => ap.snomed.pt
			},
			{
				def: 'status',
				header: 'internaciones.clinical-assessment-diagnosis.diagnostics.table.columns.STATUS',
				display: (row) => this.healthClinicalStatus?.find(status => status.id === row.statusId).description
			},
			{
				def: 'verificacion',
				header: 'internaciones.clinical-assessment-diagnosis.diagnostics.table.columns.VERIFICATION',
				display: (row) => this.verifications?.find(verification => verification.id === row.verificationId)?.description
			},
			{
				def: 'type',
				display: row => row.main ? 'internaciones.clinical-assessment-diagnosis.diagnostics.table.columns.MAIN' : '',
			},
		],
		displayedColumns: [],
		selection: new SelectionModel<DiagnosesGeneralStateDto>(true, [])
	};
	isAllSelected = this.tableService.isAllSelected;
	masterToggle = this.tableService.masterToggle;

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	public hasError = hasError;
	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly internmentService: InternacionService,
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly contextService: ContextService,
		private readonly mapperService: MapperService,
		private readonly tableService: TableService,
		private readonly formBuilder: FormBuilder,
		private readonly snackBarService: SnackBarService,
		private readonly permissionsService: PermissionsService,
	) {
		this.diagnostics.displayedColumns = this.diagnostics.columns?.map(c => c.def).concat(['select']);
	}

	ngOnInit(): void {
		this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(this.data.diagnosisInfo.internmentEpisodeId).pipe(
			map((internmentEpisodeSummary: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisodeSummary))
		);

		const diagnosesGeneralState$ = this.internmentStateService.getDiagnosesGeneralState(this.data.diagnosisInfo.internmentEpisodeId).pipe(
			map(diagnostics => diagnostics.filter(od => od.statusId === HEALTH_CLINICAL_STATUS.ACTIVO))
		);
		diagnosesGeneralState$.subscribe(diagnostics => {
			this.permissionsService.hasContextAssignments$(ROLES_FOR_ACCESS_MAIN).subscribe(canAccesMain => {
				diagnostics = canAccesMain ? diagnostics : diagnostics.filter(diagnostic => canAccesMain ? !diagnostic.main : true);
				this.diagnostics.data = diagnostics;
				this.diagnostics.selection.select(diagnostics.find(d => d.id === this.data.diagnosisInfo.diagnosisId));
			});
		});

		const healthClinicalMasterData$ = this.internacionMasterDataService.getHealthClinical();
		healthClinicalMasterData$.subscribe(healthClinical => {
			this.healthClinicalStatus = healthClinical;
		});

		const healthVerificationMasterData$ = this.internacionMasterDataService.getHealthVerification();
		healthVerificationMasterData$.subscribe(healthVerification => {
			this.verifications = healthVerification;
		});

		this.form = this.formBuilder.group({
			currentIllnessNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			physicalExamNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			studiesSummaryNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			evolutionNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			clinicalImpressionNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			otherNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
		});
	}

	save(): void {
		if (this.form.valid) {
			const evolutionNote: EvolutionDiagnosisDto = {
				diagnosis: this.diagnostics.selection.selected.filter(d => d.main === false),
				notes: this.form.value,
				mainDiagnosis: this.diagnostics.selection.selected.find(d => d.main === true)
			};
			this.evolutionNoteService.createEvolutionDiagnosis(evolutionNote, this.data.diagnosisInfo.internmentEpisodeId).subscribe(
				_ => {
					this.snackBarService.showSuccess('internaciones.clinical-assessment-diagnosis.messages.SUCCESS');
					this.dockPopupRef.close(setFieldsToUpdate(evolutionNote));
				},
				_ => this.snackBarService.showError('internaciones.clinical-assessment-diagnosis.messages.ERROR')
			);
		}

		function setFieldsToUpdate(evolutionNote: EvolutionDiagnosisDto): InternmentFields {
			return {
				diagnosis: !!evolutionNote.diagnosis.map(diagnosis => diagnosis.id),
				evolutionClinical: !!evolutionNote.notes,
			}
		}
	}
}
