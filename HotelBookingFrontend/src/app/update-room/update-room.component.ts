import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { HotelApiService } from '../services/hotel-api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Room } from '../rooms/room.model';
AuthService;
@Component({
  selector: 'app-update-room',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './update-room.component.html',
  styleUrl: './update-room.component.css',
})
export class UpdateRoomComponent {
  authService = inject(AuthService);
  router = inject(Router);
  route = inject(ActivatedRoute);
  hotelApi = inject(HotelApiService);
  image: File | null = null;
  currentRoomDetails: Room | null = null;
  updateRoomForm = new FormGroup({
    roomName: new FormControl('', Validators.required),
    roomType: new FormControl('', Validators.required),
    roomDescription: new FormControl('', Validators.required),
    pricePerNight: new FormControl('', Validators.required),
    roomCapacity: new FormControl('', Validators.required),
    //TODO: For the time being, I'm only taking the drive urls as inputs
    mainImgSrc: new FormControl(null, Validators.required),
    country: new FormControl('', Validators.required),
    zip: new FormControl('', Validators.required),
    state: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    phone: new FormControl('', Validators.required),
  });
  curRoomId: string | null = null;

  ngOnInit() {
    if (this.authService.getUserId() == null) {
      this.router.navigateByUrl('login');
    }

    this.route.paramMap.subscribe((res) => {
      console.log(res.get('roomId'));
      this.curRoomId = res.get('roomId');
    });
    this.getRoom();
  }

  getRoom() {
    this.hotelApi.getRoom(this.curRoomId ?? '')?.subscribe({
      next: (res) => {
        this.currentRoomDetails = res;
        console.log(this.currentRoomDetails);
        //patch the form

        this.updateRoomForm.patchValue({
          roomName: this.currentRoomDetails.roomName.toString(),
          roomCapacity: this.currentRoomDetails.roomCapacity.toString(),
          roomDescription: this.currentRoomDetails.roomDescription.toString(),
          pricePerNight: this.currentRoomDetails.pricePerNight.toString(),
          
          roomType: this.currentRoomDetails.roomType.toString(),
          country: this.currentRoomDetails.roomCountry.toString(),
          zip: this.currentRoomDetails.roomZip.toString(),
          state: this.currentRoomDetails.roomState.toString(),
          city: this.currentRoomDetails.roomCity.toString(),
          address: this.currentRoomDetails.roomAddress.toString(),
          phone: this.currentRoomDetails.roomPhone.toString(),
        });
      },
    });
  }

  onSubmit() {
    //get creatorId from AuthService and add it to that!
    let newRoomValue = this.updateRoomForm.value;
    const formData: FormData = new FormData();
    formData.append('roomId',this.curRoomId??'');
    formData.append('roomName', newRoomValue.roomName ?? '');
    formData.append('roomType', newRoomValue.roomType ?? '');
    formData.append('roomDescription', newRoomValue.roomDescription ?? '');
    formData.append('roomCapacity', newRoomValue.roomCapacity ?? '');
    // formData.append("creatorId", this.authService.getUserId() );
    formData.append('pricePerNight', newRoomValue.pricePerNight ?? '');
    formData.append('roomAddress', newRoomValue.address ?? '');
    formData.append('roomCity', newRoomValue.city ?? '');
    formData.append('roomState', newRoomValue.state ?? '');
    formData.append('roomZip', newRoomValue.zip ?? '');
    formData.append('roomCountry', newRoomValue.country ?? '');
    formData.append('roomPhone', newRoomValue.phone ?? '');
    if (this.image) {
      formData.append('newImg', this.image, this.image?.name);
    }

    console.log('Form Data');
    console.log(formData);

    this.hotelApi.updateRoom(formData).subscribe({
      next: (res) => {
        alert('Room updated Succesfully');
        //After successful addition, move to all rooms
        this.router.navigateByUrl('rooms');
      },
      error: (err) => console.log(err),
    });
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.image = file;
    }
  }
}
