package com.example.proyecto_gtics.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

@Entity
@Getter
@Setter
@Table(name = "codigo_colegio")
public class CodigoColegio {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo")
    private String codigo;


}
