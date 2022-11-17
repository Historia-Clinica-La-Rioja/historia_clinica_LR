import { Component } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { ActivatedRoute, Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';


@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	routePrefix: string;

	tabActiveIndex = 0;

	constructor(
		private readonly router: Router,
		public readonly route: ActivatedRoute,
		private readonly contextService: ContextService,
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/turnos`;
	}

	goToNewAgenda(): void {
		this.router.navigate([`${this.routePrefix}/nueva-agenda/`]);
	}

	tabChanged(tabChangeEvent: MatTabChangeEvent): void {
		this.tabActiveIndex = tabChangeEvent.index;
	}

}

