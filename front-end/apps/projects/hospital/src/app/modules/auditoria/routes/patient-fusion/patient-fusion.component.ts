import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IdentificationTypeDto } from '@api-rest/api-model';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ContextService } from '@core/services/context.service';

const ROUTE_CONTROL_PATIENT_DUPLICATE = "auditoria/control-pacientes-duplicados"
@Component({
	selector: 'app-patient-fusion',
	templateUrl: './patient-fusion.component.html',
	styleUrls: ['./patient-fusion.component.scss']
})
export class PatientFusionComponent implements OnInit {
	private readonly routePrefix;
	listPatientData: any[];
	identificationTypeList: IdentificationTypeDto[];
	constructor(private router: Router, private contextService: ContextService, private personMasterDataService: PersonMasterDataService) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;
			});
	}

	ngOnInit(): void {
	}

	goToBack() {
		this.router.navigate([this.routePrefix + ROUTE_CONTROL_PATIENT_DUPLICATE])
	}

	getIdentificationType(value: number) {
		return this.identificationTypeList?.find(type => type.id === value).description
	}
}
