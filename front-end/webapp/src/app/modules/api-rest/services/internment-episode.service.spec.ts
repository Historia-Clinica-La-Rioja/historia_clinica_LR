import { TestBed } from '@angular/core/testing';

import { InternmentEpisodeService } from './internment-episode.service';

describe('InternmentEpisodeService', () => {
  let service: InternmentEpisodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InternmentEpisodeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
