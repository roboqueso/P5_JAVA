package Test4;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;

/**
 * sketch   ->  added incrementor param to shapeJous() to help with the trial of finding the perfect point count ( incrementor )
 * s    ->  saves .png
 * esc  ->  saves .png, kills sketch
 */


/**
 * FIXED the "Tessellation Error: out of memory" that comes from Test3
 *  logic bomb : don't use 2D logic w/3D coordinates.
 *  everything before Test4 uses ( xx > width && yy > height ) to track screen being done
 *      w/translate(xx,yy), the shape keeps drawing to the right and below the screen before resetting the screen
 *      Thus causing shapes with even more vertices than what was already viewable.
 *      TESSELATION FREAKOUT!!!
 *
 *  TODO: figure out calculation for keeping 3D shapes within height/width : translate(x,y) if(x>width && y>height)
 *
 */
public class Main extends PApplet {


    int cX, cY, xx, yy;
    int ct = 1, w = 36;
    PShape tmp;
//  720p 1280 x 720

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

        background(0x424242);

        smooth();
        stroke( 255 );
        noFill();
        strokeWeight(PI);
        textSize(w/2);

        //  setup variables
        cX = width/2;
        cY = height/2;
        xx = yy = (w/2);
    }

    @Override
    public  void  draw ()  {

        // debug for reference
        fill((int)frameCount%255, (int)xx*w%255, (int)yy*w%255, 100 );
        text( ct, xx, yy );

        //  don't use 2D logic w/3D coordinates
        translate(xx, yy, w);
        //  make it
        tmp = shapeJous(xx, yy, (float)(w*0.75), ct);
        //  draw it
        shape( tmp );

// debug
println(xx + "," + yy + " | " + width + " , " + height + " : " + Runtime.getRuntime().freeMemory());


//  NOTE : there's some dimension voodoo happening here
//  The "VISIBLE" xx / yy are actually < width & height
        //  don't use 2D logic w/3D coordinates
        if (xx >= (width/2)) {
            xx = (w /2);
            yy += w;

        //  don't use 2D logic w/3D coordinates
        } else if (yy >= (height*.6) ) {
            System.gc(); // optionally add one of these

            //  DEBUG and save for reference
            save(this+"_"+ ct +".png");

            xx = yy = (w/2);

            // only increment after drawing finishes entire screen
            background(0x424242);
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

            shp.vertex(x, y);
        }
        shp.endShape();

        return shp;
    }



    /**
     * Start and run Test1.Main
     * @param args
     */
    public  static  void  main ( String []  args ) {
        PApplet.main( "Test4.Main" );
    }

}
