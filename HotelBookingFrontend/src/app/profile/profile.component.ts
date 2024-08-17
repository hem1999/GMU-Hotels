import {
  Component,
  OnInit,
  WritableSignal,
  inject,
  signal,
} from '@angular/core';
import { ListBookingsComponent } from '../list-bookings/list-bookings.component';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { HotelApiService } from '../services/hotel-api.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ListBookingsComponent, CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  route = inject(ActivatedRoute);
  hotelApi = inject(HotelApiService);
  router = inject(Router);
  currentUserId: String | null = null;
  currentUserProfile: WritableSignal<any> = signal({}); //TODO: Later declare the profile type
  userType:String = "USER";
  //For booking button
  showBookings = false;
  bookingButtonText = 'My Bookings';
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

  ngOnInit(): void {
    //Getting userId from route
    this.route.paramMap.subscribe(
      (params) => (this.currentUserId = params.get('userId'))
    );
    console.log("at ",this.currentUserId);
    //Getting the current userdetails into the signal
    this.getProfile(this.currentUserId);
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
      next: res => {
        alert("Update Successful!")
        this.toggleUpdateForm();
      },
      error: err =>{
        alert(err);
      }
    })

    this.getProfile(this.currentUserId);

  }

  getProfile(userId: String | null) {
    if (userId == null) return;
    let obs = this.hotelApi.getUserDetails(this.currentUserId).subscribe({
      next: (res) => {
        console.log(res);
        this.currentUserProfile.set(res);

        if(this.currentUserProfile().userType == "ADMIN"){
          this.userType = 'ADMIN';
        }

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

  toggleBookings() {
    if (this.showUpdateForm) {
      this.toggleUpdateForm();
    }
    this.showBookings = !this.showBookings;
    this.bookingButtonText = 'Back';
    if (!this.showBookings) {
      this.bookingButtonText = 'My Bookings';
    }
  }

  toggleUpdateForm() {
    if (this.showBookings) {
      this.toggleBookings();
    }
    this.showUpdateForm = !this.showUpdateForm;
    this.updateButtonText = 'Back';
    if (!this.showUpdateForm) {
      this.updateButtonText = 'Update';
    }
  }
}
