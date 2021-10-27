import { Component, Inject, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnomedService } from '../../../../services/snomed.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MotivoNuevaConsultaService } from '../../services/motivo-nueva-consulta.service';
import { Medicacion, MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { Problema, ProblemasService } from '../../../../services/problemas.service';
import { ProcedimientosService } from '../../../../services/procedimientos.service';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';
import { SignosVitalesNuevaConsultaService } from '../../services/signos-vitales-nueva-consulta.service';
import {
	AntecedenteFamiliar,
	AntecedentesFamiliaresNuevaConsultaService
} from '../../services/antecedentes-familiares-nueva-consulta.service';
import { Alergia, AlergiasNuevaConsultaService } from '../../services/alergias-nueva-consulta.service';
import { DateFormat, dateToMomentTimeZone, momentFormat, newMoment } from '@core/utils/moment.utils';
import { ClinicalSpecialtyDto, CreateOutpatientDto, HealthConditionNewConsultationDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HealthConditionService } from '@api-rest/services/healthcondition.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { MatDialog } from '@angular/material/dialog';
import { SuggestedFieldsPopupComponent } from '../../../../../presentation/components/suggested-fields-popup/suggested-fields-popup.component';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';
import { NewConsultationSuggestedFieldsService } from '../../services/new-consultation-suggested-fields.service';
import { TranslateService } from '@ngx-translate/core';
import { MIN_DATE } from "@core/utils/date.utils";

@Component({
	selector: 'app-nueva-consulta-dock-popup',
	templateUrl: './nueva-consulta-dock-popup.component.html',
	styleUrls: ['./nueva-consulta-dock-popup.component.scss']
})
export class NuevaConsultaDockPopupComponent implements OnInit {
	disableConfirmButton = false;
	formEvolucion: FormGroup;
	errores: string[] = [];
	motivoNuevaConsultaService: MotivoNuevaConsultaService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	problemasService: ProblemasService;
	procedimientoNuevaConsultaService: ProcedimientosService;
	datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
	signosVitalesNuevaConsultaService: SignosVitalesNuevaConsultaService;
	antecedentesFamiliaresNuevaConsultaService: AntecedentesFamiliaresNuevaConsultaService;
	alergiasNuevaConsultaService: AlergiasNuevaConsultaService;
	readOnlyProblema = false;
	apiErrors: string[] = [];
	public today = newMoment();
	fixedSpecialty = true;
	defaultSpecialty: ClinicalSpecialtyDto;
	specialties: ClinicalSpecialtyDto[];
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	public hasError = hasError;
	severityTypes: any[];
	criticalityTypes: any[];
	minDate = MIN_DATE;

	constructor(
		@Inject(OVERLAY_DATA) public data: NuevaConsultaData,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly outpatientConsultationService: OutpatientConsultationService,
		private readonly snackBarService: SnackBarService,
		private readonly healthConditionService: HealthConditionService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly dialog: MatDialog,
		private readonly translateService: TranslateService,
	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.problemasService = new ProblemasService(formBuilder, this.snomedService, this.snackBarService);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.datosAntropometricosNuevaConsultaService =
			new DatosAntropometricosNuevaConsultaService(formBuilder, this.internacionMasterDataService);
		this.signosVitalesNuevaConsultaService = new SignosVitalesNuevaConsultaService(formBuilder);
		this.antecedentesFamiliaresNuevaConsultaService = new AntecedentesFamiliaresNuevaConsultaService(formBuilder, this.snomedService);
		this.alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
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
		this.formEvolucion.controls['clinicalSpecialty'].markAsTouched();
	}

	ngOnInit(): void {

		this.setProfessionalSpecialties();

		if (this.data.idProblema) {
			this.readOnlyProblema = true;
			this.healthConditionService.getHealthCondition(this.data.idProblema).subscribe(p => {
				this.problemasService.addProblemToList(this.buildProblema(p));
			});
		}

		this.formEvolucion = this.formBuilder.group({
			evolucion: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			clinicalSpecialty: [null, [Validators.required]],
		});
		this.motivoNuevaConsultaService.error$.subscribe(motivoError => {
			this.errores[0] = motivoError;
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
		this.signosVitalesNuevaConsultaService.heartRateError$.subscribe(frecuenciaCardiacaError => {
			this.errores[4] = frecuenciaCardiacaError;
		});
		this.signosVitalesNuevaConsultaService.respiratoryRateError$.subscribe(frecuenciaRespiratoriaError => {
			this.errores[5] = frecuenciaRespiratoriaError;
		});
		this.signosVitalesNuevaConsultaService.temperatureError$.subscribe(temperaturaCorporalError => {
			this.errores[6] = temperaturaCorporalError;
		});
		this.signosVitalesNuevaConsultaService.bloodOxygenSaturationError$.subscribe(saturacionOxigenoError => {
			this.errores[7] = saturacionOxigenoError;
		});
		this.signosVitalesNuevaConsultaService.systolicBloodPressureError$.subscribe(presionSistolicaError => {
			this.errores[8] = presionSistolicaError;
		});
		this.signosVitalesNuevaConsultaService.diastolicBloodPressureError$.subscribe(presionDiastolicaError => {
			this.errores[9] = presionDiastolicaError;
		});

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.problemasService.setSeverityTypes(healthConditionSeverities);
		});

		this.internacionMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities => {
			this.criticalityTypes = allergyCriticalities;
			this.alergiasNuevaConsultaService.setCriticalityTypes(allergyCriticalities);
		});
	}

	save(): void {
		const nuevaConsulta: CreateOutpatientDto = this.buildCreateOutpatientDto();
		const fieldsService = new NewConsultationSuggestedFieldsService(nuevaConsulta, this.translateService);

		this.apiErrors = [];
		this.addErrorMessage(nuevaConsulta);
		if ((this.isValidConsultation()) && (this.formEvolucion.valid)) {
			if (!fieldsService.nonCompletedFields .length) {
				this.createConsultation(nuevaConsulta);
				this.disableConfirmButton = true;
			}
			else {
				this.openDialog(fieldsService.nonCompletedFields , fieldsService.presentFields, nuevaConsulta);
			}
		} else {
			this.disableConfirmButton = false;
			this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR');
		}
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
				this.createConsultation(nuevaConsulta);
				this.disableConfirmButton = true;
			}
		});
	}

	private createConsultation(nuevaConsulta: CreateOutpatientDto) {
		this.outpatientConsultationService.createOutpatientConsultation(nuevaConsulta, this.data.idPaciente).subscribe(
			_ => {
				this.snackBarService.showSuccess('ambulatoria.paciente.nueva-consulta.messages.SUCCESS');
				this.dockPopupRef.close(mapToFieldsToUpdate(nuevaConsulta));
			},
			response => {
				this.disableConfirmButton = false;
				if (response.errors)
					response.errors.forEach(val => {
						this.apiErrors.push(val);
					});
				this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR');
			}
		);

		function mapToFieldsToUpdate(nuevaConsultaDto: CreateOutpatientDto) {
			return {
				allergies: !!nuevaConsultaDto.allergies?.length,
				personalHistories: !!nuevaConsultaDto.problems?.length,
				familyHistories: !!nuevaConsultaDto.familyHistories?.length,
				vitalSigns: !!nuevaConsultaDto.vitalSigns,
				medications: !!nuevaConsultaDto.medications?.length,
				anthropometricData: !!nuevaConsultaDto.anthropometricData,
				problems: !!nuevaConsultaDto.problems?.length
			};
		}
	}

	public isValidConsultation(): boolean {
		return (this.errores.find(elem =>
			elem !== undefined
		) === undefined);
	}

	private addErrorMessage(consulta: CreateOutpatientDto): void {
		if (parseInt(consulta.anthropometricData?.height?.value, 10) < 0) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_MIN');
		} else if (parseInt(consulta.anthropometricData?.height?.value, 10) > 1000) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_MAX');
		}

		if (parseInt(consulta.anthropometricData?.weight?.value, 10) < 0) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_MIN');
		} else if (parseInt(consulta.anthropometricData?.weight?.value, 10) > 1000) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_MAX');
		}

		if (parseInt(consulta.vitalSigns.heartRate?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setHeartRateError('ambulatoria.paciente.nueva-consulta.errors.FRECUENCIA_CARDIACA_MIN');
		}

		if (parseInt(consulta.vitalSigns.respiratoryRate?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setRespiratoryRateError('ambulatoria.paciente.nueva-consulta.errors.FRECUENCIA_RESPIRATORIA_MIN');
		}

		if (parseInt(consulta.vitalSigns.temperature?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setTemperatureError('ambulatoria.paciente.nueva-consulta.errors.TEMPERATURA_CORPORAL_MIN');
		}

		if (parseInt(consulta.vitalSigns.bloodOxygenSaturation?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setBloodOxygenSaturationError('ambulatoria.paciente.nueva-consulta.errors.SATURACION_OXIGENO_MIN');
		}

		if (parseInt(consulta.vitalSigns.diastolicBloodPressure?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setDiastolicBloodPressureError('ambulatoria.paciente.nueva-consulta.errors.TENSION_DIASTOLICA_MIN');
		}

		if (parseInt(consulta.vitalSigns?.systolicBloodPressure?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setSystolicBloodPressureError('ambulatoria.paciente.nueva-consulta.errors.TENSION_SISTOLICA_MIN');
		}

		hasError(this.formEvolucion, 'maxlength', 'evolucion') ?
			this.errores[10] = 'ambulatoria.paciente.nueva-consulta.errors.MAX_LENGTH_NOTA'
			: this.errores[10] = undefined;
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
			allergies: this.alergiasNuevaConsultaService.getAlergias().map((alergia: Alergia) => {
				return {
					categoryId: null,
					snomed: alergia.snomed,
					startDate: null,
					statusId: null,
					verificationId: null,
					criticalityId: alergia.criticalityId,
				};
			}),
			anthropometricData: this.datosAntropometricosNuevaConsultaService.getDatosAntropometricos(),
			evolutionNote: this.formEvolucion.value?.evolucion,
			familyHistories: this.antecedentesFamiliaresNuevaConsultaService.getAntecedentesFamiliares().map((antecedente: AntecedenteFamiliar) => {
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
			problems: this.problemasService.getProblemas().map(
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
			vitalSigns: this.signosVitalesNuevaConsultaService.getSignosVitales(),
			clinicalSpecialtyId: this.defaultSpecialty?.id

		};
	}

	editProblema() {
		if (this.problemasService.editProblem()) {
			this.readOnlyProblema = false;
		}
	}

	setDefaultSpecialty() {
		this.defaultSpecialty = this.formEvolucion.controls.clinicalSpecialty.value;
	}

}

export interface NuevaConsultaData {
	idPaciente: number;
	idProblema?: number;
}
