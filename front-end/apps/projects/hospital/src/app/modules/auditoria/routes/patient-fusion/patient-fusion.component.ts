import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';

const ROUTE_CONTROL_PATIENT_DUPLICATE = "auditoria/control-pacientes-duplicados"
@Component({
	selector: 'app-patient-fusion',
	templateUrl: './patient-fusion.component.html',
	styleUrls: ['./patient-fusion.component.scss']
})
export class PatientFusionComponent implements OnInit {
	private readonly routePrefix;
	constructor(private router: Router, private contextService: ContextService) { this.routePrefix = 'institucion/' + this.contextService.institutionId + '/' }

	ngOnInit(): void {
	}

	goToBack() {
		this.router.navigate([this.routePrefix + ROUTE_CONTROL_PATIENT_DUPLICATE])
	}

}
