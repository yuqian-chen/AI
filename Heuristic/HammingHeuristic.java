
public class HammingHeuristic implements AStarHeuristic{
	public int getCost(Board state, Board goalState)
	{
		return state.compare(goalState);
		//To DO;
	}
}

