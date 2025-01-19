import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-contact-details',
	templateUrl: './contact-details.component.html',
	styleUrls: ['./contact-details.component.scss']
})
export class ContactDetailsComponent {
	email: string;
	phone: string
	@Input() set contactDetails(value: ContactDetails) {
		if (value) {
			this.phone = value.phoneNumber ? `(${value.phonePrefix}) ${value.phoneNumber}` : `(${value.phonePrefix})`;
			if (value.email)
				this.email = value.email;
		}
	};

	constructor() { }

}

export interface ContactDetails {
	phonePrefix: string;
	phoneNumber: string;
	email?: string;
}
