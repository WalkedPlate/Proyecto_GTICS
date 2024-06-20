package com.example.proyecto_gtics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CantProductPorOrdenClase implements CantidadProductosPorOrden {
    private Integer idOrdenes;
    private Integer estadoOrden;
    private Integer sedesIdSedes;
    private Integer productosIdProductos;
    private Integer cantidad;
}
