package comp475.teammuon.projecthulk;

import java.util.ArrayList;
import java.util.regex.*;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.sql.*;

public class CR_Session
{
    private ArrayList<CR_Event>     eventList;
    private String                  filename;
    private int                     size;
    private String                  md5hash;

    public CR_Session()
    {
        size                        = 0;
        filename                    = "";
        eventList                   = new ArrayList<CR_Event>();
        md5hash                     = "";
    }
    
    public CR_Session(String strIn)
    {
        this();
        this.readFile(strIn);
    }

    public CR_Session(Connection inConn, String sessHash)
    {
        this();
        this.parseDB(inConn, sessHash);
    }
    
    public int getSize()
    {   return size;        }
    
    public String getFilename()
    {   return filename;    }
    
    public String getHash()
    {   return md5hash;     }

    public boolean saveCSVFile(String strOutFile)
    {
        try
        {
            int eventID = 0;
            PrintWriter out = new PrintWriter(strOutFile, "UTF-8");
            
            out.println("EventID,Port,Trigger1,Trigger2,Rise,Fall");
            
            for(CR_Event x : this.getEvents())
            {
                for(CR_EventData y : x.getEvents())
                {
                    String line = Integer.toString(eventID)
                           + "," + Byte.toString(y.getPort())
                           + "," + Long.toString(y.getTriggerCount1().getLong())
                           + "," + Long.toString(y.getTriggerCount2().getLong())
                           + "," + Integer.toString(y.getRise().getInt())
                           + "," + Integer.toString(y.getFall().getInt());
                    out.println(line);
                }
                
                eventID++;
            }
  
            out.close();
        }
        catch(Exception e)
        {   System.out.println("[ERROR]" + e.getMessage());     }
        
        return true;
    }
    
    public ArrayList<CR_Event> getEvents()
    {   return eventList;                   }
    
    private void parseDB(Connection inConn, String sessHash)
    {
        try
        {
            String ssGet = "SELECT * FROM SESSION WHERE MD5HASH = ?";
            String evGet = "SELECT EVENT_ID FROM EVENT NATURAL JOIN SESSION "
                    + "WHERE MD5HASH = ?";
            String edGet = "SELECT TRIGGERCOUNT1, TRIGGERCOUNT2, PORT, "
                    + "EDGERISE, EDGEFALL, GPSSATELLITES, GPSLATENCY, GPSVALID, "
                    + "DAQSTATUS, EVENT_ID FROM EVENTDATA NATURAL JOIN EVENT "
                    + "NATURAL JOIN SESSION WHERE MD5HASH = ? AND "
                    + "EVENT_ID = ?";
            
            PreparedStatement ss = inConn.prepareStatement(ssGet);
            ss.setString(1, sessHash);
            
            ResultSet ssRS = ss.executeQuery();
            
            if(ssRS.next())
            {
                this.md5hash = ssRS.getString(1);
                this.filename = ssRS.getString(2);
                
                PreparedStatement ev = inConn.prepareStatement(evGet);
                ev.setString(1, sessHash);
                ResultSet evRS = ev.executeQuery();
                
                while(evRS.next())
                {
                    CR_Event cv = new CR_Event(evRS.getLong(1));
                   
                    PreparedStatement ed = inConn.prepareStatement(edGet);
                    ed.setString(1, sessHash);
                    ed.setLong(2, evRS.getLong(1));
                    ResultSet edRS = ed.executeQuery();

                    while(edRS.next())
                    {
                        CR_EventData crEVD = new CR_EventData();
                        
                        crEVD.setTriggerCount1(new HexNumber(edRS.getLong(1)));
                        crEVD.setTriggerCount2(new HexNumber(edRS.getLong(2)));
                        crEVD.setPort((byte)edRS.getInt(3));
                        crEVD.setRise(new HexNumber(edRS.getLong(4)));
                        crEVD.setFall(new HexNumber(edRS.getLong(5)));
                        crEVD.setSatelliteCount((byte)edRS.getInt(6));
                        crEVD.setGPSLatency(edRS.getLong(7));
                        crEVD.setGPSValid(edRS.getInt(8) == 1);
//@todo                        crEVD.setDate(edRS.getDate(9));
                        crEVD.setStatus(new HexNumber(edRS.getInt(9)));
                        
                        cv.addEvent(crEVD);
                    }
                    
                    this.eventList.add(cv);
                }
            }
            
            else
                System.out.println("Dindn't find shit, yo!");
                
            
        } catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void readFile(String strInFile)
    {
        Pattern cosmicRayLine = Pattern.compile(
                "[0-9a-fA-F]{8} [0-9a-fA-F]{2} [0-9a-fA-F]{2} [0-9a-fA-F]{2}"
                +" [0-9a-fA-F]{2} [0-9a-fA-F]{2} [0-9a-fA-F]{2} [0-9a-fA-F]{2}"
                +" [0-9a-fA-F]{2} [0-9a-fA-F]{8} [0-9]{6}.[0-9]{3} [0-9]{6}"
                +" [AVav] [0-9]{2} [0-9a-fA-F] .[0-9]{4}"
        );
        
        try
        {
            long eID = 0;
            String inLine;
            File inFile = new File(strInFile);
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            DigestInputStream inStream = new DigestInputStream(
                    new FileInputStream(inFile), md5Digest);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(inStream));
            
            filename = inFile.getName();

            CR_Event[] cr = new CR_Event[4];
            cr[0] = new CR_Event(eID++);
            cr[1] = new CR_Event(eID++);
            cr[2] = new CR_Event(eID++);
            cr[3] = new CR_Event(eID++);

            while((inLine = in.readLine()) != null)
            {
                if(cosmicRayLine.matcher(inLine).matches())
                {
                    CR_DataLine cdata = new CR_DataLine(inLine);
      
                    for(int i = 0;i < 4;i++)
                    {
                        if (cdata.getRise(i).getInt() >= 128
                                && cr[i].getEvents().size() > 0)
                        {
                            eventList.add(cr[i]);
                            cr[i] = new CR_Event(eID++);
                        }

                        CR_EventData crEVD = new CR_EventData();

                        crEVD.setPort((byte) (i + 1));
                        crEVD.setTriggerCount1(cdata.getTriggerCount1());
                        crEVD.setTriggerCount2(cdata.getTriggerCount2());
                        crEVD.setRise(cdata.getRise(i));
                        crEVD.setFall(cdata.getFall(i));
                        crEVD.setSatelliteCount(cdata.getSatelliteCount());
                        crEVD.setGPSValid(cdata.isGPSValid());
                        crEVD.setDate(cdata.getDate());
                        crEVD.setStatus(cdata.getStatus());
                        crEVD.setGPSLatency(cdata.getGPSLatency());

                        cr[i].addEvent(crEVD);
                        size++;
                    }
                }
            }
            
            if(cr[0].getEvents().size() > 0)
                eventList.add(cr[0]);
            
            if(cr[1].getEvents().size() > 0)
                eventList.add(cr[1]);
            
            if(cr[2].getEvents().size() > 0)
                eventList.add(cr[2]);
            
            if(cr[3].getEvents().size() > 0)
                eventList.add(cr[3]);
            
            in.close();
            
            byte[] bytehash = md5Digest.digest(); 
            
            for(byte b : bytehash)
                md5hash += Byte.toString(b);
        }

        catch(Exception e)
        {   System.out.println("[ERROR]" + e.getMessage());     }
    }
}