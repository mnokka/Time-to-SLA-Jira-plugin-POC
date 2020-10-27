//
// POC to try get current SD ticket data:
// SLA time agreement, Used SD ticket time
// for time calculations
// to be used as Script Runner postfunction
// 

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.security.JiraAuthenticationContext;


// https://docs.atlassian.com/jira-servicedesk/4.13.0/com/atlassian/servicedesk/api/sla/info/SlaInformationService.html
import com.atlassian.servicedesk.api.sla.info.SlaInformationService;
import com.atlassian.servicedesk.api.sla.info.SlaInformationQuery;
import com.atlassian.servicedesk.api.util.paging.SimplePagedRequest;
import com.atlassian.servicedesk.api.sla.info.SlaInformation;
import com.atlassian.servicedesk.api.util.paging.PagedResponse;
import com.atlassian.servicedesk.api.sla.info.SlaInformationOngoingCycle;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.bc.ServiceResult;

import com.onresolve.scriptrunner.runner.customisers.PluginModule
import com.onresolve.scriptrunner.runner.customisers.WithPlugin;

import org.apache.log4j.Logger
import org.apache.log4j.Level  
import java.util.regex.Pattern

@WithPlugin("com.atlassian.servicedesk")
@PluginModule

//Provides methods to query Service Level Agreement information about an issue
SlaInformationService slaInformationService;


// set logging to Jira log
def logger = Logger.getLogger("SDGetter") 
logger.setLevel(Level.DEBUG) // or INFO
logger.info("---------- SDGetter started -----------")

logger.debug("Issue: $issue")


// complains (warn) if no Jira assignee present in SD ticket
// A query parameter object for the SlaInformationService calls
final SlaInformationQuery query = buildSlaInformationQuery(slaInformationService, issue);
logger.debug("query: $query")

//complains (warn) if request type not exists ?? cant set in SD disk
final PagedResponse<SlaInformation> pagedResponse = getSlaInformationList(slaInformationService, query);
logger.debug("pagedResponse: $pagedResponse")


List TheList=pagedResponse.getResults()
Size= TheList.size()
logger.debug("TheList: $TheList")
logger.debug("Size: $Size")


TheList.each {
    //logger.debug("ITEM: $it")
	def re = it.toString() =~ /(.+)(goalDuration=)(\d+)(.+)(elapsedTime=)(\d+)(.+)/   // SlaInformationImpl{id=8, name=TaskSLAForTesting, completedCycles=[], ongoingCycle=Optional[SlaInformationOngoingCycleImpl{startTime=2020-10-27T07:51:59.258Z, breachTime=Optional[2020-10-28T20:24:59.258Z], breached=false, paused=false, withinCalendarHours=true, goalDuration=131580000, elapsedTime=3631211, remainingTime=127948789}]}
    //logger.debug("re: $re")
 
	if (re.matches() ) {
	    //logger.debug("HITHIT")
		//def Long goalDuration= re[0][3] 
	  	//def String 	elapsedTime= re[0][6]  
	  	def value1=re[0][3]
	  	def value2=re[0][6]
	  	Long goalDuration=value1.toInteger() // convert to int, this works
	  	Long elapsedTime=value2.toInteger() // convert to int, this works
		//logger.debug("${re[0][0]}")
		logger.debug("goalDuration: ${goalDuration}     elapsedTime: $elapsedTime" )
	}
	else {
		//log.debug("No match" )
	}
}


final SlaInformation slaInformation = getSlaInformation(pagedResponse);
logger.debug("slaInformation: $slaInformation")

//duration=slaInformation.goalDuration()
//goalDuration
// dies
//final SlaInformationOngoingCycle slaInformationOngoingCycle = getSlaInformationOngoingCycle(slaInformation.getOngoingCycle());
//logger.debug("slaInformationOngoingCycle: $slaInformationOngoingCycle")



//String MYslaInformationOngoingCycle = slaInformationOngoingCycle.toString()
//logger.debug ("MYslaInformationOngoingCycle: $MYslaInformationOngoingCycle");

//Date dateStartTime = Date.from(slaInformationOngoingCycle.getStartTime())
//logger.debug ("dateStartTime: $dateStartTime");




logger.info("---------- SDGetter stopped -----------")



// ******************************************************************************
// ----- methods ------


// todo: functionalize this
SlaInformationQuery buildSlaInformationQuery(SlaInformationService slaInformationService, 
                                             Issue issue) {
    return slaInformationService.newInfoQueryBuilder()  // Create a builder to build SlaInformationQuerys  https://docs.atlassian.com/jira-servicedesk/4.13.0/com/atlassian/servicedesk/api/sla/info/SlaInformationService.html#newInfoQueryBuilder--
        .issue(issue.getId())                            // A query parameter object for the SlaInformationService calls    https://docs.atlassian.com/jira-servicedesk/4.13.0/com/atlassian/servicedesk/api/sla/info/SlaInformationQuery.html
        //.pagedRequest(SimplePagedRequest.paged(0, 10))  // some default then?
        .build()
}

PagedResponse<SlaInformation> getSlaInformationList(SlaInformationService slaInformationService,
                                                    SlaInformationQuery query) {
    final JiraAuthenticationContext jiraAuthenticationContext = ComponentAccessor.getJiraAuthenticationContext();
    final ApplicationUser applicationUser = jiraAuthenticationContext.getLoggedInUser();
    return slaInformationService.getInfo(applicationUser, query)
}

SlaInformation getSlaInformation( PagedResponse<SlaInformation> pagedResponse ) {
    return pagedResponse.getResults().find { "Time to resolution".equals(it.getName()) }
}


// dies
SlaInformationOngoingCycle getSlaInformationOngoingCycle(Optional<SlaInformationOngoingCycle> optionalSlaInformationOngoingCycle) {
    if(optionalSlaInformationOngoingCycle.empty()) { return null; }
    return optionalSlaInformationOngoingCycle.get();
}


