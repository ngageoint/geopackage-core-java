#Change Log
All notable changes to this project will be documented in this file.
Adheres to [Semantic Versioning](http://semver.org/).

---

## 1.1.5 (TBD)

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
