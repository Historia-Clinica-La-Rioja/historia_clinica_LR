import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SnomedService } from 'src/app/modules/historia-clinica/services/snomed.service';
import { Problema, ProblemasNuevaConsultaService } from '../../services/problemas-nueva-consulta.service';
import { MotivoNuevaConsultaService } from '../../services/motivo-nueva-consulta.service';
import { Medicacion, MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { ProcedimientosNuevaConsultaService } from '../../services/procedimientos-nueva-consulta.service';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { SignosVitalesNuevaConsultaService } from '../../services/signos-vitales-nueva-consulta.service';
import { AntecedentesFamiliaresNuevaConsultaService } from '../../services/antecedentes-familiares-nueva-consulta.service';
import { CreateOutpatientDto } from '@api-rest/api-model';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';

@Component({
	selector: 'app-nueva-consulta',
	templateUrl: './nueva-consulta.component.html',
	styleUrls: ['./nueva-consulta.component.scss']
})
export class NuevaConsultaComponent implements OnInit {

	formEvolucion: FormGroup;

	motivoNuevaConsultaService: MotivoNuevaConsultaService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	problemasNuevaConsultaService: ProblemasNuevaConsultaService;
	procedimientoNuevaConsultaService: ProcedimientosNuevaConsultaService;
	datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
	signosVitalesNuevaConsultaService: SignosVitalesNuevaConsultaService;
	antecedentesFamiliaresNuevaConsultaService: AntecedentesFamiliaresNuevaConsultaService;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly outpatientConsultationService: OutpatientConsultationService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly snackBarService: SnackBarService
	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, snomedService);
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, snomedService);
		this.problemasNuevaConsultaService = new ProblemasNuevaConsultaService(formBuilder, snomedService);
		this.procedimientoNuevaConsultaService = new ProcedimientosNuevaConsultaService(formBuilder, snomedService);
		this.datosAntropometricosNuevaConsultaService =
			new DatosAntropometricosNuevaConsultaService(formBuilder, internacionMasterDataService);
		this.signosVitalesNuevaConsultaService = new SignosVitalesNuevaConsultaService(formBuilder);
		this.antecedentesFamiliaresNuevaConsultaService = new AntecedentesFamiliaresNuevaConsultaService(formBuilder, snomedService);
	}

	ngOnInit(): void {
		this.formEvolucion = this.formBuilder.group({
			evolucion: [null]
		});
	}

	private goToAmbulatoria(idPaciente: number) {
		this.router.navigateByUrl(`institucion/${this.contextService.institutionId}/ambulatoria/paciente/${idPaciente}`);
	}

	save() {
		this.route.paramMap.subscribe((params) => {
			const idPaciente = Number(params.get('idPaciente'));

			const nuevaConsulta: CreateOutpatientDto = this.buildCreateOutpatientDto();

			this.outpatientConsultationService.createOutpatientConsultation(nuevaConsulta, idPaciente).subscribe(
				_ => {
					this.snackBarService.showSuccess('ambulatoria.paciente.nueva-consulta.messages.SUCCESS');
					this.goToAmbulatoria(idPaciente);
				},
				_ => {
					this.snackBarService.showSuccess('ambulatoria.paciente.nueva-consulta.messages.ERROR');
				}
			);
		});


	}

	private buildCreateOutpatientDto(): CreateOutpatientDto {
		return {
			allergies: [],
			anthropometricData: this.datosAntropometricosNuevaConsultaService.getDatosAntropometricos(),
			evolutionNote: this.formEvolucion.value?.evolucion,
			familyHistories: [],
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
			reasons: [this.motivoNuevaConsultaService.getMotivoConsulta()],
			vitalSigns: this.signosVitalesNuevaConsultaService.getSignosVitales()
		};
	}
}
