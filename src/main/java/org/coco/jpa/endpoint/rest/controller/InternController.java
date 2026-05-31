package org.coco.jpa.endpoint.rest.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.coco.jpa.endpoint.rest.mapper.InternMapper;
import org.coco.jpa.model.Intern;
import org.coco.jpa.model.dto.InternDto;
import org.coco.jpa.service.InternService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interns")
@AllArgsConstructor
public class InternController {

  private final InternService internService;
  private final InternMapper internMapper;

  @GetMapping
  public ResponseEntity<List<InternDto>> getAllInterns(
      @RequestParam(defaultValue = "0") int _start,
      @RequestParam(defaultValue = "10") int _end,
      @RequestParam(required = false) String _sort,
      @RequestParam(defaultValue = "ASC") String _order,
      @RequestParam(required = false) String department,
      @RequestParam(required = false) Boolean isRemunerate,
      @RequestParam(required = false) Long managerId,
      HttpServletResponse response) {

    int page = _start / (_end - _start);
    int size = _end - _start;

    Sort.Direction direction = Sort.Direction.fromString(_order.toUpperCase());
    String sortField = (_sort != null && !_sort.isEmpty()) ? _sort : "id";
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

    List<Intern> interns = internService.findAllByFilters(department, isRemunerate, managerId, pageable);
    long total = internService.countByFilters(department, isRemunerate, managerId);

    response.setHeader("X-Total-Count", String.valueOf(total));
    response.setHeader("Access-Control-Expose-Headers", "X-Total-Count");

    List<InternDto> dtos = interns.stream().map(internMapper::toRest).toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping(params = "id")
  public ResponseEntity<List<InternDto>> getInternsByIds(@RequestParam List<Long> id) {
    List<Intern> interns = internService.findAllByIds(id);
    List<InternDto> dtos = interns.stream().map(internMapper::toRest).toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/manager/{managerId}")
  public ResponseEntity<List<InternDto>> getInternsByManager(@PathVariable Long managerId) {
    List<Intern> interns = internService.findByManagerId(managerId);
    List<InternDto> dtos = interns.stream().map(internMapper::toRest).toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<InternDto> getInternById(@PathVariable Long id) {
    if (!internService.exists(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(internMapper.toRest(internService.findById(id)));
  }

  @PostMapping
  public ResponseEntity<InternDto> createIntern(@Valid @RequestBody Intern intern) {
    Intern saved = internService.create(intern);
    return ResponseEntity.status(HttpStatus.CREATED).body(internMapper.toRest(saved));
  }

  @PutMapping("/{id}")
  public ResponseEntity<InternDto> updateIntern(
      @PathVariable Long id, @Valid @RequestBody Intern intern) {
    if (!internService.exists(id)) {
      return ResponseEntity.notFound().build();
    }
    Intern saved = internService.update(id, intern);
    return ResponseEntity.ok(internMapper.toRest(saved));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<InternDto> patchIntern(
      @PathVariable Long id, @RequestBody Map<String, Object> updates) {
    if (!internService.exists(id)) {
      return ResponseEntity.notFound().build();
    }
    Intern saved = internService.partialUpdate(id, updates);
    return ResponseEntity.ok(internMapper.toRest(saved));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteIntern(@PathVariable Long id) {
    if (!internService.exists(id)) {
      return ResponseEntity.notFound().build();
    }
    internService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
