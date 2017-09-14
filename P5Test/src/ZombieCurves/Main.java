package ZombieCurves;



import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;


/**
 * CURVES
 *
 * this build is all about x/y based fill + a dash of emissive to punch up the RED
 *
 * getSeven() new helper
 *
 * TODO: visit https://processing.org/tutorials/curves/
 *  a. line
 *  b. curveVertex
 *  c. TODO : implement shapeJouse in all possible vertex / curve scenarios
 *
 * 4 x/y based COLOR themes in draw()
 *
 * TODO: update the "stamp" to be a cool looking debug bar
 *  : sketch name
 *      ( parse "this" into something readable and making pngs easy to connect back to corresponding .java )
 *  : debug info like incrementer
 *  : fleXible X positioning based on message character length??
 *
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

    int cX, cY, xx, yy, xyInc;
    int ct = 9, w = 42;
    PShape tmp = new PShape();

    //  TODO: is there a smarter way to "get relative" when saving PNGs from a running PApplet?
    String PROJECT_ROOT = System.getProperty("user.dir");
    String PNG_OUT = PROJECT_ROOT + "/out/";



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public  void  settings ()  {
        size(1280, 720, "processing.opengl.PGraphics3D");
        smooth(8);  //  smooth() can only be used in settings();
        pixelDensity(displayDensity());
// TODO - WTF is sketchSmooth();
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

        noStroke();

        beginShape();

            for(int vv = 0; vv < tmp.getVertexCount(); vv++ )
            {
                PVector vect = tmp.getVertex(vv);   // GET VERTEX 1ST, color below depends on it

                //  no texture w/curveVertex
                //stroke(frameCount%140, 225);
                stroke(vect.x);

                emissive(vv,0,0);    //  just a skooch of RED

//  DEEP ON THE BLUES
//fill(getSeven( (int)vect.x,255), getSeven( (int)vect.y,245), getSeven( (int)vect.z,255), 225);    //  better without %42 or 255
//emissive(getSeven( (int)vect.x,255), getSeven( (int)vect.y,245), getSeven( (int)vect.z,255) );    //  a lil too hot w/ noStroke();

//  DECENT PASTEL / WATER COLOR LIKE
//fill(getSeven(7,(int)vect.x), getSeven(w ,245), getSeven(7,(int)vect.y), 225);
//emissive(getSeven(7,(int)vect.x), getSeven(w ,245), getSeven(7,(int)vect.y));

//  HOT PINK ICE QUEEN
//fill(getSeven( w, (int)vect.x), getSeven( w, (int)vect.y), getSeven( (int)vect.x,width ), 225);
//emissive(getSeven( w, (int)vect.x), getSeven( w, (int)vect.y), getSeven( (int)vect.x,width ));

//  DIRTY RASTA
fill(getSeven( xx, (int)vect.x), getSeven( yy, (int)vect.y ), getSeven( vv, (int)vect.z),  225);

//    emissive(255,0,0);    //  RED + DIRTY RASTA is all red & yellow like fire!!!!


                curveVertex( vect.x, vect.y, vect.z );
            }

        endShape(CLOSE);    // NEW





//  TODO : P3D LIGHT AND SHAPE TESTING
//        noStroke();
//        translate(xx, yy, w+ct);
//
//        pushMatrix();
//
//                rotateX(-PI * 2 );
//                rotateY(PI * 3);
//
//            // fill(255,0,0); // =(
//            ambientLight(xx,yy,w+ct);
//            shape(tmp);
//        popMatrix();
//
//
//
//
//



        if (xx >= width) {
            xx = w;
            yy += xyInc;

            //  don't use 2D logic w/3D coordinates
        } else if (yy >= height ) {


            //  STAMP IT
            fill(225,0,0,69);
            rect(0, 0, w*2, w*HALF_PI);

            fill(225);
            textSize(42);

            text( ct, w/2, w);

            savePng();

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
                savePng();
            }
            break;
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * exit function
     */
    private void doExit(){
        noLoop();
        System.gc();
        exit();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * SAVE PNG helper
     */
    void savePng(){
        save(  PNG_OUT + this + "_" + ct + ".png");
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
        shp.beginShape(POLYGON);

        float x, y;

        for ( int t = 0; t <= 180; t+=inc)  //  180 instead of 360?
        {
            //  NEW HOTNESS!
            x = a - amp * cos((a * t * TWO_PI)/360);
            y = b - amp * sin((b * t * TWO_PI)/360);

            //  Z mods INC
            shp.vertex(x, y, t%inc);
        }

        shp.endShape(CLOSE);

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Start and run ZombieCurves.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "ZombieCurves.Main" );
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