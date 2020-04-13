import { Component, OnInit } from '@angular/core';
import { MenuItem } from '@core/core-model';
import {SIDEBAR_MENU} from '../constants/menu';

@Component({
	selector: 'app-pacientes-layout',
	templateUrl: './pacientes-layout.component.html',
	styleUrls: ['./pacientes-layout.component.scss']
})
export class PacientesLayoutComponent implements OnInit {

	public menuItems: MenuItem[] = SIDEBAR_MENU;

	constructor() {
	}

	ngOnInit(): void {
	}

}
