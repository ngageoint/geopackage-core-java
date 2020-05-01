INSERT INTO gpkgext_tile_matrix_set(tms, description, uri, srs_id, min_x, min_y, max_x, max_y)
    SELECT table_name, "automatically generated TMS", table_name, srs_id, min_x, min_y, max_x, max_y
    FROM gpkg_tile_matrix_set;

INSERT INTO gpkgext_tile_matrix(tms_id, zoom_level, matrix_width, matrix_height, tile_width, tile_height, pixel_x_size, pixel_y_size, left, top)
    SELECT a.id, b.zoom_level, b.matrix_width, b.matrix_height, b.tile_width, b.tile_height, b.pixel_x_size, b.pixel_y_size, a.min_x, a.max_y
    FROM gpkgext_tile_matrix_set a, gpkg_tile_matrix b
    WHERE a.tms = b.table_name;

INSERT INTO gpkgext_tile_matrix_tables(table_name, tms_id, min_level, max_level)
    SELECT a.tms, a.id, min(b.zoom_level), max(b.zoom_level)
    FROM gpkgext_tile_matrix_set a, gpkgext_tile_matrix b
    WHERE a.id = b.tms_id
    GROUP BY tms;

DROP TABLE gpkg_tile_matrix_set;

CREATE VIEW gpkg_tile_matrix_set AS
    SELECT a.table_name, b.srs_id, b.min_x, b.min_y, b.max_x, b.max_y
    FROM gpkgext_tile_matrix_tables a, gpkgext_tile_matrix_set b
    WHERE a.tms_id = b.id;

DROP TABLE gpkg_tile_matrix;

CREATE VIEW gpkg_tile_matrix AS
    SELECT a.table_name, b.zoom_level, b.matrix_width, b.matrix_height, b.tile_width, b.tile_height, b.pixel_x_size, b.pixel_y_size
    FROM gpkgext_tile_matrix_tables a, gpkgext_tile_matrix b
    WHERE a.tms_id = b.tms_id AND b.zoom_level >= a.min_level AND b.zoom_level <= a.max_level AND b.id NOT IN (
        SELECT DISTINCT tm_id FROM gpkgext_tile_matrix_variable_widths
    );
