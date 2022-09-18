import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnexosIIComponent } from './anexos-ii.component';

describe('AnexosIIComponent', () => {
  let component: AnexosIIComponent;
  let fixture: ComponentFixture<AnexosIIComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnexosIIComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AnexosIIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
