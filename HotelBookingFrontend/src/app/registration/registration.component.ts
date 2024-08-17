import { Component, inject } from '@angular/core';
import { FormGroup, ReactiveFormsModule, FormControl, Validators} from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { RegistrationRequest, UserType } from './registration.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {
  authService = inject(AuthService);


  signUpForm = new FormGroup(
    {
      username: new FormControl('',Validators.required),
      firstName: new FormControl('',Validators.required),
      lastName: new FormControl('',Validators.required),
      email: new FormControl('',Validators.required),
      password: new FormControl('',Validators.required),
      address:new FormControl('',Validators.required),
      city:new FormControl('',Validators.required),
      zip:new FormControl('',Validators.required),
      state:new FormControl('',Validators.required),
      phone:new FormControl('',Validators.required),
      userType: new FormControl('',Validators.required),
      country: new FormControl('',Validators.required)
    }
  )

  onClickSignup(){
    //form submission successful!
    //navigate to Login


    const formValue = this.signUpForm.value;
    let requestBody:RegistrationRequest = {
      "username" :formValue.username?? '',
      "firstname": formValue.firstName?? '',
      "lastname": formValue.lastName??'',
      "email":formValue.email??'',
      "password":formValue.password??'',
      "address": formValue.address??'',
      "city": formValue.city??'',
      "zip": formValue.zip??'',
      "state": formValue.state??'',
      "phone": formValue.phone??'',
      "country": formValue.country??'',
      "userType": formValue.userType=='ADMIN' ? UserType.ADMIN : UserType.USER
    }

    this.authService.postUserRegistration(requestBody).subscribe(
      {
        next: res => {
          alert("User registration successful, please confirm the email address")
        },
        error: err => {
          alert("Something went wrong! try again later");
        }
      }
    )
  }
}
