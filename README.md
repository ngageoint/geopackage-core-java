# GeoPackage Core Java

#### GeoPackage Core Libs ####

The GeoPackage Libraries were developed at the National Geospatial-Intelligence Agency (NGA) in collaboration with [BIT Systems](https://www.bit-sys.com/index.jsp). The government has "unlimited rights" and is releasing this software to increase the impact of government investments by providing developers with the opportunity to take things in new directions. The software use, modification, and distribution rights are stipulated within the [MIT license](http://choosealicense.com/licenses/mit/).

### Pull Requests ###
If you'd like to contribute to this project, please make a pull request. We'll review the pull request and discuss the changes. All pull request contributions to this project will be released under the MIT license.

Software source code previously released under an open source license and then modified by NGA staff is considered a "joint work" (see 17 USC § 101); it is partially copyrighted, partially public domain, and as a whole is protected by the copyrights of the non-government authors and must be released according to the terms of the original open source license.

### About ###

GeoPackage Core provides core functionality for GeoPackage implementations of the Open Geospatial Consortium [GeoPackage](http://www.geopackage.org/) [spec](http://www.geopackage.org/spec/).

It is the core library of both the [GeoPackage Android](https://github.com/ngageoint/geopackage-android) SDK and [GeoPackage Java](https://github.com/ngageoint/geopackage-java) library.

### Usage ###

#### GeoPackage Android ####

The [GeoPackage Android](https://github.com/ngageoint/geopackage-android) SDK is an Android GeoPackage implementation.

#### GeoPackage SDK Sample ####

The [GeoPackage MapCache](https://github.com/ngageoint/geopackage-mapcache-android) app provides an Android example of using the [GeoPackage Android](https://github.com/ngageoint/geopackage-android) SDK.

#### GeoPackage Java ####

The [GeoPackage Java](https://github.com/ngageoint/geopackage-java) library is a Java GeoPackage implementation.

### Build ###

The following repository must be built first (Central Repository Artifacts Coming Soon):
* [GeoPackage WKB Java] (https://github.com/ngageoint/geopackage-wkb-java)

Build this repository using Eclipse and/or Maven:

    mvn clean install

### Dependencies ###

#### Remote ####

* [WKB](https://github.com/ngageoint/geopackage-wkb-java) (The MIT License (MIT)) - GeoPackage Well Known Binary Lib
* [OrmLite](http://ormlite.com/) (Open Source License) - Object Relational Mapping (ORM) Library

#### Embedded ####

* [Proj4J](http://trac.osgeo.org/proj4j/) (Apache License, Version 2.0) - Projection Library

