#Change Log
All notable changes to this project will be documented in this file.
Adheres to [Semantic Versioning](http://semver.org/).

---

## 1.2.2 (TBD)

* TBD

## [1.2.1] (https://github.com/ngageoint/geopackage-core-java/releases/tag/1.2.1) (02-02-2017)

* GeoPackage spec version 1.2 changes and updates
* Time zone fix on database persisted dates
* Elevation Extension support
* Elevation Tiles table type (2d-gridded-coverage)
* Contents Data Type (features, tiles, attributes, elevation) functionality
* User Attributes table support
* Built in support for WGS 84 Geographic 3d projection (EPSG:4979)
* Table and column name SQL quotations to allow uncommon but valid names
* Elevation query algorithms including Nearest Neighbor, Bilinear, and Bicubic
* Elevation unbounded results elevation queries
* Tile bounding box utility methods precision adjustments
* Additional Tile Dao zoom level determinations, including the closest
* Spatial Reference System definition_12_163 column changed to definition_12_063
* Deprecated the User Geometry Types Extension per spec removal
* Deprecated gpkx file extension for extended GeoPackages per spec removal
* GeoPackage application id and user version
* OrmLite Core version updated to 5.0

## [1.2.0] (https://github.com/ngageoint/geopackage-core-java/releases/tag/1.2.0) (06-22-2016)

* Spatial Reference System DAO create from EPSG code in addition to SRS id
* Projection unit retrieval
* Dropped web mercator terminology from method names that only require consistent units per pixel
* Zoom level from tile size in meters method
* Tile DAO utility methods
* WGS84 tile grid, bounding box, tiles per side, and tile size methods

## [1.1.8](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.1.8) (05-10-2016)

* GeoPackage 1.1.0 spec updates
* GeoPackage application id updated to GP11
* OGC Well known text representation of Coordinate Reference Systems extension support
* Core Connection column exists, add column, and query single result methods
* Base extension with implementations and support for: CRS WKT, Geometries, Metadata, Schema, WebP, and Zoom Other
* Projection creations from Spatial Reference Systems
* Projection backup creation using proj4j configurations
* Data Column Constraint column name changes per spec

## [1.1.7](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.1.7) (04-18-2016)

* Additional GeoPackage methods to get all tables and check table types
* wkb version update to 1.0.2

## [1.1.6](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.1.6) (02-19-2016)

* Feature Tile Table Linker core abstraction
* Additional Feature Tile Table Linker methods for retrieving table names
* Bounding box constructor from an existing bounding box
* Bound a WGS 84 bounding box with Web Mercator limits utility method

## [1.1.5](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.1.5) (02-02-2016)

* Feature Tile Link Extension implementation - http://ngageoint.github.io/GeoPackage/docs/extensions/feature-tile-link.html
* GeoPackage drop table method
* Added delete all methods for the Geometry Index extension data access objects
* Contents open database connections fix - [Issue #20](https://github.com/ngageoint/geopackage-core-java/issues/20)
* Resource loading change to fix table creation on Windows - [Issue #21](https://github.com/ngageoint/geopackage-core-java/issues/21)
* Delete extensions when deleting a user table

## [1.1.4](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.1.4) (01-15-2016)

* Added remote proj4j dependency and removed embedded proj4j code - [Issue #18](https://github.com/ngageoint/geopackage-core-java/issues/18)

## [1.1.3](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.1.3) (12-16-2015)

* Geometry projection transformations - [Issue #16](https://github.com/ngageoint/geopackage-core-java/issues/16)

## [1.1.2](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.1.2) (12-14-2015)

* Table Index Extension invalid foreign key fix - [Issue #14](https://github.com/ngageoint/geopackage-core-java/issues/14)
* Execute SQL on GeoPackage method

## [1.1.1](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.1.1) (11-20-2015)

* Javadoc project links to external libraries
* Additional GeoPackage createFeatureTableWithMetadata methods - [Issue #10](https://github.com/ngageoint/geopackage-core-java/issues/10)
* min and max column query methods - [Issue #11](https://github.com/ngageoint/geopackage-core-java/issues/11)
* Determine bounding box from Tile Grid methods - [Issue #12](https://github.com/ngageoint/geopackage-core-java/issues/12)
* Data Columns Dao get data column by table and column names method
* wkb version update to 1.0.1

## [1.1.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.1.0) (10-08-2015)

* NGA Table Index Extension implementation - http://ngageoint.github.io/GeoPackage/docs/extensions/geometry-index.html
* Data Object Access delete object method fixes for tables with complex primary keys
* User Core DAO get bounding box and zoom level methods for features and tiles
* Get Tile Grid from single point and zoom level method

## [1.0.1](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.0.1) (09-23-2015)

* Added new GeoPackageCoreCache functionality

## [1.0.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.0.0) (09-15-2015)

* Initial Release
