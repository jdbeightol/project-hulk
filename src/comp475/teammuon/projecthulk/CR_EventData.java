package comp475.teammuon.projecthulk;

import java.util.Date;

public class CR_EventData
{
    //Private declarations of the record values.
    private byte            port;
    private byte            gpsSatellites;  //Word 14
    
    private boolean         newEvent;
    private boolean         riseValid;
    private boolean         fallValid;
    private boolean         gpsValid;       //Word 13
    
    private long            gpsLatency;     //Word 16
    
    private CR_DataLine     inputString;    //Raw version of the input.
    private HexNumber       triggerCount[]; //Words 1 and 10
    private HexNumber       edgeRise;       //Word 2, 4, 6, or 8
    private HexNumber       edgeFall;       //Word 3, 5, 7, or 9
    private HexNumber       riseValue;
    private HexNumber       fallValue;
    private HexNumber       daqStatus;      //Word 15   
    
    private Date            gpsTime;        //Words 11 and 12
    
    //  The default constructor.
    public CR_EventData()
    {
        port                = 0;
        gpsSatellites       = 0;
        gpsLatency          = 0;

        newEvent            = false;
        riseValid           = false;
        fallValid           = false;
        gpsValid            = false;

        inputString         = new CR_DataLine();
        
        triggerCount        = new HexNumber[2];

        triggerCount[0]     = new HexNumber();
        triggerCount[1]     = new HexNumber("FFFFFFFF");
        edgeRise            = new HexNumber();
        edgeFall            = new HexNumber();
        riseValue           = new HexNumber();
        fallValue           = new HexNumber();

        gpsTime             = new Date(0);
        daqStatus           = new HexNumber();
    }
    
    //  Copy constructor.
    public CR_EventData(CR_EventData crIn)
    {
        port                = crIn.port;
        newEvent            = crIn.newEvent;
        inputString         = crIn.inputString;
        riseValue           = crIn.riseValue;
        fallValue           = crIn.fallValue;
 
        riseValid           = crIn.riseValid;
        fallValid           = crIn.fallValid;
        
        edgeRise            = crIn.edgeRise;
        edgeFall            = crIn.edgeFall;

        triggerCount[0]     = crIn.triggerCount[0];
        triggerCount[1]     = crIn.triggerCount[1];

        gpsTime             = crIn.gpsTime;
        gpsValid            = crIn.gpsValid;
        gpsSatellites       = crIn.gpsSatellites;
        daqStatus           = crIn.daqStatus;
        gpsLatency          = crIn.gpsLatency;
    }
    
    public void setRise(HexNumber hnIn)
    {   
        this.edgeRise = hnIn;
        this.riseValue = hnIn;

        if ((this.edgeRise.getInt() & (1 << 7)) != 0)
        {
            this.setNewEvent(true);
            this.riseValue.setValue(this.riseValue.getInt() - 128);
        }

        if ((this.edgeRise.getInt() & (1 << 6)) != 0)
            this.riseValue.setValue(this.riseValue.getInt() - 64);

        if ((this.edgeRise.getInt() & (1 << 5)) != 0)
            this.riseValue.setValue(this.riseValue.getInt() - 32);
    }

    public void setFall(HexNumber hnIn)
    {   
        this.edgeFall = hnIn;
        this.fallValue = hnIn;

        if ((this.edgeFall.getInt() & (1 << 7)) != 0)
        {
            this.setNewEvent(true);
            this.fallValue.setValue(this.fallValue.getInt() - 128);
        }

        if ((this.edgeFall.getInt() & (1 << 6)) != 0)
            this.fallValue.setValue(this.fallValue.getInt() - 64);

        if ((this.edgeFall.getInt() & (1 << 5)) != 0)
            this.fallValue.setValue(this.fallValue.getInt() - 32);
    }
    
    public void setNewEvent(boolean blIn)
    {   this.newEvent = blIn;                   }
    
    public void setInputString(CR_DataLine crdIn)
    {   this.inputString = crdIn;               }
    
    public void setTriggerCount1(HexNumber hnIn)
    {   this.triggerCount[0] = hnIn;            }

    public void setTriggerCount2(HexNumber hnIn)
    {   this.triggerCount[1] = hnIn;            }
    
    public void setSatelliteCount(byte bIn)
    {   this.gpsSatellites = bIn;               }

    public void setGPSValid(boolean boolIn)
    {   this.gpsValid = boolIn;                 }

    public void setDate(Date dateIn)
    {   this.gpsTime = dateIn;                  }

    public void setStatus(HexNumber hnIn)
    {   this.daqStatus = hnIn;                  }

    public void setGPSLatency(long lngIn)
    {   this.gpsLatency = lngIn;                }
    
    public void setPort(byte btIn)
    {   this.port = btIn;                       }

    public boolean isNewEvent()
    {   return newEvent;            }
    
    public boolean isRiseValid()
    {   return riseValid;           }
    
    public boolean isFallValid()
    {   return fallValid;           }
    
    //  Function to return the original input string.
    public CR_DataLine getInputString()
    {   return inputString;         }

    //  Function to return the first trigger count.
    public HexNumber getTriggerCount1()
    {   return triggerCount[0];     }

    //  Function to return the second trigger count.ol.
    public HexNumber getTriggerCount2()
    {   return triggerCount[1];     }

    //  Function to return the rising edge.
    public HexNumber getRise()
    {   return edgeRise;            }

    //  Function to return the falling edge.
    public HexNumber getFall()
    {   return edgeFall;            }

    public HexNumber getRiseValue()
    {   return riseValue;           }

    public HexNumber getFallValue()
    {   return fallValue;           }

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

    //  Function to return the latency of the GPS.
    public long getGPSLatency()
    {   return gpsLatency;          }
    
    public byte getPort()
    {   return port;                }
}