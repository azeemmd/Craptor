
package craptor.swing.editor;

import java.io.*;
import javax.swing.text.*;



/**
 * Description: plug-in for JEditorPane to provide SQL syntax highlighting
 * @author Jean Lazarou
 * @version 1.0
 */
public class SQLDocument extends PlainDocument
{
    public static final int NORMAL = 0;
    public static final int KEYWORD = 1;
    public static final int LITERAL = 2;
    public static final int COMMENT = 3;
    public static final int SEPARATOR = 4;
    public static final int OPERATOR = 5;
    public static final int SPACES = 6;

    SQLScanner scanner;

    public SQLDocument()
    {
	    super(new GapContent(1024));

        scanner = new SQLScanner(this);
    }

    public SQLDocument(File file) throws IOException
    {
	    super(new GapContent(1024));

        load(file);

        scanner = new SQLScanner(this);
    }

    public Range getRange(int p0)
    {
        return scanner.getRanges().getInsideRange(p0);
    }

    public void setText(String text)
    {
        try
        {
            remove(getStartPosition().getOffset(), getLength());
            insertString(0, text, null);
        }
        catch(BadLocationException ble)
        {
        }
    }

    public void load(File file) throws FileNotFoundException, IOException
    {
        String line;
        FileInputStream fis = new FileInputStream(file);
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(fis));

        try
        {
            remove(getStartPosition().getOffset(), getLength());

            line = reader.readLine();

            while (line != null)
            {
                insertString(getLength(), line, null);

                line = reader.readLine();
            }
        }
        catch(BadLocationException ble)
        {
        }

        reader.close();
    }

    public void save(File file) throws IOException
    {
        FileWriter fis = new FileWriter(file);

        try
        {
            String content = getContent().getString(0, getContent().length());

            fis.write(StringTools.replace(content, '\n', System.getProperty("line.separator")));
        }
        catch(BadLocationException ble)
        {
        }

        fis.flush();
        fis.close();
    }
}
