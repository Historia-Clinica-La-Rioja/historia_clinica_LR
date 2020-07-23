import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { SnomedService } from 'src/app/modules/historia-clinica/services/snomed.service';
import { ProblemasNuevaConsultaService } from '../../services/problemas-nueva-consulta.service';
import { MotivoNuevaConsultaService } from '../../services/motivo-nueva-consulta.service';
import { MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { ProcedimientosNuevaConsultaService } from '../../services/procedimientos-nueva-consulta.service';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { SignosVitalesNuevaConsultaService } from '../../services/signos-vitales-nueva-consulta.service';

@Component({
	selector: 'app-nueva-consulta',
	templateUrl: './nueva-consulta.component.html',
	styleUrls: ['./nueva-consulta.component.scss']
})
export class NuevaConsultaComponent implements OnInit {

	motivoNuevaConsultaService: MotivoNuevaConsultaService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	problemasNuevaConsultaService: ProblemasNuevaConsultaService;
	procedimientoNuevaConsultaService: ProcedimientosNuevaConsultaService;
	datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
	signosVitalesNuevaConsultaService: SignosVitalesNuevaConsultaService;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly internacionMasterDataService: InternacionMasterDataService
	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, snomedService);
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, snomedService);
		this.problemasNuevaConsultaService = new ProblemasNuevaConsultaService(formBuilder, snomedService);
		this.procedimientoNuevaConsultaService = new ProcedimientosNuevaConsultaService(formBuilder, snomedService);
		this.datosAntropometricosNuevaConsultaService = new DatosAntropometricosNuevaConsultaService(formBuilder, internacionMasterDataService);
		this.signosVitalesNuevaConsultaService = new SignosVitalesNuevaConsultaService(formBuilder);
	}

	ngOnInit(): void {
	}

	save() {
	}

}
