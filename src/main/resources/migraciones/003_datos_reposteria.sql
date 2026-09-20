-- Documentación (no ejecutar): datos de ingredientes y recetas de repostería cargados
-- directamente en el servidor remoto y en el local el 2026-09-20, con medidas y precios
-- de referencia (invento realista, editable desde la app en Ingredientes/Recetas).
--
-- No borra ni modifica ninguna fila existente: solo agrega ingredientes, 2 recetas
-- nuevas (Cookies, Torta de Chocolate) y enriquece con más ingredientes las recetas
-- que ya existían (Marmolada, y Brownie/Cupcake donde existían).

-- Ingredientes nuevos (precioIngredientes = precio del paquete/unidad indicado en el nombre)
INSERT INTO ingredientes (nombre, precioIngredientes, stockIngredientes, stockMinimo) VALUES
  ('Manteca-200g', 9000, 20, 8),
  ('ChocolateCobertura-500g', 32000, 15, 5),
  ('ChipsChocolate-200g', 18000, 12, 5),
  ('PolvoDeHornear-100g', 7000, 20, 8),
  ('EsenciaDeVainilla-30ml', 6000, 15, 5),
  ('Leche-1L', 8000, 20, 8),
  ('Avena-500g', 9000, 15, 5),
  ('NuezPicada-200g', 28000, 10, 4),
  ('Bicarbonato-100g', 5000, 15, 5),
  ('CocoRallado-200g', 15000, 10, 4);

-- Receta + producto: Cookies (galletas con chips de chocolate y nuez), precio 3000
INSERT INTO recetas (nombre, descripcion) VALUES ('Cookies', 'Galletas con chips de chocolate y nuez');
-- INSERT INTO producto (nombre, precio, idRecetas) VALUES ('Cookies', 3000, <idRecetas generado>);
-- INSERT INTO detalle_receta (idRecetas, idIngredientes, cantUso) VALUES
--   (<idCookies>, <idHarina500g>, 1), (<idCookies>, <idManteca>, 1), (<idCookies>, <idAzucar>, 1),
--   (<idCookies>, <idHuevo>, 1), (<idCookies>, <idChipsChocolate>, 1), (<idCookies>, <idPolvoHornear>, 1),
--   (<idCookies>, <idBicarbonato>, 1), (<idCookies>, <idNuezPicada>, 1);

-- Receta + producto: Torta de Chocolate (torta húmeda con cobertura), precio 55000
INSERT INTO recetas (nombre, descripcion) VALUES ('Torta de Chocolate', 'Torta humeda de chocolate con cobertura');
-- INSERT INTO producto (nombre, precio, idRecetas) VALUES ('Torta de Chocolate', 55000, <idRecetas generado>);
-- INSERT INTO detalle_receta (idRecetas, idIngredientes, cantUso) VALUES
--   (<idTorta>, <idHarina500g>, 1), (<idTorta>, <idChocolateCobertura>, 1), (<idTorta>, <idManteca>, 1),
--   (<idTorta>, <idHuevo>, 1), (<idTorta>, <idAzucar>, 1), (<idTorta>, <idLeche>, 1),
--   (<idTorta>, <idPolvoHornear>, 1), (<idTorta>, <idEsenciaVainilla>, 1);

-- Recetas existentes enriquecidas con ingredientes nuevos (solo INSERT en detalle_receta,
-- nada se borró ni editó): Marmolada +Manteca/PolvoDeHornear/Leche/EsenciaDeVainilla;
-- Brownie +ChocolateCobertura/Manteca/Azucar(x2)/ChipsChocolate (donde existía la receta);
-- Cupcake +Azucar/Huevo/Manteca/PolvoDeHornear/EsenciaDeVainilla (donde existía la receta).
