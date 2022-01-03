import { Component, Inject, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { MotivoNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlergiasNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { MedicacionesNuevaConsultaService } from "@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service";
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';
import { PersonalHistoriesNewConsultationService } from "@historia-clinica/modules/ambulatoria/services/personal-histories-new-consultation.service";
import { newMoment } from "@core/utils/moment.utils";
import { ClinicalSpecialtyDto, OdontologyConceptDto, OdontologyConsultationDto, OdontologyDentalActionDto } from '@api-rest/api-model';
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
import { combineLatest } from 'rxjs';
import { toDentalAction, toOdontologyAllergyConditionDto, toOdontologyDiagnosticDto, toOdontologyMedicationDto, toOdontologyPersonalHistoryDto, toOdontologyProcedureDto } from '@historia-clinica/modules/odontologia/utils/mapper.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { take } from 'rxjs/operators';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { MIN_DATE } from "@core/utils/date.utils";
import { OdontologyReferenceService, Reference } from '../../services/odontology-reference.service';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyCareLineService } from '@api-rest/services/clinical-specialty-care-line.service';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';

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

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	errors: string[] = [];
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
			evolution: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
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


		this.reasonNewConsultationService.error$.subscribe(reasonError => {
			this.errors[0] = reasonError;
		});

		this.otherDiagnosticsNewConsultationService.error$.subscribe(otherDiagnosticsError => {
			this.errors[1] = otherDiagnosticsError;
		});

		this.odontologyConsultationService.getConsultationIndices(this.data.patientId).subscribe(odontologyConsultationArray => {
			const odontologyConsultation = odontologyConsultationArray.find(odontologyConsultation => odontologyConsultation.permanentTeethPresent !== null && odontologyConsultation.temporaryTeethPresent !== null)
			if (odontologyConsultation) {
				this.form.controls['permanentTeethPresent'].setValue(odontologyConsultation.permanentTeethPresent);
				this.form.controls['temporaryTeethPresent'].setValue(odontologyConsultation.temporaryTeethPresent);
			}
		});

	}

	private addErrorMessage(): void {
		this.errors[2] = hasError(this.form, 'maxlength', 'evolution') ?
			'La nota de evolución debe tener como máximo 1024 caracteres'
			: undefined;
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
		}
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
			}
		);

	}

	private mapFieldsToUpdate(odontologyDto: OdontologyConsultationDto): FieldsToUpdate {
		return {
			allergies: !!odontologyDto.allergies?.length,
			personalHistories: !!odontologyDto.personalHistories?.length,
			medications: !!odontologyDto.medications?.length,
			problems: !!odontologyDto.diagnostics?.length || !!odontologyDto.dentalActions?.length
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
			personalHistories: this.personalHistoriesNewConsultationService.getAntecedentesPersonales().map(toOdontologyPersonalHistoryDto),
			permanentTeethPresent: this.form.value.permanentTeethPresent,
			temporaryTeethPresent: this.form.value.temporaryTeethPresent,
			references: this.odontologyReferenceService.getOdontologyReferences(),
		};
	}

	private uploadRefFilesAndCreateConsultation(odontologyDto: OdontologyConsultationDto) {
		const references: Reference[] = this.odontologyReferenceService.getReferences();

		if (!references.length) {
			this.createConsultation(odontologyDto);
			return;
		}

		let numberOfReferences = 0;
		let numberOfFiles = 0;
		references.forEach((reference: Reference) => {

			if (!reference.referenceFiles.length) {
				odontologyDto.references = this.odontologyReferenceService.getOdontologyReferences();
				this.createConsultation(odontologyDto);
				return;
			}

			reference.referenceFiles.forEach(referenceFile => {
				this.referenceFileService.uploadReferenceFiles(this.data.patientId, referenceFile).subscribe(fileId => {
					numberOfFiles++;
					reference.referenceIds.push(fileId);
					this.odontologyReferenceService.addFileIdAt(reference.referenceNumber, fileId);
					if (numberOfFiles === reference.referenceFiles.length) {
						numberOfReferences++;
						numberOfFiles = 0;
						
						if (numberOfReferences === references.length) {
							odontologyDto.references = this.odontologyReferenceService.getOdontologyReferences();
							this.createConsultation(odontologyDto);
						}
					}
				},
				error => {
					this.errorToUpdateReferenceFiles(references);
				});
			});
		});
	}
	
	private errorToUpdateReferenceFiles(references: Reference[]) {
		this.snackBarService.showError('Error al guardar documento de nueva consulta odontológica');
		references.forEach(reference => {
			this.referenceFileService.deleteReferenceFiles(reference.referenceIds).subscribe(
				() => {
					reference.referenceIds = [];
				});
		});
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
