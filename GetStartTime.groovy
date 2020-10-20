//
// Time to SLA Jira plugin POC trial
//
// mika.nokka1@gmail.com 20.10.2020 ,  MIT licenced
//
// Vendor code  as a base:https://confluence.snapbytes.com/time-to-sla/knowledge-base/groovy-scripts-for-tts-fields
//
// Trying to get Time to SLA plugin custom field "starting time" and inserting it to another string custom field
// to be used in another Time to SLA custom field as it's "starting time" 
//







import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.customfields.CustomFieldType
import com.atlassian.jira.issue.fields.CustomField
import java.util.*
import java.text.SimpleDateFormat
import java.sql.Timestamp
  
import org.apache.log4j.Logger
import org.apache.log4j.Level  
  
  
// set logging to Jira log
def logger = Logger.getLogger("TimetoSLA") 
logger.setLevel(Level.DEBUG) // or INFO
logger.info("---------- TimetoSLA started -----------")
  
// get CustomFieldManager instance
def customFieldManager = ComponentAccessor.getCustomFieldManager()
// find your TTS custom field's ID and put it here instead of 10600

// using ID harcdcoding intentionally for POC
def ttsField = customFieldManager.getCustomFieldObject("customfield_10211")

// get custom field value object
def ttsFieldValue = issue.getCustomFieldValue(ttsField)
// date/time formatter will be used to format date attributes
def formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
// all attributes will be stored to result variable
def result = ""

// TTS custom field returns list, let's iterate
Date originDate  
if (ttsFieldValue && ttsFieldValue.size() > 0) {
   ttsFieldValue.each {
       String slaName = it.slaName
       int slaValueAsMinutes = it.slaValue
       String slaValueAsTimeString = it.slaValueAsString
       String originStatusName = it.originStatusName
       String targetStatusName = it.targetStatusName
       originDate = it.statusDate
       Date expectedTargetDate = it.slaTargetDate
       Date actualTargetDate = it.untilDate
       long timeLeftTillSla = it.timeToSla // if less than 0, overdue
       String timeLeftTillSlaAsTimeString = it.timeToSlaAsString // if there is overdue, overdue string
       boolean isPaused = it.paused
       boolean startDateProvidedByDateCustomField = it.startDateProvidedByDateCustomField
       boolean endDateProvidedByDateCustomField = it.endDateProvidedByDateCustomField
       boolean negotiationDateProvidedByDateCustomField = it.negotiationDateProvidedByDateCustomField
  
       result = """
          [SLA Name: $slaName]
          [SLA Value As Minutes: $slaValueAsMinutes]
          [SLA Value As Time String: $slaValueAsTimeString]
          [Origin Status: $originStatusName]
          [Target Status: $targetStatusName]
          [Origin Date: ${formatter.format(originDate)}]
          [Expected Target Date: ${expectedTargetDate ? formatter.format(expectedTargetDate) : 'Not yet defined'}]
          [Actual Target Date: ${actualTargetDate ? formatter.format(actualTargetDate) : 'Not yet'}]
          [Time Left Till SLA as milis: $timeLeftTillSla]
          [Time Left Till SLA as Time String: $timeLeftTillSlaAsTimeString]
          [Is SLA in Paused Status: $isPaused]
          [Is Start Date Provided By Date Custom Field: $startDateProvidedByDateCustomField]
          [Is End Date Provided By Date Custom Field: $endDateProvidedByDateCustomField]
          [Is Negotiation Date Provided By Date Custom Field: $negotiationDateProvidedByDateCustomField]
       """
     }
}
  
result.toString()


String StartTiming= "xxx"
if (originDate) {
   StartTiming= formatter.format(originDate)
   }

// Setting starting timig to custom field
// going to use JSU/JWT to pass this information to other project's task 

final customFieldName = "StartingTiming"
def TimingField = customFieldManager.getCustomFieldObjects(issue).find { it.name == customFieldName }
//def TimingField = customFieldManager.getCustomFieldObject("customfield_10211")	
issue.setCustomFieldValue(TimingField, StartTiming)

// Datepicker cusdtom field is needed to Time to SLA plugin customer field to use it as starting date (i.e passing used time from one project ticket to another)
final customDateFieldNAme="TimingDateField"
def DateField = customFieldManager.getCustomFieldObjects(issue).find { it.name == customDateFieldNAme }
issue.setCustomFieldValue(DateField, originDate)



logger.debug ("Data: $result")
logger.info ("Issue: $issue, SLA start timing: $StartTiming")
logger.info("---------- TimetoSLA ended -----------")
