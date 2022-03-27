import { Component, Inject, OnInit } from '@angular/core';
import {
	AllergyConditionDto,
	DiagnosisDto,
	EpicrisisDto, EpicrisisObservationsDto,
	HealthHistoryConditionDto,
	ImmunizationDto,
	MasterDataInterface,
	MedicationDto,
	ResponseAnamnesisDto,
	ResponseEpicrisisDto,
	SnomedDto,
} from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { EpicrisisService } from '@api-rest/services/epicrisis.service';
import { DatePipe } from '@angular/common';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SelectionModel } from '@angular/cdk/collections';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ContextService } from '@core/services/context.service';
import { TableCheckbox } from '@material/model/table.model';
import { TableService } from '@core/services/table.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { DiagnosisEpicrisisService } from '../../services/diagnosis-epicrisis.service';
import { hasError } from '@core/utils/form.utils';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { SnomedService, SnomedSemanticSearch } from '@historia-clinica/services/snomed.service';
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { OVERLAY_DATA } from "@presentation/presentation-model";
import {
	InternmentFields
} from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";

@Component({
	selector: 'app-epicrisis-dock-popup',
	templateUrl: './epicrisis-dock-popup.component.html',
	styleUrls: ['./epicrisis-dock-popup.component.scss']
})
export class EpicrisisDockPopupComponent implements OnInit {

	private healthClinicalStatus;

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	isAllSelected = this.tableService.isAllSelected;
	masterToggle = this.tableService.masterToggle;
	public hasError = hasError;

	diagnosticsEpicrisisService: DiagnosisEpicrisisService;

	snomedConcept: SnomedDto;
	verifications: MasterDataInterface<string>[];
	anamnesis: ResponseAnamnesisDto;
	form: FormGroup;
	formDiagnosis: FormGroup;

	medications: MedicationDto[] = [];
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

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly epicrisisService: EpicrisisService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly datePipe: DatePipe,
		private readonly snackBarService: SnackBarService,
		private readonly tableService: TableService,
		private readonly contextService: ContextService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly internmentStateService: InternmentStateService,
		private readonly snomedService: SnomedService) {
		this.familyHistories.displayedColumns = this.familyHistories.columns?.map(c => c.def).concat(['select']);
		this.personalHistories.displayedColumns = this.personalHistories.columns?.map(c => c.def).concat(['select']);
		this.allergies.displayedColumns = this.allergies.columns?.map(c => c.def).concat(['select']);
		this.immunizations.displayedColumns = this.immunizations.columns?.map(c => c.def).concat(['select']);

	}

	ngOnInit(): void {

		this.diagnosticsEpicrisisService = new DiagnosisEpicrisisService(this.internacionMasterDataService, this.internmentStateService, this.tableService, this.data.patientInfo.internmentEpisodeId);


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

		const epicrisis$ = this.epicrisisService.getInternmentGeneralState(this.data.patientInfo.internmentEpisodeId);
		epicrisis$.subscribe(response => {
			this.waitToResponse = true;
			this.form.controls.mainDiagnosis.setValue(response.mainDiagnosis);
			this.diagnosticsEpicrisisService.setInternmentMainDiagnosis(response.mainDiagnosis);
			this.diagnosticsEpicrisisService.initTable(response.diagnosis);

			this.personalHistories.data = response.personalHistories ? response.personalHistories : [];
			this.familyHistories.data = response.familyHistories ? response.familyHistories : [];
			this.allergies.data = response.allergies ? response.allergies : [];
			this.immunizations.data = response.immunizations ? response.immunizations : [];
		});

	}

	save(): void {
		if (this.form.valid) {
			const epicrisis: EpicrisisDto = {
				confirmed: true,
				notes: epicrisisObservationsDto(this.form),
				mainDiagnosis: this.form.value.mainDiagnosis,
				diagnosis: this.diagnosticsEpicrisisService.getSelectedAlternativeDiagnostics(),
				familyHistories: this.familyHistories.selection.selected,
				personalHistories: this.personalHistories.selection.selected,
				medications: this.medications,
				immunizations: this.immunizations.selection.selected,
				allergies: this.allergies.selection.selected
			};
			this.epicrisisService.createDocument(epicrisis, this.data.patientInfo.internmentEpisodeId)
				.subscribe((epicrisisResponse: ResponseEpicrisisDto) => {
					this.snackBarService.showSuccess('internaciones.epicrisis.messages.SUCCESS');
					this.dockPopupRef.close(fieldsToUpdate(epicrisis));
				}, _ => this.snackBarService.showError('internaciones.epicrisis.messages.ERROR'));
		} else {
			this.snackBarService.showError('internaciones.epicrisis.messages.ERROR');
			this.form.markAllAsTouched();

		}

		function fieldsToUpdate(epicrisis: EpicrisisDto): InternmentFields {
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

		function epicrisisObservationsDto(form: FormGroup): EpicrisisObservationsDto {
			return {
				evolutionNote: form.value.evolutionNote,
				indicationsNote: form.value.indicationsNote,
				otherNote: form.value.otherNote,
				physicalExamNote: form.value.physicalExamNote,
				studiesSummaryNote: form.value.studiesSummaryNote
			}

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

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.formDiagnosis.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.formDiagnosis.reset();
	}

}
