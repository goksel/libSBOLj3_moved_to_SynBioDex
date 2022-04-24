# libSBOLj3 - A Java library for the Synthetic Biology Open Language 3
The libSBOLj Java library has been developed for the [Synthetic Biology Open Language 3.0](https://sbolstandard.org/data-model-specification). The library is  under development and is currently available as an alpha release. 

SBOL represents data using RDF graphs, which can be serialised in different formats. The libSBOLj3 library supports the following RDF formats.
* RDF/XML (File extension: rdf)
* Turtle (File extension: ttl)
* N-Triples (File extension: nt)
* JSON-LD (File extension: jsonld)
* RDF/JSON (File extension: rj)

## How to use libSBOLj3

### As a Maven dependency in a Maven project
Use this option if you are developing a Java application using [Maven](https://maven.apache.org/). Add the following libSBOLj3 dependency to your Maven applications's POM file (pom.xml). Please also make sure that you include the Nexus' Snapshots repository URL in the POM file. 
``` 
</dependencies>
	...
   <dependency>
      <groupId>org.sbolstandard</groupId>
      <artifactId>libSBOLj3</artifactId>
      <version>1.4-SNAPSHOT</version>
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
The libSBOLj3 library is available as a JAR file. Please download the file from the [releases page](https://github.com/goksel/libSBOLj3/tags). A single JAR file (with the "withDepencencies" suffix), which includes all the required libSBOLj3 related dependencies, is also available.

## SBOL Examples
[Several SBOL3 examples](https://github.com/goksel/libSBOLj3/tree/master/libSBOLj3/output) are available as part of the libSBOLj3 library. These examples have also been made available as part of the [SBOL Test Suite](https://github.com/SynBioDex/SBOLTestSuite/tree/master/SBOL3). Some of these examples have beeen explained in the recent SBOL3 paper, titled "[The Synthetic Biology Open Language (SBOL) Version 3: Simplified Data Exchange for Bioengineering](https://doi.org/10.3389/fbioe.2020.01009)".

## Getting Started
Please see the [tutorial code](https://github.com/goksel/libSBOLj3/tree/feature/combine2020/libSBOLj3/output/combine2020) and the [COMBINE 2020 slides](https://github.com/SynBioDex/Community-Media/blob/master/2020/COMBINE20/pySBOL3-COMBINE-2020.pptx) for more details. The tutorial code includes additional examples to crete interactions, constraints, component references and so on.

### Creating a new SBOL document
The SBOLDocument class is used to create SBOL documents which act as containers to create and access other SBOL entities. Although not required, the base URI can be used as prefix for all new SBOL entities. 
```java
URI base=URI.create("https://synbiohub.org/public/igem/");
SBOLDocument doc=new SBOLDocument(base);
```	
The following sections summarises how to create the [i13504](http://parts.igem.org/Part:BBa_I13504) device, formed of an RBS, a CDS and a terminator parts. Both the device and the parts are represented as SBOL components.
### Creating parts and sequences
The newly created SBOLDocument object then be used as a *factory* to create new SBOL entities. These entities can then be defined via their different properties. The following example creates an RBS part, which is represented as a Component in SBOL. 
```java
//Create the RBS component
Component rbs = doc.createComponent("B0034", Arrays.asList(ComponentType.DNA.getUrl())); 
rbs.setName("B0034");
rbs.setDescription("RBS (Elowitz 1999)");
rbs.setRoles(Arrays.asList(Role.RBS));
		
//Create a sequence entity for the RBS component
Sequence rbs_seq=doc.createSequence("B0034_Sequence");
rbs_seq.setElements("aaagaggagaaa");
rbs_seq.setEncoding(Encoding.NucleicAcid);
rbs.setSequences(Arrays.asList(rbs_seq.getUri()));	
```
The libSBOLj3 library, which provide both high level and low level APIs to construct sequences and to annotate sequence features. The above code can also be written as below using the high level API.
```java
Component rbs=SBOLAPI.createDnaComponent(doc, "B0034", "rbs", "RBS (Elowitz 1999)", Role.RBS, "aaagaggagaaa");	
```

### Sequence construction
 For example, a composite device can be constructed from simpler building blocks (e.g. rbs or cds) and sequence features (scar sequences). 
 Let's first define our composite device component that we want to create from simpler building blocks.
```java
Component device= SBOLAPI.createDnaComponent(doc, "i13504", "i13504", "Screening plasmid intermediate", ComponentType.DNA.getUrl(), null);	
```

The device is then constructed by adding other parts (rbs, cds and termintor components) and sequence features (scar sequences).
```java
SBOLAPI.appendComponent(doc, device,rbs,Orientation.inline);	
SBOLAPI.appendSequenceFeature(doc, device, "tactag", Orientation.inline);
SBOLAPI.appendComponent(doc, device,gfp, Orientation.inline);
SBOLAPI.appendSequenceFeature(doc, device, "tactagag", Orientation.inline);
SBOLAPI.appendComponent(doc, device,term, Orientation.inline);
```

These subcomponents and fetatures can be iterated using related properties.
```java
for (SubComponent subComp: device.getSubComponents()){
   System.out.println(subComp.getIsInstanceOf());
}
```

### Reading and writing SBOL documents
 The libSBOLj3 library provides methods to store SBOL documents in memory variables and to read documents from these variables.
 ```java
 //Write using the RDF Turtle format
 String output=SBOLIO.write(doc, "Turtle");
 //Read using the RDF Turtle format
 SBOLDocument doc2=SBOLIO.read(output, "Turtle"); 
```
 The libSBOLj3 library alsoprovides methods to store SBOL documents in files and to read documents from these files.

```java
//Write
 SBOLIO.write(doc, new File("sbol.ttl"), "Turtle");
 //Read
 SBOLIO.read(doc, new File("sbol.ttl"), "Turtle");
``` 

The following constants can be used to set the RDF serialisation type:

```Turtle```, ```RDF/XML-ABBREV```, ```JSON-LD```, ```RDFJSON```, ```N-TRIPLES```.

### Looking up for SBOL entities
SBOL utilises URIs to link different entities. An SBOL entity may store a reference to another entity for more details. These additional details can be retrieved using the ```getIdentified``` method which expects the URI of the entity to retrieve, and its type. The followig example shows retrieving nucleotide sequences of the rbs component. The Sequence entity, the URI of which is referenced in the rbs component, is retrieved first. Its elements property is then used to read the nucleotides information.
```java
Sequence rbsSeq = doc.getIdentified(sequenceURI, Sequence.class);
String nucleotides = rbsSeq.getElements();	
```

### Looking up (Querying) using graph pattern matching
Multiple SBOL entities that can match to a given pattern can also be searched for. These enties are returned using SPARQL SELECT queries via the ```getIdentifieds``` method. This method expects a partial SPARQL query, which would normally be included between "```WHERE {```" and "```}```" in SPARQL queries. The rest of the query is constructed by libSBOLj3 using the URI prefixes that are already specified in SBOL documents. It is assumed that the first column of the SPARQL query result includes URIs of SBOL entities of one type only. For example, the following example retrieves all SBOL Component entities with the role:SO:0000141 (promoter) and type SBO:0000251 (DNA).
```java
List<Component> components=(List<Component>)doc.getIdentifieds("?identified a sbol:Component; sbol:role  SO:0000141; sbol:type SBO:0000251 .", Component.class);
   System.out.println("Graph query results:");
   for (Component component:components){
    	System.out.println("  " +  component.getDisplayId());
   }
```

The libSBOLj3 library creates the following SPARQL query to get the results using the code above.
```
PREFIX CHEBI: <https://identifiers.org/CHEBI:>
PREFIX GO: <https://identifiers.org/GO:>
PREFIX sbol: <http://sbols.org/v3#>
PREFIX EDAM: <https://identifiers.org/edam:>
PREFIX SO: <https://identifiers.org/SO:>
PREFIX prov: <http://www.w3.org/ns/prov#>
PREFIX om: <http://www.ontology-of-units-of-measure.org/resource/om-2/>
SELECT ?identified
WHERE {
   ?identified a sbol:Component; 
            sbol:role  SO:0000141; 
            sbol:type SBO:0000251 .
}
```
