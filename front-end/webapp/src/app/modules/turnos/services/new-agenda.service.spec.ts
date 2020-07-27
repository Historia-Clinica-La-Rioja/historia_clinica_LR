import { TestBed } from '@angular/core/testing';

import { NewAgendaService } from './new-agenda.service';

describe('NewAgendaService', () => {
  let service: NewAgendaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NewAgendaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
