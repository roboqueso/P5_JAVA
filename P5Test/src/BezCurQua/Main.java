package BezCurQua;



import processing.core.PApplet;

import processing.core.PShape;
import processing.core.PVector;


/**
 * Continued curve vertec RnD : Bezier vs Curve vs Quadratic.
 *
 * bezierVertex() https://processing.org/reference/bezierVertex_.html
 *
 * X
 *
 * curveVertex() https://processing.org/reference/curveVertex_.html
 *
 * X
 *
 * quadraticVertex() https://processing.org/reference/quadraticVertex_.html
 *      LOSER -> quadraticVertex() is now voted off the project, too gnarly
 *      * don't spend any more time with quadraticVertex
 *
 * NEW FUNCTIONS : pdeName(), getTimestamp(), savePng()
 *
 * All saving of PNG goes through savePng("") now
 *
 * DEBUG - point count now showing
 *  // debug - show point count
    text( tmp.getVertexCount() , xx, yy );
 *
 * ---------------------------------
 *  HISTORY
 * ---------------------------------
 *
 * TODO: get your javadoc game back, make this a proper changelog
 *
 * getSeven() new helper
 *
 *
 * 4 x/y based COLOR themes in draw()
 **
 *
 * sweet FILL() bro!
 *
 * code cleanups
 *
 * - Dropping shapeJuan for shapeJous()
 *  ** NEW HOTNESS promoted - start looking at curves
 *
 * - FILL YOURSELF!Shape.texture( get() )
 *      * HOT FILL TWEAK
 *      texture( get(xx, yy, w,w) );
 *
 * - MODing out at 225 vs 255 ( don't stroke all the way white w/white background )
 *
 * - MORE SMOOTH RnD
 *  -> sketchSmooth()?
 *  -> you can only smooth() in settings();
 *  -> should all the smooth magic existing in settings only?
 *
 *  STROKE HINTS ON :: things looking "pretty smooth", could it be smoother via native P5 or JAVA? ( before shaders )
 *
 * TODO: revisit http://creative-co.de/better_looking_processing/
 *
 *
 *
 * - doExit() NO LONGER saves png.  This was causing a 37th blank png to be created
 *
 * - noFill()   ->  focusing on maths, not color ATM
 *
 * TODO: Lissajous knot https://en.wikipedia.org/wiki/Lissajous_knot
 * research knots?

 //  MATT BERNHARDT
 //  https://www.openprocessing.org/sketch/57613#


 * - solved relative base path situation
 *      String PROJECT_ROOT = System.getProperty("user.dir");
 String PNG_OUT = PROJECT_ROOT + "/out/";
 *
 *
 * - PNGs are now saving to the P5Test/out folder
 * save(  PNG_OUT + this + "_" + ct + ".png");
 *
 * - new util functions : doExit(), savePng().
 *
 * - xyInc : PI CHANGES EVERYTHING!
 *  - changed to w*PI
 *      - grid looks great, nice spacing
 *      - shapes are totally different than before !
 *
 * - w = 42
 *
 * - grid adjusted
 *  - xx/yy starting point = w
 *  - xyInc : grid control in one place
 *
 * - strokeWeight(2) : 2 like xyInc which is w*2 -> PI too fat for this setup
 *  - if strokeWeight goes up, xyInc will need adjustment
 *
 * - using actual w param for shape size
 *
 * - Using a mixture of 2D & 3D shapes
 *
 * - 9 - 36 trials
 *  - NEW incrementer debug stamp in the middle-ish
 *
 * - Z VALUE IN SHAPEJOUS () CRRRRAAAAZZYYYYY MAAAANNNN
 *  - Z mods INC
 *

 * - setupStage() util for resetting stage.  Hoping to locate some P5 smoothing magic to draw better lines

 *
 *  sketch   ->  added incrementor param to shapeJous() to help with the trial of finding the perfect point count ( incrementor )
 * s    ->  saves .png
 * esc  ->  saves .png, kills sketch
 */
public class Main extends PApplet {



    int CURVE_MODE = 0; //  0 bezier, 1 curve

    int cX, cY, xx, yy, xyInc;
    int ct = 9, w = 42;
    PShape tmp = new PShape();
    PVector vect;
    PVector ctl1 = new PVector();
    PVector ctl2 = new PVector();

    //  TODO: is there a smarter way to "get relative" when saving PNGs from a running PApplet?
    String PROJECT_ROOT = System.getProperty("user.dir");
    String PNG_OUT = PROJECT_ROOT + "/out/";




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public  void  settings ()  {
        size(1280, 720, "processing.opengl.PGraphics3D");
        smooth(8);  //  smooth() can only be used in settings();
        pixelDensity(displayDensity());

        // TODO - what is sketchSmooth();
        sketchSmooth();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    /**
     * The setup() function is run once, when the program starts.
     * It's used to define initial environment properties such as screen size and to load media such as images and fonts as the program starts.
     * There can only be one setup() function for each program and it shouldn't be called again after its initial execution.
     If the sketch is a different dimension than the default, the size() function or fullScreen() function must be the first line in setup().
     Note: Variables DECLARED within setup() are not accessible within other functions, including draw().
     */
    public  void  setup ()  {


    // TODO: do these do anything?
    hint(ENABLE_OPTIMIZED_STROKE);
    hint(ENABLE_STROKE_PERSPECTIVE);
    hint(ENABLE_STROKE_PURE);

    hint(ENABLE_DEPTH_MASK);
    hint(ENABLE_DEPTH_SORT);
    hint(ENABLE_DEPTH_TEST);

    hint(ENABLE_ASYNC_SAVEFRAME);
    hint(ENABLE_BUFFER_READING);
    hint(ENABLE_TEXTURE_MIPMAPS);
    // TODO: what HINTs should be looked at for additional smoothing

        setupStage();

        //  setup variables
        cX = width/2;
        cY = height/2;

        xx = yy = w;
        xyInc = (int)(w*2);    // x|y increment grid control

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public  void  draw ()  {

        lights(); //    because P3D

        tmp = shapeJous(xx, yy, w, ct);

// debug - show point count
text( tmp.getVertexCount() , xx, yy );

        beginShape();

            //  put down a vertex for the curves
            vertex(xx, yy, w);

            //  using final vertex as control point
            ctl2 = tmp.getVertex(tmp.getVertexCount() - 1);

            for(int vv = 0; vv < tmp.getVertexCount(); vv++ )
            {
                vect = tmp.getVertex(vv);   // GET VERTEX 1ST, color below depends on it

                    switch (CURVE_MODE) {
                        case 0:

                            fill(vect.x %255, vect.y  %255, vect.z  %255);
                            stroke(vv*w, 100);


                            //  TODO : bezierDetail
                            bezierDetail( 20+vv );

                            //  NICE & FLOWERY
                            bezierVertex(   ctl2.x, ctl2.y, ctl2.z,
                                        xx+vv, yy+vv, w+vv,
                                            vect.x, vect.y, vect.z );

//  BETA
//                            bezierVertex(xx+vv, yy+vv, w+vv,
//                                    ctl2.x+w+vv, ctl2.y+w+vv, ctl2.z+w+vv,
//                                    vect.x, vect.y, vect.z
//                                         );


                            break;

                        case 1:



                            fill(vect.z %255, vect.x  %255, vect.y  %255);
                            stroke(vv*w, 100);

                            //  TODO : curveDetail
                            curveDetail(20+vv);

                            curveVertex(vect.x, vect.y, vect.z);


                            break;
                    }

            }

        endShape(CLOSE);




        if (xx >= width) {
            xx = w;
            yy += xyInc;

            //  don't use 2D logic w/3D coordinates
        } else if (yy >= height ) {

            //  Save for reference
            savePng(ct + " = " + CURVE_MODE);

            xx = yy = w;

            // only increment after drawing finishes entire screen
            ct++;

            System.gc();

            //  reset stage
            setupStage();

        } else {
            xx += xyInc;
        }



        //  stopper
        if(ct>36){
            doExit();
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void keyPressed(){

        switch(keyCode){

            case ESC:{
                doExit();
            }
            break;

            case 's':
            case 'S':
            {
                savePng("");
            }
            break;
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * exit function
     */
    private void doExit(){
        //  save on exit
        savePng("ERICFICKES.COM");

        //  TODO: what's the most effective GC technique for processing?
        System.gc();
        noLoop();
        exit();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Stamps bottom left corner of sketch with current sketchname + timestamp + msg.
     * Next saves a PNG named SketchnameTimestamp.png
     *
     * @implNote : this function uses the global variable w
     */
    void savePng(String msg){
//  TODO: fix this label action
        //  STAMP IT
        fill(0xEF2017);
        textSize(w);
        text(pdeName() + "/" + msg, w, w*PI);

        save(  PNG_OUT + pdeName() + getTimestamp() + ".png");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Helper function to clear stage
     */
    private void setupStage() {

        //  reset stage
        background(0xEFEFEF);

        strokeCap(ROUND);
        strokeJoin(ROUND);
        strokeWeight(2);

        noFill();

    }







    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Lissajous PShape maker
     * @param a     X coordinate
     * @param b     Y coordinate
     * @param amp   Amplitude or size
     * @param inc   Loop magic incrementer [ 1 - 36 supported ]. (360 / inc) = number of points in returned PShape
     * @return  PShape containing vertices in the shape of a lissajous pattern
     */
    private PShape shapeJous( float a, float b, float amp, int inc )
    {
        //  PROTOTYPING : trying to locate universal ideal INCrementor for lisajouss loop
        //  Ideal range is someplace between 1 and 36
        if( ( inc < 1 ) || ( inc > 36 ) ) {
            inc = 1;
        }

        PShape shp = createShape();
        shp.beginShape(POLYGON);   //  POLYGON

        float x, y;

        for ( int t = 0; t <= 180; t+=inc)  //  180 instead of 360?
        {
            //  NEW HOTNESS!
            x = a - amp * cos((a * t * TWO_PI)/360);
            y = b - amp * sin((b * t * TWO_PI)/360);

            //  Z mods INC
            shp.vertex(x, y, t%inc);
        }

        shp.endShape(CLOSE); //  CLOSE

        return shp;
    }


    /**
     * Pick a random multiple of 7 between a min and max number pair
     *
     * TODO: gogo JAVA magic, let's smarten up this old P5 helper function
     */
    int getSeven(int min, int max)
    {
        int safecatch = 1, val=1;

        //	be safe
        if((min<max) && max>0)
        {
            while( ((val%7)!=0 ) && safecatch<7)
            {
                val = (int)random(min,max);
                // don't loop more than 7 times
                safecatch++;
            }
        }
        return val;
    }

    /**
     * Spit out timestamp.  Current format "MM-dd-yyy-HHmmss"
     * @return
     */
    String getTimestamp() {
        return new java.text.SimpleDateFormat("MM-dd-yyy-HHmmss").format(new java.util.Date());
    }


    /**
     * Returns FOLDER.MAIN "pdeName"
     *
     * @return
     */
    String pdeName() {
        return split( this.toString(), "@")[0];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Start and run BezCurQua.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "BezCurQua.Main" );
    }

}




/*

NOTE: LEAVING HERE AS IT MIGHT COME IN HANDY FOR LATER

//  Create a PShape of 7 points, centered at specified cx, cy
    PShape shape7(int cx, int cy, int sz)
    {
        //  createShape(kind, params)
        //  @kind   int: either POINT, LINE, TRIANGLE, QUAD, RECT, ELLIPSE, ARC, BOX, SPHERE
        //  @p      float[]: parameters that match the kind of shape
        PShape shape7;

        shape7 = createShape();
        shape7.beginShape();

        //  LET'S BE POSITIVE
        cx = ( cx < 0 ) ? -cx : cx;
        cy = ( cy < 0 ) ? -cy : cy;


        for (int i = 0; i < 6; i = i+1)
        {
            //  pick seven points using circle logic
            shape7.vertex(   cx - int( cos(radians(random(360))) * sz ),
                cy - int( sin(radians(random(360))) * sz ) );
        }

        shape7.endShape(CLOSE);

        return shape7;
    }
*/