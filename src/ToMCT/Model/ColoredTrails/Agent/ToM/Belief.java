package ToMCT.Model.ColoredTrails.Agent.ToM;

public abstract class Belief  {

    // FIELDS
    private double def;
    private double beliefs[][];

    // CONSTRUCTORS

    public Belief(int h, int w){
        def = 1.0;
        this.beliefs = new double[h][w];

        for(int i=0; i<h; i++)
            for(int k=0; k<w; k++)
                this.beliefs[i][k] = def;
    }

    // SETTERS

    public void init(int x, int y){
        this.beliefs[x][y] = def;
    }

    public void put(int x, int y, double val){
        if(val<0.0 || val>1.0)
            return;

        this.beliefs[x][y] = val;
    }

    public Belief setDef(double val){
        this.def = val;
        return this;
    }

    // GETTERS

    public double get(int x, int y){
        return this.beliefs[x][y];
    }
}
