import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import {MasterDataDto, MasterDataInterface} from '@api-rest/api-model';
import { OtherIndicationTypeDto } from './internment-indication.service';

@Injectable({
	providedIn: 'root'
})
export class InternacionMasterDataService {

	constructor(private http: HttpClient) {
	}

	getAllergyClinical(): Observable<any[]> {
		const url = `${environment.apiBase}/internments/masterdata/allergy/clinical`;
		return this.http.get<any[]>(url);
	}

	getAllergyVerifications(): Observable<MasterDataInterface<string>[]> {
		const url = `${environment.apiBase}/internments/masterdata/allergy/verification`;
		return this.http.get<[]>(url);
	}

	getAllergyCategories(): Observable<MasterDataInterface<string>[]> {
		const url = `${environment.apiBase}/internments/masterdata/allergy/category`;
		return this.http.get<[]>(url);
	}

	getHealthClinical(): Observable<any[]> {
		const url = `${environment.apiBase}/internments/masterdata/health/clinical`;
		return this.http.get<any[]>(url);
	}

	getHealthClinicalDown(): Observable<any[]> {
		const url = `${environment.apiBase}/internments/masterdata/health/clinical/down`;
		return this.http.get<any[]>(url);
	}

	getHealthVerification(): Observable<MasterDataInterface<string>[]> {
		const url = `${environment.apiBase}/internments/masterdata/health/verification`;
		return this.http.get<[]>(url);
	}

	getHealthVerificationDown(): Observable<MasterDataInterface<string>[]> {
		const url = `${environment.apiBase}/internments/masterdata/health/verification/down`;
		return this.http.get<[]>(url);
	}

	getInmunizationClinical(): Observable<MasterDataInterface<string>[]> {
		const url = `${environment.apiBase}/internments/masterdata/inmunization`;
		return this.http.get<[]>(url);
	}

	getMedicationClinical(): Observable<MasterDataInterface<string>[]> {
		const url = `${environment.apiBase}/internments/masterdata/medication`;
		return this.http.get<[]>(url);
	}

	getBloodTypes(): Observable<MasterDataInterface<string>[]> {
		const url = `${environment.apiBase}/internments/masterdata/bloodtypes`;
		return this.http.get<[]>(url);
	}

	getClinicalSpecialty(): Observable<MasterDataInterface<string>[]> {
		const url = `${environment.apiBase}/internments/masterdata/clinical/specialty`;
		return this.http.get<any[]>(url);
	}

	getDischargeType(): Observable<MasterDataInterface<string>[]> {
		const url = `${environment.apiBase}/internments/masterdata/discharge/type`;
		return this.http.get<any[]>(url);
	}

	getAllergyCriticality(): Observable<any[]> {
		const url = `${environment.apiBase}/internments/masterdata/allergy/criticality`;
		return this.http.get<any[]>(url);
	}

	getHealthSeverity(): Observable<any[]> {
		const url = `${environment.apiBase}/internments/masterdata/health/severity`;
		return this.http.get<any[]>(url);
	}

	getVias(): Observable<any[]> {
		const url = `${environment.apiBase}/internments/masterdata/vias`;
		return this.http.get<any[]>(url);
	}

	getViasPharmaco(): Observable<any[]> {
		const url = `${environment.apiBase}/internments/masterdata/vias-pharmaco`;
		return this.http.get<any[]>(url);
	}

	getViasPremedication(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/vias-premedication`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getViasAnestheticPlan(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/vias-anesthetic-plan`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getViasAnestheticAgent(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/vias-anesthetic-agent`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getViasNonAnestheticDrug(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/vias-non-anesthetic-drug`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getViasAntibioticProphylaxis(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/vias-antibiotic-prophylaxis`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getOtherIndicationTypes(): Observable<OtherIndicationTypeDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/other-indication-type`;
		return this.http.get<OtherIndicationTypeDto[]>(url);
	}

	getUnits(): Observable<any[]> {
		const url = `${environment.apiBase}/internments/masterdata/units`;
		return this.http.get<any[]>(url);
	}

	getDocumentTypes(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/document/type`;
		return this.http.get<MasterDataDto[]>(url);
	}

    public getAnestheticTechniqueTypes(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/anesthetic-technique`;
		return this.http.get<MasterDataDto[]>(url);
    }

    public getTrachealIntubationTypes(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/tracheal-intubation`;
		return this.http.get<MasterDataDto[]>(url);
    }

    public getBreathingTypes(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/breathing`;
		return this.http.get<MasterDataDto[]>(url);
    }

    public getCircuitTypes(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/circuit`;
		return this.http.get<MasterDataDto[]>(url);
    }

}
