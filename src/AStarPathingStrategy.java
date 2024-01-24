import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy{
    private static final Comparator<Point> COST_COMPARATOR = Comparator.comparingDouble(p -> p.gCost + p.hCost);
    public List<Point> computePath(Point start, Point end,
                                      Predicate<Point> canPassThrough,
                                      BiPredicate<Point, Point> withinReach,
                                      Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new ArrayList<>();
        List<Point> closedList = new LinkedList<>();
        // points we've already been to
        PriorityQueue<Point> openList = new PriorityQueue<>(COST_COMPARATOR);
        // points we need to consider

        start.gCost = 0;
        start.hCost = calcHCostAtoB(start, end);
        openList.add(start);
        while (!openList.isEmpty()){
            Point current = openList.poll();
            if (withinReach.test(current, end)){
                while (current != null){
                    path.add(current);
                    if (current.equals(start)){
                        break;
                    }
                    current = current.parent;
                }
                path = path.reversed();
                path.removeFirst();
                return path;
            }

            List<Point> neighbors = potentialNeighbors.apply(current)
                    .filter(canPassThrough)
                    .filter(p -> !closedList.contains(p))
                    .toList();

            int tempG = current.gCost + 1;
            double tempF = tempG + calcHCostAtoB(current, end);

            for (Point p : neighbors){
                p.gCost = tempG;
                p.hCost = calcHCostAtoB(p, end);
                double f = p.gCost + p.hCost;
                for (Point openpoint : openList.stream().filter(p::equals).toList()){
                    if ((openpoint.gCost + openpoint.hCost) >= (p.gCost + p.hCost)){
                        openList.remove(openpoint);
                        p.parent = current;
                        openList.add(p);
                    }
                }
                if (!openList.contains(p) || (tempF > p.gCost + p.hCost)){
                    p.parent = current;
                    openList.add(p);
                }
            }
            closedList.add(current);
        }

        /*define closed list
          define open list
          while (true){
            Filtered list containing neighbors you can actually move to
            Check if any of the neighbors are beside the target
            set the g, h, f values
            add them to open list if not in open list
            add the selected node to close list
          return path*/
        return path;
    }
    public static double calcHCostAtoB(Point a, Point b){
        return Math.abs(b.getX() - a.getX()) + Math.abs(b.getY() - a.getY());
        //return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
    }
}