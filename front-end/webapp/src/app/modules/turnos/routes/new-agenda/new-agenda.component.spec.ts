import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewAgendaComponent } from './new-agenda.component';

describe('NewAgendaComponent', () => {
  let component: NewAgendaComponent;
  let fixture: ComponentFixture<NewAgendaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewAgendaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewAgendaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
