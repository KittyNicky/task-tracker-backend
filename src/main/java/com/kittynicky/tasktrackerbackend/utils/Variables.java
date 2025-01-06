package com.kittynicky.tasktrackerbackend.utils;

public class Variables {
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String AUTH_HEADER_VALUE = "Bearer ";

    public static final String GREETING_MAIL_RESPONSE_SUBJECT = "Hello, it's a task-tracker-service";
    public static final String GREETING_MAIL_RESPONSE_TEXT = """
            <h1>Hello, %s!</h1>
            <br>
            <body>
            <h3>Welcome to the task-tracker-service!</h3>
            You have successfully registered in the service.
            <br> 
            Come on, create your first task!
            </body>
            """;
}