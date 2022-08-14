import { Component, OnInit, Inject, ViewChild, ElementRef } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ClinicalTermDto, ClinicalSpecialtyDto, NursingConsultationDto, HCEPersonalHistoryDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { NursingPatientConsultationService } from '@api-rest/services/nursing-patient-consultation.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError, scrollIntoError } from '@core/utils/form.utils';
import { newMoment } from '@core/utils/moment.utils';
import { ProblemasService } from '@historia-clinica/services/problemas.service';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TranslateService } from '@ngx-translate/core';
import { SuggestedFieldsPopupComponent } from '@presentation/components/suggested-fields-popup/suggested-fields-popup.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';
import { MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { MotivoNuevaConsultaService } from '../../services/motivo-nueva-consulta.service';
import { NewNurseConsultationSuggestedFieldsService } from '../../services/new-nurse-consultation-suggested-fields.service';
import { FactoresDeRiesgoFormService } from '../../../../services/factores-de-riesgo-form.service';
import { NuevaConsultaData } from '../nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';

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
	formEvolucion: FormGroup;
	motivoNuevaConsultaService: MotivoNuevaConsultaService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	problemasService: ProblemasService;
	procedimientoNuevaConsultaService: ProcedimientosService;
	datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	readOnlyProblema = false;
	apiErrors: string[] = [];
	today = newMoment();
	fixedSpecialty = true;
	fixedProblem = true;
	defaultSpecialty: ClinicalSpecialtyDto;
	defaultProblem: HCEPersonalHistoryDto;
	specialties: ClinicalSpecialtyDto[];
	problems: ClinicalTermDto[];
	searchConceptsLocallyFFIsOn = false;
	@ViewChild('apiErrorsView') apiErrorsView: ElementRef;


	readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	hasError = hasError;
	severityTypes: any[];
	criticalityTypes: any[];
	healthProblemOptions = [];


	constructor(
		@Inject(OVERLAY_DATA) public data: NuevaConsultaData,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly nursingPatientConsultationService: NursingPatientConsultationService,
		private readonly snackBarService: SnackBarService,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly dialog: MatDialog,
		private readonly translateService: TranslateService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly el: ElementRef,
	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.problemasService = new ProblemasService(formBuilder, this.snomedService, this.snackBarService);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.datosAntropometricosNuevaConsultaService =
			new DatosAntropometricosNuevaConsultaService(formBuilder, this.hceGeneralStateService, this.data.idPaciente, this.internacionMasterDataService, this.translateService);
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(formBuilder, translateService);
	}

	setProfessionalSpecialties() {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
			this.setSpecialtyFields(specialties, false);
		});
	}


	setSpecialtyFields(specialtyArray, fixedSpecialty) {
		this.specialties = specialtyArray;
		this.fixedSpecialty = fixedSpecialty;
		this.defaultSpecialty = specialtyArray[0];
		this.formEvolucion.get('clinicalSpecialty').setValue(this.defaultSpecialty);
	}

	setProblem() {
		this.hceGeneralStateService.getActiveProblems(this.data.idPaciente).subscribe((activeProblems: HCEPersonalHistoryDto[]) => {
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

			this.hceGeneralStateService.getChronicConditions(this.data.idPaciente).subscribe((chronicProblems: HCEPersonalHistoryDto[]) => {
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

		this.setProfessionalSpecialties();
		this.setProblem();

		this.formEvolucion = this.formBuilder.group({
			evolucion: [],
			clinicalSpecialty: [],
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
			}
		}
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
		this.nursingPatientConsultationService.createNursingPatientConsultation(nursingConsultationDto, this.data.idPaciente).subscribe(
			_ => {
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
		return true;
	}

	private buildCreateOutpatientDto(): NursingConsultationDto {
		return {
			anthropometricData: this.datosAntropometricosNuevaConsultaService.getDatosAntropometricos(),
			clinicalSpecialtyId: this.defaultSpecialty.id,
			evolutionNote: this.formEvolucion.value?.evolucion,
			problem: this.formEvolucion.value?.clinicalProblem,
			procedures: this.procedimientoNuevaConsultaService.getProcedimientos(),
			riskFactors: this.factoresDeRiesgoFormService.getFactoresDeRiesgo(),
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

}
