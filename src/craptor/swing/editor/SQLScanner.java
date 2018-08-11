
package craptor.swing.editor;

import java.io.*;

import java.util.HashSet;

import javax.swing.text.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Jean Lazarou
 * @version 1.0
 */
public class SQLScanner implements DocumentListener
{
    HashSet keywords;

    int base;
    SQLDocument doc;
    Token token = new Token();
    Segment segment = new Segment();
    SQLRanges ranges = new SQLRanges();

    public SQLScanner(File keywords, SQLDocument doc)
    {
        this.keywords = loadKeywords(keywords);

        this.doc = doc;

        doc.addDocumentListener(this);
    }

    public SQLScanner(SQLDocument doc)
    {
        if (s_keywords == null)
        {
            s_keywords = loadKeywords(new File("keywords"));
        }

        keywords = s_keywords;

        this.doc = doc;

        doc.addDocumentListener(this);
    }

    private void reset() throws BadLocationException
    {
        // we should be able to not rescan all the document..
        doc.getText(0, doc.getLength(), segment);

        segment.first();

        if (doc.getStartPosition().getOffset() != 0)
            base = - doc.getStartPosition().getOffset();
        else
            base = 0;
    }

    public SQLRanges getRanges()
    {
        return ranges;
    }

    private boolean scan()
    {
        if (segment.getEndIndex() == segment.getIndex())
        {
            return false;
        }

        char c = segment.current();

        if (c == '-')
        {
            c = segment.next();

            if (c == '-')
            {
                segment.previous();

                retrieveLineComment (segment, token, false);

                return true;
            }
            else
            {
                c = segment.previous();
            }
        }
        else if (c == '/')
        {
            c = segment.next();

            if (c == '*')
            {
                segment.previous();

                retrieveBlockComment(segment, token);

                return true;
            }
            else
            {
                c = segment.previous();
            }
        }

        if (c == '"')
        {
            retrieveString (segment, '"', token);

            return true;
        }
        else if (c == '\'')
        {
            retrieveString (segment, '\'', token);

            return true;
        }
        else if (isSeparator(c))
        {
            token.type = SQLDocument.SEPARATOR;
            token.value.array = segment.array;
            token.value.offset = segment.getIndex();
            token.value.count = 1;

            if (isEnd(segment.next()))
                ranges.setSeparator(base + token.value.offset, -1);
            else
                ranges.setSeparator(base + token.value.offset, base + token.value.offset + 1);

            return true;
        }
        else if (c == '#')
        {
            retrieveLineComment (segment, token, true);

            return true;
        }
        else if (isSpace(c))
        {
            retrieveSpaces (segment, token);

            return true;
        }
        else if (isOperator(c))
        {
            retrieveOperator(segment, token);
            return true;
        }
        else if (!isEnd(c))
        {
            retrieveNormal (segment, token);
            return true;
        }

        return false;
    }

    public static final boolean isSpace(char c)
    {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }

    public static final boolean isSeparator(char c)
    {
        return c == ';' || c == ',' || c == '(' || c == ')';
    }

    public static final boolean isOperator(char c)
    {
        return c == '<' || c == '>' || c == '=' ||
               c == '+' || c == '-' || c == '*' || c == '/' ||
               c == '.' || c == '|';
    }

    public static final boolean isStringStart(char c)
    {
        return c == '"' || c == '\'';
    }

    public static final boolean isEnd(char c)
    {
        return c == 0 || c == Segment.DONE;
    }

    private void retrieveString(Segment source, char sep, Token result)
    {
        char c;

        result.type = SQLDocument.LITERAL;
        result.value.array = source.array;
        result.value.offset = source.getIndex();

        do
        {

            c = source.next();

            if (c == '\\')
            {
                c = source.next();

                if (c == sep)
                {
                    c = source.next();
                }
            }
            else if (c == sep)
            {
                c = source.next();

                if (c != sep)
                {
                    c = source.previous();
                }
                else
                {
                    c = source.next();
                }
            }

        } while (!isEnd(c) && c != sep);

        source.next();

        result.value.count = source.getIndex() - result.value.offset;

        int p1 = c == sep ? base + result.value.offset + result.value.count : -1;

        if (sep == '"')
            ranges.setDouble(base + result.value.offset, p1);
        else
            ranges.setSingle(base + result.value.offset, p1);
    }

    private void retrieveBlockComment(Segment source, Token result)
    {
        result.type = SQLDocument.COMMENT;
        result.value.array = source.array;
        result.value.offset = source.getIndex();

        char c;

        c = source.next();

        while (!isEnd(c))
        {
            if (c == '*')
            {
                c = source.next();

                if (c == '/') break;
            }

            c = source.next();
        }

        if (c == '/') source.next();

        result.value.count = source.getIndex() - result.value.offset;

        int p1 = c == '/' ? base + result.value.offset + result.value.count : -1;

        ranges.setComment(base + result.value.offset, p1);
    }

    private void retrieveLineComment(Segment source, Token result, boolean sharp)
    {
        char c;

        result.type = SQLDocument.COMMENT;
        result.value.array = source.array;
        result.value.offset = source.getIndex();

        do
        {

            c = source.next();

        } while (!isEnd(c) && c != '\n' && c != '\r');

        result.value.count = source.getIndex() - result.value.offset;

        int p1 = isEnd(c) ? -1 : base + result.value.offset + result.value.count;

        if (sharp)
            ranges.setSharpLineComment(base + result.value.offset, p1);
        else
            ranges.setLineComment(base + result.value.offset, p1);
    }

    private void retrieveSpaces(Segment source, Token result)
    {
        char c;

        result.type = SQLDocument.SPACES;
        result.value.array = source.array;
        result.value.offset = source.getIndex();

        do
        {

            c = source.next();

        } while (isSpace(c));

        result.value.count = source.getIndex() - result.value.offset;

        int p1 = isEnd(c) ? -1 : base + result.value.offset + result.value.count;

        ranges.setSpaces(base + result.value.offset, p1);
    }

    private void retrieveOperator(Segment source, Token result)
    {
        /**
         * here we accept operators like '<=', '>=' and '<>' which are
         * valid 2-char operators, but we also accept bad things like
         * '>*', '+-<>=+-', etc.
         */

        char c;

        result.type = SQLDocument.OPERATOR;
        result.value.array = source.array;
        result.value.offset = source.getIndex();

        do
        {

            c = source.next();

        } while (isOperator(c));

        result.value.count = source.getIndex() - result.value.offset;

        int p1 = isEnd(c) ? -1 : base + result.value.offset + result.value.count;

        ranges.setOperator(base + result.value.offset, p1);
    }

    private void retrieveNormal(Segment source, Token result)
    {
        char c;

        result.value.array = source.array;
        result.value.offset = source.getIndex();

        do
        {

            c = source.next();

        } while (!isEnd(c) && !isOperator(c) && !isSpace(c) &&
                              !isSeparator(c) && !isStringStart(c));

        result.value.count = source.getIndex() - result.value.offset;

        int p0 = base + result.value.offset;
        int p1 = isEnd(c) ? -1 : base + result.value.offset + result.value.count;

        String s = new String(source.array, result.value.offset, result.value.count);

        if (keywords.contains(s.toLowerCase()))
        {
            result.type = SQLDocument.KEYWORD;
            ranges.setKeyword(p0, p1);
            return;
        }

        try
        {
            if (c == '.' && s.length() > 0 && Character.isDigit(s.charAt(0)))
            {
                do
                {

                    c = source.next();

                } while (!isEnd(c) && !isOperator(c) && !isSpace(c) &&
                                      !isSeparator(c) && !isStringStart(c));

                result.value.count = source.getIndex() - result.value.offset;

                p1 = isEnd(c) ? -1 : base + result.value.offset + result.value.count;

                s = new String(source.array, result.value.offset, result.value.count);
            }

            Double.parseDouble(s);

            result.type = SQLDocument.LITERAL;

            ranges.setLiteral(p0, p1);

            return;
        }
        catch (NumberFormatException ne)
        {
            result.type = SQLDocument.NORMAL;
            ranges.setNormal(p0, p1);
            return;
        }
    }

    public void rescan()
    {
        try
        {
            ranges.clear();

            reset();

            while (scan())
            {
            }
        }
        catch (BadLocationException ex)
        {
        }
    }

    // --- Token class ---------------------------------------------------------

    static class Token
    {
        int type;
        Segment value = new Segment();
    }

    // --- static part, load keywords dictionnary ------------------------------

    /**
     *  set the default keywords set
     */
    static public void setKeywords(HashSet keywords)
    {
        s_keywords = keywords;
    }

    // DocumentListener interface
    public void insertUpdate(DocumentEvent e)
    {
        rescan();
    }

    public void removeUpdate(DocumentEvent e)
    {
        rescan();
    }

    public void changedUpdate(DocumentEvent e)
    {
    }

    /**
     *  load all the SQL language keywords from the given file.
     */
    static public HashSet loadKeywords(File file)
    {
        HashSet keywords = new HashSet();

        try
        {
            StreamTokenizer reader = new StreamTokenizer(new BufferedReader(new FileReader(file)));

            while (reader.nextToken() != StreamTokenizer.TT_EOF)
            {
                if (reader.sval != null) keywords.add(reader.sval.toLowerCase());
            }
        }
        catch(FileNotFoundException fnfe)
        {
            System.err.println("Cannot load SQL keywords");
        }
        catch(IOException ioe)
        {
            System.err.println("Cannot load SQL keywords");
        }

        return keywords;
    }

    static HashSet s_keywords;
}