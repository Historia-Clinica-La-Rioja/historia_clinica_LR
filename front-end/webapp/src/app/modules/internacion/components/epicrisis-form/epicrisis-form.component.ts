import { Component, OnInit } from '@angular/core';
import {
	AllergyConditionDto,
	DiagnosisDto,
	EpicrisisDto,
	HealthConditionDto,
	HealthHistoryConditionDto,
	InmunizationDto,
	MasterDataInterface,
	MedicationDto,
	ResponseAnamnesisDto,
	ResponseEpicrisisDto,
} from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { EpicrisisService } from '@api-rest/services/epicrisis.service';
import { EpicrisisReportService } from '@api-rest/services/epicrisis-report.service';
import { DatePipe } from '@angular/common';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SelectionModel } from '@angular/cdk/collections';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ContextService } from '@core/services/context.service';
import { TableCheckbox } from 'src/app/modules/material/model/table.model';
import { TableService } from '@core/services/table.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';

@Component({
	selector: 'app-epicrisis-form',
	templateUrl: './epicrisis-form.component.html',
	styleUrls: ['./epicrisis-form.component.scss']
})
export class EpicrisisFormComponent implements OnInit {

	private internmentEpisodeId: number;
	private patientId: number;
	private healthClinicalStatus;

	isAllSelected = this.tableService.isAllSelected;
	masterToggle = this.tableService.masterToggle;

	internmentMainDiagnosis: DiagnosisDto;
	alternativeDiagnostics: HealthConditionDto[];
	verifications: MasterDataInterface<string>[];
	anamnesis: ResponseAnamnesisDto;
	form: FormGroup;

	medications: MedicationDto[] = [];
	diagnosis: TableCheckbox<DiagnosisDto> = {
		data: [],
		columns: [
			{
				def: 'diagnosis',
				header: 'internaciones.epicrisis.diagnosticos.table.columns.DIAGNOSIS',
				display: ap => ap.snomed.pt
			},
			{
				def: 'status',
				header: 'internaciones.epicrisis.diagnosticos.table.columns.STATUS',
				display: (row) => this.healthClinicalStatus?.find(status => status.id === row.statusId).description
			},
			{
				def: 'verificacion',
				header: 'internaciones.epicrisis.diagnosticos.table.columns.VERIFICATION',
				display: (row) => this.verifications?.find(verification => verification.id === row.verificationId)?.description
			},
		],
		displayedColumns: [],
		selection: new SelectionModel<DiagnosisDto>(true, [])
	};
	personalHistories: TableCheckbox<HealthHistoryConditionDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.antecedentes-familiares.table.columns.FAMILY_HISTORY',
				display: af => af.snomed.pt
			}
		],
		displayedColumns: [],
		selection: new SelectionModel<HealthHistoryConditionDto>(true, [])
	};
	familyHistories: TableCheckbox<HealthHistoryConditionDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.antecedentes-familiares.table.columns.FAMILY_HISTORY',
				display: af => af.snomed.pt
			}
		],
		displayedColumns: [],
		selection: new SelectionModel<HealthHistoryConditionDto>(true, [])
	};
	allergies: TableCheckbox<AllergyConditionDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.alergias.table.columns.ALLERGY',
				display: a => a.snomed.pt
			}
		],
		displayedColumns: [],
		selection: new SelectionModel<AllergyConditionDto>(true, [])
	};
	inmunizations: TableCheckbox<InmunizationDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.vacunas.table.columns.INMUNIZATION',
				display: v => v.snomed.pt
			},
			{
				def: 'date',
				header: 'internaciones.epicrisis.vacunas.table.columns.REGISTRY_DATE',
				display: v => this.datePipe.transform(v.administrationDate, 'dd/MM/yyyy')
			},
		],
		displayedColumns: [],
		selection: new SelectionModel<InmunizationDto>(true, [])
	};

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly epicrisisService: EpicrisisService,
		private readonly epicrisisReportService: EpicrisisReportService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly datePipe: DatePipe,
		private readonly snackBarService: SnackBarService,
		private readonly tableService: TableService,
		private readonly contextService: ContextService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly internmentStateService: InternmentStateService
	) {
		this.diagnosis.displayedColumns = (['mainDiagnosis']).concat(this.diagnosis.columns?.map(c => c.def)).concat(['select']);
		this.familyHistories.displayedColumns = this.familyHistories.columns?.map(c => c.def).concat(['select']);
		this.personalHistories.displayedColumns = this.personalHistories.columns?.map(c => c.def).concat(['select']);
		this.allergies.displayedColumns = this.allergies.columns?.map(c => c.def).concat(['select']);
		this.inmunizations.displayedColumns = this.inmunizations.columns?.map(c => c.def).concat(['select']);

	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params: ParamMap) => {
				this.internmentEpisodeId = Number(params.get('idInternacion'));
				this.patientId = Number(params.get('idPaciente'));
			}
		);

		this.form = this.formBuilder.group({
			mainDiagnosis: [null],
			snomed: [null],
			observations: this.formBuilder.group ({
				evolutionNote: [null, Validators.required],
				indicationsNote: [null, Validators.required],
				otherNote: [null],
				physicalExamNote: [null, Validators.required],
				studiesSummaryNote: [null, Validators.required]
			}),
		});

		const healthClinicalMasterData$ = this.internacionMasterDataService.getHealthClinical();
		healthClinicalMasterData$.subscribe(healthClinical => {
			this.healthClinicalStatus = healthClinical;
		});

		const healthVerificationMasterData$ = this.internacionMasterDataService.getHealthVerification();
		healthVerificationMasterData$.subscribe(healthVerification => {
			this.verifications = healthVerification;
		});

		const alternativeDiagnostics$ = this.internmentStateService.getActiveAlternativeDiagnosesGeneralState(this.internmentEpisodeId);
		alternativeDiagnostics$.subscribe(alternativeDiagnostics => this.alternativeDiagnostics = alternativeDiagnostics);

		const epicrisis$ = this.epicrisisService.getInternmentGeneralState(this.internmentEpisodeId);
		epicrisis$.subscribe(response => {
			this.internmentMainDiagnosis = response.mainDiagnosis;
			this.diagnosis.data = response.diagnosis ? [response.mainDiagnosis].concat(response.diagnosis) : [];
			this.diagnosis.selection.toggle(response.mainDiagnosis);
			this.form.controls.mainDiagnosis.setValue(response.mainDiagnosis);

			this.personalHistories.data = response.personalHistories ? response.personalHistories : [];
			this.familyHistories.data = response.familyHistories ? response.familyHistories : [];
			this.allergies.data = response.allergies ? response.allergies : [];
			this.inmunizations.data = response.inmunizations ? response.inmunizations : [];
		});

	}

	save(): void {
		if (this.form.valid) {
			const epicrisis: EpicrisisDto = {
				confirmed: true,
				notes: this.form.value.observations,
				mainDiagnosis: this.form.value.mainDiagnosis,
				diagnosis: this.getAlternativeDiagnostics(),
				familyHistories: this.familyHistories.selection.selected,
				personalHistories: this.personalHistories.selection.selected,
				medications: this.medications,
				inmunizations: this.inmunizations.selection.selected,
				allergies: this.allergies.selection.selected
			};
			console.log(epicrisis);
			this.epicrisisService.createDocument(epicrisis, this.internmentEpisodeId)
				.subscribe((epicrisisResponse: ResponseEpicrisisDto) => {
					this.snackBarService.showSuccess('internaciones.epicrisis.messages.SUCCESS');
					this.goToInternmentSummary();
				}, _ => this.snackBarService.showError('internaciones.epicrisis.messages.ERROR'));
		} else {
			this.snackBarService.showError('internaciones.epicrisis.messages.ERROR');
		}
	}

	back(): void {
		window.history.back();
	}

	setConfirmed(diagnosis: DiagnosisDto): void {
		this.diagnosis.selection.deselect(this.form.value.mainDiagnosis);
		this.diagnosis.selection.select(diagnosis);
	}

	isActive(diagnosis: DiagnosisDto): boolean {
		return (!!this.alternativeDiagnostics?.find(alternativeDiagnosis => alternativeDiagnosis.id === diagnosis.id)) ||
			(diagnosis === this.internmentMainDiagnosis);
	}

	diagnosisMasterToggle(): void {
		this.tableService.masterToggle(this.diagnosis.data, this.diagnosis.selection);
		this.diagnosis.selection.select(this.form.value.mainDiagnosis);
	}

	private getAlternativeDiagnostics(): DiagnosisDto[] {
		return this.diagnosis.selection.selected
			.filter(diagnosis => diagnosis.id !== this.form.value.mainDiagnosis.id);
	}

	private goToInternmentSummary(): void {
		const url = `institucion/${this.contextService.institutionId}/internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}`;
		this.router.navigate([url]);
	}

}
