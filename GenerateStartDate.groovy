//
// Small POC to calculate startdate in the past usign used time (currenttime-usedtime) 
//
// mika.nokka1@gmail.com 20.10.2020 ,  MIT licenced
//
//
// import com.atlassian.jira.component.ComponentAccessor
// import com.atlassian.jira.issue.CustomFieldManager
// import com.atlassian.jira.issue.MutableIssue
// import com.atlassian.jira.issue.customfields.CustomFieldType
//import com.atlassian.jira.issue.fields.CustomField
import java.util.*
import java.text.SimpleDateFormat
import java.text.DateFormat
import java.util.TimeZone
import java.sql.Timestamp
import groovy.time.TimeCategory
  
//import org.apache.log4j.Logger
//import org.apache.log4j.Level  


String TimeSpentMillisecs = "80966000"
Long MilliSecTime=0
int days=0
int hours=0
int minutes=0
int seconds=0


//def pattern = "yyyy-MM-dd"
//def date = new SimpleDateFormat(pattern).parse(TimeSpentMillisecs)


println Locale.getDefault()
//Date d = new Date(1317322560000)
//Date d = new Date(80966000)
//println d
//print "millis: $TimeSpentMillisecs"

MilliSecTime = TimeSpentMillisecs.toInteger()
//int days = (MilliSecTime / (1000 * 60 * 60 * 24)).intValue() % 365
//int hours = (MilliSecTime / (1000 * 60 * 60)).intValue() % 24;
//int minutes = (MilliSecTime / (1000 * 60)).intValue() % 60
//int seconds = (MilliSecTime / 1000).intValue() % 60;
//println "Used time millissec string: $TimeSpentMillisecs"
//println "days: $days hours: $hours minutes: $minutes seconds: $seconds"

def FirstDateObject = MilliSecsToDate(MilliSecTime)
//Date FirstDate = FirstDateObject.mydate
//println "FirstDate : $FirstDateObject.mydate"

//String DateInput=days+"-"+hours+"-"+minutes+"-"+seconds
//def pattern = "dd-MM-SS"
//def date = new SimpleDateFormat(pattern).parse(DateInput)
//println "Created Date object (used time since beginning January 1, 1970, 00:00:00 GMT): $date"


Date CurrentDate=new Date()
long CurrentDateMillisecs=CurrentDate.getTime()
println "CurrentDate: $CurrentDate (CurrentDateMillisecs:$CurrentDateMillisecs)"
MilliSecsToDate(CurrentDateMillisecs)

long BackDatedTimeMillisecs = CurrentDateMillisecs - MilliSecTime
println "BackDatedTimeMillisecs: $BackDatedTimeMillisecs"

Date BackDate
(BackDate,days,hours) = MilliSecsToDate(BackDatedTimeMillisecs)
//def BackDates = MilliSecsToDate(BackDatedTimeMillisecs)
/*Date BackDate=BackDates[0]
days=BackDates[0]
hours=BackDates[1]
minutes=BackDates[2]
seconds=BackDates[3]
*/
println "BackDate: $BackDate days: $days hours: $hours minutes: $minutes seconds: $seconds"



/*
use (groovy.time.TimeCategory) {
  println "BackDatedDate: $BackDatedDate"
  println BackDatedDate-45.hours
}
*/


// *************************************************************************************************** 
// Take millisecs and convert them to Date object and individual days,minutes,hours,seconds items
//
def MilliSecsToDate(TimeSpentMillisecs) {
       
       
       
MilliSecTime = TimeSpentMillisecs.toInteger()

int days = (MilliSecTime / (1000 * 60 * 60 * 24)).intValue() % 365
int hours = (MilliSecTime / (1000 * 60 * 60)).intValue() % 24;
int minutes = (MilliSecTime / (1000 * 60)).intValue() % 60
int seconds = (MilliSecTime / 1000).intValue() % 60;
println "----> Got millisecs: $TimeSpentMillisecs"
println "----> Calculated time tokens:  days: $days hours: $hours minutes: $minutes seconds: $seconds"
String DateInput=days+"-"+hours+"-"+minutes+"-"+seconds
def pattern = "dd-MM-SS"
Date  mydate = new SimpleDateFormat(pattern).parse(DateInput)
println "----> Created Date object: $mydate"
Date DirectDate= new Date(TimeSpentMillisecs)
println "----> Directly converted  Date object: $DirectDate"
return [mydate,days,hours]
//return [mydate,days,hours,minutes,seconds]
}
// ************************************************************************