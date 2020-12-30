import { TestBed } from '@angular/core/testing';

import { EmergencyCareEpisodeStateService } from './emergency-care-episode-state.service';

describe('EmergencyCareEpisodeStateService', () => {
  let service: EmergencyCareEpisodeStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmergencyCareEpisodeStateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
