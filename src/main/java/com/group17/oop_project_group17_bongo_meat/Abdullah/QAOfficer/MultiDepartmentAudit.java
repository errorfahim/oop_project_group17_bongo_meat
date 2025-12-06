package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class MultiDepartmentAudit implements Serializable {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> departments;
    private String auditType;
    private String observations;
    private LocalDate submissionDate;

    public MultiDepartmentAudit(LocalDate startDate, LocalDate endDate, List<String> departments, String auditType, String observations, LocalDate submissionDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.departments = departments;
        this.auditType = auditType;
        this.observations = observations;
        this.submissionDate = submissionDate;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public List<String> getDepartments() { return departments; }
    public String getAuditType() { return auditType; }
    public String getObservations() { return observations; }
    public LocalDate getSubmissionDate() { return submissionDate; }
}

