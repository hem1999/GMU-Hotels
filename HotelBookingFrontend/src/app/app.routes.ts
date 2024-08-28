import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { RoomsComponent } from './rooms/rooms.component';
import {AppComponent} from './app.component'
import { ProfileComponent } from './profile/profile.component';
import { AcitvateAccountComponent } from './acitvate-account/acitvate-account.component';
import { AddRoomComponent } from './add-room/add-room.component';
import { SingleRoomComponent } from './single-room/single-room.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { ThankyouComponent } from './thankyou/thankyou.component';
import { UpdateRoomComponent } from './update-room/update-room.component';

export const routes: Routes = [
    {
        path:"addRoom",
        component: AddRoomComponent

    },
    {
        path:"updateRoom/:roomId",
        component: UpdateRoomComponent
    },
    {
        path:"activate-account",
        component: AcitvateAccountComponent

    },
    {
        path:"login",
        component: LoginComponent

    },
    {
        path: "signup",
        component: RegistrationComponent
    },
    {
        path: "rooms",
        component: RoomsComponent
    },
    {
        path: "profile/:userId",
        component: ProfileComponent
    },
    {
        path: "room/:roomId",
        component: SingleRoomComponent
    },
    {
        path: "checkout",
        component: CheckoutComponent
    },
    {
        path: "thankyou",
        component: ThankyouComponent
    }
];
