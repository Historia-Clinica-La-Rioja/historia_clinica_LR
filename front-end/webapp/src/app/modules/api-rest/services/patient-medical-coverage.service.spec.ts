import { TestBed } from '@angular/core/testing';

import { PatientMedicalCoverageService } from './patient-medical-coverage.service';

describe('PatientMedicalCoverageService', () => {
  let service: PatientMedicalCoverageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PatientMedicalCoverageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
