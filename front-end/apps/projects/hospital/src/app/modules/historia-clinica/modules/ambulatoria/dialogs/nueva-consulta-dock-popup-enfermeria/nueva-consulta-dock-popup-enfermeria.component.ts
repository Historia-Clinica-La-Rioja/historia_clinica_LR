import { PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS } from './../../../../../core/utils/pattern.utils';
import { Component, OnInit, Inject, ViewChild, ElementRef } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ClinicalTermDto, ClinicalSpecialtyDto, NursingConsultationDto, HCEPersonalHistoryDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { NursingPatientConsultationService } from '@api-rest/services/nursing-patient-consultation.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';
import { newMoment } from '@core/utils/moment.utils';
import { hasMaxTwoDecimalDigits, PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { DATOS_ANTROPOMETRICOS, FACTORES_DE_RIESGO } from '@historia-clinica/constants/validation-constants';
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
	formEvolucion: FormGroup;
	errores: string[] = [];
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
	@ViewChild('errorsView') errorsView: ElementRef;


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
	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.problemasService = new ProblemasService(formBuilder, this.snomedService, this.snackBarService);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.datosAntropometricosNuevaConsultaService =
			new DatosAntropometricosNuevaConsultaService(formBuilder, this.hceGeneralStateService, this.data.idPaciente, this.internacionMasterDataService);
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(formBuilder);
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
			evolucion: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			clinicalSpecialty: [],
			clinicalProblem: []
		});

		this.problemasService.error$.subscribe(problemasError => {
			this.errores[1] = problemasError;
		});
		this.datosAntropometricosNuevaConsultaService.heightError$.subscribe(tallaError => {
			this.errores[2] = tallaError;
		});
		this.datosAntropometricosNuevaConsultaService.weightError$.subscribe(pesoError => {
			this.errores[3] = pesoError;
		});
		this.datosAntropometricosNuevaConsultaService.headCircumferenceError$.subscribe(headCircumferenceError => {
			this.errores[11] = headCircumferenceError;
		});
		this.factoresDeRiesgoFormService.heartRateError$.subscribe(frecuenciaCardiacaError => {
			this.errores[4] = frecuenciaCardiacaError;
		});
		this.factoresDeRiesgoFormService.respiratoryRateError$.subscribe(frecuenciaRespiratoriaError => {
			this.errores[5] = frecuenciaRespiratoriaError;
		});
		this.factoresDeRiesgoFormService.temperatureError$.subscribe(temperaturaCorporalError => {
			this.errores[6] = temperaturaCorporalError;
		});
		this.factoresDeRiesgoFormService.bloodOxygenSaturationError$.subscribe(saturacionOxigenoError => {
			this.errores[7] = saturacionOxigenoError;
		});
		this.factoresDeRiesgoFormService.systolicBloodPressureError$.subscribe(presionSistolicaError => {
			this.errores[8] = presionSistolicaError;
		});
		this.factoresDeRiesgoFormService.diastolicBloodPressureError$.subscribe(presionDiastolicaError => {
			this.errores[9] = presionDiastolicaError;
		});
		this.factoresDeRiesgoFormService.bloodGlucoseError$.subscribe(bloodGlucoseError => {
			this.errores[12] = bloodGlucoseError;
		});
		this.factoresDeRiesgoFormService.glycosylatedHemoglobinError$.subscribe(glycosylatedHemoglobinError => {
			this.errores[13] = glycosylatedHemoglobinError;
		});
		this.factoresDeRiesgoFormService.cardiovascularRiskError$.subscribe(cardiovascularRiskError => {
			this.errores[14] = cardiovascularRiskError;
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
		this.addErrorMessage(nursingConsultationDto);
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
				setTimeout(() => {
					this.errorsView.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
				}, 500);
			}
		}
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
		return (this.errores.find(elem =>
			elem !== undefined
		) === undefined);
	}

	private addErrorMessage(consulta: NursingConsultationDto): void {
		let value = consulta.anthropometricData?.height?.value;
		if (value && !PATTERN_INTEGER_NUMBER.test(value)) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_NOT_INTEGER');
		}
		else if (parseInt(value, 10) < DATOS_ANTROPOMETRICOS.MIN.height) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_MIN');
		}
		else if (parseInt(value, 10) > DATOS_ANTROPOMETRICOS.MAX.height) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_MAX');
		}

		value = consulta.anthropometricData?.weight?.value;
		if (parseInt(value, 10) < DATOS_ANTROPOMETRICOS.MIN.weight) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_MIN');
		}
		else if (parseInt(value, 10) > DATOS_ANTROPOMETRICOS.MAX.weight) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_MAX');
		}

		value = consulta.anthropometricData?.headCircumference?.value;
		if (value && !PATTERN_NUMBER_WITH_MAX_2_DECIMAL_DIGITS.test(value)) {
			this.datosAntropometricosNuevaConsultaService.setHeadCircumferenceError('ambulatoria.paciente.nueva-consulta.errors.HEAD_CIRCUNFERENCE_NOT_VALID');
		}
		else if ((parseInt(value, 10) < DATOS_ANTROPOMETRICOS.MIN.headCircumference) || (parseInt(value, 10) > DATOS_ANTROPOMETRICOS.MAX.headCircumference)) {
			this.datosAntropometricosNuevaConsultaService.setHeadCircumferenceError('ambulatoria.paciente.nueva-consulta.errors.HEAD_CIRCUNFERENCE_RANGE');
		}

		value = consulta.riskFactors.heartRate?.value;
		if (parseInt(value, 10) < FACTORES_DE_RIESGO.MIN.heartRate) {
			this.factoresDeRiesgoFormService.setHeartRateError('ambulatoria.paciente.nueva-consulta.errors.FRECUENCIA_CARDIACA_MIN');
		}

		value = consulta.riskFactors.respiratoryRate?.value;
		if (parseInt(value, 10) < FACTORES_DE_RIESGO.MIN.respiratoryRate) {
			this.factoresDeRiesgoFormService.setRespiratoryRateError('ambulatoria.paciente.nueva-consulta.errors.FRECUENCIA_RESPIRATORIA_MIN');
		}

		value = consulta.riskFactors.temperature?.value;
		if (parseInt(value, 10) < FACTORES_DE_RIESGO.MIN.temperature) {
			this.factoresDeRiesgoFormService.setTemperatureError('ambulatoria.paciente.nueva-consulta.errors.TEMPERATURA_CORPORAL_MIN');
		}

		value = consulta.riskFactors.bloodOxygenSaturation?.value;
		if (parseInt(value, 10) < FACTORES_DE_RIESGO.MIN.bloodOxygenSaturation) {
			this.factoresDeRiesgoFormService.setBloodOxygenSaturationError('ambulatoria.paciente.nueva-consulta.errors.SATURACION_OXIGENO_MIN');
		}

		value = consulta.riskFactors.diastolicBloodPressure?.value;
		if (parseInt(value, 10) < FACTORES_DE_RIESGO.MIN.diastolicBloodPressure) {
			this.factoresDeRiesgoFormService.setDiastolicBloodPressureError('ambulatoria.paciente.nueva-consulta.errors.TENSION_DIASTOLICA_MIN');
		}

		value = consulta.riskFactors?.systolicBloodPressure?.value;
		if (parseInt(value, 10) < FACTORES_DE_RIESGO.MIN.systolicBloodPressure) {
			this.factoresDeRiesgoFormService.setSystolicBloodPressureError('ambulatoria.paciente.nueva-consulta.errors.TENSION_SISTOLICA_MIN');
		}

		value = consulta.riskFactors?.bloodGlucose?.value;
		if ((parseInt(value, 10) < FACTORES_DE_RIESGO.MIN.bloodGlucose) || (parseInt(value, 10) > FACTORES_DE_RIESGO.MAX.bloodGlucose)) {
			this.factoresDeRiesgoFormService.setBloodGlucoseError('ambulatoria.paciente.nueva-consulta.errors.BLOOD_GLUCOSE_RANGE');
		}

		value = consulta.riskFactors?.glycosylatedHemoglobin?.value;
		if ((parseFloat(value) < FACTORES_DE_RIESGO.MIN.glycosylatedHemoglobin) || (parseFloat(value) > FACTORES_DE_RIESGO.MAX.glycosylatedHemoglobin)) {
			this.factoresDeRiesgoFormService.setGlycosylatedHemoglobinError('ambulatoria.paciente.nueva-consulta.errors.GLYCOSYLATED_HEMOGLOBIN_RANGE');
		}
		else if (value && !hasMaxTwoDecimalDigits(value)) {
			this.factoresDeRiesgoFormService.setGlycosylatedHemoglobinError('ambulatoria.paciente.nueva-consulta.errors.MAX_TWO_DECIMAL_DIGITS');
		}

		value = consulta.riskFactors?.cardiovascularRisk?.value;
		if ((parseInt(value, 10) < FACTORES_DE_RIESGO.MIN.cardiovascularRisk) || (parseInt(value, 10) > FACTORES_DE_RIESGO.MAX.cardiovascularRisk)) {
			this.factoresDeRiesgoFormService.setCardiovascularRiskError('ambulatoria.paciente.nueva-consulta.errors.CARDIOVASCULAR_RISK_RANGE');
		}

		this.errores[10] =
			hasError(this.formEvolucion, 'maxlength', 'evolucion') ?
				'ambulatoria.paciente.nueva-consulta.errors.MAX_LENGTH_NOTA'
				: undefined;
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
