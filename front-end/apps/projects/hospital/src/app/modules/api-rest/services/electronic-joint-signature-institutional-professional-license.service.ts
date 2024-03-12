import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ElectronicJointSignatureInstitutionProfessionalDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ElectronicJointSignatureInstitutionalProfessionalLicenseService {
  private URL = `${environment.apiBase}` + '/institution/' + `${this.contextService.institutionId}` + `/electronic-joint-signature/get-current-institution-professionals`;
  constructor(private contextService: ContextService,
    private http: HttpClient) { }

  getInstitutionalProfessionalsLicense(): Observable<ElectronicJointSignatureInstitutionProfessionalDto[]> {
    return this.http.get<ElectronicJointSignatureInstitutionProfessionalDto[]>(this.URL);
  }
}
