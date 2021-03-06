Vroom
=====

A Java framework for building RESTful web services on Google App Engine
---

**Vroom** makes it easy to build real-world web services on GAE by:

1. establishing a 3-tier framework into which you can organize your code - an API tier, a business logic tier, and a service  tier.  This keeps the code in each tier simpler and more focused, reduces coupling, and increases testability.
2. providing base classes in each tier to simplify the implementation of common use cases such as CRUD and query operations.
3. speeding implementation of a RESTful web API by providing helper classes to assist with content negotiation and conversion between JSON representations and domain objects.

Vroom leverages several best-of-breed open source libraries, including:
 - Restlet (a RESTful API framework)
 - Jackson (for JSON<->POJO serialization/deserialization)
 - Objectify (for interacting with App Engine datastore)

Vroom makes it easy to implement common use cases by providing structure, boilerplate code, and API glue, leaving you to focus on the "special sauce" of your application.  Examples include:
- CRUD (create/read/update/delete) operations on persistent resources
- (optional) automatic timestamping of creation/modification date properties on datastore entities
- retrieving collections of resources
- querying to filter collection results
- uploading and retrieving files using Google Cloud Storage
- uploading and retrieving images, leveraging GAE's ImagesService to perform server-side transformations such as resizing, rotating, flipping, and cropping
- supporting multiple resource representations, allowing you to evolve your representations while still supporting older clients

Vroom includes
 - the core Vroom framework library.
 - a small library of helper base classes for implementing Vroom-compatible Data Transfer Objects (DTOs).  This is useful for sharing code with Java clients such as Android apps.
 - (optional) extension libraries for integrating Google Cloud Storage, the Google Maps API, the GAE ImagesService, and the Joda Time library.
 - a sample RESTful web service built using Vroom, demonstrating common use cases, including use of the Vroom extensions.

