package com.TenniSchool.tenniSchool.exception.model;

import com.TenniSchool.tenniSchool.exception.Error;

public class ConflictException extends TennisException{
    public ConflictException(Error error, String message){
        super(error, message);
    }
}
