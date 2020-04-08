import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

const ROUTE_SEARCH = 'pacientes/search';
const ROUTE_ADD = 'pacientes/add';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	constructor(private router: Router) {
	}

	ngOnInit(): void {
	}

	onClickBusqueda(): void {
		this.router.navigate([ROUTE_SEARCH]);
	}

	onClickAdd(): void {
		this.router.navigate([ROUTE_ADD]);
	}
}
