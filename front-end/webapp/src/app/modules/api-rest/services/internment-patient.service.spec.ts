import { TestBed } from '@angular/core/testing';

import { InternmentPatientService } from './internment-patient.service';

describe('InternmentPatientService', () => {
  let service: InternmentPatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InternmentPatientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
