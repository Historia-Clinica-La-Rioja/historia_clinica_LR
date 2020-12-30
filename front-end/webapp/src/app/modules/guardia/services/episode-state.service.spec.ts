import { TestBed } from '@angular/core/testing';

import { EpisodeStateService } from './episode-state.service';

describe('EpisodeStateService', () => {
  let service: EpisodeStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EpisodeStateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
