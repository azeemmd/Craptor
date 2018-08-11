
package craptor.swing.editor;

/**
 * Description: Some useful string functions..
 * @author Jean Lazarou
 * @version 1.0
 */
public class StringTools
{
    public static String replace(String s, char oldChar, String newSequence)
    {
        char c;

        StringBuffer buffer = new StringBuffer(s.length());

        for (int i = 0; i < s.length(); ++i)
        {
            c = s.charAt(i);

            if (c == oldChar)
            {
                buffer.append(newSequence);
            }
            else
            {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    public static String replace(String s, char oldChar, String newSequence, int times)
    {
        char c;
        int count = 0;

        StringBuffer buffer = new StringBuffer(s.length());

        for (int i = 0; i < s.length(); ++i)
        {
            c = s.charAt(i);

            if (c == oldChar)
            {
                count++;
                buffer.append(newSequence);

                if (count >= times)
                {
                    return buffer.toString();
                }
            }
            else
            {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }
}