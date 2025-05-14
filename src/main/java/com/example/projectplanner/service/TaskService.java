package com.example.projectplanner.service;

import com.example.projectplanner.entity.Task;
import com.example.projectplanner.entity.User;
import com.example.projectplanner.repository.TaskRepository;
import com.example.projectplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;

import java.util.List;

@Service
@Validated
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Helper: Ambil user dari Authentication
    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // Ambil semua task milik user login
    public List<Task> getTasksByUser(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return taskRepository.findByUser(user);
    }

    // Ambil satu task, hanya jika milik user login
    public Task getTaskById(@NotNull Long taskId, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not allowed to access this task");
        }
        return task;
    }

    // Buat task baru (untuk user login)
    public Task createTask(@Valid @NotNull Task task, Authentication authentication) {
        User user = getCurrentUser(authentication);
        task.setUser(user);
        return taskRepository.save(task);
    }

    // Update task (hanya jika milik user login)
    public Task updateTask(@NotNull Long taskId, @Valid @NotNull Task updatedTask, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not allowed to update this task");
        }
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setPriority(updatedTask.getPriority());
        task.setStatus(updatedTask.getStatus());
        task.setDueDate(updatedTask.getDueDate());
        return taskRepository.save(task);
    }

    // Hapus task (hanya jika milik user login)
    public void deleteTask(@NotNull Long taskId, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not allowed to delete this task");
        }
        taskRepository.delete(task);
    }
}