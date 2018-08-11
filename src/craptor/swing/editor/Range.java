
package craptor.swing.editor;

/**
 * @author Jean Lazarou
 * @version 1.0
 */
public class Range
{
	public RangeDef def;

	public int p0;
	public int p1;

	public Range (int p0, int p1)
	{
		this.p0 = p0;
		this.p1 = p1;
	}

	public Range (int p0, int p1, RangeDef def)
	{
		this.p0 = p0;
		this.p1 = p1;

		this.def = def;
	}

	public boolean equals(Object o)
	{
		if (o == this) return true;

		if (!(o instanceof Range)) return false;

		Range other = (Range) o;

		if (other == null) return false;

		if (def == null) return other.def == null;

		return other.p0 == p0 && other.p1 == p1 && def.equals(other.def);
	}

	public String toString()
	{
		return def + " [" + p0 + ", " + p1 + "]";
	}
}