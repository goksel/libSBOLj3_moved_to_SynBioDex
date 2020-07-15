package org.sbolstandard.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;

import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.ComponentReference;
import org.sbolstandard.entity.Feature;
import org.sbolstandard.entity.Identified;
import org.sbolstandard.entity.Interaction;
import org.sbolstandard.entity.Location;
import org.sbolstandard.entity.Participation;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.Sequence;
import org.sbolstandard.entity.SubComponent;
import org.sbolstandard.entity.Location.LocationBuilder;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.Encoding;
import org.sbolstandard.vocabulary.Orientation;
import org.sbolstandard.vocabulary.RestrictionType;

public class SBOLAPI {

	  public static List<Interaction> createInteraction(List<URI> interactionTypes, Component parent, Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles) throws SBOLGraphException
	    {
	    	List<Interaction> interactions=new ArrayList<Interaction>();
	    	List<SubComponent> features1=createSubComponents(parent, participant1);
	    	List<SubComponent> features2=createSubComponents(parent, participant2);
	    	
	    	for (Feature feature1: features1)
	    	{
	    		for (Feature feature2: features2)
	    		{
	    			Interaction interaction=createInteraction(interactionTypes, parent, feature1, participant1Roles, feature2, participant2Roles);	
	    			interactions.add(interaction);
	    		}
	    	}
	    	return interactions;
	    }
	  
	  	private static List<SubComponent> createSubComponents (Component parent, Component child) throws SBOLGraphException 
	  	{
	    	List<SubComponent> subComponents=getSubComponents(parent, child);
	    	//If not DNA and there is no subComponent yet, add a subcomponent for the child
	    	if (subComponents.size()==0 && !child.getTypes().contains(ComponentType.DNA.getUrl()))
	    	{
	    		String localName=createLocalName(DataModel.SubComponent.uri, parent.getSubComponents());
		    	SubComponent subComponent=parent.createSubComponent(append(parent.getUri(), localName), child.getUri());
		    	subComponents.add(subComponent);
	    	}
	    	return subComponents;
	  	}
	    
	    public static List<SubComponent> getSubComponents(Component parent, Component child) throws SBOLGraphException
	    {
	    	List<SubComponent> found= new ArrayList<SubComponent>();
	    	List<SubComponent> features=parent.getSubComponents();
	    	for (SubComponent feature: features)
	    	{
	    		if (feature.getIsInstanceOf().equals(child.getUri()))
	    		{
	    			found.add(feature);
	    		}
	    	}
	    	return found;
	    }
	    
	    
	    public static  Interaction createInteraction(List<URI> interactionTypes, Component container, Feature participant1, List<URI> participant1Roles, Feature participant2, List<URI> participant2Roles) throws SBOLGraphException
	    {
	    	String localName=createLocalName(DataModel.Interaction.uri, container.getInteractions()); 
	    	Interaction interaction= container.createInteraction(append(container.getUri(), localName), interactionTypes);
	    	createParticipation(interaction, participant1Roles, participant1);
	    	createParticipation(interaction, participant2Roles, participant2);
	    	return interaction;
	    }
	    
	   
	    public static  Participation createParticipation(Interaction interaction, List<URI> roles, Feature feature) throws SBOLGraphException
	    {
	    	String localName=createLocalName(DataModel.Participation.uri, interaction.getParticipations()); 
	    	URI uri=append(interaction.getUri(), localName);
	    	Participation participation=interaction.createParticipation(uri, roles, feature.getUri());
	    	return participation;
	    }
	    
	    private static  int getIndex(List items) 
	    {
	    	int index=1;
	    	if (items!=null)
	    	{
	    		index=items.size()+1;
	    	}
	    	return index;
	    }
	    
	    private static  int getIndex(Set items) 
	    {
	    	int index=1;
	    	if (items!=null)
	    	{
	    		index=items.size()+1;
	    	}
	    	return index;
	    }
	    
	    private static String getSubString(String data, String searchString)
	    {
	    	String found=null;
	    	int index=data.indexOf(searchString);
    		if (index!=-1 && data.length()>index+searchString.length())
 	    	{
 	    		found=data.substring(index+searchString.length());
 	    	}
    		return found;
	    }
	    
	    private static String getLocal(URI uri)
	    {
	    	String local=null;
	    	String uriString=uri.toString();
	    	local=getSubString(uriString, "#");
	    	if (local==null)
	    	{
	    		local=getSubString(uriString, "/");
	    	}
	    	return local;
	    }
	    
	    public static String createLocalName(URI entityType, List items)
	    {
	    	int suffix=getIndex(items);
	    	return createLocalName(entityType, suffix);
	    }
	    
	    private static String createLocalName(URI entityType, int suffix)
	    {
	    	return createLocalName(entityType, String.valueOf(suffix));
	    }
	    
	    private static String createLocalName(URI entityType, String suffix)
	    {
	    	String displayId=getLocal(entityType).toLowerCase() + "_" + suffix;
	    	return displayId;
	    }
	    
	    public static void appendComponent(SBOLDocument document, Component parent, Component child) throws SBOLGraphException 
	    {
//	    	int index=1;
//	    	if (parent.getSubComponents()!=null)
//	    	{
//	    		/*for (SubComponent subComponent: parent.getSubComponents())
//	    		{
//	    			URI subComponentUri=subComponent.getIsInstanceOf();
//	    			Component component=(Component)document.getIdentified(subComponentUri, Component.class);
//	    			if (component.getTypes().contains(ComponentType.DNA.getUrl()))
//	    			{
//	    				index++;
//	    			}
//	    			
//	    		}*/
//	    		index=parent.getSubComponents().size()+1;
//	    	}
	    	String localName=createLocalName(DataModel.SubComponent.uri, parent.getSubComponents());
	    	SubComponent subComponent=parent.createSubComponent(append(parent.getUri(), localName), child.getUri());
	    	subComponent.setOrientation(Orientation.inline);
	    	
	    	List<URI> sequences= parent.getSequences();
	    	Sequence sequence=null;
	    	if (sequences!=null && sequences.size()>0)
	    	{
	    		 sequence=(Sequence)document.getIdentified(sequences.get(0),Sequence.class);
	    	}
	    	else
	    	{
	    		sequence=createSequence(document, parent, Encoding.NucleicAcid, "");	
	    	}
	    	
	    	
	    	if ( child.getSequences()!=null && child.getSequences().size()>0)
	    	{
	    		URI childSequenceUri=child.getSequences().get(0);
	    		Sequence childSequence=(Sequence)document.getIdentified(childSequenceUri, Sequence.class);
	    		sequence.setElements(sequence.getElements() + childSequence.getElements());
	    		String locationLocalName=createLocalName(DataModel.Location.uri, subComponent.getLocations());
	    		URI locationUri=append(subComponent.getUri(),locationLocalName);
	    		int start=sequence.getElements().length() + 1;
	        	int end=start + childSequence.getElements().length()-1;
	        	LocationBuilder builder=new Location.RangeLocationBuilder(locationUri, start, end);
	        	subComponent.createLocation(builder);
	    	}
	    }
	    
	    public static Component createDnaComponent(SBOLDocument doc, URI uri, String name, String displayId, String description, URI role, String sequence) throws SBOLGraphException
	    {
	    	Component dna=createComponent(doc, uri, ComponentType.DNA.getUrl(), name, displayId, description, role);
	    	if (sequence!=null && sequence.length()>0)
	    	{
	    		createSequence(doc, dna, Encoding.NucleicAcid, sequence);
	    	}
	    	return dna;
	    }
	    
	    public static Component createDnaComponent(SBOLDocument doc, String displayId, String description, URI role, String sequence) throws SBOLGraphException
	    {
	    	Component dna=createComponent(doc, append(doc.getBaseURI(),displayId), ComponentType.DNA.getUrl(), displayId, displayId, description, role);
	    	if (sequence!=null && sequence.length()>0)
	    	{
	    		createSequence(doc, dna, Encoding.NucleicAcid, sequence);
	    	}
	    	return dna;
	    }
	    
	    public static Component createProteinComponent(SBOLDocument doc, Component container, URI uri, String name, String displayId, String description, URI role, String sequence) throws SBOLGraphException
	    {
	    	Component protein=createComponent(doc, uri, ComponentType.Protein.getUrl(), name, displayId, description, role);
	    	container.createSubComponent(append(container.getUri(), displayId),  protein.getUri());
	    	if (sequence!=null && sequence.length()>0)
	    	{
	    		createSequence(doc, protein, Encoding.AminoAcid, sequence);
	    	}
	    	return protein;
	    }
	    
	    private static String getDisplayId(Identified identified)	    
	    {
	    	return identified.getDisplayId();
	    }
	    
	    public static SubComponent addSubComponent(SBOLDocument doc, Component parent, Component child) throws SBOLGraphException
	    {
	    	SubComponent subComponent=parent.createSubComponent(append(parent.getUri(), getDisplayId(child)),  child.getUri());
	    	return subComponent;
	    }
	    
	    
	    public static Sequence createSequence(SBOLDocument doc, Component component, Encoding encoding, String elements) throws SBOLGraphException
	    {
	    	String localName=createLocalName(DataModel.Sequence.uri, component.getSequences());
	    	Sequence seq=createSequence(doc, append(component.getUri(),localName), component.getName(), localName, component.getName() + " sequence" , elements, encoding);
	 		component.setSequences(Arrays.asList(seq.getUri())); 
	 		return seq;
	    }
	   
	    
	    public static Component createComponent(SBOLDocument doc, URI uri, URI type, String name, String displayId, String description, URI role) throws SBOLGraphException
	    {
	    	Component component=doc.createComponent(uri, Arrays.asList(type)); 
	        setCommonProperties(component, name, displayId, description);
	        if (role!=null)
	        {
	        	component.setRoles(Arrays.asList(role));
	        }
	        
	        return component;   
	    }
	    
	   
	    
	    public static URI append(URI uri, String id)
	    {
	    	return append(uri.toString(),id);   	
	    }
	    
	    public static URI append(URI uri, String type, String id)
	    {
	    	return append(append(uri.toString(),type),id);   	
	    }
	    
	    public static URI append(String text, String add)
	    {
	    	if (text.endsWith("/") || text.endsWith("#"))
	    	{
	    		return URI.create(String.format("%s%s", text,add));
	    	}
	    	else
	    	{	
	    		return URI.create(String.format("%s/%s", text,add));
	    	}
	    }
	    
	    
	    public static Sequence createSequence(SBOLDocument doc, URI uri, String name, String displayId, String description, String sequence, Encoding encoding) throws SBOLGraphException
	    {
	        Sequence sequenceEntity=doc.createSequence(uri);
	        setCommonProperties(sequenceEntity, name, displayId, description);
	        if (sequence!=null)
	        {
	        	sequenceEntity.setElements(sequence);
	        }
	        sequenceEntity.setEncoding(encoding);
	        return sequenceEntity;
	        
	    }
	    
	    private static void setCommonProperties(Identified identified, String name, String displayId, String description)
	    {
	    	identified.setName(name);
	    	identified.setDisplayId(displayId);
	    	identified.setDescription(description);
	    }
	    
	    public static void mapTo(SubComponent subComponentInContainer,Component container, Component parent, Component child) throws SBOLGraphException
		{
			 List<ComponentReference> childReferences=createComponentReference(container, parent, child);
			 if (childReferences!=null)
			 {
				 for (ComponentReference compRef: childReferences)
				 {
					 String localName=SBOLAPI.createLocalName(DataModel.Constraint.uri, container.getConstraints());
					 container.createConstraint(SBOLAPI.append(container.getUri(), localName), RestrictionType.Identity.verifyIdentical, subComponentInContainer.getUri(), compRef.getUri());
				 } 
			 }
		}
		
	    public static  List<ComponentReference> createComponentReference(Component container, Component parent, Component child) throws SBOLGraphException
		{
			List<ComponentReference> componentReferences=null;
			List<SubComponent> subComponents=getSubComponents(parent, child);
			if (subComponents!=null)
			{
				for (SubComponent subComponent:subComponents)
				{
					String localName=SBOLAPI.createLocalName(DataModel.ComponentReference.uri, container.getComponentReferences());
					ComponentReference compRef=container.createComponentReference(SBOLAPI.append(container.getUri(),localName), subComponent.getUri(), child.getUri());
			        if (componentReferences==null)
			        {
			        	componentReferences=new ArrayList<ComponentReference>();
			        }
			        componentReferences.add(compRef);
				}
			}
			return componentReferences;
		}
		
		/*private static List<URI> getSubComponent(Component parent, Component child) throws SBOLGraphException
		{
			List<URI> result=null;
			for (SubComponent subComponent:parent.getSubComponents())
			{
				if (subComponent.getIsInstanceOf().equals(child.getUri()))
				{
					if (result==null)
					{
						result=new ArrayList<URI>();
					}
					result.add(subComponent.getUri());
				}
				
			}
			return result;
		}
		*/
		
	   /* 
	    public static List<Interaction> createInteractionDel(List<URI> interactionTypes, Component parent, Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles) throws SBOLGraphException, SBOLException
	    {
	    	List<Interaction> interactions=new ArrayList<Interaction>();
	    	List<SubComponent> features1=getSubComponents(parent, participant1);
	    	List<SubComponent> features2=getSubComponents(parent, participant2);
	    	
	    	for (Feature feature1: features1)
	    	{
	    		for (Feature feature2: features2)
	    		{
	    			Interaction interaction=createInteractionDel(interactionTypes, parent, feature1, participant1Roles, feature2, participant2Roles);	
	    			interactions.add(interaction);
	    		}
	    	}
	    	return interactions;
	    }
	    
	    public static  Interaction createInteractionDel(List<URI> interactionTypes, Component container, Feature participant1, List<URI> participant1Roles, Feature participant2, List<URI> participant2Roles) throws SBOLGraphException, SBOLException
	    {
	    	int index=getIndex(container.getInteractionsDel());
	    	Interaction interaction= container.createInteractionDel(append(container.getUri(), "interaction" + index), interactionTypes);
	    	createParticipation(interaction, participant1Roles, participant1);
	    	createParticipation(interaction, participant2Roles, participant2);
	    	return interaction;
	    }
	    
	    public static Set<Interaction> createInteractionDel2(List<URI> interactionTypes, Component parent, Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles) throws SBOLGraphException, SBOLException
	    {
	    	Set<Interaction> interactions=new HashSet<Interaction>();
	    	List<SubComponent> features1=getSubComponents(parent, participant1);
	    	List<SubComponent> features2=getSubComponents(parent, participant2);
	    	
	    	for (Feature feature1: features1)
	    	{
	    		for (Feature feature2: features2)
	    		{
	    			Interaction interaction=createInteractionDel2(interactionTypes, parent, feature1, participant1Roles, feature2, participant2Roles);	
	    			interactions.add(interaction);
	    		}
	    	}
	    	return interactions;
	    }
	    
	    public static  Interaction createInteractionDel2(List<URI> interactionTypes, Component container, Feature participant1, List<URI> participant1Roles, Feature participant2, List<URI> participant2Roles) throws SBOLGraphException, SBOLException
	    {
	    	int index=getIndex(container.getInteractionsDel2());
	    	Interaction interaction= container.createInteractionDel2(append(container.getUri(), "interaction" + index), interactionTypes);
	    	createParticipation(interaction, participant1Roles, participant1);
	    	createParticipation(interaction, participant2Roles, participant2);
	    	return interaction;
	    }
	    */
	    
}
