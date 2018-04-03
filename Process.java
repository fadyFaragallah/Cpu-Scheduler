package sample;

public class Process {
    private String id;
    private float arrivalTime;
    private float departureTime;
    private float waitingTime;
    private float responseTime;
    private int priority;
    private float elapsedTime;
    private float remainingTime;
    private float burstTime;
    public Process()
    {

    }
    public	Process(String id,float arrivalTime,float burstTime)
    {
        this.id=id;
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
        this.remainingTime=burstTime;

    }
    public void setId(String id)
    {
        this.id=id;
    }
    public String getId()
    {
        return this.id;
    }
    public void setBurstTime(float burstTime)
    {
        this.burstTime=burstTime;
    }
    public float getBurstTime()
    {
        return this.burstTime;
    }
    public void setArrivalTime(float arrivalTime)
    {
        this.arrivalTime=arrivalTime;
    }
    public float getArrivalTime()
    {
        return this.arrivalTime;
    }
    public void setPriority(int priority)
    {
        this.priority=priority;
    }
    public int getPriority()
    {
        return this.priority;
    }
    public void setDepartureTime(float departureTime)
    {
        this.departureTime=departureTime;
    }
    public float getDepartureTime()
    {
        return this.departureTime;
    }
    public float getWaitingTime()
    {
        return this.waitingTime;
    }
    public void setWaitingTime(float waitingTime)
    {
        this.waitingTime=waitingTime;
    }
    public float getResponseTime()
    {
        return this.responseTime;
    }
    public void setResponseTime(float responseTime)
    {
        this.responseTime=responseTime;
    }
    public float getRemainingTime()
    {
        return this.remainingTime;
    }
    public void setRemainigTime(float remainingTime)
    {
        this.remainingTime=remainingTime;
    }
    public float getElapsedTime()
    {
        return this.elapsedTime;
    }
    public void setElapsedTime(float elapsedTime)
    {
        this.elapsedTime=elapsedTime;
    }

}
