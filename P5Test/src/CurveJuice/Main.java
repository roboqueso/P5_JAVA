package CurveJuice;



import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import sun.jvm.hotspot.debugger.posix.elf.ELFSectionHeader;


/**
 * CURVE!
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


        tmp = shapeJous(xx, yy, w, ct);

        stroke(frameCount%225);


        //  just one shape
        beginShape();

            // NOTE: this only works with vertex
            // FILL YOURSELF -> don't get FULL desktop, just get w x w
//            texture( get(xx,yy, w*3, w*3) );


            for(int vv = 0; vv < tmp.getVertexCount(); vv++ )
            {
                PVector vect = tmp.getVertex(vv);

//  no fill w/texture(), use tint()
//  tint(vect.x, vect.y, vect.z);
//  vertex( vect.x, vect.y, vect.z, vect.y, vect.x );

                //  no texture w/curveVertex
                fill(vect.x, vect.y, vect.z);
                curveVertex( vect.x, vect.y, vect.z );
            }

        endShape(CLOSE);    // NEW

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



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Start and run CurveJuice.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "CurveJuice.Main" );
    }

}
