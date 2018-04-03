package sample;

import java.util.ArrayList;

public class scheduler {
    public static void main(String []args)
    {
        ArrayList<Process>p=new ArrayList<>();
        p.add(new Process("p1",0,5));
        p.get(p.size()-1).setPriority(1);
        p.add(new Process("p2",1,3));
        p.get(p.size()-1).setPriority(2);
        p.add(new Process("p3",3,2));
        p.add(new Process("p4",9.5f,1));
        p.add(new Process("p2",12,2));
        sort(p);

        print(calc_RR(p,1));
    }
    ///////////////////sorting by arrival time////////////////////
    public static void sort(ArrayList<Process> p)
    {
        for(int i=0;i<p.size();i++)
        {
            for(int j=i+1;j<p.size();j++)
            {
                if(p.get(i).getArrivalTime()>p.get(j).getArrivalTime())
                {
                    Process temp=new Process();
                    temp=p.get(i);
                    p.set(i, p.get(j));
                    p.set(j, temp);
                }
            }
        }
    }
    ///////////////////////reset//////////////////
    public static void reset(ArrayList<Process>p)
    {
        for(Process x:p)
        {
            x.setRemainigTime(x.getBurstTime());
        }
    }
    ///////////////FCFS///////////////////////////
    public static ArrayList<Process> calc_FCFS(ArrayList<Process>p)
    {
        // p is sorted
        ArrayList<Process>sequence=new ArrayList<Process>();
        int seq_index=0;
        for(int i=0;i<p.size();i++)
        {
            if(i==0 &&(p.get(i).getArrivalTime()!=0))
            {
                Process gap=new Process();
                gap.setArrivalTime(0);
                gap.setBurstTime(p.get(i).getArrivalTime());
                gap.setDepartureTime(gap.getBurstTime());
                gap.setId("Gap");
                sequence.add(seq_index, gap);
                seq_index++;
                //put the ith process in the sequence
                p.get(i).setResponseTime(p.get(i).getArrivalTime());
                p.get(i).setDepartureTime(p.get(i).getResponseTime()+p.get(i).getBurstTime());
                sequence.add(seq_index, p.get(i));
                seq_index++;
            }
            else if(i==0)
            {
                p.get(i).setResponseTime(p.get(i).getArrivalTime());
                p.get(i).setDepartureTime(p.get(i).getResponseTime()+p.get(i).getBurstTime());
                sequence.add(seq_index, p.get(i));
                seq_index++;
            }
            else if((i!=0)&&(p.get(i).getArrivalTime()>p.get(i-1).getDepartureTime()))
            {
                Process gap=new Process();
                gap.setArrivalTime(p.get(i-1).getDepartureTime());
                gap.setBurstTime(p.get(i).getArrivalTime()-p.get(i-1).getDepartureTime());
                gap.setDepartureTime(gap.getBurstTime()+gap.getArrivalTime());
                gap.setId("Gap");
                sequence.add(seq_index, gap);
                seq_index++;
                //put the ith process in the sequence
                p.get(i).setResponseTime(p.get(i).getArrivalTime());
                p.get(i).setDepartureTime(p.get(i).getResponseTime()+p.get(i).getBurstTime());
                sequence.add(seq_index, p.get(i));
                seq_index++;
            }
            else
            {
                p.get(i).setResponseTime(p.get(i-1).getDepartureTime());
                p.get(i).setDepartureTime(p.get(i).getResponseTime()+p.get(i).getBurstTime());
                //new process to add to the sequence
                Process x=new Process(p.get(i).getId(),p.get(i-1).getDepartureTime(),p.get(i).getBurstTime());
                x.setDepartureTime(p.get(i).getDepartureTime());
                sequence.add(seq_index, x);
                seq_index++;
            }
        }

        return sequence;
    }
    //////////////SJF and PRIORITY////////////////////////////
    public static ArrayList<Process> calc_FSJ_PR(ArrayList<Process>p,int type)
    {
        // p is sorted
        ArrayList<Process>sequence=new ArrayList<Process>();

        ArrayList<Process> readyQueue=new ArrayList<Process>();

        float CurrentTime=0;
        int lastProcess=-1;

        while(true)
        {

            if((lastProcess+1)==p.size())
                break;

            for(int i=0;i<p.size();i++)
            {
                if((p.get(i).getArrivalTime()<=CurrentTime)&&(p.get(i).getRemainingTime()!=0))
                {
                    //put it in the readyQueue
                    Boolean IN=false;
                    for(int j=0;j<readyQueue.size();j++)
                    {
                        if(readyQueue.get(j).getId()==p.get(i).getId())
                        {
                            IN=true;
                            break;
                        }
                    }
                    if(!IN)
                    {
                        readyQueue.add(readyQueue.size(), p.get(i));
                    }

                }
                else if(p.get(i).getRemainingTime()==0)
                {
                    continue;
                }
                else
                    break;
            }
            // here we have a queue of ready Process to take the cpu
            //if the queue is empty and it's the first process
            if((readyQueue.size()==0)&&(sequence.size()==0))
            {
                Process gap=new Process("Gap",0f,p.get(0).getArrivalTime());
                gap.setDepartureTime(p.get(0).getArrivalTime());
                sequence.add(sequence.size(),gap);
                CurrentTime+=gap.getBurstTime();

            }
            //if the queue is empty and it's not the first process
            else if(readyQueue.size()==0)
            {
                Process gap=new Process("Gap",sequence.get(sequence.size()-1).getDepartureTime(),p.get(lastProcess+1).getArrivalTime()-sequence.get(sequence.size()-1).getDepartureTime());
                gap.setDepartureTime(gap.getArrivalTime()+gap.getBurstTime());
                sequence.add(sequence.size(),gap);
                CurrentTime+=gap.getBurstTime();
            }
            //queue not empty
            else
            {
                int inIndex=0;

                int minIndex=0;

                for(int j=0;j<readyQueue.size();j++)
                {
                    if(type==1)
                    {
                        if(readyQueue.get(j).getBurstTime()<readyQueue.get(minIndex).getBurstTime())
                            minIndex=j;
                    }
                    else if(type==2)
                    {
                        if(readyQueue.get(j).getPriority()<readyQueue.get(minIndex).getPriority())
                            minIndex=j;
                    }
                }

                for(int k=0;k<p.size();k++)
                {
                    if(p.get(k).getId()==readyQueue.get(minIndex).getId())
                    {
                        inIndex=k;
                        break;
                    }
                }
                //new to be drawn correctly
                Process x=new Process(p.get(inIndex).getId(),CurrentTime,p.get(inIndex).getBurstTime());
                p.get(inIndex).setRemainigTime(0);
                CurrentTime+=p.get(inIndex).getBurstTime();
                x.setDepartureTime(CurrentTime);
                p.get(inIndex).setDepartureTime(CurrentTime);
                sequence.add(sequence.size(),x);
                readyQueue.remove(minIndex);
                lastProcess++;

            }
        }

        return sequence;
    }

    //////////////SJF and PRIORITY////////////////////////////
    public static ArrayList<Process> calc_FSJ_PR_P(ArrayList<Process>p,int type)
    {
        // p is sorted
        ArrayList<Process>sequence=new ArrayList<Process>();

        ArrayList<Process> readyQueue=new ArrayList<Process>();

        float CurrentTime=0;
        int lastProcess=-1;

        while(true)
        {

            if((lastProcess+1)==p.size())
                break;

            for(int i=0;i<p.size();i++)
            {
                if((p.get(i).getArrivalTime()<=CurrentTime)&&(p.get(i).getRemainingTime()!=0))
                {
                    //put it in the readyQueue
                    Boolean IN=false;
                    for(int j=0;j<readyQueue.size();j++)
                    {
                        if(readyQueue.get(j).getId()==p.get(i).getId())
                        {
                            IN=true;
                            break;
                        }
                    }
                    if(!IN)
                    {
                        readyQueue.add(readyQueue.size(), p.get(i));
                    }

                }
                else if(p.get(i).getRemainingTime()==0)
                {
                    continue;
                }
                else
                    break;
            }
            // here we have a queue of ready Process to take the cpu
            //if the queue is empty and it's the first process
            if((readyQueue.size()==0)&&(sequence.size()==0))
            {
                Process gap=new Process("Gap",0f,p.get(0).getArrivalTime());
                gap.setDepartureTime(p.get(0).getArrivalTime());
                sequence.add(sequence.size(),gap);
                CurrentTime+=gap.getBurstTime();

            }
            //if the queue is empty and it's not the first process
            else if(readyQueue.size()==0)
            {
                Process gap=new Process("Gap",sequence.get(sequence.size()-1).getDepartureTime(),p.get(lastProcess+1).getArrivalTime()-sequence.get(sequence.size()-1).getDepartureTime());
                gap.setDepartureTime(gap.getArrivalTime()+gap.getBurstTime());
                sequence.add(sequence.size(),gap);
                CurrentTime+=gap.getBurstTime();
            }
            //queue not empty
            else
            {
                int inIndex=0;

                int minIndex=0;

                for(int j=0;j<readyQueue.size();j++)
                {
                    if(type==1)
                    {
                        if(readyQueue.get(j).getRemainingTime()<readyQueue.get(minIndex).getRemainingTime())
                            minIndex=j;
                    }
                    else if(type==2)
                    {
                        if(readyQueue.get(j).getPriority()<readyQueue.get(minIndex).getPriority())
                            minIndex=j;
                    }
                }

                for(int k=0;k<p.size();k++)
                {
                    if(p.get(k).getId()==readyQueue.get(minIndex).getId())
                    {
                        inIndex=k;
                        break;
                    }
                }


                if(((lastProcess+1)==p.size()-1)||(inIndex==(p.size()-1)))
                {
                    //the last process

                    Process x=new Process(p.get(inIndex).getId(),CurrentTime,p.get(inIndex).getRemainingTime());
                    CurrentTime+=p.get(inIndex).getRemainingTime();
                    p.get(inIndex).setDepartureTime(CurrentTime);
                    x.setDepartureTime(CurrentTime);
                    sequence.add(sequence.size(),x);
                    readyQueue.remove(minIndex);
                    p.get(inIndex).setRemainigTime(0);
                    lastProcess++;
                }
                else
                {
                    for (int c = inIndex + 1; c < p.size(); c++)
                    {
                        if((p.get(inIndex).getArrivalTime()>= p.get(c).getArrivalTime())||(p.get(c).getRemainingTime()==0)||(p.get(c).getArrivalTime()<CurrentTime))
                        {
                            if(c==(p.size()-1)) {
                                if (sequence.size() > 0) {
                                    CurrentTime += p.get(inIndex).getRemainingTime();
                                    Process new_p = new Process(p.get(inIndex).getId(), sequence.get(sequence.size() - 1).getDepartureTime(), p.get(inIndex).getRemainingTime());
                                    new_p.setDepartureTime(CurrentTime);
                                    sequence.add(sequence.size(), new_p);
                                    p.get(inIndex).setRemainigTime(0);
                                    p.get(inIndex).setDepartureTime(CurrentTime);
                                    readyQueue.remove(minIndex);
                                    lastProcess++;
                                    break;
                                } else  {
                                    CurrentTime += p.get(inIndex).getRemainingTime();
                                    Process new_p = new Process(p.get(inIndex).getId(), 0, p.get(inIndex).getRemainingTime());
                                    new_p.setDepartureTime(CurrentTime);
                                    sequence.add(sequence.size(), new_p);
                                    p.get(inIndex).setRemainigTime(0);
                                    p.get(inIndex).setDepartureTime(CurrentTime);
                                    readyQueue.remove(minIndex);
                                    lastProcess++;
                                    break;
                                }
                            }
                            else
                                continue;
                        }
                        //it'll finish before the coming process comes
                        if (p.get(inIndex).getRemainingTime() <= (p.get(c).getArrivalTime() - CurrentTime))
                        {
                            Process x=new Process(p.get(inIndex).getId(),CurrentTime,p.get(inIndex).getBurstTime());
                            CurrentTime += p.get(inIndex).getRemainingTime();
                            p.get(inIndex).setRemainigTime(0);
                            p.get(inIndex).setDepartureTime(CurrentTime);
                            x.setDepartureTime(CurrentTime);
                            if(sequence.size()==0)
                            {
                                x.setArrivalTime(0);
                            }
                            else
                            {
                                x.setArrivalTime(sequence.get(sequence.size()-1).getDepartureTime());
                            }
                            sequence.add(sequence.size(), x);
                            readyQueue.remove(minIndex);
                            lastProcess++;
                            break;
                        }
                        //mistake here
                        else if ((type==1)&&(((p.get(inIndex).getRemainingTime()-(p.get(c).getArrivalTime()-CurrentTime))) > p.get(c).getRemainingTime()))
                        {

                                //switch context we must create a new process
                                p.get(inIndex).setRemainigTime(p.get(inIndex).getRemainingTime()-(p.get(c).getArrivalTime() - CurrentTime));

                                Process new_P=new Process();
                                new_P.setId(p.get(inIndex).getId());
                               // new_P.setArrivalTime(CurrentTime);
                            if(sequence.size()==0)
                            {
                                new_P.setArrivalTime(0);
                            }
                            else
                            {
                                new_P.setArrivalTime(sequence.get(sequence.size()-1).getDepartureTime());
                            }
                                CurrentTime = p.get(c).getArrivalTime();
                                new_P.setDepartureTime(CurrentTime);
                                new_P.setBurstTime(p.get(inIndex).getBurstTime());
                                sequence.add(sequence.size(), new_P);
                                //sequence.get(sequence.size() - 1).setDepartureTime(CurrentTime);
                                //to take it again instead of updating its remaining time in the ready queue
                                readyQueue.remove(minIndex);
                                break;

                        }
                        else if((type==2)&&(p.get(c).getPriority()<p.get(inIndex).getPriority()))
                        {

                                //switch context we must create a new process
                                p.get(inIndex).setRemainigTime(p.get(inIndex).getRemainingTime()-(p.get(c).getArrivalTime() - CurrentTime));

                                Process new_P=new Process();
                                new_P.setId(p.get(inIndex).getId());
                               // new_P.setArrivalTime(CurrentTime);
                            if(sequence.size()==0)
                            {
                                new_P.setArrivalTime(0);
                            }
                            else
                            {
                                new_P.setArrivalTime(sequence.get(sequence.size()-1).getDepartureTime());
                            }
                                CurrentTime = p.get(c).getArrivalTime();
                                new_P.setDepartureTime(CurrentTime);
                                new_P.setBurstTime(p.get(inIndex).getBurstTime());
                                sequence.add(sequence.size(), new_P);
                                //	sequence.get(sequence.size() - 1).setDepartureTime(CurrentTime);
                                //to take it again instead of updating its remaining time in the ready queue
                                readyQueue.remove(minIndex);
                                break;

                        }
                        else
                        {
                            //it won't finish before the coming one but it's still the shortest
                            //if it's the shortest until the last one
                            if(c==(p.size()-1))
                            {
                                if(sequence.size()>0) {
                                    CurrentTime += p.get(inIndex).getRemainingTime();
                                    Process new_p = new Process(p.get(inIndex).getId(), sequence.get(sequence.size() - 1).getDepartureTime(), p.get(inIndex).getRemainingTime());
                                    new_p.setDepartureTime(CurrentTime);
                                    sequence.add(sequence.size(), new_p);
                                    p.get(inIndex).setRemainigTime(0);
                                    p.get(inIndex).setDepartureTime(CurrentTime);
                                    readyQueue.remove(minIndex);
                                    lastProcess++;
                                }
                                else
                                {
                                    CurrentTime += p.get(inIndex).getRemainingTime();
                                    Process new_p = new Process(p.get(inIndex).getId(), 0, p.get(inIndex).getRemainingTime());
                                    new_p.setDepartureTime(CurrentTime);
                                    sequence.add(sequence.size(), new_p);
                                    p.get(inIndex).setRemainigTime(0);
                                    p.get(inIndex).setDepartureTime(CurrentTime);
                                    readyQueue.remove(minIndex);
                                    lastProcess++;
                                }
                            }
                            else
                                {
                                p.get(inIndex).setRemainigTime(p.get(inIndex).getRemainingTime() - (p.get(c).getArrivalTime() - CurrentTime)); // mistake in calculation
                                CurrentTime = p.get(c).getArrivalTime(); // should be p.get(c).getArrivalTime()
                                }
                        }
                    }

                }

            }
        }

        return sequence;
    }
    //////////////////////RR///////////////////////////////
    public static ArrayList<Process>calc_RR(ArrayList<Process>p,float Quantum)
    {
        ArrayList<Process> sequence=new ArrayList<Process>();
        float CurrentTime=0;
        int lastProcess=-1;
        ArrayList<Process> switchQueue=new ArrayList<Process>();
        while(true)
        {
            for(int i=lastProcess+1;i<p.size();i++)
            {

                if((p.get(i).getArrivalTime()<CurrentTime)&&((switchQueue.size()>0)&&(switchQueue.get(switchQueue.size()-1).getDepartureTime()==CurrentTime)))
                {
                    switchQueue.add(switchQueue.size()-1, p.get(i));
                    lastProcess++;
                }
                else if((p.get(i).getArrivalTime()==CurrentTime)&&((switchQueue.size()>0)&&(switchQueue.get(switchQueue.size()-1).getDepartureTime()==CurrentTime)))
                {
                    switchQueue.add(switchQueue.size()-1, p.get(i));
                    lastProcess++;
                }
                else if((p.get(i).getArrivalTime()<=CurrentTime)&&(switchQueue.size()==0))
                {
                    switchQueue.add(switchQueue.size(), p.get(i));
                    lastProcess++;
                }
                else
                    break;
            }
            //gap in the first
            if((switchQueue.size()==0)&&(sequence.size()==0))
            {
                Process gap=new Process("Gap",0f,p.get(0).getArrivalTime());
                gap.setDepartureTime(CurrentTime+gap.getBurstTime());
                sequence.add(sequence.size(), gap);
                CurrentTime=gap.getDepartureTime();
            }
            //gap in the middle
            else if(switchQueue.size()==0)
            {
                Process gap=new Process("Gap",sequence.get(sequence.size()-1).getDepartureTime(),p.get(lastProcess+1).getArrivalTime()-CurrentTime);
                gap.setDepartureTime(CurrentTime+gap.getBurstTime());
                sequence.add(sequence.size(), gap);
                CurrentTime=gap.getDepartureTime();
            }
            else
            {
                Process x=new Process();
                x.setId(switchQueue.get(0).getId());
                x.setBurstTime(switchQueue.get(0).getBurstTime());
                x.setArrivalTime(CurrentTime);

                if(switchQueue.get(0).getRemainingTime()<=Quantum)
                {
                    //process remaining time <= quantum

                    x.setDepartureTime(CurrentTime+switchQueue.get(0).getRemainingTime());
                    switchQueue.get(0).setDepartureTime(CurrentTime+switchQueue.get(0).getRemainingTime());
                    switchQueue.get(0).setRemainigTime(0);
                    CurrentTime=switchQueue.get(0).getDepartureTime();
                    switchQueue.remove(0);
                    sequence.add(sequence.size(),x);
                }
                else
                {
                    //process remaining time > quantum
                    x.setDepartureTime(CurrentTime+Quantum);
                    switchQueue.get(0).setRemainigTime(switchQueue.get(0).getRemainingTime()-Quantum);
                    CurrentTime+=Quantum;
                    switchQueue.get(0).setDepartureTime(CurrentTime);
                    Process out=new Process();
                    out=switchQueue.get(0);
                    switchQueue.remove(0);
                    switchQueue.add(switchQueue.size(), out);
                    sequence.add(sequence.size(),x);
                }

            }


            if(((lastProcess+1)==p.size())&&(switchQueue.size()==0))
                break;
        }


        return sequence;
    }


    ////////////////printing//////////////////////
    public static void print(ArrayList<Process>p)
    {
        for(Process x:p)
        {
            System.out.println("id : "+x.getId()+" Arrived at "+x.getArrivalTime()+" it's burst is "+x.getBurstTime()+" departured at "+x.getDepartureTime());
        }
    }
}
