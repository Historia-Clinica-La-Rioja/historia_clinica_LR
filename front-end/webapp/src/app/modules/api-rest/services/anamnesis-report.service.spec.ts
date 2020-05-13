import { TestBed } from '@angular/core/testing';

import { AnamnesisReportService } from './anamnesis-report.service';

describe('AnamnesisReportService', () => {
  let service: AnamnesisReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnamnesisReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
