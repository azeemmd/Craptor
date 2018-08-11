
package craptor.swing.editor;

import javax.swing.text.*;

/**
 * Description: a document with SQL scipt content
 * @author Jean Lazarou
 * @version 1.0
 */
public class SQLEditorKit extends DefaultEditorKit
{
    SQLContext preferences;

    public SQLEditorKit()
    {
    }

    public SQLContext getStylePreferences()
    {
        if (preferences == null)
        {
            preferences = new SQLContext();
	    }

	    return preferences;
    }

    public void setStylePreferences(SQLContext prefs)
    {
	    preferences = prefs;
    }

    // --- EditorKit methods -------------------------

    /**
     * Get the MIME type of the data that this
     * kit represents support for.  This kit supports
     * the type <code>text/sql</code>.
     */
    public String getContentType()
    {
	    return "text/sql";
    }

    public Object clone()
    {
        SQLEditorKit kit = new SQLEditorKit();
        kit.preferences = preferences;
        return kit;
    }

    public Document createDefaultDocument()
    {
	    return new SQLDocument();
    }

    /**
     * Fetches a factory that is suitable for producing
     * views of any models that are produced by this
     * kit.  The default is to have the UI produce the
     * factory, so this method has no implementation.
     *
     * @return the view factory
     */
    public final ViewFactory getViewFactory()
    {
	    return getStylePreferences();
    }
}
