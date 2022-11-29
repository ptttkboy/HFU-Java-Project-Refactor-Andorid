package com.example.project_demo1.network;

public abstract class Result {

    public boolean isSuccess = false;
    private Result() {}
    // success
    public static class Success<T> extends Result {
        T data;

        public Success(T data) {
            this.data = data;
            super.isSuccess = true;
        }

        public T getData() {
            return data;
        }
    }

    // failure
    public static class Error extends Result {

        int status;
        String message;
        Long timeStamp;

        public Error() {
            super.isSuccess = false;
        }

        public Error(int status, String message, Long timeStamp) {
            this.status = status;
            this.message = message;
            this.timeStamp = timeStamp;
            super.isSuccess = false;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    ", timeStamp=" + timeStamp +
                    '}';
        }
    }
}
