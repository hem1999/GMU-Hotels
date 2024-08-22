import {Component, inject, OnInit, signal, Signal, WritableSignal} from '@angular/core';
import { Room } from './room.model';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { HotelApiService } from '../services/hotel-api.service';


type availabilityStatus = 'NOT_CHECKED' | 'AVAILABLE' | 'NOT_AVAILABLE' | 'CHECKED';

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './rooms.component.html',
  styleUrl: './rooms.component.css'
})
export class RoomsComponent implements OnInit{
  rooms: WritableSignal<Room[]> = signal<Room[]>([]);

  //injecting router to programatically navigate
  router = inject(Router);
  hotelApi = inject(HotelApiService);

  // Form control for startDate and endDate inputs to check for availability
  startDate = new FormControl('',Validators.required);
  endDate = new FormControl('',Validators.required);
  chkAvailabilityError:String = '';
  isAvailable:availabilityStatus  = 'NOT_CHECKED';
  // Form control for startDate and endDate inputs to fetch available rooms
  filterStartDate = new FormControl('', Validators.required);
  filterEndDate = new FormControl('', Validators.required);

  ngOnInit(){
    this.getRooms();
  }

  getRooms(){

    let allRooms: Room[]; 
    allRooms = [];
    this.hotelApi.getAllRooms().subscribe({
      next:  res => {
        this.rooms.set(res);
      }
    })
  }

  onCheckAvailability(currentRoomId: Number, roomName: String){
    if(this.startDate.value == "" || this.endDate.value == ""){
      alert("Please fill dates before you submit the dates");
      return ;
    }
    const d1 = this.startDate?.value;
    const d2 = this.endDate?.value;
    if(d1 && d2 && (new Date(d1) > new Date(d2))){
      alert("End Date can't be before start Date");
      this.startDate.setValue('');
      this.endDate.setValue('');
      return ;
    }
    this.isAvailable = 'NOT_AVAILABLE';

    //TODO: check for availability through api and finalize
    if(this.startDate.value != null && this.endDate.value!=null){
      this.hotelApi.getIsRoomAvailable(currentRoomId.toString(),this.startDate.value, this.endDate.value).subscribe({
        next: res => {
          if(res){
            
            alert(`${roomName} is available for selected dates.`);
            this.startDate.setValue('');
          }else{
            this.chkAvailabilityError= "Room is not available";
          }
        },
        error: err => console.log(err)
      })
    }
    
   
  }

  onDateBasedFilter(){
    console.log(this.filterStartDate);
    console.log(this.filterStartDate);
    //TODO: fetch on available rooms and update the signal that reflects in the view.
    this.rooms.set([...this.rooms().slice(2,5)]);
  }

  
  onClearDateFilter(){
    this.getRooms();
  }

  goToRoom(roomId:Number){
    this.router.navigateByUrl(`room/${roomId}`);
  }


}
