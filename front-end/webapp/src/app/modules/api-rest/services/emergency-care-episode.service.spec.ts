import { TestBed } from '@angular/core/testing';

import { EmergencyCareEpisodeService } from './emergency-care-episode.service';

describe('EmergencyCareEpisodeService', () => {
  let service: EmergencyCareEpisodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmergencyCareEpisodeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
