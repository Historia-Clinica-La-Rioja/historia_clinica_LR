import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ColoredLabelComponent } from './colored-label.component';

describe('ColoredLabelComponent', () => {
  let component: ColoredLabelComponent;
  let fixture: ComponentFixture<ColoredLabelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ColoredLabelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ColoredLabelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
