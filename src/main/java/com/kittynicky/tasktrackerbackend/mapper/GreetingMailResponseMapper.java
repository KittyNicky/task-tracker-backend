package com.kittynicky.tasktrackerbackend.mapper;

import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.dto.GreetingMailResponse;
import com.kittynicky.tasktrackerbackend.utils.Variables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GreetingMailResponseMapper implements Mapper<User, GreetingMailResponse> {

    @Override
    public GreetingMailResponse map(User from) {
        return GreetingMailResponse.builder()
                .subject(Variables.GREETING_MAIL_RESPONSE_SUBJECT)
                .email(from.getEmail())
                .text(String.format(Variables.GREETING_MAIL_RESPONSE_TEXT, from.getUsername()))
                .build();
    }
}
