package ToMCT.View.GraphicComponents;

import javax.swing.*;
import java.awt.*;

public abstract class RelJComponent extends JLayeredPane {

    private double x, y;
    private double height, width;

    private RelJComponent parent;

    //CONSTRUCTORS

    //Build with only width and height (and default starting point)
    public RelJComponent(double height, double width){
        this(0, 0, height, width);
    }

    //Build with parent, width, and hegith (and default starting point)
    public RelJComponent(RelJComponent parent, double height, double width){
        this(parent, 0, 0, height, width);
    }

    //Specify starting point (top left corner) and width and height
    public RelJComponent(double x, double y, double height, double width){
        this(null, x, y, height, width);
    }

    //Specify parent, starting point (top left corner) and width and height
    public RelJComponent(RelJComponent parent, double x, double y, double height, double width){

        //Set fields

        this.x = x;
        this.y = y;

        this.height = height;
        this.width = width;

        this.parent = parent;

        //Set layout manager (bounds are the screen edges)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,screenSize.width, screenSize.height);

        //Add this component pane to the parent's pane
        if(parent != null)
            parent.add(this);

    }

    //METHODS

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        PaintAbsElement(g, getAbsX(), getAbsY(), getAbsHeight(), getAbsWidth());
    }

    //ABSTRACT METHODS

    //Paint component converting its relative coordinates to absolute coordinates (and size)
    //Must be implemented depending on the pane's subject (rectangle, oval, ....)
    public abstract void PaintAbsElement(Graphics g, int x, int y, int height, int width);

    //GETTERS (from relative to absolute coordinates)

    private int getAbsWidth(){

        if(parent == null)
            return getWidth();

        return (int) Math.round( width*parent.getAbsWidth() );
    }

    private int getAbsHeight(){

        if(parent == null)
            return getHeight();

        return (int) Math.round( height*parent.getAbsHeight() );
    }

    private int getAbsX(){

        if(parent == null)
            return (int) Math.round(x);

        return (int) Math.round( x*parent.getAbsWidth() + parent.getAbsX() );
    }

    private int getAbsY(){

        if(parent == null)
            return (int) Math.round(y);

        return (int) Math.round( y*parent.getAbsHeight() + parent.getAbsY() );
    }
}
