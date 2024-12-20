# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def basicSearch(problem, datastructure, useCost=False):
    # datastructure contains nodes
    visited_states = set()  # contains states, states vary by problem
    # A node looks like (STATE, [PATH], COST)
    is_goal = False
    starting_node = (problem.getStartState(), [], 0)  # Node?
    if useCost:
        datastructure.push(starting_node, starting_node[2])
    else:
        datastructure.push(starting_node)
    goal_tuple = None
    while not is_goal and datastructure:
        # checking if we ran out of possible nodes to check
        if datastructure.isEmpty():
            return "Failure"
        # popping off node of interest (Top of stack)
        parent_node = datastructure.pop()
        parent_state = parent_node[0]
        if problem.isGoalState(parent_state):
            is_goal = True
            goal_tuple = parent_node
            # return parent's directions
        else:
            if parent_state not in visited_states:
                visited_states.add(parent_state)
                successors = problem.getSuccessors(parent_state)
                parent_path = parent_node[1]
                for child in successors:
                    child_path = [] + parent_path
                    child_path.append(child[1])
                    child_node = (child[0], child_path, parent_node[2] + child[2])
                    if useCost:
                        datastructure.push(child_node, child_node[2])
                    else:
                        datastructure.push(child_node)

    if goal_tuple is not None:
        #print("Result Actions: ", goal_tuple[1])
        return goal_tuple[1]
    else:
        print("NO RESULT")
    return None

def depthFirstSearch(problem):
    """
    Search the deepest nodes in the search tree first.

    Your search algorithm needs to return a list of actions that reaches the
    goal. Make sure to implement a graph search algorithm.

    To get started, you might want to try some of these simple commands to
    understand the search problem that is being passed in:

    print("Start:", problem.getStartState())
    print("Is the start a goal?", problem.isGoalState(problem.getStartState()))
    print("Start's successors:", problem.getSuccessors(problem.getStartState()))
    """
    "*** YOUR CODE HERE ***"
    dfs_stack = util.Stack()
    return basicSearch(problem, dfs_stack)


def breadthFirstSearch(problem):
    """Search the shallowest nodes in the search tree first."""
    "*** YOUR CODE HERE ***"
    bfs_queue = util.Queue() # contains nodes
    return basicSearch(problem, bfs_queue)


def uniformCostSearch(problem):
    """Search the node of least total cost first."""
    "*** YOUR CODE HERE ***"
    # datastructure contains nodes
    ucs_queue = util.PriorityQueue()
    return basicSearch(problem, ucs_queue, True)

def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    ucs_queue = util.PriorityQueue()
    return 0

#def aStar(node, problem, heuristic):
def aStar(aStarParams):
    # using for priority queue's function
    node = aStarParams[0]
    problem = aStarParams[1]
    heuristic = aStarParams[2]
    if len(node) < 3:
        node_cost = 0
    else:
        node_cost = node[2]
    position = node[0]
    heuristic_cost = heuristic(position, problem)
    cost = node_cost + heuristic_cost
    return cost


def aStarSearch(problem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    "*** YOUR CODE HERE ***"
    # datastructure contains nodes
    astar_queue = util.PriorityQueueWithFunction(aStar)
    visited_states = set()  # contains states, states vary by problem
    # A node looks like (STATE, [PATH], COST)
    is_goal = False
    starting_node = (problem.getStartState(), [], 0)  # Node?
    starting_aStar_node = (starting_node, problem, heuristic)
    astar_queue.push(starting_aStar_node)

    goal_tuple = None
    while not is_goal and astar_queue:
        # checking if we ran out of possible nodes to check
        if astar_queue.isEmpty():
            return "Failure"
        # popping off node of interest (Top of stack)
        aStarNode = astar_queue.pop()
        parent_node = aStarNode[0]
        parent_state = parent_node[0]
        if problem.isGoalState(parent_state):
            is_goal = True
            goal_tuple = parent_node
            # return parent's directions
        else:
            if parent_state not in visited_states:
                visited_states.add(parent_state)
                successors = problem.getSuccessors(parent_state)
                parent_path = parent_node[1]
                for child in successors:
                    child_path = [] + parent_path
                    child_path.append(child[1])
                    child_node = (child[0], child_path, parent_node[2] + child[2])
                    aStar_node = (child_node, problem, heuristic)
                    astar_queue.push(aStar_node)

    if goal_tuple is not None:
        #print("Result Actions: ", goal_tuple[1])
        return goal_tuple[1]
    else:
        print("NO RESULT")
    return None


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
