import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';

const ROUTE_CONTROL_PATIENT_DUPLICATE = "auditoria/control-pacientes-duplicados"
export const ROUTE_UNLINK_PATIENT ="auditoria/desvincular-pacientes"
export const ROUTE_EMPADRONAMIENTO = "auditoria/empadronamiento"
@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	private readonly routePrefix;

	constructor(private router: Router, private contextService: ContextService) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnInit(): void {
	}

	goToPatientsFusion() {
		this.router.navigate([this.routePrefix + ROUTE_CONTROL_PATIENT_DUPLICATE]);
	}

	goToEmpadronamiento() {
		this.router.navigate([this.routePrefix + ROUTE_EMPADRONAMIENTO]);
	}
	goToUnlinkPatient(){
		this.router.navigate([this.routePrefix + ROUTE_UNLINK_PATIENT]);
	}
}
