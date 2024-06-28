package com.example.proyecto_gtics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDni {

    private String type;
    private Integer status;
    private String message;
    private Data data;

    @Getter
    @Setter
    public static class Data{
        private String dni;
        private String nombres;
        private String apellido_paterno;
        private String apellido_materno;
    }


}
