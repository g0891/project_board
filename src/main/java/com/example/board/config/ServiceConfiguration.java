package com.example.board.config;

import com.example.board.service.PersonService;
import com.example.board.service.ProjectService;
import com.example.board.service.ReleaseService;
import com.example.board.service.TaskService;
import com.example.board.service.implementation.PersonServiceImpl;
import com.example.board.service.implementation.ProjectServiceImpl;
import com.example.board.service.implementation.ReleaseServiceImpl;
import com.example.board.service.implementation.TaskServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public PersonService getPersonService() {
        return new PersonServiceImpl();
    }

    @Bean
    public ProjectService getProjectService() { return new ProjectServiceImpl(); }

    @Bean
    public ReleaseService getReleaseService() { return new ReleaseServiceImpl(); }

    @Bean
    public TaskService getTaskService() {return new TaskServiceImpl(); }

}
