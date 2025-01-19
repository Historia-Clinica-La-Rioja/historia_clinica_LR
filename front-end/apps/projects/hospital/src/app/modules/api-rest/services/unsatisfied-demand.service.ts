import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UnsatisfiedAppointmentDemandDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UnsatisfiedDemandService {
  private baseUrl = `${environment.apiBase}/institution/${this.contextService.institutionId}/unsatisfied-demand`;

  constructor(private http: HttpClient,
    private contextService: ContextService) { }

  saveUnsatisfiedAppointmentDemand(registrationUnsatisfiedDemand: UnsatisfiedAppointmentDemandDto): Observable<number> {
    const url = this.baseUrl + '/register'
    return this.http.post<number>(url, registrationUnsatisfiedDemand);
  }
}
