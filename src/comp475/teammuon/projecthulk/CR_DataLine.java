package comp475.teammuon.projecthulk;

import java.util.Date;
import java.util.Scanner;

public class CR_DataLine
{
    //Private declarations of the record values.
    private String          inputString;    //Raw version of the input.
    private HexNumber[]     triggerCount;   //Words 1 and 10
    private HexNumber[]     edgeRise;       //Words 2, 4, 6, 8
    private HexNumber[]     edgeFall;       //Words 3, 5, 7, 9
    private Date            gpsTime;        //Words 11 and 12
    private boolean         gpsValid;       //Word 13
    private byte            gpsSatellites;  //Word 14
    private HexNumber       daqStatus;      //Word 15
    private long            gpsLatency;     //Word 16

    //  The default constructor.
    public CR_DataLine()
    {
        inputString     = "";
        triggerCount    = new HexNumber[2];
        edgeRise        = new HexNumber[4];
        edgeFall        = new HexNumber[4];

        triggerCount[0] = new HexNumber();
        triggerCount[1] = new HexNumber("FFFFFFFF");

        edgeRise[0]     = new HexNumber();
        edgeRise[1]     = new HexNumber();
        edgeRise[2]     = new HexNumber();
        edgeRise[3]     = new HexNumber();

        edgeFall[0]     = new HexNumber();
        edgeFall[1]     = new HexNumber();
        edgeFall[2]     = new HexNumber();
        edgeFall[3]     = new HexNumber();

        gpsTime         = new Date(0);
        gpsValid        = false;
        gpsSatellites   = 0;
        daqStatus       = new HexNumber();
        gpsLatency      = 0;
    }

    //  Constructor to handle an input string.
    public CR_DataLine(String strIn)
    {   
        this();
        this.parseLine(strIn);
    }
    
    //  Copy constructor.
    public CR_DataLine(CR_DataLine crIn)
    {
        inputString     = crIn.inputString;

        triggerCount[0] = crIn.triggerCount[0];
        triggerCount[1] = crIn.triggerCount[1];

        edgeRise[0]     = crIn.edgeRise[0];
        edgeRise[1]     = crIn.edgeRise[1];
        edgeRise[2]     = crIn.edgeRise[2];
        edgeRise[3]     = crIn.edgeRise[3];

        edgeFall[0]     = crIn.edgeFall[0];
        edgeFall[1]     = crIn.edgeFall[1];
        edgeFall[2]     = crIn.edgeFall[2];
        edgeFall[3]     = crIn.edgeFall[3];

        gpsTime         = crIn.gpsTime;
        gpsValid        = crIn.gpsValid;
        gpsSatellites   = crIn.gpsSatellites;
        daqStatus       = crIn.daqStatus;
        gpsLatency      = crIn.gpsLatency;
    }

    //  Function to return the original input string.
    public String getInputString()
    {   return inputString;         }

    //  Function to return the first trigger count.
    public HexNumber getTriggerCount1()
    {   return triggerCount[0];     }

    //  Function to return the second trigger count.
    public HexNumber getTriggerCount2()
    {   return triggerCount[1];     }
    
    //  Function to return the rising edge value of port i.
    public HexNumber getRise(int i)
    {   return edgeRise[i];         }
    
    //  Function to return the falling edge value of port i.
    public HexNumber getFall(int i)
    {   return edgeFall[i];         }

    //  Function to return the date.
    public Date getDate()
    {   return gpsTime ;            }

    //  Function to return if the GPS Signal is valid.
    public boolean isGPSValid()
    {   return gpsValid;            }

    //  Function to return the GPS Satellite count.
    public byte getSatelliteCount()
    {   return gpsSatellites;       }

    //  Function to return the status of the board.
    public HexNumber getStatus()
    {   return daqStatus;           }

    //  Function to return the latency of the gps.
    public long getGPSLatency()
    {   return gpsLatency;          }

    //  Function to input a raw string of data.
    public void parseLine(String strIn)
    {
        String strTime, strDate, strLatency;
        Scanner stringParser = new Scanner(strIn);
        
        inputString = strIn;

        try
        {
            triggerCount[0].setValue(stringParser.next());
            edgeRise[0].setValue(stringParser.next());
            edgeFall[0].setValue(stringParser.next());
            edgeRise[1].setValue(stringParser.next());
            edgeFall[1].setValue(stringParser.next());
            edgeRise[2].setValue(stringParser.next());
            edgeFall[2].setValue(stringParser.next());
            edgeRise[3].setValue(stringParser.next());
            edgeFall[3].setValue(stringParser.next());
            triggerCount[1].setValue(stringParser.next());

            strTime = stringParser.next();
            strDate = stringParser.next();

            if(stringParser.next().equals("A"))
                gpsValid = true;
            else
                gpsValid = false;
            
            gpsSatellites = Byte.parseByte(stringParser.next());
            daqStatus.setValue(stringParser.next());

            if ((strLatency = stringParser.next()).charAt(0) == '+')
                gpsLatency = Long.parseLong(strLatency.substring(1));
            else
                gpsLatency = Long.parseLong(strLatency);
        }
        
        catch(Exception e)
        {   System.err.println("[ERROR]" + e.getMessage());     }
        
        
        finally
        {   stringParser.close();                               }
    }
}