
package com.smartdev.ufoss.controller;

import com.smartdev.ufoss.converter.CourseConverter;
import com.smartdev.ufoss.dto.CourseDTO;
import com.smartdev.ufoss.entity.CourseEntity;
import com.smartdev.ufoss.repository.CoursesRepository;
import com.smartdev.ufoss.service.CourseService;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CoursesController {

    private final CourseService coursesService;

    @GetMapping("/categories/{category}/courses/{courseId}")
    public CourseEntity getCourseById(
            @PathVariable("category") String category,
            @PathVariable("courseId") UUID id) {

        return coursesService.findByIDAndCategory(id, category);
    }

    @PostMapping("/categories/{category}/courses")
    public CourseEntity addNewCourse(
            @PathVariable("category") String category,
            @RequestBody CourseDTO newCourse) {

        return coursesService.addByCategory(CourseConverter.toEntity(newCourse), category);
    }

    @DeleteMapping("/categories/{category}/courses/{courseId}")
    public void deleteCourseById(
            @PathVariable("category") String category,
            @PathVariable("courseId") UUID id) {

        coursesService.deleteByIdAndCategory(id, category);
    }

    @PutMapping("/categories/{category}/courses/{courseId}")
    public CourseEntity updateCourse(
            @PathVariable("category") String category,
            @PathVariable("courseId") UUID id,
            @RequestBody CourseDTO course) {

        return coursesService.updateByIdAndCategory(id, CourseConverter.toEntity(course), category);
    }

    CoursesRepository coursesRepository;

    @GetMapping("/categories/{category}/courses")
    public ResponseEntity<Map<String, Object>> findCoursesWithFilter(
            @PathVariable("category") String category,
            @RequestParam(value = "ratings", defaultValue = "0", required = false) Double ratings,
            @RequestParam(value = "criteria", defaultValue = "newest", required = false) String criteria,
            @RequestParam(value = "sortByPrice", defaultValue = "asc", required = false) String sortByPrice,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        Sort sort = Sort.by("price");
        sort = "asc".equalsIgnoreCase(sortByPrice) ? sort.ascending() : sort.descending();

        Pageable paging = PageRequest.of(page, size, sort);

        return coursesService.findCoursesWithFilter(category, ratings, criteria, sortByPrice, paging);
    }
}

