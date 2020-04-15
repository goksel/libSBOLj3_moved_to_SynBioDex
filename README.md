# libSBOLj

<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:sbol="http://sbols.org/v3#">
   <sbol:Sequence rdf:about="http://myseq.org/seqabc">
      <sbol:encoding rdf:resource="http://sbols.org/v3#iupacNucleicAcid"/>
      <sbol:elements>aaaatttggg</sbol:elements>
      <sbol:description>pTetR promoter</sbol:description>
      <sbol:name>pTetR</sbol:name>
      <sbol:displayId>pTetR</sbol:displayId>
   </sbol:Sequence>
</rdf:RDF>


@base          <http://testbase.org> .
@prefix sbol:  <http://sbols.org/v3#> .

<http://myseq.org/seqabc>
        a                 sbol:Sequence ;
        sbol:description  "pTetR promoter" ;
        sbol:displayId    "pTetR" ;
        sbol:elements     "aaaatttggg" ;
        sbol:encoding     sbol:iupacNucleicAcid ;
        sbol:name         "pTetR" .
