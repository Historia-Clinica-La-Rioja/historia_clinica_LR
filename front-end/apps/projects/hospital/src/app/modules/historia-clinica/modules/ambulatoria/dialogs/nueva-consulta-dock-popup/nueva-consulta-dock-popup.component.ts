import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, ClinicalSpecialtyDto, CreateOutpatientDto, HealthConditionNewConsultationDto, OutpatientProblemDto, SnomedDto, SnomedECL, SnvsToReportDto } from '@api-rest/api-model.d';
import { HealthConditionService } from '@api-rest/services/healthcondition.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { hasError, scrollIntoError } from '@core/utils/form.utils';
import { DateFormat, dateToMomentTimeZone, momentFormat, newMoment } from '@core/utils/moment.utils';
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService, SEVERITY_CODES } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { TranslateService } from '@ngx-translate/core';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SuggestedFieldsPopupComponent } from '../../../../../presentation/components/suggested-fields-popup/suggested-fields-popup.component';
import { FactoresDeRiesgoFormService } from '../../../../services/factores-de-riesgo-form.service';
import { Problema } from '../../../../services/problemas.service';
import { ProcedimientosService } from '../../../../services/procedimientos.service';
import { SnomedService } from '../../../../services/snomed.service';
import { Alergia, AlergiasNuevaConsultaService } from '../../services/alergias-nueva-consulta.service';
import { AmbulatoryConsultationReferenceService, ReferenceInformation } from '../../services/ambulatory-consultation-reference.service';
import {
	AntecedenteFamiliar,
	AntecedentesFamiliaresNuevaConsultaService
} from '../../services/antecedentes-familiares-nueva-consulta.service';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';
import { Medicacion, MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { MotivoNuevaConsultaService } from '../../services/motivo-nueva-consulta.service';
import { NewConsultationSuggestedFieldsService } from '../../services/new-consultation-suggested-fields.service';

import { DatePipe } from '@angular/common';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { SnvsMasterDataService } from "@api-rest/services/snvs-masterdata.service";
import { NewConsultationAddProblemFormComponent } from '@historia-clinica/dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { NewConsultationAddReasonFormComponent } from '@historia-clinica/dialogs/new-consultation-add-reason-form/new-consultation-add-reason-form.component';
import { NewConsultationAllergyFormComponent } from '@historia-clinica/dialogs/new-consultation-allergy-form/new-consultation-allergy-form.component';
import { NewConsultationMedicationFormComponent } from '@historia-clinica/dialogs/new-consultation-medication-form/new-consultation-medication-form.component';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { forkJoin, Observable, of } from 'rxjs';
import { NewConsultationFamilyHistoryFormComponent } from '../new-consultation-family-history-form/new-consultation-family-history-form.component';
import { PreviousDataComponent } from '../previous-data/previous-data.component';
import { HCEPersonalHistory } from '../reference/reference.component';
import { SnvsReportsResultComponent } from '../snvs-reports-result/snvs-reports-result.component';
import { EpisodeData } from '@historia-clinica/components/episode-data/episode-data.component';
import { HierarchicalUnitService } from '@historia-clinica/services/hierarchical-unit.service';
import { ConfirmarPrescripcionComponent } from '../ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { PrescriptionTypes } from '../../services/prescripciones.service';
import { NewConsultationPersonalHistoriesService, PersonalHistory } from '../../services/new-consultation-personal-histories.service';
import { NewConsultationPersonalHistoryFormComponent } from '../new-consultation-personal-history-form/new-consultation-personal-history-form.component';
import { BoxMessageInformation } from '@historia-clinica/components/box-message/box-message.component';
import * as _ from 'lodash';
import { ConceptsList } from 'projects/hospital/src/app/modules/hsi-components/concepts-list/concepts-list.component';

const TIME_OUT = 5000;

@Component({
	selector: 'app-nueva-consulta-dock-popup',
	templateUrl: './nueva-consulta-dock-popup.component.html',
	styleUrls: ['./nueva-consulta-dock-popup.component.scss']
})
export class NuevaConsultaDockPopupComponent implements OnInit {
	showWarningViolenceSituation = false;
	dataName: string[];
	disableConfirmButton = false;
	formEvolucion: UntypedFormGroup;
	motivoNuevaConsultaService: MotivoNuevaConsultaService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
	procedimientoNuevaConsultaService: ProcedimientosService;
	datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	antecedentesFamiliaresNuevaConsultaService: AntecedentesFamiliaresNuevaConsultaService;
	personalHistoriesNewConsultationService: NewConsultationPersonalHistoriesService;
	alergiasNuevaConsultaService: AlergiasNuevaConsultaService;
	apiErrors: string[] = [];
	public today = newMoment();
	fixedSpecialty = true;
	defaultSpecialty: ClinicalSpecialtyDto;
	specialties: ClinicalSpecialtyDto[];
	public hasError = hasError;
	momentFormat = momentFormat;
	DateFormat = DateFormat;
	severityTypes: any[];
	criticalityTypes: any[];
	public reportFFIsOn: boolean;
	searchConceptsLocallyFFIsOn = false;
	ambulatoryConsultationReferenceService: AmbulatoryConsultationReferenceService;
	readonly SEVERITY_CODES = SEVERITY_CODES;
	collapsedAnthropometricDataSection = false;
	collapsedRiskFactorsSection = false;
	collapsedReferenceRequest = true;
	isEnablePopUpConfirm: boolean = true;
	boxMessageInfo: BoxMessageInformation;

	snowstormServiceNotAvailable = false;
	snowstormServiceErrorMessage: string;
	episodeData: EpisodeData;
	touchedConfirm = false;
	referenceSituationViolence = null;
	allergyContent: ConceptsList = {
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
	isAllergyNoRefer: boolean = true;

	@ViewChild('apiErrorsView') apiErrorsView: ElementRef;
	@ViewChild('referenceRequest') sectionReference: ElementRef;

	constructor(
		@Inject(OVERLAY_DATA) public data: NuevaConsultaData,
		private readonly hierarchicalUnitFormService: HierarchicalUnitService,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly outpatientConsultationService: OutpatientConsultationService,
		private readonly healthConditionService: HealthConditionService,
		private readonly dialog: MatDialog,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly translateService: TranslateService,
		private readonly snvsMasterDataService: SnvsMasterDataService,
		private readonly datePipe: DatePipe,
		private readonly referenceFileService: ReferenceFileService,
		private readonly el: ElementRef,
		private readonly snowstormService: SnowstormService,
	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.ambulatoryConsultationProblemsService = new AmbulatoryConsultationProblemsService(formBuilder, this.snomedService, this.snackBarService, this.snvsMasterDataService, this.dialog);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.datosAntropometricosNuevaConsultaService =
			new DatosAntropometricosNuevaConsultaService(formBuilder, this.hceGeneralStateService, this.data.idPaciente, this.internacionMasterDataService, this.translateService, this.datePipe);
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(formBuilder, translateService, this.hceGeneralStateService, this.data.idPaciente, this.datePipe);
		this.personalHistoriesNewConsultationService = new NewConsultationPersonalHistoriesService(this.snomedService, this.snackBarService);
		this.antecedentesFamiliaresNuevaConsultaService = new AntecedentesFamiliaresNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService, this.internacionMasterDataService);
		this.ambulatoryConsultationReferenceService = new AmbulatoryConsultationReferenceService(this.dialog, this.data, this.ambulatoryConsultationProblemsService);
		this.featureFlagService.isActive(AppFeature.HABILITAR_GUARDADO_CON_CONFIRMACION_CONSULTA_AMBULATORIA).subscribe(isEnabled => this.isEnablePopUpConfirm = isEnabled);
	}

	ngOnInit(): void {

		if (this.data.idProblema) {
			this.healthConditionService.getHealthCondition(this.data.idProblema).subscribe(p => {
				this.ambulatoryConsultationProblemsService.addProblemToList(this.buildProblema(p));
			});
		}

		this.formEvolucion = this.formBuilder.group({
			evolucion: [],
		});

		if (this.data.problem) {
			this.snowstormService.getSNOMEDConcepts({ term: this.data.problem, ecl: SnomedECL.DIAGNOSIS }).subscribe(
				result => {
					const p = result.items.find(p => p.pt === this.data.problem);
					const snomed: SnomedDto = {
						pt: p.pt,
						sctid: p.sctid
					}
					const problem: Problema = {
						snomed: snomed
					}
					this.ambulatoryConsultationProblemsService.addProblemToList(problem);
				},
				error => {
					this.snackBarService.showError('historia-clinica.snowstorm.CONCEPTS_COULD_NOT_BE_OBTAINED');
					this.snowstormServiceErrorMessage = error.text ? error.text : error.message;
					this.snowstormServiceNotAvailable = true;
				}
			);
		}

		if (this.data.evolution)
			this.formEvolucion.controls.evolucion.setValue(this.data.evolution);

		this.datosAntropometricosNuevaConsultaService.setPreviousAnthropometricData();

		this.factoresDeRiesgoFormService.setPreviousRiskFactorsData();

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.ambulatoryConsultationProblemsService.setSeverityTypes(healthConditionSeverities);
		});

		this.internacionMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities => {
			this.criticalityTypes = allergyCriticalities;
			this.alergiasNuevaConsultaService.setCriticalityTypes(allergyCriticalities);
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_REPORTE_EPIDEMIOLOGICO).subscribe(isOn => {
			this.reportFFIsOn = isOn;
			this.ambulatoryConsultationProblemsService.setReportFF(isOn);
		});
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
			this.ambulatoryConsultationProblemsService.setSearchConceptsLocallyFF(isOn);

		});
	}

	setBoxMessageInfo() {
		this.boxMessageInfo = {
			title: 'historia-clinica.include-previous-data-question.TITLE',
			question: 'historia-clinica.include-previous-data-question.violence-situations.QUESTION',
			message: this.translateService.instant('historia-clinica.include-previous-data-question.violence-situations.DESCRIPTION',
				{ problem: this.dataName }),
			viewError: this.touchedConfirm,
			addButtonLabel: 'historia-clinica.include-previous-data-question.violence-situations.ADD',
			discardButtonLabel: 'historia-clinica.include-previous-data-question.violence-situations.DISCARD',
			showButtons: true
		}
	}


	previousDataIsConfirmed(): Observable<boolean> {
		if ((this.factoresDeRiesgoFormService.getShowPreloadedRiskFactorsData()) ||
			(this.datosAntropometricosNuevaConsultaService.getShowPreloadedAnthropometricData())) {
			const dialogRef = this.dialog.open(PreviousDataComponent,
				{
					data: {
						factoresDeRiesgoFormService: this.factoresDeRiesgoFormService,
						datosAntropometricosNuevaConsultaService: this.datosAntropometricosNuevaConsultaService
					},
					disableClose: true,
					autoFocus: false
				});
			return dialogRef.afterClosed();
		}
		else return of(true);
	}

	previousAlertReference() {
		const problems = this.ambulatoryConsultationProblemsService.getProblemas().map(p => ({
			pt: p.snomed.pt,
			sctid: p.snomed.sctid
		}));
		if(!this.touchedConfirm && this.showWarningViolenceSituation){
			this.touchedConfirm = true;
		}
			this.snowstormService.areConceptsECLRelated(SnomedECL.VIOLENCE_PROBLEM, problems).subscribe(res => {
				if (res.length) {
					this.dataName = res.map(p=> ` "${p.pt}"`);
					this.showWarningViolenceSituation = true;
					this.collapsedReferenceRequest = false;
					this.setBoxMessageInfo();
					setTimeout(() => {
						this.sectionReference.nativeElement.scrollIntoView({ behavior: 'smooth' });
					}, 200);
				} else {
					this.showWarningViolenceSituation = false;
					this.touchedConfirm = false;
					this.save();
				}
			});
	}

	confirmForm(){
		if(this.referenceSituationViolence === null){
			this.previousAlertReference();
		}else if(this.referenceSituationViolence || !this.referenceSituationViolence){
			this.save();
		}
	}

	save(): void {
		this.previousDataIsConfirmed().subscribe((answerPreviousData: boolean) => {

			const nuevaConsulta: CreateOutpatientDto = this.buildCreateOutpatientDto();
			const fieldsService = new NewConsultationSuggestedFieldsService(nuevaConsulta, this.translateService);

			this.apiErrors = [];

			if (answerPreviousData) {
				if ((this.isValidConsultation()) && (this.formEvolucion.valid)) {
					if (!fieldsService.nonCompletedFields.length) {
						this.uploadReferencesFileAndCreateConsultation(nuevaConsulta);
					}
					else {
						(this.isEnablePopUpConfirm)
							? this.openDialog(fieldsService.nonCompletedFields, fieldsService.presentFields, nuevaConsulta)
							: this.uploadReferencesFileAndCreateConsultation(nuevaConsulta)
					}
				} else {
					this.disableConfirmButton = false;
					if (!this.isValidConsultation()) {
						if (this.datosAntropometricosNuevaConsultaService.getForm().invalid) {
							this.collapsedAnthropometricDataSection = false;
							setTimeout(() => {
								scrollIntoError(this.datosAntropometricosNuevaConsultaService.getForm(), this.el)
							}, 300);
						}
						else if (this.factoresDeRiesgoFormService.getForm().invalid) {
							this.collapsedRiskFactorsSection = false;
							setTimeout(() => {
								scrollIntoError(this.factoresDeRiesgoFormService.getForm(), this.el)
							}, 300);
						}
						if (this.hierarchicalUnitFormService.isValidForm()) {
							setTimeout(() => {
								scrollIntoError(this.hierarchicalUnitFormService.getForm(), this.el)
							}, 300);
						}
					}

				}
			}

		});
	}

	openReferenceDialog(event) {
		if (event) {
			this.ambulatoryConsultationReferenceService.openReferenceDialog();
			this.referenceSituationViolence = true;
		} else {
			this.referenceSituationViolence = false;
		}
		this.showWarningViolenceSituation = false;
	}

	private openDialog(nonCompletedFields: string[], presentFields: string[], nuevaConsulta: CreateOutpatientDto): void {
		const dialogRef = this.dialog.open(SuggestedFieldsPopupComponent, {
			data: {
				nonCompletedFields,
				presentFields
			}
		});
		dialogRef.afterClosed().subscribe(confirmado => {
			if (confirmado) {
				this.uploadReferencesFileAndCreateConsultation(nuevaConsulta);
			}
		});
	}

	private uploadReferencesFileAndCreateConsultation(nuevaConsulta: CreateOutpatientDto) {
		let references: ReferenceInformation[] = this.ambulatoryConsultationReferenceService.getReferences();
		if (!references.length) {
			this.goToCreateConsultation(nuevaConsulta);
			return;
		}

		const referencesToUpdate: Observable<number[]>[] = [];

		references.forEach(reference => {
			if (reference.referenceFiles.length > 0) {
				const obs = this.referenceFileService.uploadReferenceFiles(this.data.idPaciente, reference.referenceFiles);
				referencesToUpdate.push(obs);
			}
		});

		if (referencesToUpdate.length) {
			forkJoin(referencesToUpdate).subscribe((referenceFileIds: number[][]) => {
				let hasError = false;
				referenceFileIds.forEach((fileIds, index) => {
					const reference = references[index];
					const filesAmount = reference.referenceFiles.length;
					for (let i = 0; i < filesAmount; i++) {
						if (fileIds[i] === undefined || fileIds[i] === null) {
							hasError = true;
							break;
						}
						this.ambulatoryConsultationReferenceService.addFileIdAt(index, fileIds[i]);
					}
				});
				if (!hasError) {
					this.goToCreateConsultation(nuevaConsulta);
				} else {
					this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR_TO_UPLOAD_FILES');
					this.errorToUploadReferenceFiles();
					hasError = false;
				}
			}, _ => {
				this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR_TO_UPLOAD_FILES');
				this.errorToUploadReferenceFiles();
			});
		} else {
			this.goToCreateConsultation(nuevaConsulta);
		}
	}

	private createConsultation(nuevaConsulta: CreateOutpatientDto) {
		const problemsToUpdate = (!nuevaConsulta.references.length) ? nuevaConsulta.problems : this.problemsToUpdate(nuevaConsulta);

		if (nuevaConsulta.references.length) {
			nuevaConsulta.problems = problemsToUpdate;
		}

		this.outpatientConsultationService.createOutpatientConsultation(nuevaConsulta, this.data.idPaciente).subscribe(
			res => {
				res.orderIds.forEach((orderId) => {
					this.openNewEmergencyCareStudyConfirmationDialog([orderId]);
				});
				this.snackBarService.showSuccess('ambulatoria.paciente.nueva-consulta.messages.SUCCESS', { duration: TIME_OUT });
				this.dockPopupRef.close(mapToFieldsToUpdate(nuevaConsulta));
				if (this.thereAreProblemsToSnvsReport()) {
					const toReport = this.createSnvsToReportList();
					setTimeout(() => {
						this.dialog.open(SnvsReportsResultComponent, {
							disableClose: true,
							autoFocus: false,
							data: {
								toReportList: toReport,
								patientId: this.data.idPaciente,
								snvsEvent: this.ambulatoryConsultationProblemsService.getSnvsEventsInformation()
							}
						});
					}, TIME_OUT);
				}
			},
			response => {
				this.disableConfirmButton = false;
				if (response.errors)
					response.errors.forEach(val => {
						this.apiErrors.push(val);
					});
				this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR');
				const filesToDelete = nuevaConsulta.references.filter(reference => reference.fileIds.length > 0);
				if (filesToDelete.length) {
					this.errorToUploadReferenceFiles();
				}
			},
			() => {
				if (this.apiErrors?.length > 0) {
					setTimeout(() => {
						this.apiErrorsView.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
					}, 500);
				}
			}
		);

		function mapToFieldsToUpdate(nuevaConsultaDto: CreateOutpatientDto) {
			return {
				allergies: !!nuevaConsultaDto.allergies?.content.length,
				personalHistories: !!nuevaConsultaDto.personalHistories?.length,
				familyHistories: !!nuevaConsultaDto.familyHistories?.length,
				riskFactors: !!nuevaConsultaDto.riskFactors,
				medications: !!nuevaConsultaDto.medications?.length,
				anthropometricData: !!nuevaConsultaDto.anthropometricData,
				problems: !!problemsToUpdate.length,
			};
		}
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
					patientId: this.data.idPaciente,
					prescriptionRequest: order,
				},
				width: '35%',
			});
	}

	public isValidConsultation(): boolean {
		if (this.datosAntropometricosNuevaConsultaService.getForm().invalid)
			return false;
		if (this.factoresDeRiesgoFormService.getForm().invalid)
			return false;
		if (this.hierarchicalUnitFormService.isValidForm()) {
			return false;
		}
		return true;
	}

	private buildProblema(p: HealthConditionNewConsultationDto) {
		const problema: Problema = {
			snomed: p.snomed,
			codigoSeveridad: p.severity,
			cronico: p.isChronic,
			fechaInicio: p.startDate ? dateToMomentTimeZone(p.startDate) : newMoment(),
			fechaFin: p.inactivationDate ? dateToMomentTimeZone(p.inactivationDate) : undefined
		};
		return problema;
	}

	private buildCreateOutpatientDto(): CreateOutpatientDto {
		return {
			allergies: {
				isReferred: (this.isAllergyNoRefer && this.alergiasNuevaConsultaService.getAlergias().length === 0) ? null: this.isAllergyNoRefer,
				content: this.alergiasNuevaConsultaService.getAlergias().map((alergia: Alergia) => {
					return {
						categoryId: null,
						snomed: alergia.snomed,
						startDate: null,
						statusId: null,
						verificationId: null,
						criticalityId: alergia.criticalityId,
					};
				}),
			},
			anthropometricData: this.datosAntropometricosNuevaConsultaService.getDatosAntropometricos(),
			evolutionNote: this.formEvolucion.value?.evolucion,
			familyHistories: this.antecedentesFamiliaresNuevaConsultaService.getAntecedentes().map((antecedente: AntecedenteFamiliar) => {
				return {
					snomed: antecedente.snomed,
					startDate: antecedente.fecha ? momentFormat(antecedente.fecha, DateFormat.API_DATE) : undefined
				};
			}),
			medications: this.medicacionesNuevaConsultaService.getMedicaciones().map((medicacion: Medicacion) => {
				return {
					note: medicacion.observaciones,
					snomed: medicacion.snomed,
					suspended: medicacion.suspendido,
				};
			}
			),
			patientMedicalCoverageId: this.episodeData.medicalCoverageId,
			personalHistories: this.personalHistoriesNewConsultationService.getPersonalHistories().map((personalHistory: PersonalHistory) => {
				return {
					inactivationDate: personalHistory.endDate ? momentFormat(personalHistory.endDate, DateFormat.API_DATE) : null,
					note: personalHistory.observations,
					snomed: personalHistory.snomed,
					startDate: momentFormat(personalHistory.startDate, DateFormat.API_DATE),
					typeId: personalHistory.type.id,
				}
			}),
			problems: this.ambulatoryConsultationProblemsService.getProblemas().map(
				(problema: Problema) => {
					return {
						severity: problema.codigoSeveridad,
						chronic: problema.cronico,
						endDate: problema.fechaFin ? momentFormat(problema.fechaFin, DateFormat.API_DATE) : undefined,
						snomed: problema.snomed,
						startDate: problema.fechaInicio ? momentFormat(problema.fechaInicio, DateFormat.API_DATE) : undefined
					};
				}
			),
			procedures: this.procedimientoNuevaConsultaService.getProcedimientos(),
			reasons: this.motivoNuevaConsultaService.getMotivosConsulta(),
			riskFactors: this.factoresDeRiesgoFormService.getFactoresDeRiesgo(),
			clinicalSpecialtyId: this.episodeData.clinicalSpecialtyId,
			references: this.ambulatoryConsultationReferenceService.getOutpatientReferences(),
			hierarchicalUnitId: this.episodeData.hierarchicalUnitId,
			involvedHealthcareProfessionalIds: this.episodeData.involvedHealthcareProfessionalIds,
		};
	}

	setDefaultSpecialty() {
		this.defaultSpecialty = this.formEvolucion.controls.clinicalSpecialty.value;
	}


	errorToUploadReferenceFiles() {
		const filesToDelete = this.ambulatoryConsultationReferenceService.getReferenceFilesIds();
		this.referenceFileService.deleteReferenceFiles(filesToDelete);
		this.ambulatoryConsultationReferenceService.deleteReferenceFilesIds();
	}

	goToCreateConsultation(nuevaConsulta: CreateOutpatientDto) {
		nuevaConsulta.references = this.ambulatoryConsultationReferenceService.getOutpatientReferences();
		this.createConsultation(nuevaConsulta);
		this.disableConfirmButton = true;
	}

	private thereAreProblemsToSnvsReport(): boolean {
		const problems = this.ambulatoryConsultationProblemsService.getProblemas();
		const firstProblemToReport = problems.find((problem: AmbulatoryConsultationProblem) =>
			(problem.isReportable && problem.epidemiologicalManualClassifications?.length)
		);
		if (firstProblemToReport)
			return true;
		return false;
	}

	private createSnvsToReportList(): SnvsToReportDto[] {
		const reportableProblems = this.ambulatoryConsultationProblemsService.getProblemas().filter((problem: AmbulatoryConsultationProblem) =>
			(problem.isReportable && problem.epidemiologicalManualClassifications?.length)
		);

		const result: SnvsToReportDto[] = [];

		reportableProblems.forEach((problem: AmbulatoryConsultationProblem) => {
			problem.snvsReports.forEach(report => {
				const toReport: SnvsToReportDto = {
					eventId: report.eventId,
					groupEventId: report.groupEventId,
					manualClassificationId: report.manualClassificationId,
					problem: {
						pt: problem.snomed.pt,
						sctid: problem.snomed.sctid
					}
				};
				result.push(toReport);
			});
		});

		return result;
	}
	private mapToOutpatientProblemDto(problem: HCEPersonalHistory): OutpatientProblemDto {
		return {
			chronic: problem.chronic,
			severity: problem.HCEHealthConditionDto.severity,
			snomed: problem.HCEHealthConditionDto.snomed,
			startDate: problem.HCEHealthConditionDto.startDate,
			statusId: problem.HCEHealthConditionDto.statusId,
		}
	}

	private problemsToUpdate(nuevaConsultaDto: CreateOutpatientDto): OutpatientProblemDto[] {
		const outpatientProblemDto: OutpatientProblemDto[] = [];

		nuevaConsultaDto.problems?.forEach(problem => outpatientProblemDto.push(problem));

		const references: ReferenceInformation[] = this.ambulatoryConsultationReferenceService.getReferences();

		references.forEach(
			(reference: ReferenceInformation) => {
				reference.referenceProblems.forEach(referenceProblem => {
					const outProblemDto = this.mapToOutpatientProblemDto(referenceProblem);
					const existProblem = outpatientProblemDto.find(problem => problem.snomed.sctid === outProblemDto.snomed.sctid);
					if (!existProblem) {
						outpatientProblemDto.push(outProblemDto);
					}
				});
			}
		);

		return outpatientProblemDto;
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

	addProblem(): void {
		this.dialog.open(NewConsultationAddProblemFormComponent, {
			data: {
				ambulatoryConsultationProblemsService: this.ambulatoryConsultationProblemsService,
				severityTypes: this.severityTypes,
				epidemiologicalReportFF: this.reportFFIsOn,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	addReason(): void {
		this.dialog.open(NewConsultationAddReasonFormComponent, {
			data: {
				reasonService: this.motivoNuevaConsultaService,
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
			width: '30%',
			disableClose: true,
		});
	}

	addFamilyHistory(): void {
		this.dialog.open(NewConsultationFamilyHistoryFormComponent, {
			data: {
				familyHistoryService: this.antecedentesFamiliaresNuevaConsultaService,
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
				medicationService: this.medicacionesNuevaConsultaService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	addProcedure(): void {
		this.dialog.open(NewConsultationProcedureFormComponent, {
			data: {
				procedureService: this.procedimientoNuevaConsultaService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	addAllergy(): void {
		this.dialog.open(NewConsultationAllergyFormComponent, {
			data: {
				allergyService: this.alergiasNuevaConsultaService,
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

}

export interface NuevaConsultaData {
	idPaciente: number;
	idProblema?: number;
	problem?: string;
	evolution?: string;
}
