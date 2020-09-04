import { TestBed } from '@angular/core/testing';

import { AgendaHorarioService } from './agenda-horario.service';

describe('NewAgendaService', () => {
  let service: AgendaHorarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgendaHorarioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
