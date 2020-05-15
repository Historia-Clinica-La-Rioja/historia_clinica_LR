import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';

import { ContextService } from '@core/services/context.service';
import { MenuItem } from '@core/core-model';

import { SIDEBAR_MENU } from './constants/menu';

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
	) {
		this.menuItems$ = of(SIDEBAR_MENU);
	}

	ngOnInit(): void {
		this.activatedRoute.paramMap.subscribe(params => {
			this.contextService.setInstitutionId(Number(params.get('id')))
		});
	}

}
