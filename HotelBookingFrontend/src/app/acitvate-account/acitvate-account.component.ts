import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-acitvate-account',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './acitvate-account.component.html',
  styleUrl: './acitvate-account.component.css'
})
export class AcitvateAccountComponent {
  router = inject(Router);
  authService = inject(AuthService);

  otpForm =new FormGroup(
    {
      otp1: new FormControl('',Validators.required),
      otp2: new FormControl('',Validators.required),
      otp3: new FormControl('',Validators.required),
      otp4: new FormControl('',Validators.required),
      otp5: new FormControl('',Validators.required),
      otp6: new FormControl('',Validators.required),
    }
  )

  onSubmitActivateAccount(){
    let formValue = this.otpForm.value;
    let submittedActivatedCode = `${formValue.otp1}${formValue.otp2}${formValue.otp3}${formValue.otp4}${formValue.otp5}${formValue.otp6}`
    this.authService.invokeActivateAccount(submittedActivatedCode).subscribe({

      next: res => {
        console.log(res);
        // localStorage.setItem("hotel_jwt",res.)
        this.router.navigateByUrl("/login");
      },

      error: err => {
        alert("Something went wrong! Please try again later");
        console.log(err);
      }

    })
    
  }

}
