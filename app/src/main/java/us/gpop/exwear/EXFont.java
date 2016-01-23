package us.gpop.exwear;

import android.content.Context;
import android.graphics.Typeface;

/** -----------------------------------------------------------------------------------------------
 *  [EXFont] CLASS
 *  DESCRIPTION: EXFont class is used to set custom font types to Android layout objects. As
 *  Android devices installed with Android OS versions before Ice Cream Sandwich (4.0) are prone
 *  to a memory leak bug with Typeface objects, this class avoids such issue by using only a single
 *  instance. This code is borrowed from LeoCardz at http://lab.leocardz.com/android-custom-font-without-memory-leak
 *  -----------------------------------------------------------------------------------------------
 */

public class EXFont {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private final Context context;
    private static EXFont instance;

    /** CLASS FUNCTIONALITY ____________________________________________________________________ **/

    // CAVFont(): Constructor for the CAVFont class.
    private EXFont(Context context) {
        this.context = context;
    }

    // getInstance(): Creates an instance of the CAVFont class.
    public static EXFont getInstance(Context context) {
        synchronized (EXFont.class) {
            if (instance == null)
                instance = new EXFont(context);
            return instance;
        }
    }

    // getTypeFace(): Retrieves the custom font family (SixTekBlack) from resources.
    public Typeface getTypeFace() {
        return Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/nexa.ttf");
    }
}
