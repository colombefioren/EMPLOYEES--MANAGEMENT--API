package org.coco.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.coco.jpa.PojaGenerated;

@PojaGenerated
@Entity
@Getter
@Setter
public class DummyUuid {
  @Id private String id;
}
