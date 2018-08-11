
package craptor.swing.editor;

/**
 * @author Jean Lazarou
 * @version 1.0
 */
public class SQLRanges
{
    static RangeDef bcod = new RangeDef(SQLDocument.COMMENT, 2, 2); // block comments
    static RangeDef lcsd = new RangeDef(SQLDocument.COMMENT, 1, 1); // line comments with #
    static RangeDef lcmd = new RangeDef(SQLDocument.COMMENT, 2, 1); // line comments with --
    static RangeDef dstd = new RangeDef(SQLDocument.LITERAL, 1, 1); // double quoted strings
    static RangeDef sstd = new RangeDef(SQLDocument.LITERAL, 1, 1); // single quoted strings

    static RangeDef litd = new RangeDef(SQLDocument.LITERAL, 1, 1); // numbers...
    static RangeDef keyd = new RangeDef(SQLDocument.KEYWORD, 1, 1); // keywords
    static RangeDef nord = new RangeDef(SQLDocument.NORMAL, 1, 1); // normal
    static RangeDef oped = new RangeDef(SQLDocument.OPERATOR, 1, 1); // operators
    static RangeDef spad = new RangeDef(SQLDocument.SPACES, 1, 1); // spaces
    static RangeDef sepd = new RangeDef(SQLDocument.SEPARATOR, 1, 1); // separators

    Ranges ranges = new Ranges();

    public void clear()
    {
        ranges.clear();
    }

    public void insertArea(int p0, int length)
    {
        ranges.insertArea(p0, length);
    }

    public void deleteArea(int p0, int length)
    {
        ranges.deleteArea(p0, length);
    }

    public boolean isInsideRange(int p0)
    {
        return ranges.isInside(p0);
    }
    public Range getInsideRange(int p0)
    {
        return ranges.getInside(p0);
    }

    public Range setKeyword(int p0, int p1)
    {
        return ranges.set(p0, p1, keyd);
    }
    public Range setLiteral(int p0, int p1)
    {
        return ranges.set(p0, p1, litd);
    }
    public Range setNormal(int p0, int p1)
    {
        return ranges.set(p0, p1, nord);
    }
    public Range setOperator(int p0, int p1)
    {
        return ranges.set(p0, p1, oped);
    }
    public Range setSpaces(int p0, int p1)
    {
        return ranges.set(p0, p1, spad);
    }
    public Range setSeparator(int p0, int p1)
    {
        return ranges.set(p0, p1, sepd);
    }

    // line comments with --
    public Range setLineComment(int p0, int p1)
    {
        return ranges.set(p0, p1, lcmd);
    }

    // line comments with #
    public Range setSharpLineComment(int p0, int p1)
    {
        return ranges.set(p0, p1, lcsd);
    }

    // block comments
    public Range setComment(int p0, int p1)
    {
        return ranges.set(p0, p1, bcod);
    }

    // single quote strings
    public Range setSingle(int p0, int p1)
    {
        return ranges.set(p0, p1, sstd);
    }

    // double quote strings
    public Range setDouble(int p0, int p1)
    {
        return ranges.set(p0, p1, dstd);
    }

    // --- Object interface ----------------------------------------------------

    public boolean equals(Object o)
    {
	    if (o == this) return true;

	    if (!(o instanceof SQLRanges)) return false;

        SQLRanges other = (SQLRanges) o;

        return ranges.equals(other.ranges);
    }

    public String toString()
    {
		return ranges.toString();
    }
}