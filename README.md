vroom
=====

A Java framework for building RESTful web services on Google App Engine

vroom makes it easier to build real-world web services on GAE by 

1. establishing a 3-tier framework into which you can insert your code - an API tier, a business logic tier, and a persistence tier.  This keeps the code in each tier simpler and more focused, reduces coupling, and increases testability
2. providing base classes for common use cases such as CRUD operations and collections with filtering support
3. simplifies implementation of a RESTful web API
 - supports content negotiation
 - helper classes automated conversion between JSON representations and domain objects

vroom leverages several best-of-breed open source libraries, including:
 - Restlet (a RESTful API framework)
 - Jackson (for JSON<->POJO serialization/deserialization)
 - Objectify (for interacting with App Engine datastore)

To install:

1) clone the github project to your local development environment

2) from a shell prompt while in the vroom-maven-parent folder, install the maven parent POM:

`mvn install`

3) if you're just going to use the vroom-dto library as part of a vroom client written in Java (e.g. an Android app), you only need to build and install vroom-dto.jar to your local maven repo.  From a shell prompt while in the vroom-dto-folder:

`mvn install`

4) if you're also going to be building a web API using the vroom framework, in addition to vroom-dto, you'll also need to build and install vroom-core.jar to your local maven repo.  From a shell prompt while in the vroom-core folder:

`mvn install`
