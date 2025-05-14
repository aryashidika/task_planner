package com.example.projectplanner.controller;

import com.example.projectplanner.entity.Task;
import com.example.projectplanner.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getTasksByUser(Authentication authentication) {
        return taskService.getTasksByUser(authentication);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id, Authentication authentication) {
        return taskService.getTaskById(id, authentication);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication authentication) {
        return taskService.createTask(task, authentication);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask, Authentication authentication) {
        return taskService.updateTask(id, updatedTask, authentication);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, Authentication authentication) {
        taskService.deleteTask(id, authentication);
    }
}
