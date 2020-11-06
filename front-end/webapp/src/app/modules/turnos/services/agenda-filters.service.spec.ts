import { TestBed } from '@angular/core/testing';

import { AgendaFiltersService } from './agenda-filters.service';

describe('AgendaFiltersService', () => {
  let service: AgendaFiltersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgendaFiltersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
