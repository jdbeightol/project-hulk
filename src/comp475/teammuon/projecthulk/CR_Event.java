package comp475.teammuon.projecthulk;

import java.util.ArrayList;

public class CR_Event
{
    private ArrayList<CR_EventData> evDataList;
    private long eventID;
    
    public CR_Event()
    {   
        evDataList = new ArrayList<CR_EventData>();
        eventID = 0;
    }
    
    public CR_Event(long inInt)
    {
        this();    
        eventID = inInt;
    }

    public void addEvent(CR_EventData inData)
    {   evDataList.add(inData);                        }
    
    public ArrayList<CR_EventData> getEvents()
    {   return evDataList;                             }
    
    public long getEventID()
    {   return eventID;                                 }
}