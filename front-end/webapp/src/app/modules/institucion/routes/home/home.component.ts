import { Component, OnInit } from '@angular/core';

import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { InstitutionDto } from '@api-rest/api-model';
import { InstitutionService } from '@api-rest/services/institution.service';
import { map } from 'rxjs/operators';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	institucion$: Observable<InstitutionDto>;

	constructor(
		private contextService: ContextService,
		private institutionService: InstitutionService,
	) { }

	ngOnInit(): void {
		this.institucion$ = this.institutionService.getInstitutions([this.contextService.institutionId]).pipe(
			map(list => list && list.length ? list[0] : undefined),
		);
	}

}
