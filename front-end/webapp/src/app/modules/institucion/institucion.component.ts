import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ContextService } from '@core/services/context.service';
import { MenuItem } from '@core/core-model';

import { SIDEBAR_MENU } from './constants/menu';
import { PermissionsService } from '../core/services/permissions.service';

@Component({
	selector: 'app-institucion',
	templateUrl: './institucion.component.html',
	styleUrls: ['./institucion.component.scss']
})
export class InstitucionComponent implements OnInit {
	menuItems$: Observable<MenuItem[]>;

	constructor(
		private activatedRoute: ActivatedRoute,
		private contextService: ContextService,
		private permissionsService: PermissionsService,
	) {

	}

	ngOnInit(): void {
		this.activatedRoute.paramMap.subscribe(params => {
			this.contextService.setInstitutionId(Number(params.get('id')));

			this.menuItems$ = this.permissionsService.filterItems$(SIDEBAR_MENU);
		});
	}

}
