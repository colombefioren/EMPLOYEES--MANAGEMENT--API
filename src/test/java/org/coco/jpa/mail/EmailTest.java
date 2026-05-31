package org.coco.jpa.mail;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.mail.internet.InternetAddress;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void should_create_record() throws Exception {
    var to = new InternetAddress("test@test.com");
    var email = new Email(to, List.of(), List.of(), "Subject", "<b>Body</b>", List.of());

    assertEquals("test@test.com", email.to().getAddress());
    assertEquals("Subject", email.subject());
    assertEquals("<b>Body</b>", email.htmlBody());
    assertTrue(email.cc().isEmpty());
    assertTrue(email.bcc().isEmpty());
    assertTrue(email.attachments().isEmpty());
  }
}
