import { TestBed } from '@angular/core/testing';

import { EpicrisisReportService } from './epicrisis-report.service';

describe('EpicrisisReportService', () => {
  let service: EpicrisisReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EpicrisisReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
