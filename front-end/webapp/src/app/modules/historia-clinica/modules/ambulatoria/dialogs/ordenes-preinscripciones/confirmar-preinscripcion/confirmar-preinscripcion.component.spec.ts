import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmarPreinscripcionComponent } from './confirmar-preinscripcion.component';

describe('ConfirmarPreinscripcionComponent', () => {
  let component: ConfirmarPreinscripcionComponent;
  let fixture: ComponentFixture<ConfirmarPreinscripcionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmarPreinscripcionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmarPreinscripcionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
