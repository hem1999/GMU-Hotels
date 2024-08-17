import { Component, inject, signal, WritableSignal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Room } from '../rooms/room.model';
import { HotelApiService } from '../services/hotel-api.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
@Component({
  selector: 'app-single-room',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './single-room.component.html',
  styleUrl: './single-room.component.css'
})
export class SingleRoomComponent {

  hotelApi = inject(HotelApiService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  authService = inject(AuthService);
  currentRoomId: String | null = null;
  currentRoom: WritableSignal<Room|null> = signal(null); 
  isRoomAvailable: boolean = false;
  availabilityForm = new FormGroup({
    startDate: new FormControl('',Validators.required),
    endDate: new FormControl('',Validators.required)
  })

  ngOnInit(){
    this.route.paramMap.subscribe(
      (params) => this.currentRoomId = params.get("roomId")
    );

    this.getRoomDetails();
  }

  getRoomDetails(){
    this.hotelApi.getRoom(this.currentRoomId)?.subscribe(
      {
        next: res => {
          this.currentRoom.set(res);
        },
        error: err => console.log(err)
      }
    )
  }

  onCheckAvailabilitySubmit(){
    //If not loggedIn redirect to the loginPage!
    if(this.authService.getUserId() == null){
      this.router.navigateByUrl("login");
      return ;
    }
    let formValue = this.availabilityForm.value;
    const d1 = formValue.startDate;
    const d2 = formValue.endDate;
    if(d1 == "" || d2 == ""){
      alert("Please fill dates before you submit the dates");
      return ;
    }
    if(d1 && d2 && (new Date(d1) > new Date(d2))){
      alert("End Date can't be before start Date");
      this.availabilityForm.reset();
      return ;
    }
    this.hotelApi.getIsRoomAvailable(this.currentRoomId,formValue.startDate??'',formValue.endDate??'').subscribe(
      {
        next: res => {
          if(!res){
            alert("Room for the given dates is not available");
          }else{
            
          this.isRoomAvailable = res;
          }
          //Later do as per the cart Service!
        },
        error: err => console.log(err)
      }
    );

  }
}
