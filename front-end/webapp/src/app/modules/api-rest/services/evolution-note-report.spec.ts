import { TestBed } from '@angular/core/testing';

import { EvolutionNoteReportService } from './evolution-note-report.service';

describe('EvolutionNoteReportService', () => {
  let service: EvolutionNoteReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvolutionNoteReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
