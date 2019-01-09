package ToMCT.Model.ColoredTrails.Agent.ToM;

public class ToMAgent implements ToM{

    // FIELDS
    private ToM model;

    private GoalBelief goalBelief;

    // CONSTRUCTOR

    public ToMAgent(int order){

        if(order>1)
            model = new ToMAgent(order-1);
        else
            model = new ZeroToMAgent();

        goalBelief = new GoalBelief();
    }

}
