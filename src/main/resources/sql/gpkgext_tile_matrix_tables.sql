CREATE TABLE 'gpkgext_tile_matrix_tables' (
  table_name TEXT PRIMARY KEY,
  tms_id INTEGER NOT NULL,
  min_level INTEGER NOT NULL,
  max_level INTEGER NOT NULL,
  CONSTRAINT fk_etmt_gc FOREIGN KEY (table_name) REFERENCES gpkg_contents(table_name)
  CONSTRAINT fk_etmt_etms FOREIGN KEY (tms_id) REFERENCES gpkgext_tile_matrix_set(id)
);
