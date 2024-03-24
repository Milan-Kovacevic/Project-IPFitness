export interface LoginResponse {
    userId: number,
    username: string,
    firstName: string,
    lastName: string,
    avatar?: string
    active: boolean,
    token: string
}
