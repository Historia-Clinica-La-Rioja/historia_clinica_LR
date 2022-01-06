import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '@environments/environment';
import {Observable} from 'rxjs';
import {PrivateHealthInsuranceDto} from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class PrivateHealthInsuranceService {

	constructor(private http: HttpClient) {}

	getAll(): Observable<PrivateHealthInsuranceDto[]> {
		const url = `${environment.apiBase}/private-health-insurance`;
		return this.http.get<PrivateHealthInsuranceDto[]>(url);
	}
}
