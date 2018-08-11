
package craptor.swing.editor;

public class RangeDef
{
    public int lmark;
    public int rmark;
    public int type;

    public RangeDef(int type, int lmark, int rmark)
    {
        this.type = type;
        this.lmark = lmark;
        this.rmark = rmark;
    }

    public void copy(RangeDef other)
    {
        type = other.type;
        lmark = other.lmark;
        rmark = other.rmark;
    }

    public boolean equals(Object o)
    {
	    if (o == this) return true;

	    if (!(o instanceof RangeDef)) return false;

    	RangeDef other = (RangeDef) o;

        return other.lmark == lmark && other.rmark == rmark && other.type == type;
    }

    public String toString()
    {
        return "<" + type + ", [" + lmark + ", ]" + rmark + ">";
    }
}