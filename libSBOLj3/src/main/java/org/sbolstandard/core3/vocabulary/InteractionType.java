package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sbolstandard.core3.util.URINameSpace;

public enum InteractionType implements HasURI{
	
		Inhibition(URINameSpace.SBO.local("0000169")),
		Stimulation(URINameSpace.SBO.local("0000170")),
		BiochemicalReaction(URINameSpace.SBO.local("0000176")),
		NonCovalentBinding(URINameSpace.SBO.local("0000177")),
		Degradation(URINameSpace.SBO.local("0000179")),
		GeneticProduction(URINameSpace.SBO.local("0000589")),
		Control(URINameSpace.SBO.local("0000168"));

		private URI uri;

		InteractionType(URI uri) {
			this.uri = uri;
		}

		public URI getUri() {
			return uri;
		}
		
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, InteractionType> lookup = new HashMap<>();

		static {
			for (InteractionType value : InteractionType.values()) {
				lookup.put(value.getUri(), value);
			}
		}

		public static InteractionType get(URI url) {
			return lookup.get(url);
		}

		public static List<ParticipationRole> mapParticipationRoles(InteractionType type) {
			switch (type) {
			case Inhibition:
				return Arrays.asList(ParticipationRole.Inhibited, ParticipationRole.Inhibitor, ParticipationRole.Promoter);
			case BiochemicalReaction:
				return Arrays.asList(ParticipationRole.Reactant, ParticipationRole.Product, ParticipationRole.Modifier, ParticipationRole.Modified);
			case Control:
				return Arrays.asList(ParticipationRole.Modifier, ParticipationRole.Modified);
			case Degradation:
				return Arrays.asList(ParticipationRole.Reactant);
			case GeneticProduction:
				return Arrays.asList(ParticipationRole.Product, ParticipationRole.Promoter, ParticipationRole.Template);
			case NonCovalentBinding:
				return Arrays.asList(ParticipationRole.Reactant, ParticipationRole.Product);
			case Stimulation:
				return Arrays.asList(ParticipationRole.Stimulator, ParticipationRole.Stimulated, ParticipationRole.Promoter);
			default:
				return null;
			}
		}
}
