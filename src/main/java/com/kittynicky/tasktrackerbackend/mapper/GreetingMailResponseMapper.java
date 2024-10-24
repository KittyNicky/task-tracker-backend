package com.kittynicky.tasktrackerbackend.mapper;

import com.kittynicky.tasktrackerbackend.database.entity.User;
import com.kittynicky.tasktrackerbackend.dto.GreetingMailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GreetingMailResponseMapper implements Mapper<User, GreetingMailResponse> {

    @Override
    public GreetingMailResponse map(User from) {
        GreetingMailResponse greetingMailResponse = new GreetingMailResponse();

        greetingMailResponse.setEmail(from.getEmail());
        greetingMailResponse.setText(String.format(greetingMailResponse.getText(), from.getUsername()));

        return greetingMailResponse;
    }
}
