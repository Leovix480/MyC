-- Migración: habilita ON UPDATE CASCADE en las FKs necesarias para poder
-- renumerar de forma segura los IDs de cliente, ingredientes, recetas y producto
-- (venta queda excluida a propósito: su ID es el número de factura ya impreso
-- en facturaMyC y no debe cambiar después de emitido).
--
-- No cambia ON DELETE (sigue en NO ACTION): un borrado que dejaría huérfanos
-- sigue estando bloqueado por la base de datos, igual que antes.
--
-- No afecta ninguna funcionalidad existente: ningún INSERT/UPDATE actual del
-- proyecto modifica su propia clave primaria, así que este cambio solo entra
-- en juego con el nuevo método renumerarDespuesDeEliminar().

-- cliente <- venta.idCliente
ALTER TABLE venta DROP FOREIGN KEY fk_Venta_Cliente1;
ALTER TABLE venta ADD CONSTRAINT fk_Venta_Cliente1
    FOREIGN KEY (idCliente) REFERENCES cliente (idCliente)
    ON DELETE NO ACTION ON UPDATE CASCADE;

-- producto <- detalle_venta.idProducto
ALTER TABLE detalle_venta DROP FOREIGN KEY fk_Producto_has_Venta_Producto1;
ALTER TABLE detalle_venta ADD CONSTRAINT fk_Producto_has_Venta_Producto1
    FOREIGN KEY (idProducto) REFERENCES producto (idProducto)
    ON DELETE NO ACTION ON UPDATE CASCADE;

-- ingredientes <- detalle_receta.idIngredientes
ALTER TABLE detalle_receta DROP FOREIGN KEY fk_Ingredientes_has_Recetas_Ingredientes;
ALTER TABLE detalle_receta ADD CONSTRAINT fk_Ingredientes_has_Recetas_Ingredientes
    FOREIGN KEY (idIngredientes) REFERENCES ingredientes (idIngredientes)
    ON DELETE NO ACTION ON UPDATE CASCADE;

-- recetas <- detalle_receta.idRecetas
ALTER TABLE detalle_receta DROP FOREIGN KEY fk_Ingredientes_has_Recetas_Recetas1;
ALTER TABLE detalle_receta ADD CONSTRAINT fk_Ingredientes_has_Recetas_Recetas1
    FOREIGN KEY (idRecetas) REFERENCES recetas (idRecetas)
    ON DELETE NO ACTION ON UPDATE CASCADE;

-- recetas <- producto.idRecetas
ALTER TABLE producto DROP FOREIGN KEY fk_Producto_Recetas1;
ALTER TABLE producto ADD CONSTRAINT fk_Producto_Recetas1
    FOREIGN KEY (idRecetas) REFERENCES recetas (idRecetas)
    ON DELETE NO ACTION ON UPDATE CASCADE;

-- Deliberadamente SIN cambios: fk_Producto_has_Venta_Venta1 (detalle_venta.idVenta -> venta.idVenta)
-- sigue en ON UPDATE NO ACTION porque venta.idVenta no se renumera.
