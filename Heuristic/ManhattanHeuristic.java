
public class ManhattanHeuristic implements AStarHeuristic{
	public int getCost(Board state, Board goalState)
	{
		return state.manhattan(goalState);
		//Implement Manhattan heuristic
	}
}

