package com.stew.expense_tracker.CustomExceptions;


public class UserAlreadyExists extends Exception{

    public UserAlreadyExists (String message) {
        super(message);
    }

}
