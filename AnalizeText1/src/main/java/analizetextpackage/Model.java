package analizetextpackage;

import java.util.Collection;
import java.util.Set;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "model")
public class Model {
	
//    @XmlElementWrapper
//    @XmlAnyElement(lax=true)
    @XmlElement(name="Rule")
    private Collection<Rule> rules;
    
    @XmlElementWrapper
    @XmlAnyElement(lax=true)
    private Set<String> approvedFacts;

	public Model() {
	}
	
 	public Model(Collection<Rule> rules, Set<String> approvedFacts) {
		this.rules = rules;
		this.approvedFacts = approvedFacts; 
	}

	public void calculate() {
		int approvedFactsSize;
		do {
			approvedFactsSize = approvedFacts.size();
			for (Rule rule : rules) {
				rule.calculate(approvedFacts);
			}
		} while (approvedFactsSize != approvedFacts.size());
	}
	
	public Set<String> getApprovedFacts() {
		return approvedFacts;
	}
	
}
