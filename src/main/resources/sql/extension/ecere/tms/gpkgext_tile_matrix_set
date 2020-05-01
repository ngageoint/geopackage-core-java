CREATE TABLE 'gpkgext_tile_matrix_set' (
  id INTEGER PRIMARY KEY,
  tms TEXT NOT NULL,
  description TEXT,
  uri TEXT NOT NULL,
  srs_id INTEGER NOT NULL,
  min_x DOUBLE NOT NULL,
  min_y DOUBLE NOT NULL,
  max_x DOUBLE NOT NULL,
  max_y DOUBLE NOT NULL,
  CONSTRAINT fk_etms_srs FOREIGN KEY (srs_id) REFERENCES gpkg_spatial_ref_sys (srs_id)
);
