import { TestBed } from '@angular/core/testing';

import { NewEpisodeService } from './new-episode.service';

describe('NewEpisodeService', () => {
  let service: NewEpisodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NewEpisodeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
