import { TestBed } from '@angular/core/testing';

import { TriageDefinitionsService } from './triage-definitions.service';

describe('TriageDefinitionsService', () => {
  let service: TriageDefinitionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TriageDefinitionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
