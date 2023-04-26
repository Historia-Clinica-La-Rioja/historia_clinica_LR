import { TestBed } from '@angular/core/testing';

import { EmergencyCareStateService } from './emergency-care-state.service';

describe('EmergencyCareStateService', () => {
  let service: EmergencyCareStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmergencyCareStateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
