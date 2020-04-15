package org.sbolstandard;

import java.util.ArrayList;
import java.util.List;

import org.sbolstandard.sequence.Component;
import org.sbolstandard.sequence.Sequence;

public class SBOLDocument {
List<Component> components;
public List<Component> getComponents() {
	return components;
}

public void setComponents(List<Component> components) {
	this.components = components;
}

public List<Sequence> getSequences() {
	return sequences;
}

public void setSequences(List<Sequence> sequences) {
	this.sequences = sequences;
}

List<Sequence> sequences;

public void addSequence(Sequence sequence)
{
	if (sequences==null)
	{
		sequences=new ArrayList<Sequence>();
	}
	sequences.add(sequence);
	
}



}
