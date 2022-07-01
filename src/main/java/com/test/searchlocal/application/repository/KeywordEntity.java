package com.test.searchlocal.application.repository;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "KEYWORD")
@DynamicInsert
@NoArgsConstructor
@Data
public class KeywordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keyword;
    private int count;
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime createdTime;
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime updatedTime;

    public KeywordEntity(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }

}

