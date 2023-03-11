package com.swivel.ignite.reporting.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Report entity
 */
@Entity
@Table(name = "report")
@NoArgsConstructor
@Getter
@Setter
public class Report implements Serializable {

    @Transient
    private static final String REPORT_ID_PREFIX = "rid-";

    @Id
    private String id;
    @Column(nullable = false)
    private String tuitionId;
    @Column(nullable = false)
    private String month;
    @Column(nullable = false)
    private boolean isPaid;
    @ElementCollection
    private Set<String> studentId = new HashSet<>();

    public Report(String tuitionId, String month, boolean isPaid) {
        this.id = REPORT_ID_PREFIX + UUID.randomUUID();
        this.tuitionId = tuitionId;
        this.month = month;
        this.isPaid = isPaid;
    }

    public void addStudentId(String studentId) {
        this.studentId.add(studentId);
    }

    public void removeStudentId(String studentId) {
        this.studentId.remove(studentId);
    }
}
