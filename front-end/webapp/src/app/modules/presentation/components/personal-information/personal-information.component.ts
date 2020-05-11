import { Component, OnInit, Input } from '@angular/core';
import { AddressDto, IdentificationTypeDto } from '@api-rest/api-model';
import { Moment } from 'moment';

@Component({
	selector: 'app-personal-information',
	templateUrl: './personal-information.component.html',
	styleUrls: ['./personal-information.component.scss']
})
export class PersonalInformationComponent implements OnInit {

	@Input() personalInformation: PersonalInformation;
	public addressPresent: boolean;
	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges() {
		if (this.personalInformation?.address) {
			this.addressPresent = Object.values(this.personalInformation?.address).find(o => o != null) ? true : false;
		}
	}
}

export class PersonalInformation {
	identificationNumber: string;
	identificationType: IdentificationTypeDto;
	cuil: string;
	address: AddressDto;
	birthDate: string;
	email: string;
	phoneNumber: string;
	medicalCoverageName: string;
	medicalCoverageAffiliateNumber: string;
}
