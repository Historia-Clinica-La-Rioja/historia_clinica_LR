import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {SnomedService} from 'src/app/modules/historia-clinica/services/snomed.service';
import {Problema, ProblemasNuevaConsultaService} from '../../services/problemas-nueva-consulta.service';
import {MotivoNuevaConsultaService} from '../../services/motivo-nueva-consulta.service';
import {Medicacion, MedicacionesNuevaConsultaService} from '../../services/medicaciones-nueva-consulta.service';
import {ProcedimientosService} from '../../../../services/procedimientos.service';
import {DatosAntropometricosNuevaConsultaService} from '../../services/datos-antropometricos-nueva-consulta.service';
import {InternacionMasterDataService} from '@api-rest/services/internacion-master-data.service';
import {SignosVitalesNuevaConsultaService} from '../../services/signos-vitales-nueva-consulta.service';

import {
	AntecedenteFamiliar,
	AntecedentesFamiliaresNuevaConsultaService
} from '../../services/antecedentes-familiares-nueva-consulta.service';
import {CreateOutpatientDto, HealthConditionNewConsultationDto} from '@api-rest/api-model';
import {DateFormat, dateToMomentTimeZone, momentFormat} from '@core/utils/moment.utils';
import {OutpatientConsultationService} from '@api-rest/services/outpatient-consultation.service';
import {ActivatedRoute, Router} from '@angular/router';
import {SnackBarService} from '@presentation/services/snack-bar.service';
import {ContextService} from '@core/services/context.service';
import {Alergia, AlergiasNuevaConsultaService} from '../../services/alergias-nueva-consulta.service';
import {HealthConditionService} from "@api-rest/services/healthcondition.service";

@Component({
	selector: 'app-nueva-consulta',
	templateUrl: './nueva-consulta.component.html',
	styleUrls: ['./nueva-consulta.component.scss']
})
export class NuevaConsultaComponent implements OnInit {

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
	readOnlyProblema: boolean = false;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly outpatientConsultationService: OutpatientConsultationService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly snackBarService: SnackBarService,
		private readonly healthConditionService: HealthConditionService,
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
		this.route.data.subscribe( data => {
				if(data.problemaReadOnly){
					this.readOnlyProblema = true;
					this.route.paramMap.subscribe( param => {
						let hcId = Number(param.get('idProblema'));
						this.healthConditionService.getHealthCondition(hcId).subscribe(p => {
							this.problemasNuevaConsultaService.addProblemToList(NuevaConsultaComponent.buildProblema(p));
						});
					});
				}
		});

		this.formEvolucion = this.formBuilder.group({
			evolucion: [null]
		});

		this.motivoNuevaConsultaService.error$.subscribe(motivoError => {
			this.errores[0]=motivoError;
		});
		this.problemasNuevaConsultaService.error$.subscribe(problemasError => {
			this.errores[1]=problemasError;
		});
		this.datosAntropometricosNuevaConsultaService.heightError$.subscribe(tallaError => {
			this.errores[2]=tallaError;
		});
		this.datosAntropometricosNuevaConsultaService.weightError$.subscribe(pesoError => {
			this.errores[3]=pesoError;
		});
		this.signosVitalesNuevaConsultaService.systolicBloodPressureError$.subscribe(presionSistolicaError => {
			this.errores[4]=presionSistolicaError;
		});
		this.signosVitalesNuevaConsultaService.diastolicBloodPressureError$.subscribe(presionDiastolicaError => {
			this.errores[5]=presionDiastolicaError;
		});
	}

	private static buildProblema(p: HealthConditionNewConsultationDto) {
		const problema: Problema = {
			snomed: p.snomed,
			cronico: p.isChronic,
			fechaInicio: dateToMomentTimeZone(p.startDate),
			fechaFin: p.inactivationDate? dateToMomentTimeZone(p.inactivationDate) : undefined
		};
		return problema;
	}

	private goToAmbulatoria(idPaciente: number) {
		this.router.navigateByUrl(`institucion/${this.contextService.institutionId}/ambulatoria/paciente/${idPaciente}`).then();
	}

	save(): void {
		this.route.paramMap.subscribe((params) => {
			const idPaciente = Number(params.get('idPaciente'));

			const nuevaConsulta: CreateOutpatientDto = this.buildCreateOutpatientDto();
			this.addErrorMessage(nuevaConsulta);

			if (this.isValidConsultation()) {

				this.outpatientConsultationService.createOutpatientConsultation(nuevaConsulta, idPaciente).subscribe(
					_ => {
						this.snackBarService.showSuccess('ambulatoria.paciente.nueva-consulta.messages.SUCCESS');
						this.goToAmbulatoria(idPaciente);
					},
					_ => {
						this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR');
					}
				);
			} else {
				this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR');

			}
		});
	}

	public isValidConsultation(): boolean {
		return(this.errores.find( elem =>
			elem !== undefined
		)===undefined);
	}

	private addErrorMessage(consulta: CreateOutpatientDto): void {
		if (!consulta.reasons?.length) {
			this.motivoNuevaConsultaService.setError('ambulatoria.paciente.nueva-consulta.errors.MOTIVO_OBLIGATORIO');
		}
		if (!consulta.anthropometricData.height) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_OBLIGATORIO');
		}
		if (!consulta.anthropometricData.weight) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_OBLIGATORIO');
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
			vitalSigns: this.signosVitalesNuevaConsultaService.getSignosVitales()
		};
	}

	editProblema() {
		if(this.problemasNuevaConsultaService.editProblem()) {
			this.readOnlyProblema = false;
		}
	}
}
