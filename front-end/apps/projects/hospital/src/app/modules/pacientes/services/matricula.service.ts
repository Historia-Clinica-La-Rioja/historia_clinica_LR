import { Injectable } from '@angular/core';
import { ValidatedLicenseNumberDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MatriculaService {

  licenseNumbers$ = new BehaviorSubject([]);

  constructor() { }

  setLicenseNumbers(newArray: ValidatedLicenseNumberDto[]) {
    this.licenseNumbers$.next(newArray);
  }

  getLicenseNumbers(): BehaviorSubject<ValidatedLicenseNumberDto[]> {
    return this.licenseNumbers$;
  }
}
