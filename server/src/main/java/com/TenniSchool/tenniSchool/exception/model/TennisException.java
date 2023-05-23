package com.TenniSchool.tenniSchool.exception.model;

import com.TenniSchool.tenniSchool.exception.Error;

public class TennisException extends RuntimeException{
    private final Error error;
    public TennisException(Error error, String message){
        super(message);
        this.error = error;
    }

    public int getHttpStatus(){
        return error.getHttpStatusCode();
    }
}
