import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
	selector: 'app-card',
	templateUrl: './card.component.html',
	styleUrls: ['./card.component.scss']
})
export class CardComponent {

	@Input() cardContent: CardModel;
	constructor(
		private readonly router: Router
	) { }

	goTo(url: string): void {
		this.router.navigateByUrl(url);
	}
}

export interface CardModel {
	avatar?: any;
	name?: string;
	dni?: string;
	header: Value[];
	headerSimple?: Value[];
	details?: any[];
	action: ValueAction;
	ranking?: number;
	hasPhysicalDischarge?: boolean;
	sectorDescription?: string;
	roomNumber?: string;
}

export interface Value {
	title: string;
	value: any;
}

export interface ValueAction {
	display: string;
	do: any;
}
