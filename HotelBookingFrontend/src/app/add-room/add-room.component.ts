import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators, FormsModule, FormControlName } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { HotelApiService } from '../services/hotel-api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-room',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './add-room.component.html',
  styleUrl: './add-room.component.css'
})
export class AddRoomComponent {

  httpClient = inject(HttpClient);
  authService = inject(AuthService);
  router = inject(Router);
  hotelApi = inject(HotelApiService);
  image: File | null = null;

  ngOnInit(){
    if(this.authService.getUserId() == null){
      this.router.navigateByUrl("login");
    }
  }
  
  addRoomForm = new FormGroup({
    "roomName": new FormControl('',Validators.required),
    "roomType":new FormControl('',Validators.required),
    "roomDescription":new FormControl('',Validators.required),
    "pricePerNight": new FormControl('',Validators.required),
    "roomCapacity": new FormControl('',Validators.required),
    //TODO: For the time being, I'm only taking the drive urls as inputs
    "mainImgSrc": new FormControl(null, Validators.required),
    // "img2": new FormControl(File),
    // "img3": new FormControl(File),
    // "img4": new FormControl(File),
    "country": new FormControl('',Validators.required),
    "zip": new FormControl('',Validators.required),
    "state": new FormControl('',Validators.required),
    "city": new FormControl('',Validators.required),
    "address": new FormControl('',Validators.required),
    "phone": new FormControl('',Validators.required)
  }
    
  );

  onSubmit(){

    //get creatorId from AuthService and add it to that!
    let newRoomValue = this.addRoomForm.value;
    const formData: FormData = new FormData();
    formData.append("roomName",newRoomValue.roomName??'');
    formData.append("roomType", newRoomValue.roomType??'');
    formData.append("roomDescription", newRoomValue.roomDescription??'');
    formData.append("roomCapacity", newRoomValue.roomCapacity??'');
    // formData.append("creatorId", this.authService.getUserId() );
    formData.append("pricePerNight", newRoomValue.pricePerNight??'');
    formData.append("roomAddress", newRoomValue.address??'');
    formData.append("roomCity", newRoomValue.city??'');
    formData.append("roomState", newRoomValue.state??'');
    formData.append("roomZip", newRoomValue.zip??'');
    formData.append("roomCountry", newRoomValue.country??'');
    formData.append("roomPhone", newRoomValue.phone??'');
    if(this.image){
      formData.append("image",this.image,this.image?.name);
    }

    console.log("Form Data");
    console.log(formData);

    this.hotelApi.postAddRoom(formData).subscribe({
      next: res => {
        alert("Room Added Succesfully");
      },
      error: err => console.log(err)
    })

    //After successful addition, move to all rooms
    this.router.navigateByUrl("rooms");
  }

  

  onFileChange(event:any){
    console.log(event);
    if(event.target.files.length > 0){
      const file = event.target.files[0];
      this.image = file;
    }
  }
}
