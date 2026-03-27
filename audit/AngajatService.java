package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService(){}

    private static class Holder{
        private static final AngajatService instance = new AngajatService();
    }
    public static AngajatService getInstance(){
        return Holder.instance;
    }

    public void addAngajat(Angajat a){
        Angajat[] copyAngajati = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0 ,copyAngajati, 0, angajati.length);
        copyAngajati[angajati.length] = a;
        angajati = copyAngajati;
        System.out.println("Angajat adăugat: " + a.getNume());

        logAction("ADD", a.getNume());
    }

    public void printAll(){
        for(Angajat a : angajati){
            System.out.println(a);
        }
    }

    public void listBySalary(){
        System.out.println("--- Angajați după salariu (descrescător) ---");
        Angajat[] copyAngajati = angajati.clone();
        Arrays.sort(copyAngajati);

        for (int i = 0; i < copyAngajati.length; i++) {
            System.out.println((i + 1) + ". " + copyAngajati[i]);
        }

    }

    public void findByDepartment(String numeDept){

        logAction("FIND_BY_DEPT", numeDept);

        System.out.println("--- Angajați din " + numeDept + " ---");
        int ok = 0;
        for(Angajat a: angajati){
            if(a.getDepartment().nume().equalsIgnoreCase(numeDept)){
                System.out.println(a);
                ok = 1;
            }
        }
        if(ok == 0){
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }

    private void logAction(String action, String target){
        AuditEntry auditEntry = new AuditEntry(action, target, LocalDateTime.now().toString());

        AuditEntry[] copyAuditLog = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0 , copyAuditLog, 0 , auditLog.length);
        copyAuditLog[auditLog.length] = auditEntry;
        auditLog = copyAuditLog;
    }

    public void printAuditLog(){
        System.out.println("--- Audit Log ---");
        for(AuditEntry entry: auditLog){
            System.out.println(entry);
        }
    }

}
