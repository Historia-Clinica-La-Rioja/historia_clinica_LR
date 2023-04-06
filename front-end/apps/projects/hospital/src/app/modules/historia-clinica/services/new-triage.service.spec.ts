import { TestBed } from '@angular/core/testing';

import { NewTriageService } from './new-triage.service';

describe('NewTriageService', () => {
  let service: NewTriageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NewTriageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
