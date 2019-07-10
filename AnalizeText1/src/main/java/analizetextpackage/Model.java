package analizetextpackage;

import java.util.Collection;
import java.util.Set;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "model", namespace="xsi:http://www.w3.org/2001/XMLSchema-instance")
public class Model {
	
	@XmlElementWrapper(name="rules", namespace="xsi:http://www.w3.org/2001/XMLSchema-instance")
	@XmlElement(name="rule", namespace="xsi:http://www.w3.org/2001/XMLSchema-instance")
    private Collection<Rule> rules;
    
	@XmlElementWrapper(name="approvedFacts", namespace="xsi:http://www.w3.org/2001/XMLSchema-instance")
	@XmlElement(name="approvedFact", namespace="xsi:http://www.w3.org/2001/XMLSchema-instance")
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


