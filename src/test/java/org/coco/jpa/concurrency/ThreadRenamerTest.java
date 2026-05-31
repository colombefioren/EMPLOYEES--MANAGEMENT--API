package org.coco.jpa.concurrency;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ThreadRenamerTest {

  @Test
  void rename_worker_thread_should_set_new_name() {
    var thread = Thread.currentThread();
    var originalName = thread.getName();

    ThreadRenamer.renameWorkerThread(thread);

    assertNotEquals(originalName, thread.getName());
    assertTrue(thread.getName().startsWith("w-"));
  }

  @Test
  void rename_frontal_thread_should_set_new_name() {
    var thread = Thread.currentThread();
    var originalName = thread.getName();

    ThreadRenamer.renameFrontalThread(thread);

    assertNotEquals(originalName, thread.getName());
    assertTrue(thread.getName().startsWith("f-"));
  }

  @Test
  void rename_thread_should_set_given_name() {
    var thread = Thread.currentThread();

    ThreadRenamer.renameThread(thread, "custom-name");

    assertEquals("custom-name", thread.getName());
  }

  @Test
  void get_random_sub_thread_name_prefix_should_use_parent_name() {
    var thread = Thread.currentThread();
    thread.setName("parent");

    var prefix = ThreadRenamer.getRandomSubThreadNamePrefixFrom(thread);

    assertTrue(prefix.startsWith("parent-"));
  }
}
