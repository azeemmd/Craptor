
package craptor.swing.editor;

import java.awt.*;

import javax.swing.text.*;

import javax.swing.event.DocumentEvent;

/**
 * A collection of styles used to render SQL text.
 * This class also acts as a factory for the views.
 *
 * @author Jean Lazarou
 * @version 1.0
 */
public class SQLContext extends StyleContext implements ViewFactory
{
    Style[] tokenStyles;

    public SQLContext()
    {
        this.tokenStyles = new Style[7];

        Style s;
        Style parent = addStyle("sql", getStyle(DEFAULT_STYLE));

        s = addStyle (null, parent);
        s.addAttribute("comment", "comment");
        tokenStyles[SQLDocument.COMMENT] = s;
        s = addStyle (null, parent);
        s.addAttribute("keyword", "keyword");
        tokenStyles[SQLDocument.KEYWORD] = s;
        s = addStyle (null, parent);
        s.addAttribute("literal", "literal");
        tokenStyles[SQLDocument.LITERAL] = s;
        s = addStyle (null, parent);
        s.addAttribute("normal", "normal");
        tokenStyles[SQLDocument.NORMAL] = s;
        s = addStyle (null, parent);
        s.addAttribute("operator", "operator");
        tokenStyles[SQLDocument.OPERATOR] = s;
        s = addStyle (null, parent);
        s.addAttribute("separator", "separator");
        tokenStyles[SQLDocument.SEPARATOR] = s;
        s = addStyle (null, parent);
        s.addAttribute("spaces", "spaces");
        tokenStyles[SQLDocument.SPACES] = s;
    }

    public View create(Element elem)
    {
	    return new SQLView(elem);
    }

    public Font getFont(int code)
    {
        if (code >= 0 && code < tokenStyles.length)
        {
            return super.getFont(tokenStyles[code]);
        }

        return null;
    }

    public Color getForeground(int code)
    {
        if (code >= 0 && code < tokenStyles.length)
        {
            return StyleConstants.getForeground(tokenStyles[code]);
        }

        return Color.black;
    }

    public Style getStyleForScanValue(int code)
    {
        if (code >= 0 && code < tokenStyles.length)
        {
            return tokenStyles[code];
        }

    	return null;
    }

    class SQLView extends WrappedPlainView
    {
        SQLView(Element elem)
        {
            super(elem);
        }

        public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f)
        {
            super.insertUpdate(e, a, f);
            super.getContainer().repaint();
        }

        public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f)
        {
            super.removeUpdate(e, a, f);
            super.getContainer().repaint();
        }

        protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException
        {
            Range range;
            Segment text = new Segment();
            SQLDocument doc = (SQLDocument) getDocument();

            Font f;
            Font defFont = getFont(SQLDocument.NORMAL);

            Color fg;

            int len = p1 - p0;

            for (; p0 < p1; )
            {
                range = doc.getRange(p0);

                if (range == null)
                {
                    len = p1 - p0;
                    f = defFont;
                    fg = getForeground(SQLDocument.NORMAL);
                }
                else if (range.p1 < 0 || range.p1 >= p1)
                {
                    len = p1 - p0;
                    f = getFont(range.def.type);
                    fg = getForeground(range.def.type);
                }
                else
                {
                    len = range.p1 - p0;
                    f = getFont(range.def.type);
                    fg = getForeground(range.def.type);
                }

                if (f == null) f = defFont;

                doc.getText(p0, len, text);

                g.setFont(f);
                g.setColor(fg);

                x = Utilities.drawTabbedText(text, x, y, g, this, text.offset);

                p0 = p0 + len;
            }

            return x;
        }
    }
}