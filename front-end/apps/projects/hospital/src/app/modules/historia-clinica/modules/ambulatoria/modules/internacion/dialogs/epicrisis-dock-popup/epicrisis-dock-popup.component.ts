import { AppFeature } from '@api-rest/api-model';
import {
	AllergyConditionDto,
	DiagnosisDto,
	EpicrisisDto,
	EpicrisisGeneralStateDto,
	EpicrisisObservationsDto,
	HealthConditionDto,
	HealthHistoryConditionDto,
	ImmunizationDto,
	MasterDataInterface,
	MedicationDto,
	ResponseAnamnesisDto,
	ResponseEpicrisisDto,
	SnomedDto,
	ExternalCauseDto
} from '@api-rest/api-model';
import { Inject, OnInit, ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { SnomedECL } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EpicrisisService } from '@api-rest/services/epicrisis.service';
import { DatePipe } from '@angular/common';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SelectionModel } from '@angular/cdk/collections';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TableCheckbox } from '@material/model/table.model';
import { TableService } from '@core/services/table.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { DiagnosisEpicrisisService } from '../../services/diagnosis-epicrisis.service';
import { hasError } from '@core/utils/form.utils';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { SnomedService, SnomedSemanticSearch } from '@historia-clinica/services/snomed.service';
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { OVERLAY_DATA } from "@presentation/presentation-model";
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { Observable, Subscription } from 'rxjs';
import { DocumentActionReasonComponent } from '../document-action-reason/document-action-reason.component';
import { MatDialog } from '@angular/material/dialog';
import { ProblemEpicrisisService } from '../../services/problem-epicrisis.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { ExternalCauseService } from '../../services/external-cause.service';
import { ControlDynamicFormService } from '../../services/control-dynamic-form.service';
import { ObstetricComponent } from '../../components/obstetric/obstetric.component';
import { ObstetricFormService } from '../../services/obstetric-form.service';

@Component({
	selector: 'app-epicrisis-dock-popup',
	templateUrl: './epicrisis-dock-popup.component.html',
	styleUrls: ['./epicrisis-dock-popup.component.scss'],
	providers: [ExternalCauseService, ControlDynamicFormService, ObstetricFormService]
})
export class EpicrisisDockPopupComponent implements OnInit {

	private healthClinicalStatus;

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	isAllSelected = this.tableService.isAllSelected;
	masterToggle = this.tableService.masterToggle;
	public hasError = hasError;

	diagnosticsEpicrisisService: DiagnosisEpicrisisService;
	problemEpicrisisService: ProblemEpicrisisService;
	snomedConcept: SnomedDto;
	snomedConceptProblem: SnomedDto;
	verifications: MasterDataInterface<string>[];
	anamnesis: ResponseAnamnesisDto;
	form: FormGroup;
	formDiagnosis: FormGroup;
	formProblem: FormGroup;
	showWarning: boolean = false;
	isDraft = false;
	isDisableConfirmButton = false;
	medications: MedicationDto[] = [];
	canConfirmedDocument = false;
	ECL = SnomedECL.DIAGNOSIS;
	searchConceptsLocallyFF = false;
	cipresEpicrisisFF = false;
	externalCauseDto: ExternalCauseDto = {};
	event$: Observable<ExternalCauseDto>;
	private externalCause$: Subscription;

	personalHistories: TableCheckbox<HealthHistoryConditionDto> = {
		data: [],
		columns: [
			{
				def: 'problemType',
				header: 'internaciones.epicrisis.antecedentes-personales.table.columns.PERSONAL_HISTORY',
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
	immunizations: TableCheckbox<ImmunizationDto> = {
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
		selection: new SelectionModel<ImmunizationDto>(true, [])
	};

	waitToResponse = false;
	@ViewChild(ObstetricComponent) formulario!: ObstetricComponent;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		private readonly featureFlagService: FeatureFlagService,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly epicrisisService: EpicrisisService,
		private readonly datePipe: DatePipe,
		private readonly snackBarService: SnackBarService,
		private readonly tableService: TableService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly internmentStateService: InternmentStateService,
		private readonly snomedService: SnomedService,
		private readonly dialog: MatDialog,
		private readonly externalCauseServise: ExternalCauseService,
		private readonly obtetricForm: ObstetricFormService,
	) {
		this.familyHistories.displayedColumns = this.familyHistories.columns?.map(c => c.def).concat(['select']);
		this.personalHistories.displayedColumns = this.personalHistories.columns?.map(c => c.def).concat(['select']);
		this.allergies.displayedColumns = this.allergies.columns?.map(c => c.def).concat(['select']);
		this.immunizations.displayedColumns = this.immunizations.columns?.map(c => c.def).concat(['select']);
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => this.searchConceptsLocallyFF = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_CAMPOS_CIPRES_EPICRISIS).subscribe(isOn => this.cipresEpicrisisFF = isOn);
	}

	ngOnInit(): void {

		this.isDraft = this.data.patientInfo.isDraft;
		this.canConfirmedDocument = this.data.patientInfo.isDraft;
		this.diagnosticsEpicrisisService = new DiagnosisEpicrisisService(this.internacionMasterDataService, this.internmentStateService, this.tableService, this.data.patientInfo.internmentEpisodeId);
		this.problemEpicrisisService = new ProblemEpicrisisService();

		this.formProblem = this.formBuilder.group({
			snomedProblem: [null, Validators.required]
		});
		this.formDiagnosis = this.formBuilder.group({
			snomed: [null, Validators.required]
		});
		this.form = this.formBuilder.group({
			mainDiagnosis: [null, Validators.required],
			snomed: [null],
			evolutionNote: [null, [Validators.required, Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			indicationsNote: [null, [Validators.required, Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			otherNote: [null, Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)],
			physicalExamNote: [null, [Validators.required, Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			studiesSummaryNote: [null, [Validators.required, Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]]
		});

		const healthClinicalMasterData$ = this.internacionMasterDataService.getHealthClinical();
		healthClinicalMasterData$.subscribe(healthClinical => {
			this.healthClinicalStatus = healthClinical;
		});

		const healthVerificationMasterData$ = this.internacionMasterDataService.getHealthVerification();
		healthVerificationMasterData$.subscribe(healthVerification => {
			this.verifications = healthVerification;
		});

		const epicrisisInternmentGeneralState$ = this.epicrisisService.getInternmentGeneralState(this.data.patientInfo.internmentEpisodeId);
		epicrisisInternmentGeneralState$.subscribe((response: EpicrisisGeneralStateDto) => {
			this.waitToResponse = true;
			this.form.controls.mainDiagnosis.setValue(response.mainDiagnosis);
			this.diagnosticsEpicrisisService.setInternmentMainDiagnosis(response.mainDiagnosis);
			this.problemEpicrisisService.setProblems(response.otherProblems);
			this.problemEpicrisisService.initTable(response.otherProblems);
			this.diagnosticsEpicrisisService.initTable(response.diagnosis);
			this.personalHistories.data = response.personalHistories;
			this.familyHistories.data = response.familyHistories;
			this.allergies.data = response.allergies;
			this.immunizations.data = response.immunizations;

			let epicrisis$;
			if (this.data.patientInfo.isDraft) {
				epicrisis$ = this.epicrisisService.getDraft(this.data.patientInfo.epicrisisId, this.data.patientInfo.internmentEpisodeId);
			} else
				if (this.data.patientInfo.epicrisisId) {
					epicrisis$ = this.epicrisisService.getEpicrisis(this.data.patientInfo.epicrisisId, this.data.patientInfo.internmentEpisodeId);
				}
			epicrisis$?.subscribe((epicrisis: ResponseEpicrisisDto) => this.setValues(epicrisis))
		});
		this.epicrisisService.existUpdatesAfterEpicrisis(this.data.patientInfo.internmentEpisodeId).subscribe((showWarning: boolean) => this.showWarning = showWarning);

		function isEqualsThenSelect(sctid1: string, sctid2: string, tableConcept: TableCheckbox<any>, concept: any) {
			if (sctid1 === sctid2)
				tableConcept.selection.select(concept)
		}
		this.externalCause$ = this.externalCauseServise.getValue().subscribe();
	}

	private setValues(e: ResponseEpicrisisDto): void {
		if (e?.obstetricEvent){
			this.obtetricForm.setValue(e.obstetricEvent);
		}
		if (e?.externalCause) {
			this.externalCauseDto = e?.externalCause;
			this.externalCauseServise.setValue(e.externalCause);
		}
		this.personalHistories.data.forEach(ph => {
			const existEqual = !!e.personalHistories.find((p) => ph.snomed.sctid === p.snomed.sctid);
			if (existEqual)
				this.personalHistories.selection.select(ph);
		});
		this.familyHistories.data.forEach(fh => {
			const existEqual = !!e.familyHistories.find((f) => fh.snomed.sctid === f.snomed.sctid);
			if (existEqual)
				this.familyHistories.selection.select(fh);
		});
		this.allergies.data.forEach(all => {
			const existEqual = !!e.allergies.find((a) => a.snomed.sctid === all.snomed.sctid);
			if (existEqual)
				this.allergies.selection.select(all);
		});
		this.immunizations.data.forEach(imm => {
			const existEqual = !!e.immunizations.find((i) => i.snomed.sctid === imm.snomed.sctid);
			if (existEqual)
				this.immunizations.selection.select(imm);
		});
		Object.keys(e.notes).forEach((key: string) => {
			if (e.notes[key]) {
				this.form.patchValue({ [key]: e.notes[key] })
			}
		});
		this.medications = e.medications;
		this.diagnosticsEpicrisisService.checkDiagnosis(e.diagnosis);
	}

	save(): void {
		if (this.form.valid && this.formulario.isValidForm()) {
			const epicrisis: EpicrisisDto = this.getEpicrisis(true);
			this.ngOnDestroy();

			if (this.data.patientInfo.epicrisisId) {
				this.openEditReason(epicrisis);
				return;
			}
			else {
				this.isDisableConfirmButton = true;
				this.epicrisisService.createDocument(epicrisis, this.data.patientInfo.internmentEpisodeId)
					.subscribe((epicrisisResponse: ResponseEpicrisisDto) => {
						this.snackBarService.showSuccess('internaciones.epicrisis.messages.SUCCESS');
						let fieldsToUpdate = this.fieldsToUpdate(epicrisis);
						this.dockPopupRef.close({ fieldsToUpdate });
					}, _ => {
						this.isDisableConfirmButton = false;
						this.snackBarService.showError('internaciones.epicrisis.messages.ERROR')
					});
			}
		} else {
			this.snackBarService.showError('internaciones.epicrisis.messages.ERROR');
			this.form.markAllAsTouched();

		}


	}

	saveDraft(): void {
		if (this.formulario.isValidForm() && this.form.valid) {
			const epicrisis = this.getEpicrisis(false);
			let obs$;
			if (this.data.patientInfo.epicrisisId) {
				obs$ = this.epicrisisService.
					updateDraft(epicrisis, this.data.patientInfo.epicrisisId, this.data.patientInfo.internmentEpisodeId)
			} else
				obs$ = this.epicrisisService.createDraftDocument(epicrisis, this.data.patientInfo.internmentEpisodeId)

			this.closeEpicrisis(obs$, epicrisis, false);
		} else {
			this.snackBarService.showError('internaciones.epicrisis.messages-draft.ERROR');
			this.form.markAllAsTouched();
		}
	}

	saveConfirmedDraft(): void {
		if (this.form.valid) {
			const epicrisis = this.getEpicrisis(true);
			let obs$ = this.epicrisisService.
				closeDraft(this.data.patientInfo.internmentEpisodeId, this.data.patientInfo.epicrisisId, epicrisis)
			this.closeEpicrisis(obs$, epicrisis, true);
		} else {
			this.snackBarService.showError('internaciones.epicrisis.messages-draft.ERROR');
			this.form.markAllAsTouched();
		}
	}

	private getEpicrisis(confirmed?: boolean): EpicrisisDto {
		return {

			confirmed: confirmed,
			notes: this.toEpicrisisObservationsDto(this.form),
			mainDiagnosis: this.form.value.mainDiagnosis,
			diagnosis: this.diagnosticsEpicrisisService.getSelectedAlternativeDiagnostics(),
			externalCause: this.externalCauseDto,
			familyHistories: this.familyHistories.selection.selected,
			personalHistories: this.personalHistories.selection.selected,
			medications: this.medications,
			immunizations: this.immunizations.selection.selected,
			allergies: this.allergies.selection.selected,
			obstetricEvent: this.formulario?.getForm(),
			otherProblems: this.problemEpicrisisService.getProblems()
		}
	}



	private toEpicrisisObservationsDto(form: FormGroup): EpicrisisObservationsDto {
		return {
			evolutionNote: form.value.evolutionNote,
			indicationsNote: form.value.indicationsNote,
			otherNote: form.value.otherNote,
			physicalExamNote: form.value.physicalExamNote,
			studiesSummaryNote: form.value.studiesSummaryNote
		}

	}

	private fieldsToUpdate(epicrisis: EpicrisisDto): InternmentFields {
		return {
			mainDiagnosis: !!epicrisis.mainDiagnosis,
			diagnosis: !!epicrisis.diagnosis,
			familyHistories: !!epicrisis.familyHistories,
			personalHistories: !!epicrisis.personalHistories,
			medications: !!epicrisis.medications,
			immunizations: !!epicrisis.immunizations,
			allergies: !!epicrisis.allergies,
			evolutionClinical: !!epicrisis.notes,
		}
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.DIAGNOSIS
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

	openSearchDialogProblem(searchValue: string) {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.DIAGNOSIS
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConceptProblemSnomed(selectedConcept));
		}
	}

	addToList(): void {
		if (this.formDiagnosis.valid && this.snomedConcept) {
			const newDiagnosis: DiagnosisDto = {
				statusId: this.healthClinicalStatus.find(clinicalStatus => clinicalStatus.description === 'Activo')?.id,
				verificationId: this.verifications.find(verification => verification.description === 'Confirmado')?.id,
				snomed: this.snomedConcept
			};
			this.diagnosticsEpicrisisService.addMainDiagnosis(newDiagnosis, this.form.controls.mainDiagnosis);
			this.resetForm();
		}
	}

	addToListProblem() {
		if (this.formProblem.valid && this.snomedConceptProblem) {
			const newHealthCondition: HealthConditionDto = {
				snomed: this.snomedConceptProblem
			};
			this.problemEpicrisisService.addProblem(newHealthCondition);
			this.resetFormProblem();
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.formDiagnosis.controls.snomed.setValue(pt);
	}

	setExternalCause(event: ExternalCauseDto) {
		this.externalCauseDto = event;
	}

	setConceptProblemSnomed(selectedConcept: SnomedDto): void {
		this.snomedConceptProblem = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.formProblem.controls.snomedProblem.setValue(pt);
	}

	setConceptProblem(selectedConcept: SnomedDto): void {
		this.snomedConceptProblem = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.formProblem.controls.snomedProblem.setValue(pt);
		this.addToListProblem();
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.formDiagnosis.reset();
	}

	resetFormProblem() {
		delete this.snomedConceptProblem;
		this.formProblem.reset();
	}

	showSuccesAndClosePopup(epicrisis: EpicrisisDto) {
		this.snackBarService.showSuccess('internaciones.epicrisis.messages.SUCCESS');
		this.dockPopupRef.close({ fieldsToUpdate: this.fieldsToUpdate(epicrisis) });
	}

	private closeEpicrisis(obs: Observable<any>, epicrisis: EpicrisisDto, openMedicalDischarge: boolean) {
		this.isDisableConfirmButton = true;
		let fieldsToUpdate = this.fieldsToUpdate(epicrisis);
		obs
			.subscribe(r => {
				this.snackBarService.showSuccess('internaciones.epicrisis.messages-draft.SUCCESS');
				this.dockPopupRef.close({ fieldsToUpdate, openMedicalDischarge });
			}, _ => {
				this.isDisableConfirmButton = false;
				this.snackBarService.showError('internaciones.epicrisis.messages.ERROR')
			});
	}

	private openEditReason(epicrisis: EpicrisisDto) {
		const dialogRef = this.dialog.open(DocumentActionReasonComponent, {
			data: {
				title: 'internaciones.dialogs.actions-document.EDIT_TITLE',
				subtitle: 'internaciones.dialogs.actions-document.SUBTITLE',
			},
			width: "50vh",
			autoFocus: false,
			disableClose: true
		});
		dialogRef.afterClosed().subscribe(reason => {
			this.isDisableConfirmButton = true;
			if (reason) {
				epicrisis.modificationReason = reason;
				this.epicrisisService.editEpicrsis(epicrisis, this.data.patientInfo.epicrisisId, this.data.patientInfo.internmentEpisodeId).subscribe(
					success => this.showSuccesAndClosePopup(epicrisis),
					_ => {
						this.isDisableConfirmButton = false;
						this.snackBarService.showError('internaciones.epicrisis.messages.ERROR')
					});
			}
		});
	}

	ngOnDestroy(): void {
		this.externalCause$.unsubscribe();
	}

}
