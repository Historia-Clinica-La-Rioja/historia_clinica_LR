import { TestBed } from '@angular/core/testing';

import { TriageService } from './triage.service';

describe('TriageService', () => {
  let service: TriageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TriageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
