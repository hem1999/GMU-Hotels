import {
  ChangeDetectorRef,
  Component,
  OnInit,
  WritableSignal,
  inject,
  signal,
} from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { HotelApiService } from '../services/hotel-api.service';
import { AuthService } from '../services/auth.service';
import { Booking } from './booking.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [

    CommonModule,
    ReactiveFormsModule,
    RouterLink,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  route = inject(ActivatedRoute);
  hotelApi = inject(HotelApiService);
  router = inject(Router);
  authService = inject(AuthService);
  cdr = inject(ChangeDetectorRef);
  currentUserId: String | null = null;
  currentUserProfile: WritableSignal<any> = signal({}); //TODO: Later declare the profile type
  userType: String = 'USER';
  //For booking button
  //For update button
  showUpdateForm = false;
  updateButtonText = 'Update';
  // update form
  updateUserForm = new FormGroup({
    username: new FormControl('', Validators.required),
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    city: new FormControl('', Validators.required),
    zip: new FormControl('', Validators.required),
    state: new FormControl('', Validators.required),
    phone: new FormControl('', Validators.required),
    isAdmin: new FormControl(false, Validators.required),
    isUser: new FormControl(true, Validators.required),
    country: new FormControl('', Validators.required),
  });

  //updating booking form
  newStartDate = new FormControl('',Validators.required);
  newEndDate = new FormControl('',Validators.required);

  visibleBookings: Booking[] = [];
  activeTab: Number = 1;


  updatingBooking:Number | null = null;

  ngOnInit(): void {
    //If not authenticattion, redirect to the login page.
    if (!this.authService.checkLoginStatus()) {
      this.router.navigateByUrl('login');
    }
    //Getting userId from route
    this.route.paramMap.subscribe((params) => {
      this.currentUserId = params.get('userId');
      //Getting the current userdetails into the signal
      this.getProfile(this.currentUserId);
    });

    
  }

  onUpdateSubmit() {
    let formValue = this.updateUserForm.value;
    let request = {
      userId: this.currentUserId,
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      phone: formValue.phone,
      address: formValue.address,
      city: formValue.city,
      state: formValue.state,
      zip: formValue.zip,
      country: formValue.country,
    };

    let obs = this.hotelApi.userUpdate(request).subscribe({
      next: (res) => {
        alert('Update Successful!');
        this.toggleUpdateForm();
        this.getProfile(this.currentUserId);
      },
      error: (err) => {
        alert(err);
      },
    });
  }

  getProfile(userId: String | null) {
    if (userId == null) return;
    let obs = this.hotelApi.getUserDetails(this.currentUserId).subscribe({
      next: (res) => {
        this.currentUserProfile.set(res);

        console.log(this.currentUserProfile())

        this.cdr.markForCheck();

        if (this.currentUserProfile().userType == 'ADMIN') {
          this.userType = 'ADMIN';
        }

        this.visibleBookings = this.currentUserProfile().bookings.upcoming;

        this.updateUserForm.patchValue({
          username: this.currentUserProfile().username,
          firstName: this.currentUserProfile().firstName,
          lastName: this.currentUserProfile().lastName,
          phone: this.currentUserProfile().phone,
          address: this.currentUserProfile().address,
          city: this.currentUserProfile().city,
          zip: this.currentUserProfile().zip,
          state: this.currentUserProfile().state,
          country: this.currentUserProfile().country,
        });
      },
    });
  }

  

  toggleUpdateForm() {
    this.showUpdateForm = !this.showUpdateForm;
    this.updateButtonText = 'Back';
    if (!this.showUpdateForm) {
      this.updateButtonText = 'Update';
    }
  }

  logout() {
    this.authService.activateLogout();
    this.router.navigateByUrl('rooms');
  }

  onBookingsUpdate() {
      this.getProfile(this.currentUserId);
  }


  // Related to bookings:
  updateBookingsView(currentView:Number){
    this.activeTab = currentView;
    switch (currentView){
      case 2:
        this.visibleBookings = this.currentUserProfile().bookings.current;
        break;
      case 3:
        this.visibleBookings = this.currentUserProfile().bookings.previous;
        break;
      default:
        this.visibleBookings = this.currentUserProfile().bookings.upcoming;
    }

  }

  goToRoom(roomId:Number){
    this.router.navigateByUrl(`/room/${roomId}`);
  }

  updateBooking(booking: Booking){
    this.updatingBooking = booking.bookingId;
    this.newStartDate.setValue(booking.startDate.toString());
    this.newEndDate.setValue(booking.endDate.toString());
  }

  onClickUpdateBooking(booking: Booking){

    // this.hotelApi.updateBooking();
    let bookingRequest = {
      bookingId: booking.bookingId,
      roomId: booking.roomId,
      startDate: this.newStartDate.value??'',
      endDate: this.newEndDate.value??''
    }
    this.hotelApi.updateBooking(bookingRequest).subscribe(
      {
        next: res => {
          let text = "nothing";
          // console.log(res.status);
          if(res.status == 202){
            text = "Booking updated successfully!";
          }
          if(res.status == 400){
            text = "Dates have a conflict!";
          }

          alert(text);
          this.updatingBooking = null;
          this.getProfile(this.currentUserId);
        }
      }
    )
    

  }

  deleteBooking(booking: Booking){
    let obs = this.hotelApi.deleteBooking(booking.bookingId).subscribe({
      next: res => {
        alert("Successfully Cancelled Booking");
        this.getProfile(this.currentUserId);
      }
    })
  }
}
