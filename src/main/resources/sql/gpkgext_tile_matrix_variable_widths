CREATE TABLE 'gpkgext_tile_matrix_variable_widths' (
  id INTEGER PRIMARY KEY,
  tm_id INTEGER NOT NULL,
  zoom_level INTEGER NOT NULL,
  min_row INTEGER NOT NULL,
  max_row INTEGER NOT NULL,
  coalesce INTEGER NOT NULL,
  CONSTRAINT fk_etmvw_etm FOREIGN KEY (tm_id) REFERENCES gpkgext_tile_matrix(id)
);
