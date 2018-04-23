package edu.wpi.cs3733d18.teamF.controller;

public class ErrorSingleton {
    private Exception error;

    private ErrorSingleton(){
        error = null;
    }
    public static ErrorSingleton getInstance() {
        return LazyInitializer2.INSTANCE;
    }

    private static class LazyInitializer2 {
        static final ErrorSingleton INSTANCE = new ErrorSingleton();
    }

    public Exception getError(){
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }
}
