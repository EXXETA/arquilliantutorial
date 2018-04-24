# Arquillian Tutorial

## Requirements

Download and copy wildfly 10.1.0Final to ``wildfly`` folder.
Install mysql connector like described here: 
[http://rafiqnayan.blogspot.de/2016/05/how-to-configure-wildfly-10-to-use-mysql.html](http://rafiqnayan.blogspot.de/2016/05/how-to-configure-wildfly-10-to-use-mysql.html)

## Configuration

Configure database as ``java:jboss/datasources/MYSQL`` in your ``standalone.xml``.
Configure database in src/test/resources/META-INF/persistence.xml``.

## Run tests

```
mvn clean test -Pit,coverage
```
