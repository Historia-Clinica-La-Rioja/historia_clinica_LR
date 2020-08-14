import { TestBed } from '@angular/core/testing';

import { HealthInsuranceService } from './health-insurance.service';

describe('HealthInsuranceService', () => {
  let service: HealthInsuranceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HealthInsuranceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
