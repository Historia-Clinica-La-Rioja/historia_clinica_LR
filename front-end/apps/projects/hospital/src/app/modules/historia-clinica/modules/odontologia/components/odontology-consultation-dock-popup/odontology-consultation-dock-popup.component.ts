import { Component, ElementRef, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, ClinicalSpecialtyDto, DateDto, OdontologyConceptDto, OdontologyConsultationDto, OdontologyDentalActionDto, OdontologyDiagnosticDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { MIN_DATE } from "@core/utils/date.utils";
import { hasError, scrollIntoError } from '@core/utils/form.utils';
import { NewConsultationAddReasonFormComponent } from '@historia-clinica/dialogs/new-consultation-add-reason-form/new-consultation-add-reason-form.component';
import { NewConsultationAllergyFormComponent } from '@historia-clinica/dialogs/new-consultation-allergy-form/new-consultation-allergy-form.component';
import { NewConsultationMedicationFormComponent } from '@historia-clinica/dialogs/new-consultation-medication-form/new-consultation-medication-form.component';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { HCEPersonalHistory } from '@historia-clinica/modules/ambulatoria/dialogs/reference/reference.component';
import { AlergiasNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service';
import { ReferenceInformation } from '@historia-clinica/modules/ambulatoria/services/ambulatory-consultation-reference.service';
import { MedicacionesNuevaConsultaService } from "@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service";
import { MotivoNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { NewConsultationPersonalHistoriesService } from '@historia-clinica/modules/ambulatoria/services/new-consultation-personal-histories.service';
import { toDentalAction, toOdontologyAllergyConditionDto, toOdontologyDiagnosticDto, toOdontologyMedicationDto, toOdontologyPersonalHistoryDto, toOdontologyProcedureDto } from '@historia-clinica/modules/odontologia/utils/mapper.utils';
import { ProblemasService } from '@historia-clinica/services/problemas.service';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SuggestedFieldsPopupComponent } from '@presentation/components/suggested-fields-popup/suggested-fields-popup.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { combineLatest, forkJoin, Observable } from 'rxjs';
import { take } from 'rxjs/operators';
import { OdontologyConsultationService } from '../../api-rest/odontology-consultation.service';
import { NewConsultationAddDiagnoseFormComponent } from '../../dialogs/new-consultation-add-diagnose-form/new-consultation-add-diagnose-form.component';
import { ActionsNewConsultationService } from '../../services/actions-new-consultation.service';
import { ActionType } from '../../services/actions.service';
import { ConceptsFacadeService } from '../../services/concepts-facade.service';
import { ConsultationSuggestedFieldsService } from '../../services/consultation-suggested-fields.service';
import { ActionedTooth, OdontogramService } from '../../services/odontogram.service';
import { OdontologyReferenceService } from '../../services/odontology-reference.service';
import { SurfacesNamesFacadeService } from '../../services/surfaces-names-facade.service';
import { EpisodeData } from '@historia-clinica/components/episode-data/episode-data.component';
import { HierarchicalUnitService } from '@historia-clinica/services/hierarchical-unit.service';
import { ConfirmarPrescripcionComponent } from '@historia-clinica/modules/ambulatoria/dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { PrescriptionTypes } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { NewConsultationPersonalHistoryFormComponent } from '@historia-clinica/modules/ambulatoria/dialogs/new-consultation-personal-history-form/new-consultation-personal-history-form.component';
import { ConceptsList } from 'projects/hospital/src/app/modules/hsi-components/concepts-list/concepts-list.component';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Component({
	selector: 'app-odontology-consultation-dock-popup',
	templateUrl: './odontology-consultation-dock-popup.component.html',
	styleUrls: ['./odontology-consultation-dock-popup.component.scss'],
	providers: [
		ConceptsFacadeService
	]
})
export class OdontologyConsultationDockPopupComponent implements OnInit {

	reasonNewConsultationService: MotivoNuevaConsultaService;
	otherDiagnosticsNewConsultationService: ProblemasService;
	severityTypes: any[];
	allergiesNewConsultationService: AlergiasNuevaConsultaService;
	criticalityTypes: any[];
	personalHistoriesNewConsultationService: NewConsultationPersonalHistoriesService;
	medicationsNewConsultationService: MedicacionesNuevaConsultaService;
	form: UntypedFormGroup;
	clinicalSpecialties: ClinicalSpecialtyDto[];
	diagnosticsNewConsultationService: ActionsNewConsultationService;
	proceduresNewConsultationService: ActionsNewConsultationService;
	otherProceduresService: ProcedimientosService;
	odontologyReferenceService: OdontologyReferenceService;

	searchConceptsLocallyFFIsOn = false;
	episodeData: EpisodeData;
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	public hasError = hasError;
	minDate = MIN_DATE;

	disableConfirmButton = false;

	isAllergyNoRefer: boolean = true;
	allergyContent: ConceptsList = {
		id: 'allergy-checkbox-concepts-list',
		header: {
			text: 'ambulatoria.paciente.nueva-consulta.alergias.TITLE',
			icon: 'cancel'
		},
		titleList: 'ambulatoria.paciente.nueva-consulta.alergias.table.TITLE',
		actions: {
			button: 'ambulatoria.paciente.nueva-consulta.alergias.ADD',
			checkbox: 'ambulatoria.paciente.nueva-consulta.alergias.NO_REFER',
		}
	}
	personalHistoriesContent: ConceptsList = {
		id: 'personal-histories-checkbox-concepts-list',
		header: {
			text: 'odontologia.odontology-consultation-dock-popup.PERSONAL_HISTORY',
			icon: 'cancel'
		},
		titleList: 'odontologia.odontology-consultation-dock-popup.REGISTERED_PERSONAL_HISTORY',
		actions: {
			button: 'odontologia.odontology-consultation-dock-popup.ADD_PERSONAL_HISTORY',
			checkbox: 'ambulatoria.paciente.nueva-consulta.alergias.NO_REFER',
		}
	}
	isPersonalHistories: boolean = true;

	constructor(
		@Inject(OVERLAY_DATA) public data: OdontologyConsultationData,
		public dockPopupRef: DockPopupRef,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly internmentMasterDataService: InternacionMasterDataService,
		private readonly odontogramService: OdontogramService,
		private readonly conceptsFacadeService: ConceptsFacadeService,
		private readonly surfacesNamesFacadeService: SurfacesNamesFacadeService,
		private readonly odontologyConsultationService: OdontologyConsultationService,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly referenceFileService: ReferenceFileService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly el: ElementRef,
		private readonly hierarchicalUnitFormService: HierarchicalUnitService,
		private readonly dateFormatPipe: DateFormatPipe

	) {
		this.reasonNewConsultationService = new MotivoNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.allergiesNewConsultationService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService, internmentMasterDataService);
		this.medicationsNewConsultationService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.personalHistoriesNewConsultationService = new NewConsultationPersonalHistoriesService(this.snomedService, this.snackBarService);
		this.otherDiagnosticsNewConsultationService = new ProblemasService(formBuilder, this.snomedService, this.snackBarService);
		this.diagnosticsNewConsultationService = new ActionsNewConsultationService(this.odontogramService, this.surfacesNamesFacadeService, ActionType.DIAGNOSTIC, this.conceptsFacadeService);
		this.proceduresNewConsultationService = new ActionsNewConsultationService(this.odontogramService, this.surfacesNamesFacadeService, ActionType.PROCEDURE, this.conceptsFacadeService);
		this.otherProceduresService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService, this.dateFormatPipe);
		this.odontologyReferenceService = new OdontologyReferenceService(this.dialog, this.data, this.otherDiagnosticsNewConsultationService);
	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			evolution: [null, null],
			permanentTeethPresent: [null, [Validators.maxLength(2), Validators.pattern('^[0-9]+$')]],
			temporaryTeethPresent: [null, [Validators.maxLength(2), Validators.pattern('^[0-9]+$')]],
		});

		this.internmentMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities => {
			this.criticalityTypes = allergyCriticalities;
			this.allergiesNewConsultationService.setCriticalityTypes(allergyCriticalities);
		});

		this.internmentMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.otherDiagnosticsNewConsultationService.setSeverityTypes(healthConditionSeverities);
		});

		this.odontologyConsultationService.getConsultationIndices(this.data.patientId).subscribe(odontologyConsultationArray => {
			const odontologyConsultation = odontologyConsultationArray.find(odontologyConsultation => odontologyConsultation.permanentTeethPresent !== null && odontologyConsultation.temporaryTeethPresent !== null)
			if (odontologyConsultation) {
				this.form.controls['permanentTeethPresent'].setValue(odontologyConsultation.permanentTeethPresent);
				this.form.controls['temporaryTeethPresent'].setValue(odontologyConsultation.temporaryTeethPresent);
			}
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => this.searchConceptsLocallyFFIsOn = isOn);
	}

	save() {
		if (this.hierarchicalUnitFormService.isValidForm()) {
			setTimeout(() => {
				scrollIntoError(this.hierarchicalUnitFormService.getForm(), this.el)
			}, 300);
		} else {
			if (this.form.valid) {
				combineLatest([this.conceptsFacadeService.getProcedures$(), this.conceptsFacadeService.getDiagnostics$()]).pipe(take(1))
					.subscribe(([procedures, diagnostics]) => {
						const allConcepts = diagnostics.concat(procedures);
						const odontologyDto: OdontologyConsultationDto = this.buildConsultationDto(allConcepts);
						const suggestedFieldsService = new ConsultationSuggestedFieldsService(odontologyDto);

						if (!suggestedFieldsService.nonCompletedFields.length) {
							this.uploadRefFilesAndCreateConsultation(odontologyDto);
						}
						else {
							this.openDialog(suggestedFieldsService.nonCompletedFields, suggestedFieldsService.presentFields, odontologyDto);
						}
					})
			}
			else {
				this.disableConfirmButton = false;
				this.snackBarService.showError('Error al guardar documento de nueva consulta odontológica');
				scrollIntoError(this.form, this.el);
			}
		}
	}

	addReason() {
		this.dialog.open(NewConsultationAddReasonFormComponent, {
			data: {
				reasonService: this.reasonNewConsultationService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	addProcedure() {
		this.dialog.open(NewConsultationProcedureFormComponent, {
			data: {
				procedureService: this.otherProceduresService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	addDiagnose() {
		this.dialog.open(NewConsultationAddDiagnoseFormComponent, {
			data: {
				diagnosesService: this.otherDiagnosticsNewConsultationService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
				severityTypes: this.severityTypes
			},
			autoFocus: false,
			width: '40%',
			disableClose: true,
		});
	}

	addAllergy(): void {
		this.dialog.open(NewConsultationAllergyFormComponent, {
			data: {
				allergyService: this.allergiesNewConsultationService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	addPersonalHistory(): void {
		this.dialog.open(NewConsultationPersonalHistoryFormComponent, {
			data: {
				personalHistoryService: this.personalHistoriesNewConsultationService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '40',
			disableClose: true,
		});
	}

	addMedication(): void {
		this.dialog.open(NewConsultationMedicationFormComponent, {
			data: {
				medicationService: this.medicationsNewConsultationService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	checkAllergyEvent($event) {
		if ($event.addPressed) {
			this.addAllergy();
		}
		this.isAllergyNoRefer = !$event.checkboxSelected;
	}

	checkPersonalHistoriesEvent = ($event) => {
		if ($event.addPressed) {
			this.addPersonalHistory();
		}
		this.isPersonalHistories = !$event.checkboxSelected;
	}

	private openDialog(nonCompletedFields: string[], presentFields: string[], odontologyDto: OdontologyConsultationDto): void {
		const dialogRef = this.dialog.open(SuggestedFieldsPopupComponent, {
			data: { nonCompletedFields, presentFields }
		});
		dialogRef.afterClosed().subscribe(confirm => {
			if (confirm) {
				this.uploadRefFilesAndCreateConsultation(odontologyDto);
			}
		});
	}

	private createConsultation(odontologyDto: OdontologyConsultationDto) {
		if (odontologyDto.references.length) {
			odontologyDto.diagnostics = this.problemsToUpdate(odontologyDto);
		}

		this.odontologyConsultationService.createConsultation(this.data.patientId, odontologyDto).subscribe(
			res => {
				res.orderIds.forEach((order) => {
					this.openNewEmergencyCareStudyConfirmationDialog([order]);
				  });
				this.snackBarService.showSuccess('El documento de consulta odontologica se guardó exitosamente');
				this.dockPopupRef.close({
					confirmed: true,
					fieldsToUpdate: this.mapFieldsToUpdate(odontologyDto)
				});
			},
			_ => {
				this.snackBarService.showError('Error al guardar documento de nueva consulta odontológica');
			}
		);

	}

	private openNewEmergencyCareStudyConfirmationDialog(order: number[]) {
		this.dialog.open(ConfirmarPrescripcionComponent,
			{
				disableClose: true,
				data: {
					titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.STUDY_TITLE',
					downloadButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.DOWNLOAD_BUTTON_STUDY',
					successLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_STUDY_SUCCESS',
					prescriptionType: PrescriptionTypes.STUDY,
					patientId: this.data.patientId,
					prescriptionRequest: order,
				},
				width: '35%',
			});
	}

	private mapFieldsToUpdate(odontologyDto: OdontologyConsultationDto): FieldsToUpdate {

		let problemsToUpdate = !!odontologyDto.diagnostics?.length || !!odontologyDto.dentalActions.length || !!odontologyDto.personalHistories.content.length;

		if (odontologyDto.references.length) {
			problemsToUpdate = !!this.problemsToUpdate(odontologyDto).length || !!odontologyDto.dentalActions.length;
		}

		return {
			allergies: !!odontologyDto.allergies?.content.length,
			personalHistories: !!odontologyDto.personalHistories?.content.length,
			medications: !!odontologyDto.medications?.length,
			problems: problemsToUpdate,
		};
	}

	private buildConsultationDto(allConcepts: OdontologyConceptDto[]): OdontologyConsultationDto {

		let dentalActions: OdontologyDentalActionDto[] = [];
		this.odontogramService.actionedTeeth.forEach((a: ActionedTooth) => dentalActions = dentalActions.concat(toDentalAction(a, allConcepts)));

		return {
			allergies: {
				isReferred: (this.isAllergyNoRefer && this.allergiesNewConsultationService.getAlergias().length === 0) ? null: this.isAllergyNoRefer,
				content: this.allergiesNewConsultationService.getAlergias().map(toOdontologyAllergyConditionDto)
			},
			evolutionNote: this.form.value.evolution,
			medications: this.medicationsNewConsultationService.getMedicaciones().map(toOdontologyMedicationDto),
			diagnostics: this.otherDiagnosticsNewConsultationService.getProblemas().map(toOdontologyDiagnosticDto),
			procedures: this.otherProceduresService.getProcedimientos().map(toOdontologyProcedureDto),
			reasons: this.reasonNewConsultationService.getMotivosConsulta(),
			clinicalSpecialtyId: this.episodeData.clinicalSpecialtyId,
			dentalActions,
			personalHistories: {
				isReferred: (this.isPersonalHistories && this.personalHistoriesNewConsultationService.getPersonalHistories().length === 0) ? null: this.isPersonalHistories,
				content: this.personalHistoriesNewConsultationService.getPersonalHistories().map(toOdontologyPersonalHistoryDto),
			},
			permanentTeethPresent: this.form.value.permanentTeethPresent,
			temporaryTeethPresent: this.form.value.temporaryTeethPresent,
			references: this.odontologyReferenceService.getOdontologyReferences(),
			patientMedicalCoverageId: this.episodeData.medicalCoverageId,
			hierarchicalUnitId: this.episodeData.hierarchicalUnitId
		};
	}

	private uploadRefFilesAndCreateConsultation(odontologyDto: OdontologyConsultationDto) {
		this.disableConfirmButton = true;
		let references: ReferenceInformation[] = this.odontologyReferenceService.getReferences();
		if (!references.length) {
			this.createConsultation(odontologyDto);
			return;
		}

		const referencesToUpdate: Observable<number[]>[] = [];

		references.forEach((reference, index) => {
			if (reference.referenceFiles?.length) {
				const obs = this.referenceFileService.uploadReferenceFiles(this.data.patientId, reference.referenceFiles);
				referencesToUpdate.push(obs);
			}

			if (referencesToUpdate.length) {
					forkJoin(referencesToUpdate).subscribe((referenceFileIds: number[][]) => {
						const filesAmount = reference.referenceFiles.length;
						for(let i = 0; i < filesAmount; i++ ){
							if (referenceFileIds[index]?.[i]) {
								this.odontologyReferenceService.addFileIdAt(index, referenceFileIds[index][i]);
							}
						}
						this.createConsultation(odontologyDto);
					}, _ => {
						this.snackBarService.showError('odontologia.odontology-consultation-dock-popup.ERROR_TO_UPLOAD_FILES');
					});
			} else {
				odontologyDto.references = this.odontologyReferenceService.getOdontologyReferences();
				this.createConsultation(odontologyDto);
			}
		});
	}

	private problemsToUpdate(odontologyDto: OdontologyConsultationDto): OdontologyDiagnosticDto[] {
		const odontologyDiagnosticDto = [];

		odontologyDto.diagnostics?.forEach(diagnostic => odontologyDiagnosticDto.push(diagnostic));

		const references: ReferenceInformation[] = this.odontologyReferenceService.getReferences();

		references.forEach(reference => {
			reference.referenceProblems.forEach(referenceProblem => {
				const odontoDiagnosticDto = this.mapToOdontologyDiagnosticDto(referenceProblem);
				const existProblem = odontologyDiagnosticDto.find(problem => problem.snomed.sctid === odontoDiagnosticDto.snomed.sctid);
				if (!existProblem) {
					odontologyDiagnosticDto.push(odontoDiagnosticDto);
				}
			});
		});

		return odontologyDiagnosticDto;
	}

	private mapToOdontologyDiagnosticDto(problem: HCEPersonalHistory): OdontologyDiagnosticDto {
		return {
			chronic: problem.chronic,
			severity: problem.HCEHealthConditionDto.severity,
			snomed: problem.HCEHealthConditionDto.snomed,
			startDate: this.buildDateDto(problem.HCEHealthConditionDto.startDate),
		}
	}

	private buildDateDto(date: string): DateDto {
		if (date) {
			const dateSplit = date.split("-");
			return (
				{
					year: Number(dateSplit[0]),
					month: Number(dateSplit[1]),
					day: Number(dateSplit[2]),
				}
			)
		}
		return null;
	}

}

export interface FieldsToUpdate {
	allergies: boolean,
	personalHistories: boolean,
	medications: boolean,
	problems: boolean
}

export interface OdontologyConsultationData {
	patientId: number;
}
