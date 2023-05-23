package com.TenniSchool.tenniSchool.exception.model;

import com.TenniSchool.tenniSchool.exception.Error;

public class BadRequestException extends TennisException{
    public BadRequestException(Error error, String message){
        super(error, message);
    }
}
