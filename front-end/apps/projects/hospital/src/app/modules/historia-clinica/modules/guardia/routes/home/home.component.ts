import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonType } from '@presentation/components/button/button.component';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	readonly RAISED = ButtonType.RAISED;

	constructor(
		private readonly router: Router,
	) { }

	goToNewEpisode(): void {
		this.router.navigate([`${this.router.url}/nuevo-episodio/administrativa`]);
	}

}
