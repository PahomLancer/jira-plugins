package com.atlassian.externaldbplugin.jira.webwork;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.UpdateIssueRequest;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.status.SimpleStatus;
import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class MyWebworkModuleAction extends JiraWebActionSupport
{
    private long issue;
    
    public class Issue {
    private String key; //ключ задачи
    private String parentkey; //ключ родителя задачи
    private int level; //уровень вложенности подзадачи
    private String name; //название задачи
    private String desc; //описание задачи
    private String status; //статус задачи
    private double complexity; //сложность задачи
    private double priority; //приоритет задачи(число)
    private double urgency; //срочность задачи
    private double weight; //общий вклад задачи
    private double weight2; //общий вклад задачи
    private double weight3; //общий вклад задачи
    private double value; //текущий вклад задачи
    private double value2; //текущий вклад задачи
    private double value3; //текущий вклад задачи
    private double contribution; //критерий принятия решений
    private double contribution2; //критерий принятия решений
    private double contribution3; //критерий принятия решений
    private double time_to_complete; //время для завершения задачи
    private double time_to_end; //время до конца задачи
    private double percantage_of_completion; //процент выполнения задачи
    private ArrayList<Issue> child_issues = new ArrayList<Issue>(); //подзадачи
    
    public ArrayList<Issue> getChild_Issues(){return this.child_issues;}
    public void setChild_Issues(ArrayList<Issue> child_issues){this.child_issues = child_issues;}
    
    public void setIssueKey(String key){this.key = key;}
    public String getIssueKey(){return this.key;}
    
    public void setIssueParentkey(String parentkey){this.parentkey = parentkey;}
    public String getIssueParentkey(){return this.parentkey;}
        
    public void setIssueLevel(int level){this.level = level;}
    public int getIssueLevel(){return this.level;}
    
    public void setIssueName(String name){this.name = name;}
    public String getIssueName(){return this.name;}
    
    public void setIssueDesc(String desc){this.desc = desc;}
    public String getIssueDesc(){return this.desc;}
    
    public void setIssueComplexity(double complexity){this.complexity = complexity;}
    public double getIssueComplexity(){return this.complexity;}
    
    public void setIssuePriority(double priority){this.priority = priority;}
    public double getIssuePriority(){return this.priority;}
    
    public void setIssueUrgency(double urgency){this.urgency = urgency;}
    public double getIssueUrgency(){return this.urgency;}
    
    public void setIssueWeight(double weight){this.weight = weight;}
    public double getIssueWeight(){return this.weight;}
    
    public void setIssueWeight2(double weight2){this.weight2 = weight2;}
    public double getIssueWeight2(){return this.weight2;}
    
    public void setIssueWeight3(double weight3){this.weight3 = weight3;}
    public double getIssueWeight3(){return this.weight3;}
    
    public void setIssueValue(double value){this.value = value;}
    public double getIssueValue(){return this.value;}
    
    public void setIssueValue2(double value2){this.value2 = value2;}
    public double getIssueValue2(){return this.value2;}
    
    public void setIssueValue3(double value3){this.value3 = value3;}
    public double getIssueValue3(){return this.value3;}
    
    public void setIssueContribution(double contribution){this.contribution = contribution;}
    public double getIssueContribution(){return this.contribution;}
    
    public void setIssueContribution2(double contribution2){this.contribution2 = contribution2;}
    public double getIssueContribution2(){return this.contribution2;}
    
    public void setIssueContribution3(double contribution3){this.contribution3 = contribution3;}
    public double getIssueContribution3(){return this.contribution3;}
    
    public void setIssueStatus(String status){this.status = status;}
    public String getIssueStatus(){return this.status;}
    
    public void setIssueTime_to_complete(double time_to_complete){this.time_to_complete = time_to_complete;}
    public double getIssueTime_to_complete(){return this.time_to_complete;}
    
    public void setIssueTime_to_end(double time_to_end){this.time_to_end = time_to_end;}
    public double getIssueTime_to_end(){return this.time_to_end;}
    
    public void setIssuePercantage_of_completion(double percantage_of_completion){this.percantage_of_completion = percantage_of_completion;}
    public double getIssuePercantage_of_completion(){return this.percantage_of_completion;}
    }
    
    private ArrayList<Issue> issues = new ArrayList<Issue>();
    public ArrayList<Issue> getIssues(){return this.issues;}
    public void setIssues(ArrayList<Issue> issue){this.issues = issue;}
     
    private static final Logger log = LoggerFactory.getLogger(MyWebworkModuleAction.class);
    
    private Project project;
    
    private String issue_id;
    public String getissue_id(){ return this.issue_id; }
    public void setissue_id(String issue_id){ this.issue_id = issue_id;}
    
    private String issue_name;
    public String getissue_name(){ return this.issue_name; }
    public void setissue_name(String issue_name){ this.issue_name = issue_name;}
    
    private String issue_desc;
    public String getissue_desc(){ return this.issue_desc; }
    public void setissue_desc(String issue_desc){ this.issue_desc = issue_desc;}
    
    private String issue_days;
    public String getissue_days(){ return this.issue_days; }
    public void setissue_days(String issue_days){ this.issue_days = issue_days;}
    
    private String issue_priority;
    public String getissue_priority(){ return this.issue_priority; }
    public void setissue_priority(String issue_priority){ this.issue_priority = issue_priority;}
    
    private String issue_status;
    public String getissue_status(){ return this.issue_status; }
    public void setissue_status(String issue_status){ this.issue_status = issue_status;}
    
    private String pup;
    public void setPup(String pup){this.pup = pup;}
    public String getPup(){return this.pup;}
    
    private String puptest;
    public void setPuptest(String puptest){this.puptest = puptest;}
    public String getPuptest(){return this.puptest;}
    
    private int index;
    public void setIndex(int index){this.index = index;}
    public int getIndex(){return this.index;}
    
    private int maxlevel;
    public void setMaxlevel(int maxlevel){this.maxlevel = maxlevel;}
    public int getMaxlevel(){return this.maxlevel;}
    
    private int scale;
    public void setScale(int scale){this.scale = scale;}
    public int getScale(){return this.scale;}
    
    private long time;
    public void setTime(long time){this.time = time;}
    public long getTime(){return this.time;}
    
    private long time2;
    public void setTime2(long time2){this.time2 = time2;}
    public long getTime2(){return this.time2;}
    
    @Override
    public String execute() throws Exception {        
        
        ComponentAccessor componentAccessor = new ComponentAccessor();
        ApplicationUser user = componentAccessor.getUserManager().getUserByName("admin");
        IssueManager issueManager = componentAccessor.getIssueManager();
        CustomFieldManager customFieldManager = componentAccessor.getCustomFieldManager();
        MutableIssue issueObject;
        Status status;
        SimpleStatus simplestatus;
        CustomField customFieldObject;
        
        //long t = issueManager.getIssueCount();
        String projectname = "LIFE-";
        int id = 1;
        int count = 0;
        issueObject = issueManager.getIssueByCurrentKey(projectname + id);
        
        //Filter options
        issue_id = getissue_id();
        issue_name = getissue_name();
        issue_desc = getissue_desc();
        issue_days = getissue_days();
        issue_priority = getissue_priority();
        issue_status = getissue_status();

        while (issueObject != null){
            
            //Creating issue local class
            Issue temp = new Issue();
            
            if (issue_id == null)
            {
                issue_id = "Is null";
                pup = issue_id;
                puptest = projectname + id;
            }
            else{
                if (issue_id.equals(projectname + id)){
                    issueObject.setSummary(issue_name);
                    issueObject.setDescription(issue_desc);
                    customFieldObject = customFieldManager.getCustomFieldObject("customfield_10101");
                    issueObject.setCustomFieldValue(customFieldObject, issue_days);
                    customFieldObject = customFieldManager.getCustomFieldObject("customfield_10100");
                    issueObject.setCustomFieldValue(customFieldObject, issue_priority);
                    issueManager.updateIssue(user, issueObject, UpdateIssueRequest.builder().build());
                    pup = issue_id;
                    puptest = projectname + id;
                }
            }
            //Getting Issue Key
            temp.setIssueKey(issueObject.getKey());
            
            //Getting Name
            temp.setIssueName(issueObject.getSummary());
            
            //Getting Description
            temp.setIssueDesc(issueObject.getDescription());
            
            //Getting Time_to_end
            long curTime = System.currentTimeMillis(); 
            Timestamp curdate = new Timestamp(curTime);
            temp.setIssueTime_to_end((-curdate.getTime() + issueObject.getDueDate().getTime()) / 3600000);
            
            //Getting Time_to_complete
            customFieldObject = customFieldManager.getCustomFieldObject("customfield_10201");
            temp.setIssueTime_to_complete(
                    Double.parseDouble(issueObject.getCustomFieldValue(customFieldObject).toString())
            );
            
            //Getting Urgency
            temp.setIssueUrgency(temp.getIssueTime_to_end() - temp.getIssueTime_to_complete());
            
            //Getting Parent issue key 
            customFieldObject = customFieldManager.getCustomFieldObject("customfield_10300");
            if (issueObject.getCustomFieldValue(customFieldObject) != null){
                temp.setIssueParentkey(
                    issueObject.getCustomFieldValue(customFieldObject).toString()
                );
            }
            else {
                temp.setIssueParentkey("No parent");
                temp.setIssueLevel(0);
            }
            
            //Getting Percantage_of_completion 
            customFieldObject = customFieldManager.getCustomFieldObject("customfield_10202");
            temp.setIssuePercantage_of_completion(
                    Double.parseDouble(issueObject.getCustomFieldValue(customFieldObject).toString())
            );
            
            //Getting Priority
            customFieldObject = customFieldManager.getCustomFieldObject("customfield_10200");
            temp.setIssuePriority(
                    Double.parseDouble(issueObject.getCustomFieldValue(customFieldObject).toString())
            );

            //Getting Status
            status = issueObject.getStatus();
            simplestatus = status.getSimpleStatus();
            temp.setIssueStatus(simplestatus.getName());
            
            //Filter for filter options
            //if (tempstr.equals("any") || tempstr.equals("") || tempstr.equals(simplestatus.getName().replaceAll(" ", ""))){
            issues.add(count, temp);
            count++;
            //}

            //Getting next issue
            id++;
            issueObject = issueManager.getIssueByCurrentKey(projectname + id);
        }
        setIssues(issues);

        maxlevel = 0;
        index = issues.size();
        for (int i = 0; i < index; i++){
            Issue temp = issues.get(i);
            String parent_key = temp.getIssueParentkey();
            for (int j = 0; j < index; j++){
                if (i != j) {
                    Issue temp2 = issues.get(j);
                    String issue_key = temp2.getIssueKey();
                    if (parent_key.equals(issue_key)){
                        temp.setIssueLevel(temp2.getIssueLevel()+1);
                        if (temp2.getIssueLevel()+1 > maxlevel){
                            maxlevel = temp2.getIssueLevel()+1;
                        }
                    }
                }
            }
            issues.set(i, temp);
        }
        setIssues(issues);
        
        double maxurgency;
        for (int level = 0; level <= maxlevel; level++){
            maxurgency = 0;
            for (int i = 0; i < index; i++){
                Issue temp = issues.get(i);
                if (level == temp.getIssueLevel()){
                    if (maxurgency < temp.getIssueUrgency()){
                        maxurgency = temp.getIssueUrgency();
                    } 
                }
            }
            for (int i = 0; i < index; i++){
                Issue temp = issues.get(i);
                if (level == temp.getIssueLevel()){
                    temp.setIssueUrgency(maxurgency - temp.getIssueUrgency() + 1);
                }
                issues.set(i, temp);
            }
        }
        setIssues(issues);
        
        for (int i = 0; i < index; i++){
            Issue temp = issues.get(i);
            String issue_key = temp.getIssueKey();
            for (int j = 0; j < index; j++){
                Issue temp2 = issues.get(j);
                String parent_key = temp2.getIssueParentkey();
                if (issue_key.equals(parent_key)){
                    temp.child_issues.add(temp2);
                }
            }
            issues.set(i, temp);
        }
        
        double sumurgency = 0;
        double sumpriority = 0;
        double sumhard = 0;
        scale = 1000;
        double tempscale = 0;
        double tempscale2 = 0;
        double tempscale3 = 0;

        for (int i = 0; i < index; i++){
            Issue temp = issues.get(i);
            if (0 == temp.getIssueLevel()){
                tempscale  = scale;
                tempscale2  = scale;
                tempscale3  = scale;
                sumurgency = sumurgency + temp.getIssueUrgency();
                sumpriority = sumpriority + temp.getIssuePriority();
                sumhard = sumhard + temp.getIssueTime_to_complete();
            }
        }
        for (int i = 0; i < index; i++){
            Issue temp = issues.get(i);
            if (0 == temp.getIssueLevel()){
                temp.setIssueUrgency(temp.getIssueUrgency()/sumurgency);
                temp.setIssuePriority(temp.getIssuePriority()/sumpriority);
                temp.setIssueTime_to_complete(temp.getIssueTime_to_complete()/sumhard);
                temp.setIssueWeight((temp.getIssueUrgency() + temp.getIssuePriority() + temp.getIssueTime_to_complete())/3*tempscale);
                temp.setIssueWeight2((temp.getIssueUrgency() + temp.getIssuePriority())/2*tempscale2);
                if (temp.getIssueUrgency() > temp.getIssuePriority()){
                    temp.setIssueWeight3(temp.getIssuePriority()/(1 + temp.getIssuePriority() - temp.getIssueUrgency())*tempscale3);
                }
                else{
                    temp.setIssueWeight3(temp.getIssueUrgency()/(1 + temp.getIssueUrgency() - temp.getIssuePriority())*tempscale3);
                }
                temp.setIssueValue(temp.getIssueWeight() * temp.getIssuePercantage_of_completion() / 100);
                temp.setIssueValue2(temp.getIssueWeight2() * temp.getIssuePercantage_of_completion() / 100);
                temp.setIssueValue3(temp.getIssueWeight3() * temp.getIssuePercantage_of_completion() / 100);
                temp.setIssueContribution(temp.getIssueWeight() - temp.getIssueValue());
                temp.setIssueContribution2(temp.getIssueWeight2() - temp.getIssueValue2());
                temp.setIssueContribution3(temp.getIssueWeight3() - temp.getIssueValue3());
            }
            issues.set(i, temp);
        }
        
        for (int level = 0; level <= maxlevel-1; level++){
            for (int i = 0; i < index; i++){
                Issue temp = issues.get(i);
                if (level == temp.getIssueLevel()){
                    tempscale = temp.getIssueWeight();  
                    tempscale2 = temp.getIssueWeight2(); 
                    tempscale3 = temp.getIssueWeight3(); 
                    sumurgency = 0;
                    sumpriority = 0;
                    sumhard = 0;
                    for (int j = 0; j < temp.child_issues.size(); j++){
                        Issue temp2 = temp.child_issues.get(j);
                        sumurgency = sumurgency + temp2.getIssueUrgency();
                        sumpriority = sumpriority + temp2.getIssuePriority();
                        sumhard = sumhard + temp2.getIssueTime_to_complete();
                    }
                    for (int j = 0; j < temp.child_issues.size(); j++){
                        Issue temp2 = temp.child_issues.get(j);
                        temp2.setIssueUrgency(temp2.getIssueUrgency()/sumurgency);
                        temp2.setIssuePriority(temp2.getIssuePriority()/sumpriority);
                        temp2.setIssueTime_to_complete(temp2.getIssueTime_to_complete()/sumhard);
                        temp2.setIssueWeight((temp2.getIssueUrgency() + temp2.getIssuePriority() + temp2.getIssueTime_to_complete())/3*tempscale);
                        temp2.setIssueWeight2((temp2.getIssueUrgency() + temp2.getIssuePriority())/2*tempscale2);
                        if (temp2.getIssueUrgency() > temp2.getIssuePriority()){
                            temp2.setIssueWeight3(temp2.getIssuePriority()/(1 + temp2.getIssuePriority() - temp2.getIssueUrgency())*tempscale3);
                        }
                        else{
                            temp2.setIssueWeight3(temp2.getIssueUrgency()/(1 + temp2.getIssueUrgency() - temp2.getIssuePriority())*tempscale3);
                        }
                        temp2.setIssueValue(temp2.getIssueWeight() * temp2.getIssuePercantage_of_completion() / 100);
                        temp2.setIssueValue2(temp2.getIssueWeight2() * temp2.getIssuePercantage_of_completion() / 100);
                        temp2.setIssueValue3(temp2.getIssueWeight3() * temp2.getIssuePercantage_of_completion() / 100);
                        temp2.setIssueContribution(temp2.getIssueWeight() - temp2.getIssueValue());
                        temp2.setIssueContribution2(temp2.getIssueWeight2() - temp2.getIssueValue2());
                        temp2.setIssueContribution3(temp2.getIssueWeight3() - temp2.getIssueValue3());
                        temp.child_issues.set(j, temp2);
                    }
                }
                issues.set(i, temp);
            }
        }
        setIssues(issues);
        
        return super.execute(); //returns SUCCESS
    }
    
    public void setIssue(long issue) {
        this.issue = issue;
    }
}

