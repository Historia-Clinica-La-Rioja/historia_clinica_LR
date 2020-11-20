import { Component, Inject, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnomedService } from '../../../../services/snomed.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MotivoNuevaConsultaService } from '../../services/motivo-nueva-consulta.service';
import { Medicacion, MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { Problema, ProblemasNuevaConsultaService } from '../../services/problemas-nueva-consulta.service';
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
import { ContextService } from '@core/services/context.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HealthConditionService } from '@api-rest/services/healthcondition.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { AppointmentsService } from '@api-rest/services/appointments.service';

@Component({
	selector: 'app-nueva-consulta-dock-popup',
	templateUrl: './nueva-consulta-dock-popup.component.html',
	styleUrls: ['./nueva-consulta-dock-popup.component.scss']
})
export class NuevaConsultaDockPopupComponent implements OnInit {

	formEvolucion: FormGroup;
	errores: string[] = [];
	motivoNuevaConsultaService: MotivoNuevaConsultaService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	problemasNuevaConsultaService: ProblemasNuevaConsultaService;
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

	constructor(
		@Inject(OVERLAY_DATA) public data: NuevaConsultaData,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly outpatientConsultationService: OutpatientConsultationService,
		private readonly contextService: ContextService,
		private readonly snackBarService: SnackBarService,
		private readonly healthConditionService: HealthConditionService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly appointmentsService: AppointmentsService,
	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, snomedService);
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, snomedService);
		this.problemasNuevaConsultaService = new ProblemasNuevaConsultaService(formBuilder, snomedService);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, snomedService);
		this.datosAntropometricosNuevaConsultaService =
			new DatosAntropometricosNuevaConsultaService(formBuilder, internacionMasterDataService);
		this.signosVitalesNuevaConsultaService = new SignosVitalesNuevaConsultaService(formBuilder);
		this.antecedentesFamiliaresNuevaConsultaService = new AntecedentesFamiliaresNuevaConsultaService(formBuilder, snomedService);
		this.alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(formBuilder, snomedService);
	}

	ngOnInit(): void {
		this.appointmentsService.considerAppointments().subscribe(consider => {
			if (consider) {
				this.clinicalSpecialtyService.getAppointmentClinicalSpecialty(this.data.idPaciente).subscribe(specialty => {
					this.specialties = [specialty];
					this.defaultSpecialty = specialty;
					this.formEvolucion.get('clinicalSpecialty').setValue(this.defaultSpecialty);
				});
			} else {
				this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe( specialties => {
					this.specialties = specialties;
					this.fixedSpecialty = false;
					this.defaultSpecialty = specialties[0];
					this.formEvolucion.get('clinicalSpecialty').setValue(this.defaultSpecialty);
				});
			}
		});


		if (this.data.idProblema) {
			this.readOnlyProblema = true;
			this.healthConditionService.getHealthCondition(this.data.idProblema).subscribe(p => {
				this.problemasNuevaConsultaService.addProblemToList(this.buildProblema(p));
			});
		}

		this.formEvolucion = this.formBuilder.group({
			evolucion: [null],
			clinicalSpecialty: []
		});
		this.motivoNuevaConsultaService.error$.subscribe(motivoError => {
			this.errores[0] = motivoError;
		});
		this.problemasNuevaConsultaService.error$.subscribe(problemasError => {
			this.errores[1] = problemasError;
		});
		this.datosAntropometricosNuevaConsultaService.heightError$.subscribe(tallaError => {
			this.errores[2] = tallaError;
		});
		this.datosAntropometricosNuevaConsultaService.weightError$.subscribe(pesoError => {
			this.errores[3] = pesoError;
		});
		this.signosVitalesNuevaConsultaService.systolicBloodPressureError$.subscribe(presionSistolicaError => {
			this.errores[4] = presionSistolicaError;
		});
		this.signosVitalesNuevaConsultaService.diastolicBloodPressureError$.subscribe(presionDiastolicaError => {
			this.errores[5] = presionDiastolicaError;
		});
	}

	save(): void {
		const nuevaConsulta: CreateOutpatientDto = this.buildCreateOutpatientDto();
		this.apiErrors = [];
		this.addErrorMessage(nuevaConsulta);
		if (this.isValidConsultation()) {
			this.outpatientConsultationService.createOutpatientConsultation(nuevaConsulta, this.data.idPaciente).subscribe(
				_ => {
					this.snackBarService.showSuccess('ambulatoria.paciente.nueva-consulta.messages.SUCCESS');
					this.dockPopupRef.close(true);
				},
				errors => {
					Object.getOwnPropertyNames(errors).forEach(val => {
						this.apiErrors.push(errors[val]);
					});
					this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR');
				}
			);
		} else {
			this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR');
		}
	}

	public isValidConsultation(): boolean {
		return(this.errores.find( elem =>
			elem !== undefined
		) === undefined);
	}

	private addErrorMessage(consulta: CreateOutpatientDto): void {
		if (!consulta.reasons?.length) {
			this.motivoNuevaConsultaService.setError('ambulatoria.paciente.nueva-consulta.errors.MOTIVO_OBLIGATORIO');
		}
		if (!consulta.anthropometricData.height) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_OBLIGATORIO');
		} else if (parseInt(consulta.anthropometricData.height.value) < 0) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_MIN');
		} else if (parseInt(consulta.anthropometricData.height.value) > 1000) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_MAX');
		}
		if (!consulta.anthropometricData.weight) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_OBLIGATORIO');
		} else if (parseInt(consulta.anthropometricData.weight.value) < 0) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_MIN');
		} else if (parseInt(consulta.anthropometricData.weight.value) > 1000) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_MAX');
		}
		if (!consulta.vitalSigns.diastolicBloodPressure ) {
			this.signosVitalesNuevaConsultaService.setDiastolicBloodPressureError('ambulatoria.paciente.nueva-consulta.errors.TENSION_DIASTOLICA_OBLIGATORIO');
		}
		if (!consulta.vitalSigns.systolicBloodPressure) {
			this.signosVitalesNuevaConsultaService.setSystolicBloodPressureError('ambulatoria.paciente.nueva-consulta.errors.TENSION_SISTOLICA_OBLIGATORIO');
		}
		if (!consulta.problems?.length) {
			this.problemasNuevaConsultaService.setError('ambulatoria.paciente.nueva-consulta.errors.PROBLEMA_OBLIGATORIO');
		}
	}

	private buildProblema(p: HealthConditionNewConsultationDto) {
		const problema: Problema = {
			snomed: p.snomed,
			cronico: p.isChronic,
			fechaInicio: dateToMomentTimeZone(p.startDate),
			fechaFin: p.inactivationDate ? dateToMomentTimeZone(p.inactivationDate) : undefined
		};
		return problema;
	}

	private buildCreateOutpatientDto(): CreateOutpatientDto {
		return {
			allergies: this.alergiasNuevaConsultaService.getAlergias().map((alergia: Alergia) => {
				return {
					categoryId: null,
					severity: null,
					snomed: alergia.snomed,
					startDate: null,
					statusId: null,
					verificationId: null
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
			problems: this.problemasNuevaConsultaService.getProblemas().map(
				(problema: Problema) => {
					return {
						chronic: problema.cronico,
						endDate: problema.fechaFin ? momentFormat(problema.fechaFin, DateFormat.API_DATE) : undefined,
						snomed: problema.snomed,
						startDate: momentFormat(problema.fechaInicio, DateFormat.API_DATE)
					};
				}
			),
			procedures: this.procedimientoNuevaConsultaService.getProcedimientos(),
			reasons: this.motivoNuevaConsultaService.getMotivosConsulta(),
			vitalSigns: this.signosVitalesNuevaConsultaService.getSignosVitales(),
			clinicalSpecialtyId: this.defaultSpecialty.id

		};
	}

	editProblema() {
		if (this.problemasNuevaConsultaService.editProblem()) {
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
