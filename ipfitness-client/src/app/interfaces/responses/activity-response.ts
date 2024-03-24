export interface ActivityResponse {
    activityId: number,
    activityType: string,
    datePosted: string,
    trainingDuration: number,
    percentageCompleted: number,
    result: number,
    summary?: string,
}
