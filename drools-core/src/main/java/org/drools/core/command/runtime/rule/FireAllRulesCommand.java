/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.core.command.runtime.rule;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.drools.core.command.IdentifiableResult;
import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.impl.KnowledgeCommandContext;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.drools.core.runtime.impl.ExecutionResultImpl;
import org.kie.command.Context;
import org.kie.runtime.KieSession;
import org.kie.runtime.rule.AgendaFilter;

@XmlAccessorType(XmlAccessType.NONE)
public class FireAllRulesCommand
    implements
    GenericCommand<Integer>, IdentifiableResult {

    @XmlAttribute
    private int          max          = -1;
    private AgendaFilter agendaFilter = null;
    
    @XmlAttribute(name="out-identifier")
    private String       outIdentifier;

    public FireAllRulesCommand() {
    }

    public FireAllRulesCommand(String outIdentifer) {
        this.outIdentifier = outIdentifer;
    }

    public FireAllRulesCommand(int max) {
        this.max = max;
    }

    public FireAllRulesCommand(AgendaFilter agendaFilter) {
        this.agendaFilter = agendaFilter;
    }

    public FireAllRulesCommand(AgendaFilter agendaFilter, int max) {
        this.agendaFilter = agendaFilter;
        this.max = max;
    }

    public FireAllRulesCommand(String outIdentifier,
                               int max,
                               AgendaFilter agendaFilter) {
        this.outIdentifier = outIdentifier;
        this.max = max;
        this.agendaFilter = agendaFilter;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public AgendaFilter getAgendaFilter() {
        return agendaFilter;
    }

    public void setAgendaFilter(AgendaFilter agendaFilter) {
        this.agendaFilter = agendaFilter;
    }

    public String getOutIdentifier() {
        return outIdentifier;
    }

    public void setOutIdentifier(String outIdentifier) {
        this.outIdentifier = outIdentifier;
    }

    public Integer execute(Context context) {
        KieSession ksession = ((KnowledgeCommandContext) context).getKieSession();
        int fired;
        if ( max != -1 && agendaFilter != null ) {
            fired = ((StatefulKnowledgeSessionImpl) ksession).session.fireAllRules( new StatefulKnowledgeSessionImpl.AgendaFilterWrapper( agendaFilter ), max );
        } else if ( max != -1 ) {
            fired = ksession.fireAllRules( max );
        } else if ( agendaFilter != null ) {
            fired = ((StatefulKnowledgeSessionImpl) ksession).session.fireAllRules( new StatefulKnowledgeSessionImpl.AgendaFilterWrapper( agendaFilter ) );
        } else {
            fired = ksession.fireAllRules();
        }

        if ( this.outIdentifier != null ) {
            ExecutionResultImpl results = ((StatefulKnowledgeSessionImpl)ksession).session.getExecutionResult();
            results.getResults().put(this.outIdentifier, fired);
        }
        return fired;
    }

    public String toString() {
        if ( max != -1 && agendaFilter != null ) {
            return "session.fireAllRules( " + agendaFilter + ", " + max + " );";
        } else if ( max != -1 ) {
            return "session.fireAllRules( " + max + " );";
        } else if ( agendaFilter != null ) {
            return "session.fireAllRules( " + agendaFilter + " );";
        } else {
            return "session.fireAllRules();";
        }
    }

}
