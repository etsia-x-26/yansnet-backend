package com.etsia.common.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "batches", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "batches_name_key", columnNames = {"name"})
})
@Builder
@AllArgsConstructor
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "departement", nullable = false, length = 100)
    private String departement;

    @NotNull
    @ColumnDefault("2026")
    @Column(name = "end_year", nullable = false)
    private Integer endYear;

    public Batch() {

    }
}