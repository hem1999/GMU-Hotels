/**
 * Represents the types of users in the system.
 */
export enum UserType {
    USER = 'USER',
    ADMIN = 'ADMIN'
}

/**
 * Represents the data required for user registration.
 */
export type RegistrationRequest = {
    firstname: string;
    lastname: string;
    username: string;
    email: string;
    password: string;
    phone: string;
    userType: UserType;
    address: string;
    city: string;
    zip: string;
    state: string;
    country: string;

}