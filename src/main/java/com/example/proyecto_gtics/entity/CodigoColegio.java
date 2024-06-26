package com.example.proyecto_gtics.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "codigo_colegio")
public class CodigoColegio implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo")
    private String codigo;


}
