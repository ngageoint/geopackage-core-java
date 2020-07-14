# Change Log
All notable changes to this project will be documented in this file.
Adheres to [Semantic Versioning](http://semver.org/).

---

## 4.0.1 (TBD)

* TBD

## [4.0.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/4.0.0) (07-14-2020)

* GeoPackage spec version 1.3.0
* sf-wkb version 2.0.3
* sf-wkt version 1.0.0
* sf-proj version 3.0.3
* oapi-features-json version 1.0.2
* Community extensions decoupling and management, including class repackaging
* Contents Data Type refactor, core data types and expanded custom data type support
* Wider support for views in place of tables, including a new GeoPackageDao
* User table creation refactor with metadata objects
* GeoPackage application id, user version, contents type, and bounding box enhancements
* DAO options to enable modifying row ids or disable column value type validation
* DAO creation methods
* Distinct select statement queries and counts
* Aggregate functions (count, max, min) refactor
* Optional autoincrement support
* Columns constraints copy fix, resolving alter table schema information losses
* More lenient checks, logging errors in place of throwing exceptions and continuing
* Geometry Data build envelope, creation, byte, Well-Known Text, and transform methods
* Image Matters Portrayal and Vector Tiles extensions
* Ecere Tile Matrix Set extension

## [3.5.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/3.5.0) (03-10-2020)

* Separation of columns from user tables to support custom column queries
* User DAO methods: query for specified columns, additional counts
* Bounding box utility methods

## [3.4.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/3.4.0) (11-14-2019)

* sf-proj version 3.0.2
* oapi-features-json version 1.0.1
* Database Result utility updates
* Additional connection and DAO query and count methods
* Additional user result get value and id methods
* Removed default values on user tile table columns
* Add Contents Id Extension table to Contents table for use in related tables
* SQLite Query Builder, moved from GeoPackage Java

## [3.3.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/3.3.0) (07-09-2019)

* sf-wkb version 2.0.2
* sf-proj version 3.0.1
* oapi-features-json version 1.0.0
* Alter table support: rename table, rename column, add column, drop column, alter column, copy table
* User DAO and table alter support: add, rename, drop, alter
* GeoPackage rename table, copy table, foreign keys, and vacuum support
* Automatic user column index assignments
* Transaction shortcut methods for the GeoPackages, connections, and User DAOs
* User column shortcut creation methods
* User table and user column copy methods and constructors
* SQL utilities for tables, columns, foreign keys, views, data transfers, checks, and vacuum
* SQLite Master table (sqlite_master) support
* Table Info (PRAGMA table_info) support
* Table and column mapping for table alterations
* Table and column constraints, including manual creation and automatic table parsing
* Core table readers
* Date Converter check for date functions
* GeoPackage Data Type find by name method
* Extensions table copy support
* Missing extension table rows for Metadata Extension and Schema Extension
* Missing comma in gpkg_2d_gridded_coverage_ancillary table schema
* gpkg_data_columns table: removed foreign key requirement, unique constraint update
* Missing comma in nga_tile_scaling table schema
* OGC API Features download to GeoPackage support
* Modifiable GeoPackageIOUtils copy buffer, defaulted at 8k byte chunks

## [3.2.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/3.2.0) (04-02-2019)

* sf-wkb version 2.0.1
* sf-proj version 3.0.0
* NGA [Contents Id](http://ngageoint.github.io/GeoPackage/docs/extensions/contents-id.html) Extension
* NGA [Feature Style](http://ngageoint.github.io/GeoPackage/docs/extensions/feature-style.html) Extension
* OGC [Related Tables](http://www.geopackage.org/18-000.html) Extension improvements
* GeoPackage user version saved as 1.2.1
* Improved GeoPackage extension cleanup
* Color support and utilities for hex, RBG, arithmetic RBG, HSL, and integer colors
* Common data type and contents methods for all User Table types
* GeoPackage contents check for table names
* Build an envelope from a Bounding Box
* WKT for Coordinate Reference Systems extension default value of 'undefined' removed
* Tile Bounding Box Utils latitude and longitude position from pixel with tile bounds
* Eclipse project cleanup

## [3.1.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/3.1.0) (10-04-2018)

* Simple Features Projection (sf-proj) version 2.0.1
* Related Tables Extension fix to save Simple Attributes and Media tables as Attribute table types
* User Column isNamed fix
* ORMLite DaoManager cache memory leak fix, visible when opening many GeoPackages
* Bounding Box methods: intersects, overlap, union, and contains
* GeoPackage Contents Bounding Box and overall Bounding Box methods
* GeoPackage Geometry Index Table index methods, independent of table creation
* GeoPackage Cache connection close improvements
* Contents (and DAO) bounds and projection methods
* Additional Date Converter format to handle format yyyy/MM/dd
* Connection query improvements
* Table Creator improvements for generic SQL script execution
* Database common Result interface and utilities, implemented by UserCoreResult
* RTree extension support improvements
* Feature Table Index improvements for limited chunked queries and bounds retrieval
* Geometry Columns projection method
* Geometry Data get or build envelope method
* Tile Matrix Set bounds and projection methods
* User DAO query, bounds, and projection improvements

## [3.0.2](https://github.com/ngageoint/geopackage-core-java/releases/tag/3.0.2) (07-27-2018)

* Properties Extension for saving GeoPackage metadata in the file
* Properties Manager for using the Properties Extension on multiple open GeoPackages
* Additional User Core DAO deletion and query methods
* Additional GeoPackage Core Connection query method
* Remove Data Columns requirement of enumerated data types
* Create Attributes Table methods with unique constraints
* GeoPackage Cache add all method
* IO Utils file extension utilities

## [3.0.1](https://github.com/ngageoint/geopackage-core-java/releases/tag/3.0.1) (07-13-2018)

* Related Tables Extension support (DRAFT version 0.1)
* Additional table, table type, and contents methods and improvements
* User DAO "like" query support
* User DAO, table, and row support for id-less schemas
* Custom User table and column implementations
* Javadoc warning fixes
* ormlite-core version 5.1
* maven and sonatype plugin version updates

## [3.0.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/3.0.0) (05-17-2018)

* WKB dependency updated to use new [Simple Features WKB library](https://github.com/ngageoint/simple-features-wkb-java)
  * Package names in dependent classes must be updated
  * GeometryType code calls must be replaced using GeometryCodes
* Common projection code moved to [Simple Features Projections library](https://github.com/ngageoint/simple-features-proj-java)
  * Package names in dependent classes must be updated
  * ProjectionFactory SRS calls must be replaced using SpatialReferenceSystem projection method
  * ProjectionTransform bounding box calls must be replaced using BoundingBox transform method
* Bounding Box projection transform method
* Spatial Reference System projection and transformation methods
* Gridded Coverage Data extension definition url

## [2.0.2](https://github.com/ngageoint/geopackage-core-java/releases/tag/2.0.2) (03-20-2018)

* Tile Scaling extension for scaling missing tiles from nearby zoom levels
* Projection Transform "is same projection" method
* Tile DAO zoom level improvements and approximate zoom level determinations
* IO Utils close quietly method and stream closing
* RTree Trigger Update 3 spec fix

## [2.0.1](https://github.com/ngageoint/geopackage-core-java/releases/tag/2.0.1) (02-13-2018)

* Coverage Data extension (previously Elevation Extension)
* WebP extension column name fix
* Zoom Other extension column name fix
* RTree Index Extension core support
* Extended Geometry bit encoding fix, set only for non standard geometries
* Tile Grid zoom increase and decrease utilities
* Zoom level determination fix for bounds resulting in a single point
* Quote wrapping improvements and additional wrapping of names in SQL operations
* wkb version update to 1.0.5

## [2.0.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/2.0.0) (11-20-2017)

* WARNING - BoundingBox.java coordinate constructor arguments order changed to (min lon, min lat, max lon, max lat)
  Pre-existing calls to BoundingBox coordinate constructor should swap the min lat and max lon values
* WARNING - TileGrid.java constructor arguments order changed to (minX, minY, maxX, maxY)
  Pre-existing calls to TileGrid constructor should swap the minY and maxX values
* Bounding Box envelope constructor
* Bounding Box projection based complementary, bounding, and expansion methods
* Query support for "columns as"
* Date Converter thread safety fix when using multiple DAOs
* Projection transformations for lists of points
* Bounding box utility improvements for overlap and point in box testing
* Tolerance distance utility methods for geometry proximity testing
* User Core methods for preparing results, checking for an id, and retrieving result positions & columns of a type
* User Row Sync implementation to support sharing user row query results
* Minor SQL changes and file renames to match spec changes
* Retrieve a projection unit without reflection
* wkb version update to 1.0.4
* maven-gpg-plugin version 1.6

## [1.3.1](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.3.1) (07-13-2017)

* Improved handling of unknown Contents bounding boxes
* User table zoom level bounding of degree unit projections
* Tile Bounding Box Utils method to bound degree unit bounding box with web mercator limits

## [1.3.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.3.0) (06-27-2017)

* Projections refactor to support additional coordinate authorities and custom projections
* Copy constructors for base, extension, and user table (features, tiles, attributes) row objects
* Improved date column support for user tables (features, tiles, attributes)

## [1.2.2](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.2.2) (06-12-2017)

* Allow user tables (feature, tile, attributes) without primary keys to support table views
* Support EPSG 900913 (GOOGLE)
* Elevation Extension scale, offset, and id columns changed to be non nullable
* Default dates for Contents last change and Metadata Reference timestamp
* wkb version update to 1.0.3

## [1.2.1](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.2.1) (02-02-2017)

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

## [1.2.0](https://github.com/ngageoint/geopackage-core-java/releases/tag/1.2.0) (06-22-2016)

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
