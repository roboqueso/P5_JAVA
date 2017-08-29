package Thirteen;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

/**
 * sketch   ->  added incrementor param to shapeJous() to help with the trial of finding the perfect point count ( incrementor )
 * s    ->  saves .png
 * esc  ->  saves .png, kills sketch
 */


/**
 * - w = 13
 * - tighten grid
 * - using actual w param for shape size
 * - 9 - 36 trials
 * - Z VALUE IN SHAPEJOUS () CRRRRAAAAZZYYYYY MAAAANNNN
 *
 *
 *  TODO: figure out calculation for keeping 3D shapes within height/width : translate(x,y) if(x>width && y>height)
 *
 */
public class Main extends PApplet {


    int cX, cY, xx, yy;
    int ct = 9, w = 13;
    PShape tmp;

    @Override
    public  void  settings ()  {
        size(1280, 720, "processing.opengl.PGraphics3D");
    }

    @Override
    /**
     * The setup() function is run once, when the program starts.
     * It's used to define initial environment properties such as screen size and to load media such as images and fonts as the program starts.
     * There can only be one setup() function for each program and it shouldn't be called again after its initial execution.
     If the sketch is a different dimension than the default, the size() function or fullScreen() function must be the first line in setup().
     Note: Variables declared within setup() are not accessible within other functions, including draw().
     */
    public  void  setup ()  {

        background(0xEFEFEF);

        smooth();
        stroke( 42 );
        noFill();
        textSize(13);

        //  setup variables
        cX = width/2;
        cY = height/2;

        xx = yy = 0;

    }

    @Override
    public  void  draw ()  {

        // debug for reference
        fill((int)frameCount%255, (int)xx*w%255, (int)yy*w%255, 100 );
        text( ct, xx, yy );

        //  don't use 2D logic w/3D coordinates
        translate(xx, yy, w);
        //  make it
        tmp = shapeJous(xx, yy, w, ct);
        //  draw it
        shape( tmp );


//  NOTE : there's some dimension voodoo happening here
//  The "VISIBLE" xx / yy are actually < width & height
        //  don't use 2D logic w/3D coordinates
        if (xx >= (width/2)) {
            xx = 0;
            yy += w;

            //  don't use 2D logic w/3D coordinates
        } else if (yy >= (height*.6) ) {
            System.gc(); // optionally add one of these

            //  DEBUG and save for reference
            save(this+"_"+ ct +".png");

            xx = yy = 0;

            // only increment after drawing finishes entire screen
            background(0xEFEFEF);
            ct++;

        } else {
            xx += w;
        }



        //  stopper
        if(ct>36){
            exit();
        }

    }


    @Override
    public void keyPressed(){

        switch(keyCode){

            case ESC:{
                save(this+".png");
                noLoop();
                super.exit();
            }
            break;

            case 's':
            case 'S':
            {
                save(this+".png");
            }
            break;
        }

    }



    //////////////////////////////////////////////////////
    //  Lissajous PShape maker
    private PShape shapeJous( float a, float b, float amp, int inc )
    {
        //  PROTOTYPING : trying to locate universal ideal INCrementor for lisajouss loop
        //  Ideal range is someplace between 1 and 36
        if( ( inc < 1 ) || ( inc > 36 ) ) {
            inc = 1;
        }

        PShape shp = createShape();
        shp.beginShape();

        float x, y;

        for ( int t = 0; t <= 360; t+=inc)
        {
            x = a - amp * sin(a * t * PI/180);
            y = b - amp * sin(b * t * PI/180);

            shp.vertex(x, y, t%180);
        }
        shp.endShape();

        return shp;
    }



    /**
     * Start and run Test1.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "Thirteen.Main" );
    }

}
