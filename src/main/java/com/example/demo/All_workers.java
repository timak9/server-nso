package com.example.demo;

enum Type {
    ENTRY, EXIT;
}
public class All_workers{
    private Worker workers_head;
    public All_workers(){};
    Worker FindWorker(int ID)
    {
        Worker worker=workers_head;
        while(worker!=null)
        {
            if(worker.getID()==ID)
                return worker;
            worker=worker.getNext_worker();
        }
        return this.add_new_worker(ID);
    }

    Worker add_new_worker(int ID){
        Worker new_worker = new Worker(ID);
        if (workers_head==null)
            workers_head=new_worker;
        else {
            Worker curr_worker=workers_head;
            while (curr_worker.getNext_worker()!=null)
            {
                curr_worker=curr_worker.getNext_worker();
            }
            curr_worker.setNext_worker(new_worker);
            new_worker.setPrevious_worker(curr_worker);
            return new_worker;
        }
        return null;
    }

    String Print_All_Worklogs()
    {
        String message = "[\n";
        Worker worker = workers_head;
        while (worker!=null)
        {
            message += worker.PrintWorklogsWorker();
            worker=worker.getNext_worker();
            if (worker!=null)
                message += ",";
            message+= "\n";
        }
        message += "]";
        return message;
    }
}

class Worker {
    private int ID;
    private WorkLog workLogs_head;
    private Worker next_worker;
    private Worker previous_worker;
    public Worker(int ID) {
        this.ID = ID;
        workLogs_head=null;
        next_worker=null;
        previous_worker=null;
    }

    Worker getNext_worker(){
        return this.next_worker;
    }

    void setNext_worker(Worker worker)
    {
        this.next_worker=worker;
    }
    Worker getPrevious_worker(){
        return this.previous_worker;
    }

    void setPrevious_worker(Worker worker)
    {
        this.previous_worker=worker;
    }
    WorkLog getWorklog_List(){
        return this.workLogs_head;
    }

    int getID(){
        return this.ID;
    }

    int addEntryLog(String formattedDate)
    {
        WorkLog new_worklog = new WorkLog(formattedDate,Type.ENTRY);
        //first entry
        if (this.workLogs_head==null)
        {
            workLogs_head=new_worklog;
            return 1;
        }
        WorkLog curr_worklog = workLogs_head;
        while (curr_worklog.getNext_worklog()!=null)
        {
            curr_worklog=curr_worklog.getNext_worklog();
        }
        // if previous log was an entry then error
        if(curr_worklog.getType()==Type.ENTRY)
            return 0;

        curr_worklog.setNext_worklog(new_worklog);
        new_worklog.setPrevious_worklog(curr_worklog);
        return 1;
    }

    int addExitLog(String formattedDate)
    {
        WorkLog new_worklog = new WorkLog(formattedDate,Type.EXIT);
        //no previous entry before exit
        if (this.workLogs_head==null)
        {
            return 0;
        }
        WorkLog curr_worklog = workLogs_head;
        while (curr_worklog.getNext_worklog()!=null)
        {
            curr_worklog=curr_worklog.getNext_worklog();
        }
        //if previous log was an exit then error
        if(curr_worklog.getType()==Type.EXIT)
            return 0;

        curr_worklog.setNext_worklog(new_worklog);
        new_worklog.setPrevious_worklog(curr_worklog);
        return 1;
    }

    String PrintWorklogs(){
        String message = "{\n" +
                "\t" + "\"employee_id\": " + ID + ",\n" + " \t\"dates\": [" +
                "\n";
        //System.out.println(message);
        WorkLog worklog = workLogs_head;
        while (worklog!=null){
            message+=("\t\t[\"" + worklog.getDateAndTime() + "\"");
            worklog = worklog.getNext_worklog();
            if (worklog==null) {
                message+="]\n";
                break;
            }
            message+= ", \"" + worklog.getDateAndTime() + "\"]";
            worklog = worklog.getNext_worklog();
            if (worklog!=null)
            {
                message += ",";
            }
            message += "\n";
        }
        message+="\t]\n" + "}";
        return message;
    }

    String PrintWorklogsWorker(){
        String message = "\t{\n" +
                "\t\t" + "\"employee_id\": " + ID + ",\n" + " \t\t\"dates\": [" +
                "\n";
        //System.out.println(message);
        WorkLog worklog = workLogs_head;
        while (worklog!=null){
            message+=("\t\t\t[\"" + worklog.getDateAndTime() + "\"");
            worklog = worklog.getNext_worklog();
            if (worklog==null) {
                message+="]\n";
                break;
            }
            message+= ", \"" + worklog.getDateAndTime() + "\"]";
            worklog = worklog.getNext_worklog();
            if (worklog!=null)
            {
                message += ",";
            }
            message += "\n";
        }
        message+="\t\t]\n" + "\t}";
        return message;
    }
}


class WorkLog {
    private String DateAndTime;
    private Type type;
    private WorkLog previous_worklog;
    private WorkLog next_worklog;

    public WorkLog(String formattedDate, Type type) {
        this.DateAndTime = formattedDate;
        this.type = type;
        previous_worklog=null;
        next_worklog=null;
    }

    WorkLog getNext_worklog(){
        return this.next_worklog;
    }

    void setNext_worklog(WorkLog worklog) {
        this.next_worklog=worklog;
    }

    void setPrevious_worklog(WorkLog worklog){
        this.previous_worklog =worklog;
    }
    Type getType(){
        return this.type;
    }

    String getDateAndTime(){
        return this.DateAndTime;
    }
}


