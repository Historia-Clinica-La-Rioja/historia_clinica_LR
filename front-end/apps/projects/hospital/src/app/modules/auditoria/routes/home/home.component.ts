import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

const ROUTE_CONTROL_PATIENT_DUPLICATE = "home/auditoria/control-pacientes-duplicados"
export const ROUTE_UNLINK_PATIENT ="home/auditoria/desvincular-pacientes"
export const ROUTE_EMPADRONAMIENTO = "home/auditoria/empadronamiento"
@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	constructor(private router: Router) {}

	ngOnInit(): void {
	}

	goToPatientsFusion() {
		this.router.navigate([ROUTE_CONTROL_PATIENT_DUPLICATE]);
	}

	goToEmpadronamiento() {
		this.router.navigate([ROUTE_EMPADRONAMIENTO]);
	}
	goToUnlinkPatient(){
		this.router.navigate([ROUTE_UNLINK_PATIENT]);
	}
}
