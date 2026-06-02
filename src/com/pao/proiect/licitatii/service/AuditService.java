package com.pao.proiect.licitatii.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instanta;
    private final DateTimeFormatter formatator;

    private AuditService() {
        this.formatator = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static synchronized AuditService getInstanta() {
        if (instanta == null) {
            instanta = new AuditService();
        }
        return instanta;
    }

    public synchronized void scrieActiune(String numeActiune) {
        try (FileWriter scriitor = new FileWriter("audit.csv", true)) {
            String moment = LocalDateTime.now().format(formatator);
            scriitor.write(numeActiune + "," + moment + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
