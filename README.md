Vroom
=====

A Java framework for building RESTful web services on Google App Engine
---

**Vroom** makes it easier to build real-world web services on GAE by:

1. establishing a 3-tier framework into which you can organize your code - an API tier, a business logic tier, and a service  tier.  This keeps the code in each tier simpler and more focused, reduces coupling, and increases testability
2. providing base classes in each tier to simplify the implementation of common use cases such as CRUD operations and collections with filtering
3. simplifying implementation of a RESTful web API by providing helper classes to quickly and easily implement content negotiation and conversion between JSON representations and domain objects

Vroom leverages several best-of-breed open source libraries, including:
 - Restlet (a RESTful API framework)
 - Jackson (for JSON<->POJO serialization/deserialization)
 - Objectify (for interacting with App Engine datastore)

Note: Vroom is currently in a pre-1.0 stage, and changing rapidly.  Breaking changes are likely.  You've been warned.

