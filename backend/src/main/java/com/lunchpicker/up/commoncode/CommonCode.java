package com.lunchpicker.up.commoncode;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "common_codes")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class CommonCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_group", nullable = false, length = 30)
    private String codeGroup;

    @Column(nullable = false, length = 40)
    private String code;

    @Column(nullable = false, length = 50)
    private String label;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
