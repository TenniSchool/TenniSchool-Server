package com.TenniSchool.tenniSchool.service;

import com.TenniSchool.tenniSchool.controller.dto.request.UserLoginRequestDto;
import com.TenniSchool.tenniSchool.controller.dto.request.UserRequestDto;
import com.TenniSchool.tenniSchool.controller.dto.response.UserResponseDto;
import com.TenniSchool.tenniSchool.domain.User;
import com.TenniSchool.tenniSchool.exception.Error;
import com.TenniSchool.tenniSchool.exception.model.BadRequestException;
import com.TenniSchool.tenniSchool.exception.model.ConflictException;
import com.TenniSchool.tenniSchool.exception.model.NotFoundException;
import com.TenniSchool.tenniSchool.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto create(UserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(Error.ALREADY_EXIST_USER_EXCEPTION, Error.ALREADY_EXIST_USER_EXCEPTION.getMessage());
        }
        User newUser = User.builder()
                .nickName(request.getNickname())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        userRepository.save(newUser);

        return UserResponseDto.of(newUser.getId(), newUser.getNickName());
    }

    @Transactional
    public Long login(UserLoginRequestDto request){
        User user = userRepository.findByEmail((request.getEmail()))
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage()));
        if (!user.getPassword().equals(request.getPassword())){
            throw new BadRequestException(Error.INVALID_PASSWORD_EXCEPTION, Error.INVALID_PASSWORD_EXCEPTION.getMessage());
        }
        return user.getId();
    }

}
