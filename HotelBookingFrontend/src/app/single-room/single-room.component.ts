import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Room } from '../rooms/room.model';
import { HotelApiService } from '../services/hotel-api.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { CartService } from '../services/cart.service';
import { cartBooking } from '../checkout/cartItem.model';
@Component({
  selector: 'app-single-room',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './single-room.component.html',
  styleUrl: './single-room.component.css'
})
export class SingleRoomComponent implements OnInit{

  hotelApi = inject(HotelApiService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  authService = inject(AuthService);
  cartService = inject(CartService);
  currentRoomId: string | undefined = undefined;
  currentRoom: WritableSignal<Room|null> = signal(null); 
  isRoomAvailable: boolean = false;
  availabilityForm = new FormGroup({
    startDate: new FormControl('',Validators.required),
    endDate: new FormControl('',Validators.required)
  })

  ngOnInit(){
    this.route.paramMap.subscribe(
      (params) => {
        console.log(params);
        this.currentRoomId = params.get("roomId")?.toString()
        console.log(params.get("roomId")?.toString());
        console.log(this.currentRoomId);
      }
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
    // console.log(this.currentRoomId);
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
          //Later do as per the cart Service!
          const room = this.currentRoom();
          if(this.currentRoomId && formValue.startDate && formValue.endDate && room && room.pricePerNight){
            let newBooking: cartBooking = {
              "roomId": this.currentRoomId,
              "startDate": formValue.startDate,
              "endDate": formValue.endDate,
              "pricePerNight": room.pricePerNight,
              "roomName": room.roomName,
              "roomAddress": room.roomAddress,
              "roomCapacity": room.roomCapacity,
              "roomCity": room.roomCity,
              "roomZip": room.roomZip
            }
            this.cartService.addBooking(newBooking);
          }
          
          
          }
          
        },
        error: err => console.log(err)
      }
    );

  }
}
