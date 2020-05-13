package org.sbolstandard.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sbolstandard.entity.Component;
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
import org.sbolstandard.vocabulary.Encoding;
import org.sbolstandard.vocabulary.Orientation;

public class SBOLAPI {

	  public static List<Interaction> createInteraction(List<URI> interactionTypes, Component parent, Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles) throws SBOLGraphException
	    {
	    	List<Interaction> interactions=new ArrayList<Interaction>();
	    	List<SubComponent> features1=getSubComponents(parent, participant1);
	    	List<SubComponent> features2=getSubComponents(parent, participant2);
	    	
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
	    	int index=getIndex(container.getInteractions());
	    	Interaction interaction= container.createInteraction(append(container.getUri(), "interaction" + index), interactionTypes);
	    	createParticipation(interaction, participant1Roles, participant1);
	    	createParticipation(interaction, participant2Roles, participant2);
	    	return interaction;
	    }
	    
	   
	    public static  Participation createParticipation(Interaction interaction, List<URI> roles, Feature feature) throws SBOLGraphException
	    {
	    	int index=getIndex(interaction.getParticipations());
	    	URI uri=append(interaction.getUri(), "participant" + index);
	    	
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
	    
	    public static void appendComponent(SBOLDocument document, Component parent, Component child) throws SBOLGraphException 
	    {
	    	int index=1;
	    	if (parent.getSubComponents()!=null)
	    	{
	    		index=parent.getSubComponents().size()+1;
	    	}
	    	
	    	SubComponent subComponent=parent.createSubComponent(URI.create(parent.getUri() + "/subComponent_" +  index), child.getUri());
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
	    	
	    		URI childSequenceUri=child.getSequences().get(0);
	    		Sequence childSequence=(Sequence)document.getIdentified(childSequenceUri, Sequence.class);
	    		sequence.setElements(sequence.getElements() + childSequence.getElements());
	    		int locationIndex=1;
	    		if (subComponent.getLocations()!=null)
	    		{
	    			locationIndex=subComponent.getLocations().size() + 1;
	    		}
	    		String locationUri=String.format("%s/location_%s", subComponent.getUri().toString(), locationIndex);
	        	int start=sequence.getElements().length() + 1;
	        	int end=start + childSequence.getElements().length()-1;
	        	
	        	LocationBuilder builder=new Location.RangeLocationBuilder(URI.create(locationUri), start, end);
	        	subComponent.createLocation(builder);	
	    }
	    
	    public static Component createDnaComponent(SBOLDocument doc, URI uri, String name, String displayId, String description, URI role, String sequence)
	    {
	    	Component dna=createComponent(doc, uri, ComponentType.DNA.getUrl(), name, displayId, description, role);
	    	if (sequence!=null && sequence.length()>0)
	    	{
	    		createSequence(doc, dna, Encoding.NucleicAcid, sequence);
	    	}
	    	return dna;
	    }
	    
	    public static Component createProteinComponent(SBOLDocument doc, Component container, URI uri, String name, String displayId, String description, URI role, String sequence)
	    {
	    	Component protein=createComponent(doc, uri, ComponentType.Protein.getUrl(), name, displayId, description, role);
	    	container.createSubComponent(append(container.getUri(), displayId),  protein.getUri());
	    	if (sequence!=null && sequence.length()>0)
	    	{
	    		createSequence(doc, protein, Encoding.AminoAcid, sequence);
	    	}
	    	return protein;
	    }
	    
	    public static Sequence createSequence(SBOLDocument doc, Component component, Encoding encoding, String elements)
	    {
	 		Sequence seq=createSequence(doc, append(component.getUri(),"seq"), component.getName() + " sequence", component.getDisplayId() + "_seq", component.getName() + " sequence" , elements, encoding);
	 		component.setSequences(Arrays.asList(seq.getUri())); 
	 		return seq;
	    }
	   
	    
	    public static Component createComponent(SBOLDocument doc, URI uri, URI type, String name, String displayId, String description, URI role)
	    {
	    	Component component=doc.createComponent(uri, Arrays.asList(type)); 
	        setCommonProperties(component, name, displayId, description);
	        component.setRoles(Arrays.asList(role));
	        
	        return component;   
	    }
	    
	    private static URI append(URI uri, String id)
	    {
	    	return URI.create(String.format("%s/%s", uri,id));
	    }
	    
	    private static URI append(String text, String add)
	    {
	    	return URI.create(String.format("%s %s", text,add));
	    }
	    
	    
	    public static Sequence createSequence(SBOLDocument doc, URI uri, String name, String displayId, String description, String sequence, Encoding encoding)
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
