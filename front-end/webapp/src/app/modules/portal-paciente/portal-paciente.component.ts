import { Component, OnInit } from '@angular/core';
import { MenuFooter } from '@presentation/components/main-layout/main-layout.component';
import { SIDEBAR_MENU } from '../portal-paciente/constants/menu';

@Component({
	selector: 'app-portal-paciente',
	templateUrl: './portal-paciente.component.html',
	styleUrls: ['./portal-paciente.component.scss']
})
export class PortalPacienteComponent implements OnInit {

	menuItems;
	menuFooterItems: MenuFooter = {user: {}};

	constructor() {
	}

	ngOnInit(): void {
		this.menuItems = SIDEBAR_MENU;
	}

}
