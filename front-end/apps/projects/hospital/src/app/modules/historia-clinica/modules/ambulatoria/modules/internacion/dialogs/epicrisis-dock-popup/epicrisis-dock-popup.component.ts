import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import {
	AllergyConditionDto, AppFeature, DiagnosisDto,
	EpicrisisDto,
	EpicrisisGeneralStateDto,
	EpicrisisObservationsDto, ExternalCauseDto, HealthConditionDto,
	HealthHistoryConditionDto,
	HospitalizationProcedureDto,
	ImmunizationDto,
	MasterDataInterface,
	MedicationDto,
	ResponseAnamnesisDto,
	ResponseEpicrisisDto,
	SnomedDto, SnomedECL
} from '@api-rest/api-model';
import { EpicrisisService } from '@api-rest/services/epicrisis.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { TableService } from '@core/services/table.service';
import { hasError } from '@core/utils/form.utils';
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { OVERLAY_DATA } from "@presentation/presentation-model";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable, Subscription } from 'rxjs';
import { ComponentEvaluationManagerService } from '../../../../services/component-evaluation-manager.service';
import { BehaviorSubject, map } from 'rxjs';
import { DocumentActionReasonComponent } from '../document-action-reason/document-action-reason.component';
import { ExternalCauseService } from '../../services/external-cause.service';
import { ControlDynamicFormService } from '../../services/control-dynamic-form.service';
import { ObstetricComponent } from '../../components/obstetric/obstetric.component';
import { DiagnosisEpicrisisService } from '../../services/diagnosis-epicrisis.service';
import { ObstetricFormService } from '../../services/obstetric-form.service';
import { ProblemEpicrisisService } from '../../services/problem-epicrisis.service';

@Component({
	selector: 'app-epicrisis-dock-popup',
	templateUrl: './epicrisis-dock-popup.component.html',
	styleUrls: ['./epicrisis-dock-popup.component.scss'],
	providers: [ExternalCauseService, ControlDynamicFormService, ObstetricFormService, ComponentEvaluationManagerService, ProblemEpicrisisService]
})
export class EpicrisisDockPopupComponent implements OnInit {


	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	isAllSelected = this.tableService.isAllSelected;
	masterToggle = this.tableService.masterToggle;
	public hasError = hasError;
	diagnosticsEpicrisisService: DiagnosisEpicrisisService;
	snomedConcept: SnomedDto;
	snomedConceptProblem: SnomedDto;
	verifications: MasterDataInterface<string>[];
	anamnesis: ResponseAnamnesisDto;
	form: UntypedFormGroup;
	formDiagnosis: UntypedFormGroup;
	formProblem: UntypedFormGroup;
	showWarning: boolean = false;
	isDraft = false;
	isDisableConfirmButton = false;
	isConfirmed: boolean = false;
	medications: MedicationDto[] = [];
	canConfirmedDocument = false;
	ECL = SnomedECL.DIAGNOSIS;
	searchConceptsLocallyFF = false;
	cipresEpicrisisFF = false;
	externalCauseDto: ExternalCauseDto = {};
	event$: Observable<ExternalCauseDto>;
	private externalCause$: Subscription;
	mainDiagnosis: DiagnosisDto;
	immunizationsData: ImmunizationDto[] = [];
	immunizations: SnomedConcept<ImmunizationDto>[] = [];
	personalHistories: SnomedConcept<HealthHistoryConditionDto>[] = [];
	familyHistories: SnomedConcept<HealthHistoryConditionDto>[] = [];
	allergies: SnomedConcept<AllergyConditionDto>[] = [];
	anthropometricDataSubject = new BehaviorSubject<boolean>(true);
	observationsSubject = new BehaviorSubject<boolean>(true);
	externalCauseDtoSubject = new BehaviorSubject<boolean>(true);
	diagnosis: DiagnosisDto[] = [];
	waitToResponse = false;
	procedures: SnomedConcept<HospitalizationProcedureDto>[] = [];


	@ViewChild(ObstetricComponent) formulario!: ObstetricComponent;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		private readonly featureFlagService: FeatureFlagService,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly epicrisisService: EpicrisisService,
		private readonly snackBarService: SnackBarService,
		private readonly tableService: TableService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly internmentStateService: InternmentStateService,
		private readonly snomedService: SnomedService,
		private readonly dialog: MatDialog,
		private readonly externalCauseServise: ExternalCauseService,
		readonly obtetricForm: ObstetricFormService,
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		readonly problemEpicrisisService: ProblemEpicrisisService,
	) {
		this.setFeatureFlags();
	}

	ngOnInit(): void {
		this.isDraft = this.data.patientInfo.isDraft;
		this.canConfirmedDocument = this.data.patientInfo.isDraft;
		this.diagnosticsEpicrisisService = new DiagnosisEpicrisisService(this.internacionMasterDataService, this.internmentStateService, this.tableService, this.data.patientInfo.internmentEpisodeId);

		this.formProblem = this.formBuilder.group({
			snomedProblem: [null, Validators.required]
		});
		this.formDiagnosis = this.formBuilder.group({
			snomed: [null, Validators.required]
		});
		this.form = this.formBuilder.group({
			mainDiagnosis: [null, Validators.required],
			snomed: [null],
			observations: this.formBuilder.group({
				evolutionNote: [null, [Validators.required]],
				indicationsNote: [null, [Validators.required]],
				otherNote: [null],
				physicalExamNote: [null, [Validators.required]],
				studiesSummaryNote: [null, [Validators.required]]
			})
		});

		const healthVerificationMasterData$ = this.internacionMasterDataService.getHealthVerification();
		healthVerificationMasterData$.subscribe(healthVerification => {
			this.verifications = healthVerification;
		});

		const epicrisisInternmentGeneralState$ = this.epicrisisService.getInternmentGeneralState(this.data.patientInfo.internmentEpisodeId);
		epicrisisInternmentGeneralState$.subscribe((response: EpicrisisGeneralStateDto) => {
			this.componentEvaluationManagerService.epicrisis = response;
			this.waitToResponse = true;
			this.form.controls.mainDiagnosis.setValue(response.mainDiagnosis);
			this.diagnosticsEpicrisisService.setInternmentMainDiagnosis(response.mainDiagnosis);
			this.problemEpicrisisService.setProblems(response.otherProblems);
			this.problemEpicrisisService.initTable(response.otherProblems);
			this.diagnosticsEpicrisisService.initTable(response.diagnosis);
			this.mainDiagnosis = response.mainDiagnosis;

			let epicrisis$;

			if (this.data.patientInfo.isDraft) {
				epicrisis$ = this.epicrisisService.getDraft(this.data.patientInfo.epicrisisId, this.data.patientInfo.internmentEpisodeId);
			} else {
				if (this.data.patientInfo.epicrisisId) {
					epicrisis$ = this.epicrisisService.getEpicrisis(this.data.patientInfo.epicrisisId, this.data.patientInfo.internmentEpisodeId);
				}
			}
			if (epicrisis$)
				epicrisis$.subscribe((epicrisis: ResponseEpicrisisDto) => this.setValues(epicrisis, response));
			else {
				this.diagnosis = response.diagnosis;

				this.personalHistories = response.personalHistories.map((objeto: HealthHistoryConditionDto) => ({
					concept: { ...objeto },
					isAdded: false
				}));

				this.familyHistories = response.familyHistories.map((objeto: HealthHistoryConditionDto) => ({
					concept: { ...objeto },
					isAdded: false
				}));

				this.allergies = response.allergies.map((objeto: AllergyConditionDto) => ({
					concept: { ...objeto },
					isAdded: false
				}));

				this.immunizations = response.immunizations.map((objeto: ImmunizationDto) => ({
					concept: { ...objeto },
					isAdded: false
				}))

				this.procedures = response.procedures.map((objeto: HospitalizationProcedureDto) => ({
					concept: { ...objeto },
					isAdded: false
				}));
			}

		});
		this.epicrisisService.existUpdatesAfterEpicrisis(this.data.patientInfo.internmentEpisodeId).subscribe((showWarning: boolean) => this.showWarning = showWarning);

		this.externalCause$ = this.externalCauseServise.getValue().subscribe();
		this.verifyConfirmed()

		this.form.get('observations').valueChanges.pipe(
			map(formData => Object.values(formData)),
			map(formValues => formValues.every(value => value === null || value === ''))
		).subscribe(allFormValuesAreNull => {
			this.observationsSubject.next(allFormValuesAreNull);
		});
	}

	private verifyConfirmed() {
		if (this.data.patientInfo.epicrisisId) {
			this.epicrisisService.getEpicrisis(this.data.patientInfo.epicrisisId, this.data.patientInfo.internmentEpisodeId).subscribe(response => {
				if (response && response.confirmed) {
					this.isConfirmed = true
				}
			})
		}
	}

	private setValues(e: ResponseEpicrisisDto, response: EpicrisisGeneralStateDto): void {
		this.componentEvaluationManagerService.epicrisisDraft = e;
		if (e?.obstetricEvent) {
			this.obtetricForm.setValue(e.obstetricEvent);
		}
		if (e?.externalCause) {
			this.externalCauseDto = e?.externalCause;
			this.externalCauseServise.setValue(e.externalCause);
		}
		this.personalHistories = response.personalHistories.map((objeto: HealthHistoryConditionDto) => ({
			concept: { ...objeto },
			isAdded: e.personalHistories.some(i => i.snomed.sctid === objeto.snomed.sctid)
		}));

		this.familyHistories = response.familyHistories.map((objeto: HealthHistoryConditionDto) => ({
			concept: { ...objeto },
			isAdded: e.familyHistories.some(i => i.snomed.sctid === objeto.snomed.sctid)
		}));

		this.allergies = response.allergies.map((objeto: AllergyConditionDto) => ({
			concept: { ...objeto },
			isAdded: e.allergies.some(i => i.snomed.sctid === objeto.snomed.sctid)
		}));

		this.immunizations = response.immunizations.map((objeto: ImmunizationDto) => ({
			concept: { ...objeto },
			isAdded: e.immunizations.some(i => i.snomed.sctid === objeto.snomed.sctid)
		}));

		this.procedures = response.procedures.map((objeto: HospitalizationProcedureDto) => ({
			concept: { ...objeto },
			isAdded: e.procedures.some(i => i.snomed.sctid === objeto.snomed.sctid)
		}));

		response.diagnosis.forEach(diag => diag.isAdded = e.diagnosis.some(d => d.snomed.sctid === diag.snomed.sctid));
		this.diagnosis = response.diagnosis;

		Object.keys(e.notes).forEach((key: string) => {
			if (e.notes[key]) {
				this.form.controls.observations.patchValue({ [key]: e.notes[key] });
			}
		});
		this.medications = e.medications;
		this.diagnosticsEpicrisisService.checkDiagnosis(e.diagnosis);
	}

	save(): void {
		if (!this.cipresEpicrisisFF) {
			if (this.form.valid) {
				this.saveEpicrisis();
			} else {
				this.snackBarService.showError('internaciones.epicrisis.messages.ERROR');
				this.form.markAllAsTouched();
			}
		} else if (this.formulario?.isValidForm() && this.form.valid) {
			this.saveEpicrisis();
		} else {
			this.snackBarService.showError('internaciones.epicrisis.messages.ERROR');
			this.formulario?.isValidForm();
			this.form.markAllAsTouched();
		}
	}

	private saveEpicrisis() {
		const epicrisis: EpicrisisDto = this.getEpicrisis(true);
		this.ngOnDestroy();
		this.isDisableConfirmButton = true;
		if (this.data.patientInfo.epicrisisId) {
			this.openEditReason(epicrisis);
		} else {
			this.epicrisisService.createDocument(epicrisis, this.data.patientInfo.internmentEpisodeId)
				.subscribe(
					(epicrisisResponse: ResponseEpicrisisDto) => {
						this.snackBarService.showSuccess('internaciones.epicrisis.messages.SUCCESS');
						let fieldsToUpdate = this.fieldsToUpdate(epicrisis);
						this.dockPopupRef.close({ fieldsToUpdate });
					},
					(_) => {
						this.isDisableConfirmButton = false;
						this.snackBarService.showError('internaciones.epicrisis.messages.ERROR');
					}
				);
		}
	}

	saveDraft(): void {
		if (this.cipresEpicrisisFF) {
			{
				if (this.formulario.isValidForm() && this.form.valid) {
					this.saveEpicrisisDraft();

				} else {
					this.snackBarService.showError('internaciones.epicrisis.messages-draft.ERROR');
					this.form.markAllAsTouched();
				}
			}
		} else

			if (this.form.valid) {
				this.saveEpicrisisDraft();
			} else {
				this.snackBarService.showError('internaciones.epicrisis.messages-draft.ERROR');
				this.form.markAllAsTouched();
			}

	}


	private saveEpicrisisDraft() {
		const epicrisis = this.getEpicrisis(false);
		let obs$;
		if (this.data.patientInfo.epicrisisId) {
			obs$ = this.epicrisisService.
				updateDraft(epicrisis, this.data.patientInfo.epicrisisId, this.data.patientInfo.internmentEpisodeId)
		} else
			obs$ = this.epicrisisService.createDraftDocument(epicrisis, this.data.patientInfo.internmentEpisodeId)

		this.closeEpicrisis(obs$, epicrisis, false);
	}

	saveConfirmedDraft(): void {
		if (this.cipresEpicrisisFF) {
			if (this.form.valid && this.formulario.isValidForm()) {
				this.saveEpicrisisConfirmedDraft();
			} else {
				this.snackBarService.showError('internaciones.epicrisis.messages-draft.ERROR');
				this.form.markAllAsTouched();
			}
		}
		else
			if (this.form.valid) {
				this.saveEpicrisisConfirmedDraft();
			} else {
				this.snackBarService.showError('internaciones.epicrisis.messages-draft.ERROR');
				this.form.markAllAsTouched();
			}
	}

	private saveEpicrisisConfirmedDraft() {
		const epicrisis = this.getEpicrisis(true);
		let obs$ = this.epicrisisService.
			closeDraft(this.data.patientInfo.internmentEpisodeId, this.data.patientInfo.epicrisisId, epicrisis)
		this.closeEpicrisis(obs$, epicrisis, true);
	}

	private getEpicrisis(confirmed?: boolean): EpicrisisDto {
		return {
			confirmed: confirmed,
			notes: this.toEpicrisisObservationsDto(this.form),
			mainDiagnosis: this.mainDiagnosis,
			diagnosis: this.diagnosis.filter(d => d.isAdded),
			externalCause: this.externalCauseDto,
			familyHistories: this.familyHistories.filter(i => i.isAdded).map(i => i.concept),
			personalHistories: this.personalHistories.filter(i => i.isAdded).map(i => i.concept),
			medications: this.medications,
			immunizations: this.immunizations.filter(i => i.isAdded).map(i => i.concept),
			allergies: this.allergies.filter(i => i.isAdded).map(i => i.concept),
			obstetricEvent: this.formulario?.getForm(),
			otherProblems: this.problemEpicrisisService.getProblems(),
			procedures: this.procedures.filter(i => i.isAdded).map(i => i.concept),
		}
	}



	private toEpicrisisObservationsDto(form: UntypedFormGroup): EpicrisisObservationsDto {
		return {
			evolutionNote: form.value.observations.evolutionNote,
			indicationsNote: form.value.observations.indicationsNote,
			otherNote: form.value.observations.otherNote,
			physicalExamNote: form.value.observations.physicalExamNote,
			studiesSummaryNote: form.value.observations.studiesSummaryNote
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
		this.componentEvaluationManagerService.externalCause = event;
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
			this.isDisableConfirmButton = false;
			if (reason) {
				epicrisis.modificationReason = reason;
				this.epicrisisService.editEpicrsis(epicrisis, this.data.patientInfo.epicrisisId, this.data.patientInfo.internmentEpisodeId).subscribe(
					success => this.showSuccesAndClosePopup(epicrisis),
					_ => {
						this.snackBarService.showError('internaciones.epicrisis.messages.ERROR')
					});
			}
		});
	}

	ngOnDestroy(): void {
		this.externalCause$.unsubscribe();
	}

	getAnthropometricData(): Observable<boolean> {
		return this.anthropometricDataSubject.asObservable();
	}

	getObservations$(): Observable<boolean> {
		return this.observationsSubject.asObservable();
	}

	private setFeatureFlags = () => {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => this.searchConceptsLocallyFF = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_CAMPOS_CIPRES_EPICRISIS).subscribe(isOn => this.cipresEpicrisisFF = isOn);
	}
}

export interface SnomedConcept<T> {
	concept: T,
	isAdded: boolean
}
