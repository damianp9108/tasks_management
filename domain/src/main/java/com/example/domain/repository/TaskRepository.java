package com.example.domain.repository;

import com.example.domain.entity.Task;
import com.example.domain.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:title IS NULL OR t.title LIKE %:title%) AND " +
            "(:dueDate IS NULL OR t.dueDate = :dueDate)")
    List<Task> findByFilters(@Param("status") TaskStatus status,
                             @Param("title") String title,
                             @Param("dueDate") LocalDate dueDate);
}