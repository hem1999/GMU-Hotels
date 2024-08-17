import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { loginRequest } from './loginRequest.model';
import { UserType } from '../registration/registration.model';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  // *******This is for a simple emailSignup form or single input***********
  // loginEmail = new FormControl('');
  // loginPassword = new FormControl(''); 
  // // you use something like this.loginEmail.setValue(<pass the value>)
  // onClickLogin(){
  //   console.log(this.loginEmail);
  //   console.log(this.loginPassword);
  // }

  authService = inject(AuthService);
  router = inject(Router)
  

  //*********Form Group*******
  loginForm = new FormGroup(
    {
      username: new FormControl('',Validators.required),
      password: new FormControl('',Validators.required)
    }
  )

  onClickLogin(){
    // console.log(this.loginForm);
    //TODO: check the conditions and setup the succesfulLogin Property.
    let loginFormValue = this.loginForm.value;
    let loginRequest: loginRequest = {
      "username": loginFormValue.username??'',
      "password": loginFormValue.password??''
    }

    this.authService.postUserAuthentication(loginRequest).subscribe({
      next: res => {
        // sessionStorage.setItem("hotel_jwtToken",res.jwtToken);
        // console.log(res);
        this.authService.setJwtToken(res.jwtToken);
        this.authService.setUserId(res.userId);
        this.authService.setRole(res.userType == 'ADMIN' ? UserType.ADMIN : UserType.USER);
        this.authService.activateSignIn();
        this.router.navigate(['rooms']);
      },
      error: err => {
        alert(err);
      }
    })
  }

}
