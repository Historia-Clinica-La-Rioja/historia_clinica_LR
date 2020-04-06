import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

const LANDING_PAGE = '';

@Component({
	selector: 'app-inicio',
	templateUrl: './inicio.component.html',
	styleUrls: ['./inicio.component.scss']
})
export class InicioComponent implements OnInit {

	constructor(
		private router: Router) {
	}

	ngOnInit(): void {
	}

	onClick(): void {
		this.router.navigate([LANDING_PAGE]);
	}

}
