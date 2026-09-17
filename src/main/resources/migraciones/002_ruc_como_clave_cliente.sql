-- Migración: reemplaza cliente.idCliente (int AUTO_INCREMENT) por cliente.ruc
-- (varchar(10) cargado por el usuario) y propaga el cambio a venta, que pasa a
-- referenciar al cliente por RUC en lugar de por ID.
--
-- Como el RUC deja de ser un número correlativo, ya no tiene sentido renumerar
-- clientes al eliminar uno: por eso se quitaron Clientes.renumerarDespuesDeEliminar()
-- y su ajuste de AUTO_INCREMENT, que sí seguían vigentes en la migración 001.
--
-- venta.idVenta no se toca: sigue siendo AUTO_INCREMENT y es el número de factura.

-- 1. Quitar la FK que depende de cliente.idCliente
ALTER TABLE venta DROP FOREIGN KEY fk_Venta_Cliente1;

-- 2. Agregar ruc en cliente y copiar los valores existentes (idCliente -> ruc)
ALTER TABLE cliente ADD COLUMN ruc varchar(10) NULL AFTER idCliente;
UPDATE cliente SET ruc = idCliente;
ALTER TABLE cliente MODIFY ruc varchar(10) NOT NULL;

-- 3. Agregar ruc en venta y copiarlo desde cliente
ALTER TABLE venta ADD COLUMN ruc varchar(10) NULL AFTER idCliente;
UPDATE venta v JOIN cliente c ON v.idCliente = c.idCliente SET v.ruc = c.ruc;
ALTER TABLE venta MODIFY ruc varchar(10) NOT NULL;

-- 4. Quitar idCliente de cliente y dejar ruc como PK (también elimina el AUTO_INCREMENT viejo)
ALTER TABLE cliente
  DROP PRIMARY KEY,
  DROP KEY idCliente_UNIQUE,
  DROP COLUMN idCliente,
  ADD PRIMARY KEY (ruc),
  ADD UNIQUE KEY ruc_UNIQUE (ruc);

-- 5. Quitar idCliente de venta y dejar la PK compuesta como (idVenta, ruc)
ALTER TABLE venta
  DROP PRIMARY KEY,
  DROP COLUMN idCliente,
  ADD PRIMARY KEY (idVenta, ruc),
  ADD KEY fk_Venta_Cliente1_idx (ruc);

-- 6. Volver a crear la FK apuntando a cliente.ruc.
--    ON UPDATE CASCADE (igual que en la migración 001) para que se pueda corregir
--    un RUC mal tipeado desde Administrar clientes sin romper las ventas del cliente.
ALTER TABLE venta ADD CONSTRAINT fk_Venta_Cliente1
    FOREIGN KEY (ruc) REFERENCES cliente (ruc)
    ON DELETE NO ACTION ON UPDATE CASCADE;
