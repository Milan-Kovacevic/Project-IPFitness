import { ExerciseResponse } from "./exercise-response";

export interface DailyExercisesResponse {
    forDate: Date,
    exercises: ExerciseResponse[]
}
