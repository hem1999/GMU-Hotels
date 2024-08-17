export type loginRequest = {
    "username": string;
    "password": string;
}

export type loginResponse = {
    "jwtToken": string;
    "userId": string;
    "userType": string;
}