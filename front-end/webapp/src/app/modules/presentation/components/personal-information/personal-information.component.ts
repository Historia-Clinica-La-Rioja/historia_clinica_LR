import { Component, OnInit, Input } from '@angular/core';
import { AddressDto, IdentificationTypeDto } from '@api-rest/api-model';
import { Address } from '@presentation/pipes/fullHouseAddress.pipe';

@Component({
	selector: 'app-personal-information',
	templateUrl: './personal-information.component.html',
	styleUrls: ['./personal-information.component.scss']
})
export class PersonalInformationComponent implements OnInit {

	@Input() personalInformation: PersonalInformation;
	public addressPresent: boolean = false;
	public address: Address;
	constructor() { }

	ngOnInit(): void {
	}

	ngOnChanges() {
		if (this.personalInformation?.address) {
			this.addressPresent = Object.values(this.personalInformation?.address).find(o => o && typeof(o) !== 'object') ? true : false;
			if (this.addressPresent){
				this.address = this.mapToAddress(this.personalInformation.address);
			}
		}
	}

	mapToAddress(addressDto: AddressDto){
		return {
			street: addressDto.street,
			number: addressDto.number,
			floor: addressDto.floor,
			apartment: addressDto.apartment
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
