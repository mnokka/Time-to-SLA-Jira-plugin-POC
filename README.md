# Time-to-SLA-Jira-plugin-POC


GetStartTime.groovy: POC to get and set Time to SLA plugin datafields

Using vendor code as a base :
https://confluence.snapbytes.com/time-to-sla/knowledge-base/groovy-scripts-for-tts-fields


Use case: Project 1 uses Time to SLA plugin and creates escalation ticket to Project 2 (JSU or similar automation plugin) 
Project 1 issue stores it's SLA starting date to date picker custom field (automation code in this repo)
JSU (or similar) automation plugin passes date picker custom field data to Project 2 date picker custom field.

Project 2 Time to SLA plugin custom field starst its' SLA time counting using passed date information (from date picker). 
This makes possible Project 2 to carry on time wise, from where Project 1 stopped working for issue

Could be used for SD escalations



