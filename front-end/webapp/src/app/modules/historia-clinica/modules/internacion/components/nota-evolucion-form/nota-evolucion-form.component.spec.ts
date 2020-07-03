import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotaEvolucionFormComponent } from './nota-evolucion-form.component';

describe('NotaEvolucionFormComponent', () => {
  let component: NotaEvolucionFormComponent;
  let fixture: ComponentFixture<NotaEvolucionFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NotaEvolucionFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotaEvolucionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
