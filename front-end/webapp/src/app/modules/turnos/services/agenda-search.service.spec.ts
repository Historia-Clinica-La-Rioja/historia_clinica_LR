import { TestBed } from '@angular/core/testing';

import { AgendaSearchService } from './agenda-search.service';

describe('agendaSearchService', () => {
  let service: AgendaSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgendaSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
