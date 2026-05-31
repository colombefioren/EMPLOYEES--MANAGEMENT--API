package org.coco.jpa.mail;

import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.util.List;
import org.coco.jpa.PojaGenerated;

@PojaGenerated
public record Email(
    InternetAddress to,
    List<InternetAddress> cc,
    List<InternetAddress> bcc,
    String subject,
    String htmlBody,
    List<File> attachments) {}
