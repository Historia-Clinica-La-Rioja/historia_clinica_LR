import { Component, OnInit } from '@angular/core';

import { environment } from '@environments/environment';

const PRODUCCION_BACKOFFICE_ROOT_URL = '/';
const DEVELOP_BACKOFFICE_ROOT_URL = 'http://localhost:3000/';

const rootUrl = () => environment.production ? PRODUCCION_BACKOFFICE_ROOT_URL : DEVELOP_BACKOFFICE_ROOT_URL;

@Component({
	selector: 'app-backoffice',
	templateUrl: './backoffice.component.html',
	styleUrls: ['./backoffice.component.scss']
})
export class BackofficeComponent implements OnInit {

	constructor() { }

	ngOnInit(): void {
		const backofficeUrl = rootUrl() + 'backoffice/index.html';
		console.log(`Redirigiendo a ${backofficeUrl}`);
		window.location.href = backofficeUrl;
	}

}
