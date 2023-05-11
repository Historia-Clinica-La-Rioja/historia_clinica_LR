import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';

@Component({
	selector: 'app-study-details',
	templateUrl: './study-details.component.html',
	styleUrls: ['./study-details.component.scss']
})
export class StudyDetailsComponent implements OnInit {

	constructor(
		private readonly router: Router,
		private readonly contextService: ContextService,
	) { }

	ngOnInit(): void {
	}

	goBack() {
		this.router.navigate([`institucion/${this.contextService.institutionId}/imagenes/lista-trabajos`]);
	}

}
