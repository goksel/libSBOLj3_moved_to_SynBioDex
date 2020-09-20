# libSBOLj3 - A Java library for the Synthetic Biology Open Language 3
The libSBOLj Java library has been developed for the Synthetic Biology Open Language 3.0. The library is  under development and is currently available as an alpha release. 

SBOL represents data using RDF graphs, which can be serialised in different formats. The libSBOLj3 library supports the following RDF formats.
* RDF/XML
* Turtle
* N3
* JSON-LD
* RDF/JSON

## How to use libSBOLj3

### As a Maven dependency in a Maven project
Use this option if you are developing a Java application using Maven. Add the following libSBOLj dependency to your Maven applications's POM file (pom.xml. Please also make sure that you include the Nexus's Snapshots repository in the POM file. 
``` 
</dependencies>
	...
   <dependency>
		<groupId>org.sbolstandard</groupId>
		<artifactId>libSBOLj3</artifactId>
		<version>1.0-SNAPSHOT</version>
	</dependency>
   ...
</dependencies>

<repositories>
   <repository>
      <id>oss-sonatype</id>
      <name>oss-sonatype</name>
      <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
      <snapshots>
         <enabled>true</enabled>
      </snapshots>
   </repository>
</repositories>
```

### As a Java dependency in a non-Maven project
The libSBOLj3 library is available as a JAR file. Please download the file from . A single JAR file (with the "withDepencencies" suffix), which includes all the required libSBOLj3 related dependencies is also available.

## SBOL Examples
Several SBOL3 examples are available as part of the libSBOLj3 library. These examples can be found at . These examples have also been made available as part of the SBOLTestSuite at https://github.com/SynBioDex/SBOLTestSuite/tree/master/SBOL3. Some of these examples have beeen explained in the recent SBOL3 paper. Please see  https://doi.org/10.3389/fbioe.2020.01009 for more details.
