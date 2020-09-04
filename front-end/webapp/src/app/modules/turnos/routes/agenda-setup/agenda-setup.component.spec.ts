import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgendaSetupComponent } from './agenda-setup.component';

describe('NewAgendaComponent', () => {
  let component: AgendaSetupComponent;
  let fixture: ComponentFixture<AgendaSetupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgendaSetupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgendaSetupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
