import { TestBed } from '@angular/core/testing';

import { MedicationRequestService } from './medication-request.service';

describe('MedicationRequestService', () => {
  let service: MedicationRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicationRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
