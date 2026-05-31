package org.coco.jpa.datastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ListGrouperTest {

  private final ListGrouper<String> grouper = new ListGrouper<>();

  @Test
  void should_group_list_by_size() {
    var result = grouper.apply(List.of("a", "b", "c", "d", "e"), 2);

    assertEquals(3, result.size());
    assertEquals(List.of("a", "b"), result.get(0));
    assertEquals(List.of("c", "d"), result.get(1));
    assertEquals(List.of("e"), result.get(2));
  }

  @Test
  void should_return_empty_when_input_empty() {
    var result = grouper.apply(List.of(), 3);

    assertTrue(result.isEmpty());
  }

  @Test
  void should_return_single_group_when_size_larger_than_list() {
    var result = grouper.apply(List.of("a", "b"), 10);

    assertEquals(1, result.size());
    assertEquals(List.of("a", "b"), result.get(0));
  }
}
