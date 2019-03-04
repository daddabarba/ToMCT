package ToMCT.Model.ColoredTrails.Agent.ToM;

public abstract class Belief  {

    // FIELDS
    private double def;
    double[][] beliefs;

    // CONSTRUCTORS

    private Belief(){
        def = 1.0;
    }

    public Belief(int h, int w){
        this();
        this.beliefs = new double[h][w];

        for(int i=0; i<h; i++)
            for(int k=0; k<w; k++)
                this.beliefs[i][k] = def;
    }

    public Belief(double[][] beliefs){
        this();
        this.beliefs = beliefs;
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

    public double[][] getBeliefs() {
        return beliefs;
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Belief))
            return false;

        Belief bf = (Belief) object;

        for(int x = 0; x<beliefs.length; x+=1){
            for(int y=0; y<beliefs[0].length; y+=1)
                if(beliefs[x][y] != ((Belief) object).get(x,y))
                    return false;
        }

        return true;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"values\":[ ");

        for(int x = 0; x<beliefs.length; x+=1){
            sb.append("[ ");
            for(int y=0; y<beliefs[0].length; y+=1)
                sb.append(beliefs[x][y] + ",");
            sb.deleteCharAt(sb.length()-1);
            sb.append("],");
        }

        sb.deleteCharAt(sb.length()-1);
        sb.append("]}");

        return sb.toString();
    }
}
