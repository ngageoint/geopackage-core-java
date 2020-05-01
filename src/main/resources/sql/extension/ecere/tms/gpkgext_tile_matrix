CREATE TABLE 'gpkgext_tile_matrix' (
  id INTEGER PRIMARY KEY,
  tms_id INTEGER NOT NULL,
  zoom_level INTEGER NOT NULL,
  matrix_width INTEGER NOT NULL,
  matrix_height INTEGER NOT NULL,
  tile_width INTEGER NOT NULL,
  tile_height INTEGER NOT NULL,
  pixel_x_size DOUBLE NOT NULL,
  pixel_y_size DOUBLE NOT NULL,
  left DOUBLE NOT NULL,
  top DOUBLE NOT NULL,
  scale_denominator DOUBLE DEFAULT NULL,
  CONSTRAINT fk_etm_etms FOREIGN KEY (tms_id) REFERENCES gpkgext_tile_matrix_set(id)
);
