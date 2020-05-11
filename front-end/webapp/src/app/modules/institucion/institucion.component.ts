import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ContextService } from '../core/services/context.service';

@Component({
	selector: 'app-institucion',
	templateUrl: './institucion.component.html',
	styleUrls: ['./institucion.component.scss']
})
export class InstitucionComponent implements OnInit {

	constructor(
		private activatedRoute: ActivatedRoute,
		private contextService: ContextService,
	) { }

	ngOnInit(): void {
		this.activatedRoute.paramMap.subscribe(params => {
			this.contextService.setInstitutionId(Number(params.get('id')))
		});
	}

}
