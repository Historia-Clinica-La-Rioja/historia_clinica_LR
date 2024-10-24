import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, NursingProcedureDto, ProcedureDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { ClinicalSpecialtyDto, ClinicalTermDto, HCEHealthConditionDto, NursingConsultationDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { NursingPatientConsultationService } from '@api-rest/services/nursing-patient-consultation.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { hasError, scrollIntoError } from '@core/utils/form.utils';
import { ProblemasService } from '@historia-clinica/services/problemas.service';
import { Procedimiento, ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TranslateService } from '@ngx-translate/core';
import { SuggestedFieldsPopupComponent } from '@presentation/components/suggested-fields-popup/suggested-fields-popup.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { FactoresDeRiesgoFormService } from '../../../../services/factores-de-riesgo-form.service';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';
import { MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { MotivoNuevaConsultaService } from '../../services/motivo-nueva-consulta.service';
import { NewNurseConsultationSuggestedFieldsService } from '../../services/new-nurse-consultation-suggested-fields.service';
import { NuevaConsultaData } from '../nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { EpisodeData } from '@historia-clinica/components/episode-data/episode-data.component';
import { HierarchicalUnitService } from '@historia-clinica/services/hierarchical-unit.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { ButtonType } from '@presentation/components/button/button.component';
import { finalize } from 'rxjs';
import { CreateOrderService } from '@historia-clinica/services/create-order.service';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { SnvsMasterDataService } from '@api-rest/services/snvs-masterdata.service';
import { ConfirmarPrescripcionComponent } from '../ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { PrescriptionTypes } from '../../services/prescripciones.service';
import { ProcedureTemplatesService } from '@api-rest/services/procedure-templates.service';
import { SearchSnomedConceptComponent } from '../search-snomed-concept/search-snomed-concept.component';
import { DialogWidth } from '@presentation/services/dialog.service';
import { Concept, ConceptDateFormComponent } from '../../modules/internacion/dialogs/concept-date-form/concept-date-form.component';
import { pushIfNotExists } from '@core/utils/array.utils';
import { toApiFormat } from '@api-rest/mapper/date.mapper';

export interface FieldsToUpdate {
	riskFactors: boolean;
	anthropometricData: boolean;
	problem: boolean;
	personalHistories: boolean;
}

const ATENCION_ENFERMERIA_TITLE = 'Atención de enfermería';
const ATENCION_ENFERMERIA_SCTID = '9632001';
@Component({
	selector: 'app-nueva-consulta-dock-popup-enfermeria',
	templateUrl: './nueva-consulta-dock-popup-enfermeria.component.html',
	styleUrls: ['./nueva-consulta-dock-popup-enfermeria.component.scss']
})

export class NuevaConsultaDockPopupEnfermeriaComponent implements OnInit {

	disableConfirmButton = false;
	collapsedAnthropometricDataSection = false;
	collapsedRiskFactorsSection = false;
	formEvolucion: UntypedFormGroup;
	motivoNuevaConsultaService: MotivoNuevaConsultaService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	problemasService: ProblemasService;
	procedimientoNuevaConsultaService: ProcedimientosService;
	datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	readOnlyProblema = false;
	apiErrors: string[] = [];
	fixedSpecialty = true;
	fixedProblem = true;
	defaultSpecialty: ClinicalSpecialtyDto;
	defaultProblem: HCEHealthConditionDto;
	specialties: ClinicalSpecialtyDto[];
	problems: ClinicalTermDto[];
	searchConceptsLocallyFFIsOn = false;
	episodeData: EpisodeData;
	createOrderService: CreateOrderService;
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
	private readonly snvsMasterDataService: SnvsMasterDataService;
	procedures: ProcedureDto[] = [];

	@ViewChild('apiErrorsView') apiErrorsView: ElementRef;


	readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	hasError = hasError;
	severityTypes: any[];
	criticalityTypes: any[];
	healthProblemOptions = [];
	ButtonType = ButtonType;

	constructor(
		@Inject(OVERLAY_DATA) public data: NuevaConsultaData,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snomedService: SnomedService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly nursingPatientConsultationService: NursingPatientConsultationService,
		private readonly snackBarService: SnackBarService,
		private readonly procedureTemplatesService: ProcedureTemplatesService,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly dialog: MatDialog,
		private readonly translateService: TranslateService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly el: ElementRef,
		private readonly hierarchicalUnitFormService: HierarchicalUnitService,
		private readonly dateFormatPipe: DateFormatPipe

	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.problemasService = new ProblemasService(formBuilder, this.snomedService, this.snackBarService);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService, this.dateFormatPipe);
		this.datosAntropometricosNuevaConsultaService =
			new DatosAntropometricosNuevaConsultaService(formBuilder, this.hceGeneralStateService, this.data.idPaciente, this.internacionMasterDataService, this.translateService);
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(formBuilder, translateService);
		this.createOrderService = new CreateOrderService(this.snackBarService, this.procedureTemplatesService);
		this.ambulatoryConsultationProblemsService = new AmbulatoryConsultationProblemsService(formBuilder, this.snomedService, this.snackBarService, this.snvsMasterDataService, this.dialog);
	}

	setProblem() {
		this.hceGeneralStateService.getActiveProblems(this.data.idPaciente).subscribe((activeProblems: HCEHealthConditionDto[]) => {
			const activeProblemsList = activeProblems.map(problem =>
			({
				severity: problem.severity,
				snomed: {
					pt: problem.snomed.pt,
					sctid: problem.snomed.sctid
				},
				startDate: problem.startDate,
				statusId: problem.statusId
			}))

			this.hceGeneralStateService.getChronicConditions(this.data.idPaciente).subscribe((chronicProblems: HCEHealthConditionDto[]) => {
				const chronicProblemsList = chronicProblems.map(problem =>
				({
					severity: problem.severity,
					snomed: {
						pt: problem.snomed.pt,
						sctid: problem.snomed.sctid
					},
					startDate: problem.startDate,
					statusId: problem.statusId
				}));
				this.healthProblemOptions = activeProblemsList.concat(chronicProblemsList);
				this.healthProblemOptions = this.healthProblemOptions.concat({
					severity: null,
					snomed: {
						pt: ATENCION_ENFERMERIA_TITLE,
						sctid: ATENCION_ENFERMERIA_SCTID
					},
					startDate: null,
					statusId: '0'
				});

				this.setProblemFields(this.healthProblemOptions, false);
			});

		});
	}

	setProblemFields(problemArray, fixedProblem) {
		this.problems = problemArray;
		this.fixedProblem = fixedProblem;
		this.defaultProblem = problemArray[problemArray.length - 1];
		this.formEvolucion.get('clinicalProblem').setValue(this.defaultProblem);
	}

	ngOnInit(): void {

		this.setProblem();

		this.formEvolucion = this.formBuilder.group({
			evolucion: [],
			clinicalProblem: []
		});

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.problemasService.setSeverityTypes(healthConditionSeverities);
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => this.searchConceptsLocallyFFIsOn = isOn);
	}

	save(): void {
		const nursingConsultationDto: NursingConsultationDto = this.buildCreateOutpatientDto();
		const fieldsService = new NewNurseConsultationSuggestedFieldsService(nursingConsultationDto, this.translateService);

		this.apiErrors = [];

		if (this.isValidConsultation()) {
			if (!fieldsService.nonCompletedFields.length) {
				this.createConsultation(nursingConsultationDto);
				this.disableConfirmButton = true;
			}
			else {
				this.openDialog(fieldsService.nonCompletedFields, fieldsService.presentFields, nursingConsultationDto);
			}
		} else {
			this.disableConfirmButton = false;
			this.snackBarService.showError('ambulatoria.paciente.new-nursing-consultation.messages.ERROR');
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

	openSearchSnomedConceptComponent(): void {
		const problems = this.ambulatoryConsultationProblemsService.getAllProblemas(this.data.idPaciente, this.hceGeneralStateService);
		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, {
			autoFocus: false,
			width: DialogWidth.MEDIUM,
			disableClose: true,
			data: this.buildProcedureDataToDialog(problems)
		});

		dialogRef.afterClosed().subscribe((snomedConcept: SnomedDto) => this.openConceptDateFormComponent(snomedConcept));
	}

	private openConceptDateFormComponent = (snomedConcept: SnomedDto) => {
		if (!snomedConcept) return;

		const dialogRef = this.dialog.open(ConceptDateFormComponent, {
			width: '35%',
			disableClose: false,
			data: this.buildConceptDateToDialog(snomedConcept)
		});
		dialogRef.afterClosed().subscribe((procedure: Concept) => {
			if (!procedure) return;

			const procedureDto: ProcedureDto = {
				performedDate: procedure?.data,
				snomed: procedure.snomedConcept
			};
			this.addProcedure(procedureDto);
		});
	}

	private addProcedure(procedure: ProcedureDto) {
		this.procedures = pushIfNotExists<ProcedureDto>(this.procedures, procedure, this.compareProcedure);
		this.procedimientoNuevaConsultaService.add({snomed: procedure.snomed, performedDate: this.fromStringToDate(procedure.performedDate)});
	}

	private fromStringToDate(date: string): Date {
		if (!date) return;

		const dateData = date.split("-");
		return new Date(+dateData[0], +dateData[1]-1, +dateData[2]);
	}

	private compareProcedure(concept1: ProcedureDto, concept2: ProcedureDto): boolean {
		return concept1.snomed.sctid === concept2.snomed.sctid
	}

	private buildProcedureDataToDialog = (problems: SnomedDto[]) => {
		const data = {
			patientId: this.data.idPaciente,
			createOrderService: this.createOrderService,
			problems: problems,
			label: 'internaciones.anamnesis.procedimientos.PROCEDIMIENTO',
			title: 'internaciones.anamnesis.procedure.ADD_PROCEDURE',
			eclFilter: SnomedECL.PROCEDURE
		}
		return data;
	}

	private buildConceptDateToDialog = (snomedConcept: SnomedDto) => {
		const data = {
			label: 'internaciones.anamnesis.procedimientos.PROCEDIMIENTO',
			add: 'internaciones.anamnesis.procedure.ADD_PROCEDURE',
			title: 'internaciones.anamnesis.procedimientos.PROCEDIMIENTO',
			snomedConcept
		}
		return data;
	}

	private openDialog(nonCompletedFields: string[], presentFields: string[], nursingConsultationDto: NursingConsultationDto): void {

		const dialogRef = this.dialog.open(SuggestedFieldsPopupComponent, {
			data: {
				nonCompletedFields,
				presentFields
			}
		});
		dialogRef.afterClosed().subscribe(confirmado => {
			if (confirmado) {
				this.createConsultation(nursingConsultationDto);
				this.disableConfirmButton = true;
			}
		});
	}

	private createConsultation(nursingConsultationDto: NursingConsultationDto) {
		this.nursingPatientConsultationService.createNursingPatientConsultation(nursingConsultationDto, this.data.idPaciente)
			.pipe(finalize(() => this.disableConfirmButton = false))
			.subscribe(
				res => {
					res.orderIds.forEach((orderId: number) => {
						this.openNewEmergencyCareStudyConfirmationDialog([orderId]);
					});
					this.snackBarService.showSuccess('ambulatoria.paciente.new-nursing-consultation.messages.SUCCESS');
					this.dockPopupRef.close(mapToFieldsToUpdate(nursingConsultationDto));
				},
				response => {
					this.disableConfirmButton = false;
					if (response.errors)
						response.errors.forEach(val => {
							this.apiErrors.push(val);
						});
					this.snackBarService.showError('ambulatoria.paciente.new-nursing-consultation.messages.ERROR');
				},
				() => {
					if (this.apiErrors?.length > 0) {
						setTimeout(() => {
							this.apiErrorsView.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
						}, 500);
					}
				}
			);

		function mapToFieldsToUpdate(nuevaConsultaEnfermeriaDto: NursingConsultationDto): FieldsToUpdate {
			return {
				riskFactors: !!nuevaConsultaEnfermeriaDto.riskFactors,
				anthropometricData: !!nuevaConsultaEnfermeriaDto.anthropometricData,
				problem: !!nuevaConsultaEnfermeriaDto.problem,
				personalHistories: !!nuevaConsultaEnfermeriaDto.problem,
			};
		}
	}

	public isValidConsultation(): boolean {
		if (this.datosAntropometricosNuevaConsultaService.getForm().invalid)
			return false;
		if (this.factoresDeRiesgoFormService.getForm().invalid)
			return false;
		if (this.hierarchicalUnitFormService.isValidForm())
			return false;
		return true;
	}

	private buildCreateOutpatientDto(): NursingConsultationDto {
		return {
			anthropometricData: this.datosAntropometricosNuevaConsultaService.getDatosAntropometricos(),
			clinicalSpecialtyId: this.episodeData.clinicalSpecialtyId,
			evolutionNote: this.formEvolucion.value?.evolucion,
			problem: this.formEvolucion.value?.clinicalProblem,
			procedures: this.procedimientoNuevaConsultaService.getProcedimientos().map(procedimiento => this.mapProcedimientoToNursingProcedure(procedimiento)),
			riskFactors: this.factoresDeRiesgoFormService.getFactoresDeRiesgo(),
			patientMedicalCoverageId: this.episodeData.medicalCoverageId,
			hierarchicalUnitId: this.episodeData.hierarchicalUnitId,
		}
	}

	setDefaultSpecialty() {
		this.defaultSpecialty = this.formEvolucion.controls.clinicalSpecialty.value;
	}

	editProblema() {
		if (this.problemasService.editProblem()) {
			this.readOnlyProblema = false;
		}
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

	private mapProcedimientoToNursingProcedure = (procedimiento: Procedimiento): NursingProcedureDto => {
		return {
		  	performedDate: procedimiento.performedDate ? toApiFormat(procedimiento.performedDate): null,
		  	snomed: procedimiento.snomed
		};
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
}
