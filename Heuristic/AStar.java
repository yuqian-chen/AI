import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;
public class AStar {

	private Board initialState;
	private Board goalState;
	private AStarHeuristic heuristic;

	public AStar(Board initial, Board goal, AStarHeuristic heur)
	{
		initialState = initial;
		initial.setCostToCurrState(0);
		goalState = goal;
		heuristic = heur;
	}

	public void search() 
	{
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PriorityQueue<Board> frontier = new PriorityQueue<Board>(11,
				new BoardComparator(this.heuristic,this.goalState)); 
		ArrayList<Board> explored = new ArrayList<Board>();
		/* Declare and initialize Frontier and Explored data structures */ 
		/* Put start node in Fringe list Frontier */
		frontier.add(initialState);
		while (!frontier.isEmpty())
		{
			Board n = frontier.poll();
			explored.add(n);
			/* Remove from Frontier list the node n for which f(n) is minimum */
			/* Add n to Explored list*/
			//n.print();
			//System.out.println("");
			if (n.equals(goalState))
			{
				/* Print the solution path and other required information */
				/* Trace the solution path from goal state to initial state using getParent() function*/
				Stack<Board> result = new Stack<Board>();
				
				while(n.getParent() != null){
					result.push(n);
					n = n.getParent();	
				}
				int totalStep = result.size();
				result.push(initialState);
				while(!result.isEmpty()){
					result.pop().print();
					System.out.println();
				}
				System.out.println(totalStep);
				System.out.println(explored.size());
				return;
			}

			ArrayList<Board> successors = n.getSuccessors();
			for (int i = 0 ;i<successors.size(); i++)
			{
				Board n1 = successors.get(i);
				/* if n1 is not already in either Frontier or Explored
				      Compute h(n1), g(n1) = g(n)+c(n, n1), f(n1)=g(n1)+h(n1), place n1 in Frontier
				   if n1 is already in either Frontier or Explored
				      if g(n1) is lower for the newly generated n1
				          Replace existing n1 with newly generated g(n1), h(n1), set parent of n1 to n
				          if n1 is in Explored list
				              Move n1 from Explored to Frontier list*/
				if(!explored.contains(n1) && !frontier.contains(n1)){
					frontier.add(n1);
				}
				else if(frontier.contains(n1)){
					ArrayList<Object> list = new ArrayList<Object> (Arrays.asList(frontier.toArray()));
					//Iterator<Board> itr = frontier.iterator();
					for(Object o : list){
						Board x = (Board)o;
						if(x.equals(n1)){
							if (
									evaluate(this.heuristic,this.goalState,n1) 
									< evaluate(this.heuristic,this.goalState,x)){
								frontier.remove(x);
								frontier.add(n1);
							}
						}
					}

				}else if(explored.contains(n1)){
					if( evaluate(this.heuristic,this.goalState,n1)  <
							evaluate(this.heuristic,this.goalState,explored.get(explored.indexOf(n1))) ){
						explored.remove(n1);
						explored.add(n1);
					}
				}

			}
		}
		System.out.println("No Solution");
	}
	public static int evaluate(AStarHeuristic heuristic,Board goalState,Board board){
		int h = heuristic.getCost(board, goalState);
		int g = board.getCostToCurrState();
		int f = h + g;
		return f;
	}

}
class BoardComparator<E> implements Comparator<E>  {
	private AStarHeuristic heuristic;
	private Board goalState;

	public BoardComparator(AStarHeuristic heuristic,Board goalState){
		this.heuristic = heuristic;
		this.goalState = goalState;
	}
	public int compare(E o1, E o2) {
		// TODO Auto-generated method stub
		return AStar.evaluate(heuristic, goalState, (Board)o1) - 
				AStar.evaluate(heuristic, goalState, (Board)o2);
	}
}