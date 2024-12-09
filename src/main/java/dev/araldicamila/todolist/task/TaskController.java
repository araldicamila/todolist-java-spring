package dev.araldicamila.todolist.task;

import dev.araldicamila.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private  ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();

        System.out.println(currentDate + " " + taskModel.getStartAt());

        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date and end date must be greater than today's date.");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date must be greater than start's date.");
        }

        var taskCreated = this.taskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");

        return this.taskRepository.findByIdUser((UUID) idUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task id does not exist.");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not have permission to update this task.");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated =  this.taskRepository.save(task);

        return  ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }
}
