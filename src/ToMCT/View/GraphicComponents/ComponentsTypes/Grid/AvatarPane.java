package ToMCT.View.GraphicComponents.ComponentsTypes.Grid;

import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;
import ToMCT.View.GraphicComponents.RelJComponent;
import ToMCT.View.Images.ImageProcessor;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class AvatarPane  extends RelJComponent implements Observer {

    private Location location;

    public AvatarPane(RelJComponent parent, Location location, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        location.addObserver(this);
        this.location = location;
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width) {

        //Paint player

        if(location.getPlayers().size()>0)
            //Paint icon for player
            g.drawImage(ImageProcessor.getImage("PlayerIcon.png"), x, y, width, height, this);

    }

    @Override
    public void update(Observable observable, Object message){
        repaint();
    }
}