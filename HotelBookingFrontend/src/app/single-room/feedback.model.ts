export type Feedback = {
    feedback: String;
    roomId: Number;
    userId: Number;
    username: String;
    rating: Number;
    feedbackDate: String;
}

export type addFeedbackType = {
    roomId: String;
    feedback: string;
    userId:String;
    rating: Number;
}