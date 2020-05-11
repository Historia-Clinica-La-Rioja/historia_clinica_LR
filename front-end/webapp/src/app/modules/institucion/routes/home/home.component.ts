import { Component, OnInit } from '@angular/core';

import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { InstitutionDto } from '@api-rest/api-model';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	institucion$: Observable<InstitutionDto>;

	constructor(
		private contextService: ContextService,
	) { }

	ngOnInit(): void {
		this.institucion$ = this.contextService.institution$;
	}

}
