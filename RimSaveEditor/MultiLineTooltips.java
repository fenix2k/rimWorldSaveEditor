package RimSaveEditor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MultiLineTooltips
{
    private static int DIALOG_TOOLTIP_MAX_SIZE = 60;
    //private static final int SPACE_BUFFER = 10;

    public static String splitToolTip(String tip)
    {
        return splitToolTip(tip,DIALOG_TOOLTIP_MAX_SIZE);
    }
    public static String splitToolTip(String tip,int dialogMaxSize)
    {
        if(tip.length() <= dialogMaxSize )
        {
            return tip;
        }

        List<String>  parts = new ArrayList<>();
        int lastSpace = 0;
        int cutPos = 0;
        int counter = 0;

        for (int i=0; i < tip.length(); i++) {
            if(tip.charAt(i) == ' ' || i == tip.length()-1) {
                lastSpace = i;
            }
            if(counter == dialogMaxSize || i == tip.length()-1) {
                String sub = tip.substring(cutPos, lastSpace+1);
                cutPos = lastSpace+1;
                counter = 0;
                parts.add(sub);
            }
            counter++;
        }


        StringBuilder  sb = new StringBuilder();
        for(int i=0;i<parts.size() - 1;i++)
        {
            sb.append(parts.get(i)+"<br>");
        }
        sb.append(parts.get(parts.size() - 1));

        return sb.toString();
    }
}
