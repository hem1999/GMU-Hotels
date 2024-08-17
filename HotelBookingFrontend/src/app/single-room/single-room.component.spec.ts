import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleRoomComponent } from './single-room.component';

describe('SingleRoomComponent', () => {
  let component: SingleRoomComponent;
  let fixture: ComponentFixture<SingleRoomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SingleRoomComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SingleRoomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
