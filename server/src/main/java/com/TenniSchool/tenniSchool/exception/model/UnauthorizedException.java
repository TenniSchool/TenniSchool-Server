package com.TenniSchool.tenniSchool.exception.model;
import com.TenniSchool.tenniSchool.exception.Error;

public class UnauthorizedException extends TennisException{
    public UnauthorizedException(Error error, String message){
        super(error, message);
    }
}
