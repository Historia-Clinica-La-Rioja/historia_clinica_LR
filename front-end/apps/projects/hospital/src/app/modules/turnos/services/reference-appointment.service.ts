import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReferenceAppointmentService {

  constructor(
    private readonly http: HttpClient,
    private readonly contextService: ContextService,
  ) { }

  associateReferenceAppointment(referenceId: number, appointmentId: number): Observable<boolean> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reference/appointment`;
    return this.http.post<boolean>(url, {
      params: {
        referenceId,
        appointmentId
      }
    });
  }
}
