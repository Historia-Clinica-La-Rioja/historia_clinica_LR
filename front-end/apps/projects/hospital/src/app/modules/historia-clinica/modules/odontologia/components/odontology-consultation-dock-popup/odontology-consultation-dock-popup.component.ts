import { Component, ElementRef, Inject, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { MotivoNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlergiasNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { MedicacionesNuevaConsultaService } from "@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service";
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError, scrollIntoError } from '@core/utils/form.utils';
import { PersonalHistoriesNewConsultationService } from "@historia-clinica/modules/ambulatoria/services/personal-histories-new-consultation.service";
import { newMoment } from "@core/utils/moment.utils";
import { ClinicalSpecialtyDto, DateDto, OdontologyConceptDto, OdontologyConsultationDto, OdontologyDentalActionDto, OdontologyDiagnosticDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ProblemasService } from '@historia-clinica/services/problemas.service';
import { ActionsNewConsultationService } from '../../services/actions-new-consultation.service';
import { ActionedTooth, OdontogramService } from '../../services/odontogram.service';
import { ConceptsFacadeService } from '../../services/concepts-facade.service';
import { SurfacesNamesFacadeService } from '../../services/surfaces-names-facade.service';
import { ActionType } from '../../services/actions.service';
import { SuggestedFieldsPopupComponent } from '@presentation/components/suggested-fields-popup/suggested-fields-popup.component';
import { OdontologyConsultationService } from '../../api-rest/odontology-consultation.service';
import { MatDialog } from '@angular/material/dialog';
import { ConsultationSuggestedFieldsService } from '../../services/consultation-suggested-fields.service';
import { combineLatest, forkJoin, Observable } from 'rxjs';
import { toDentalAction, toOdontologyAllergyConditionDto, toOdontologyDiagnosticDto, toOdontologyMedicationDto, toOdontologyPersonalHistoryDto, toOdontologyProcedureDto } from '@historia-clinica/modules/odontologia/utils/mapper.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { take } from 'rxjs/operators';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { MIN_DATE } from "@core/utils/date.utils";
import { OdontologyReferenceService, Reference } from '../../services/odontology-reference.service';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyCareLineService } from '@api-rest/services/clinical-specialty-care-line.service';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { HCEPersonalHistory } from '@historia-clinica/modules/ambulatoria/dialogs/reference/reference.component';
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { NewConsultationAddReasonFormComponent } from '@historia-clinica/dialogs/new-consultation-add-reason-form/new-consultation-add-reason-form.component';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { NewConsultationAddDiagnoseFormComponent } from '../../dialogs/new-consultation-add-diagnose-form/new-consultation-add-diagnose-form.component';
import { NewConsultationAllergyFormComponent } from '@historia-clinica/dialogs/new-consultation-allergy-form/new-consultation-allergy-form.component';
import { NewConsultationPersonalHistoryFormComponent } from '../../dialogs/new-consultation-personal-history-form/new-consultation-personal-history-form.component';
import { NewConsultationMedicationFormComponent } from '@historia-clinica/dialogs/new-consultation-medication-form/new-consultation-medication-form.component';

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
	personalHistoriesNewConsultationService: PersonalHistoriesNewConsultationService;
	medicationsNewConsultationService: MedicacionesNuevaConsultaService;
	form: FormGroup;
	clinicalSpecialties: ClinicalSpecialtyDto[];
	diagnosticsNewConsultationService: ActionsNewConsultationService;
	proceduresNewConsultationService: ActionsNewConsultationService;
	otherProceduresService: ProcedimientosService;
	odontologyReferenceService: OdontologyReferenceService;

	searchConceptsLocallyFFIsOn = false;
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	public hasError = hasError;
	public today = newMoment();
	minDate = MIN_DATE;

	constructor(
		@Inject(OVERLAY_DATA) public data: OdontologyConsultationData,
		public dockPopupRef: DockPopupRef,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: FormBuilder,
		private readonly internmentMasterDataService: InternacionMasterDataService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly odontogramService: OdontogramService,
		private readonly conceptsFacadeService: ConceptsFacadeService,
		private readonly surfacesNamesFacadeService: SurfacesNamesFacadeService,
		private readonly odontologyConsultationService: OdontologyConsultationService,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly careLineService: CareLineService,
		private readonly clinicalSpecialtyCareLine: ClinicalSpecialtyCareLineService,
		private readonly referenceFileService: ReferenceFileService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly el: ElementRef,
	) {
		this.reasonNewConsultationService = new MotivoNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.allergiesNewConsultationService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.medicationsNewConsultationService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.personalHistoriesNewConsultationService = new PersonalHistoriesNewConsultationService(formBuilder, this.snomedService, this.snackBarService);
		this.otherDiagnosticsNewConsultationService = new ProblemasService(formBuilder, this.snomedService, this.snackBarService);
		this.diagnosticsNewConsultationService = new ActionsNewConsultationService(this.odontogramService, this.surfacesNamesFacadeService, ActionType.DIAGNOSTIC, this.conceptsFacadeService);
		this.proceduresNewConsultationService = new ActionsNewConsultationService(this.odontogramService, this.surfacesNamesFacadeService, ActionType.PROCEDURE, this.conceptsFacadeService);
		this.otherProceduresService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.odontologyReferenceService = new OdontologyReferenceService(this.dialog, this.data, this.otherDiagnosticsNewConsultationService, this.clinicalSpecialtyCareLine, this.careLineService);
	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			evolution: [null, null],
			clinicalSpecialty: [null, [Validators.required]],
			permanentTeethPresent: [null, [Validators.maxLength(2), Validators.pattern('^[0-9]+$')]],
			temporaryTeethPresent: [null, [Validators.maxLength(2), Validators.pattern('^[0-9]+$')]],
		});

		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(clinicalSpecialties => {
			this.form.patchValue({ clinicalSpecialty: clinicalSpecialties[0]?.id });
			this.form.controls['clinicalSpecialty'].markAsTouched();
			this.clinicalSpecialties = clinicalSpecialties;
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
			this.snackBarService.showError('Error al guardar documento de nueva consulta odontológica');
			scrollIntoError(this.form, this.el);
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
			width: '35%',
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
			width: '35%',
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
			_ => {
				this.snackBarService.showSuccess('El documento de consulta odontologica se guardó exitosamente');
				this.dockPopupRef.close({
					confirmed: true,
					fieldsToUpdate: this.mapFieldsToUpdate(odontologyDto)
				});
			},
			_ => {
				this.snackBarService.showError('Error al guardar documento de nueva consulta odontológica');
				const filesToDelete = odontologyDto.references.filter(reference => reference.fileIds.length > 0);
				if (filesToDelete.length) {
					this.errorToUploadReferenceFiles();
				}
			}
		);

	}

	private mapFieldsToUpdate(odontologyDto: OdontologyConsultationDto): FieldsToUpdate {

		let problemsToUpdate = !!odontologyDto.diagnostics?.length || !!odontologyDto.dentalActions.length;

		if (odontologyDto.references.length) {
			problemsToUpdate = !!this.problemsToUpdate(odontologyDto).length || !!odontologyDto.dentalActions.length;
		}

		return {
			allergies: !!odontologyDto.allergies?.length,
			personalHistories: !!odontologyDto.personalHistories?.length,
			medications: !!odontologyDto.medications?.length,
			problems: problemsToUpdate,
		};
	}

	private buildConsultationDto(allConcepts: OdontologyConceptDto[]): OdontologyConsultationDto {

		let dentalActions: OdontologyDentalActionDto[] = [];
		this.odontogramService.actionedTeeth.forEach((a: ActionedTooth) => dentalActions = dentalActions.concat(toDentalAction(a, allConcepts)));

		return {
			allergies: this.allergiesNewConsultationService.getAlergias().map(toOdontologyAllergyConditionDto),
			evolutionNote: this.form.value.evolution,
			medications: this.medicationsNewConsultationService.getMedicaciones().map(toOdontologyMedicationDto),
			diagnostics: this.otherDiagnosticsNewConsultationService.getProblemas().map(toOdontologyDiagnosticDto),
			procedures: this.otherProceduresService.getProcedimientos().map(toOdontologyProcedureDto),
			reasons: this.reasonNewConsultationService.getMotivosConsulta(),
			clinicalSpecialtyId: this.form.value.clinicalSpecialty,
			dentalActions,
			personalHistories: this.personalHistoriesNewConsultationService.getAntecedentes().map(toOdontologyPersonalHistoryDto),
			permanentTeethPresent: this.form.value.permanentTeethPresent,
			temporaryTeethPresent: this.form.value.temporaryTeethPresent,
			references: this.odontologyReferenceService.getOdontologyReferences(),
		};
	}

	private uploadRefFilesAndCreateConsultation(odontologyDto: OdontologyConsultationDto) {

		let references: Reference[] = this.odontologyReferenceService.getReferences();
		if (!references.length) {
			this.createConsultation(odontologyDto);
			return;
		}

		const filesToUpdate: Observable<number>[] = [];

		references.forEach(reference => {
			reference.referenceFiles.forEach(file => {
				const obs = this.referenceFileService.uploadReferenceFiles(this.data.patientId, file);
				filesToUpdate.push(obs);
			})
		});

		if (filesToUpdate.length) {

			forkJoin(filesToUpdate).subscribe((referenceFileId: number[]) => {
				let indexRefFilesIds = 0;
				references.forEach(
					(reference: Reference, index: number) => {
						const filesAmount = reference.referenceFiles.length;
						for (let i = indexRefFilesIds; i < indexRefFilesIds + filesAmount; i++) {
							this.odontologyReferenceService.addFileIdAt(index, referenceFileId[i]);
						}
						indexRefFilesIds += filesAmount;
					}
				);
				odontologyDto.references = this.odontologyReferenceService.getOdontologyReferences();
				this.createConsultation(odontologyDto);
			}, _ => {
				this.snackBarService.showError('odontologia.odontology-consultation-dock-popup.ERROR_TO_UPLOAD_FILES');
				this.errorToUploadReferenceFiles();
			}
			);
		}
		else {
			odontologyDto.references = this.odontologyReferenceService.getOdontologyReferences();
			this.createConsultation(odontologyDto);
		}
	}

	private errorToUploadReferenceFiles() {
		const filesToDelete = this.odontologyReferenceService.getReferenceFilesIds();
		this.referenceFileService.deleteReferenceFiles(filesToDelete);
		this.odontologyReferenceService.deleteReferenceFilesIds();
	}

	private problemsToUpdate(odontologyDto: OdontologyConsultationDto): OdontologyDiagnosticDto[] {
		const odontologyDiagnosticDto = [];

		odontologyDto.diagnostics?.forEach(diagnostic => odontologyDiagnosticDto.push(diagnostic));

		const references: Reference[] = this.odontologyReferenceService.getReferences();

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
			severity: problem.hcePersonalHistoryDto.severity,
			snomed: problem.hcePersonalHistoryDto.snomed,
			startDate: this.buildDateDto(problem.hcePersonalHistoryDto.startDate),
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
