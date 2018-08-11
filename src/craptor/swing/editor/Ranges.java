
package craptor.swing.editor;

import java.util.Vector;
import java.util.Iterator;

/**
 * @author Jean Lazarou
 * @version 1.0
 */
public class Ranges
{
    Vector col = new Vector();

    public Ranges()
    {
    }

    public Iterator iterator()
    {
        return col.iterator();
    }

    public void clear()
    {
        col.clear();
    }

    public int size()
    {
	    return col.size();
    }

    public void insertArea(int p0, int length)
    {
        Range range;

        int p1 = p0 + length;

        int n = col.size() - 1;

        for (int i = n; i >= 0; i--)
        {
            range = (Range) col.elementAt(i);

            if (range.p0 <= p0 && p0 < range.p0 + range.def.lmark)
            {
                col.removeElementAt(i);
            }
            else if (p0 < range.p1 && p0 > range.p1 - range.def.rmark)
            {
                col.removeElementAt(i);
            }
            else if (range.p1 == -1)
            {
                if (p0 < range.p0)
                    range.p0 += length;
            }
            else if (p0 <= range.p0)
            {
                range.p0 += length;
                range.p1 += length;
            }
            else if (p0 > range.p0 && p0 < range.p1)
            {
                range.p1 += length;
            }
        }
    }

    public void deleteArea(int p0, int length)
    {
        Range range;

        int p1 = p0 + length;

        int n = col.size() - 1;

        for (int i = n; i >= 0; i--)
        {
            range = (Range) col.elementAt(i);

            if (p0 < range.p0 + range.def.lmark && p1 >= range.p0 + range.def.lmark)
            {
                col.removeElementAt(i);
            }
            else if (p0 < range.p1 && p1 > range.p1 - range.def.rmark)
            {
                col.removeElementAt(i);
            }
            else if (range.p1 < 0)
            {
                if (range.p0 >= p1)
                {
                    range.p0 -= length;
                }
                else if (range.p0 >= p0)
                {
                    col.removeElementAt(i);
                }
            }
            else if (p0 <= range.p0 && range.p1 <= p1)
            {
                col.removeElementAt(i);
            }
            else if (range.p0 <= p0 && p1 <= range.p1)
            {
                range.p1 -= length;
            }
            else if (p1 <= range.p0)
            {
                range.p0 -= length;
                range.p1 -= length;
            }
            else if (p0 < range.p1 && range.p1 <= p1)
            {
                col.removeElementAt(i);
            }
            else if (p0 < range.p0 && range.p0 < p1)
            {
                col.removeElementAt(i);
            }
        }
    }

    public boolean isInside(int p0)
    {
        return getInside(p0) != null;
    }

    public Range getInside(int p0)
    {
        Range range;

        int n = col.size();

        for (int i = 0; i < n; i++)
        {
            range = (Range) col.elementAt(i);

            if (range.p0 <= p0 && (p0 < range.p1 || range.p1 < 0))
            {
                return range;
            }
        }

        return null;
    }

    public Range set(int p0, int p1, RangeDef def)
    {
        Range range = intersect(p0, p1);

        if (range != null)
        {
            range.def = def;

            return range;
        }

        range = new Range(p0, p1, def);

        col.addElement(range);

        return range;
    }

    private Range intersect(int p0, int p1)
    {
        Range range;
        Range reuse = null;

        int n = col.size() - 1;

        for (; n >= 0; --n)
        {
            range = (Range) col.elementAt(n);

            if (range.p1 < 0 || p1 < 0)
            {
                if (range.p1 < 0 && p1 < 0)
                {
                    range.p0 = p0;

                    if (reuse == null)
                    {
                        reuse = range;
                    }
                    else
                    {
                        col.removeElementAt(n);
                    }
                }
                else if (range.p1 < 0 && p1 > range.p0)
                {
                    range.p0 = p0;
                    range.p1 = p1;

                    if (reuse == null)
                    {
                        reuse = range;
                    }
                    else
                    {
                        col.removeElementAt(n);
                    }
                }
                else if (p1 < 0 && p0 < range.p1)
                {
                    range.p0 = p0;
                    range.p1 = -1;

                    if (reuse == null)
                    {
                        reuse = range;
                    }
                    else
                    {
                        col.removeElementAt(n);
                    }
                }

                continue;
            }

            if (range.p0 >= p0 && range.p1 <= p1)
            {
                range.p0 = p0;
                range.p1 = p1;

                if (reuse == null)
                {
                    reuse = range;
                }
                else
                {
                    col.removeElementAt(n);
                }
            }
            else if (p0 >= range.p0 && p1 <= range.p1)
            {
                range.p0 = p0;
                range.p1 = p1;

                if (reuse == null)
                {
                    reuse = range;
                }
                else
                {
                    col.removeElementAt(n);
                }
            }
            else if (p0 >= range.p0 && p0 < range.p1)
            {
                range.p0 = p0;
                range.p1 = p1;

                if (reuse == null)
                {
                    reuse = range;
                }
                else
                {
                    col.removeElementAt(n);
                }
            }
            else if (p1 >= range.p0 && p1 <= range.p1)
            {
                range.p0 = p0;
                range.p1 = p1;

                if (reuse == null)
                {
                    reuse = range;
                }
                else
                {
                    col.removeElementAt(n);
                }
            }
        }

        return reuse;
    }

    public boolean equals(Object o)
    {
	    if (o == this) return true;

	    if (!(o instanceof Ranges)) return false;

        Ranges other = (Ranges) o;

        if (col.size() != other.col.size()) return false;

        int match = 0;
        int n = col.size();

        for (int i = n - 1; i >= 0; --i)
        {
            for (int j = n - 1; j >= 0; --j)
            {
                if (col.elementAt(i).equals(other.col.elementAt(j)))
                {
                    match++;
                    break;
                }
            }
        }

	    return match == n;
    }

    public String toString()
    {
		StringBuffer buffer = new StringBuffer();

        Range r;
        String sep = "";

		buffer.append("{");

        for (int i = 0; i < col.size(); i++)
        {
		    buffer.append(sep);
		    buffer.append(col.elementAt(i).toString());

            sep = ", ";
        }

		buffer.append("}");

		return buffer.toString();
    }
}