package comp475.teammuon.projecthulk;

public class HexNumber 
{
    //  This is a map of valid hex characters.  Each hex character is stored at an index
    //  of its integer equivalent.
    private static char[] hexLookup
    = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    
    //  This is where the integer representation of the number is stored.
    private long hexData;
    
    //  The default constructor.
    public HexNumber()    
    {   this.hexData = 0;               }
    
    //  The copy constructor.
    public HexNumber(HexNumber hexIn)
    {   this.hexData = hexIn.hexData;   }
    
    //  Constructor to handle integers.
    public HexNumber(int intIn)
    {   this.setValue((long)intIn);     }
    
    //  Constructor to handle long integers.    
    public HexNumber(long longIn)
    {   this.setValue(longIn);          }
    
    //  Constructor to handle hex values passed as a string.
    public HexNumber(String strIn)
    {   this.setValue(strIn);           }
    
    //  Function to convert the current number to a string in hex.
    //  The function checks if the value is negative.  If so, it 
    public String toString()
    {   return (hexData < 0)?"-0x" + toHex(hexData):"0x" + toHex(hexData);      }
    
    //  Function to return the currently stored number as hex.
    public String getHex()
    {   return (hexData < 0)?"-" + toHex(hexData):"" + toHex(hexData);          }

    //  Function to provide the long integer form of the number.
    public long getLong()
    {   return this.hexData;            }

    //  Function to provide the integer form of the number.
    public int getInt()
    {   return (int)this.hexData;       }
    
   //  Function to set the number to a given integer value.
    public void setValue(long longIn)
    {   this.hexData = longIn;           }
    
    //  Function to return the length of the hexadecimal number.
    public long length()
    {
        /*     
         *      The calculation is such that the number of hexidecimal digits
         *      is equivalent to 1 plus the greatest integer of the logarithm base 
         *      16 of the integer value.   
         */
        if( hexData != 0)
            return (int)Math.floor(Math.log(Math.abs((double)hexData)) / Math.log(16)) + 1;
        else
            return 1;
    }
    
    //  Function to set the number to a given hex value.
    public void setValue(String strIn)
    {
        boolean blBad = false;
        boolean negative = false;
        
        //  Check if the hex value is negative, we need to ensure the integer will be negative.
        if(strIn != null && strIn.length() > 0)
        {
            if(strIn.charAt(0) == '-')
            {
                negative = true;
                strIn = strIn.substring(1);
            }

            //  Look for and remove '#' at the beginning.
            if(strIn.charAt(0) == '#')
                strIn = strIn.substring(1);        

            //  Look for and remove '0x' at the beginning.
            if(strIn.length() > 1 && strIn.charAt(0) == '0' 
                    && Character.toLowerCase(strIn.charAt(1)) == 'x')
                strIn = strIn.substring(2);        


            //  Make sure every character is valid.
            for(char x : strIn.toCharArray())
            {
                boolean blFound = false;

                for(char y : hexLookup)
                    if(Character.toUpperCase(x) == y)
                        blFound = true;

                if(!blFound)
                    blBad = true;
            }
        }
        
        //  If this is a valid hex number, convert it to an integer and store it.
        if(!blBad)
        {
            if(negative == false)
                this.hexData = tolong(strIn);
            else
                this.hexData = tolong(strIn) * -1;
        }
    }
    
    //  This function converts a hex number to its integer equivalent.
    private long tolong(String strIn)
    {
        char[] x = strIn.toCharArray();
        long longSum = 0;
        
        /*  
         *  This loop checks each digit of the hexadecimal word and multiplies its value 
         *  by 16 to the power of the digit's physical position, where the right-most
         *  position is 0, and each digit to the left is 1 more than the previous.  Each 
         *  of these products is then summed to represent the decimal equivalent.
         */
        for(int intPos = 0; intPos < x.length; intPos++)
            longSum += Math.pow(16, x.length - (intPos + 1)) * Character.getNumericValue(x[intPos]);
        
        return longSum;
    }
    
    //  This function converts an integer to its hexadecimal equivalent. 
    private String toHex(long longIn)
    {
        long longRem;
        
        if(longIn < 0)
            longIn = longIn * -1;        
        
        longRem = longIn % 16;
        
        //  This function is recursive, and should be changed to improve reliability and performance.
        if(longIn - longRem == 0)
            return Character.toString(hexLookup[(int)longRem]);
        
        else
            return toHex((longIn - longRem) / 16) + Character.toString(hexLookup[(int)longRem]);
    }
}