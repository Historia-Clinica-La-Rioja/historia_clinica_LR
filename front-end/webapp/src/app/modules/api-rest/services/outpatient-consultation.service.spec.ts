import { TestBed } from '@angular/core/testing';

import { OutpatientConsultationService } from './outpatient-consultation.service';

describe('OutpatientConsultationService', () => {
  let service: OutpatientConsultationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OutpatientConsultationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
