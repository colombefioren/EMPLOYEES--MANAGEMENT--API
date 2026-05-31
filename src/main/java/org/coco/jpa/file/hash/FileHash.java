package org.coco.jpa.file.hash;

import org.coco.jpa.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
