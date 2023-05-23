package com.TenniSchool.tenniSchool.exception.model;

import com.TenniSchool.tenniSchool.exception.Error;

public class NotFoundException extends TennisException{
    public NotFoundException(Error error, String message){
        super(error, message);
    }
}
