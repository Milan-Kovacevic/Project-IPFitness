export interface RegisterRequest {
    firstName: string,
    lastName: string,
    username: string,
    password: string,
    city: string,
    mail: string,
    picture?: File
}
