import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';

const ROUTE_PATIENTS_FUSION = "auditoria/fusion-pacientes"
@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	private readonly routePrefix;

	constructor(private router: Router,	private contextService: ContextService) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnInit(): void {
	}

	goToPatientsFusion() {
		this.router.navigate([this.routePrefix + ROUTE_PATIENTS_FUSION]);
	}
}
