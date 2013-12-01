vroom
=====

A java framework for building RESTtful web services on Google App Engine using Restlet and Objectify

To install:

1) clone the github project to your local development environment

2) from a shell prompt while in the vroom-maven-parent folder, install the maven parent POM:
`mvn install`

3) if you're just going to use the vroom-dto library as part of a vroom client written in Java (e.g. an Android app), you only need to build and install vroom-dto.jar.  From a shell prompt while in the vroom-dto-folder:
`mvn install`

4) if you're also going to be building a web API using the vroom framework, you'll also need to build and install vroom-core.jar.  From a shell prompt while in the vroom-core folder:
`mvn install`
