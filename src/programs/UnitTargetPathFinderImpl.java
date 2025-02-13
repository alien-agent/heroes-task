package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

// 4. UnitTargetPathFinderImpl.getTargetPath
// Сложность: O((W * H) log (W * H)), где W — ширина поля (27), H — высота поля (21).
// Обоснование:
// - Инициализация очереди и структур данных: O(1).
// - Основной цикл BFS: O((W * H) log (W * H)), так как каждая клетка посещается один раз,
//   а операции с приоритетной очередью занимают O(log (W * H)).
// - Восстановление пути: O(W * H) в худшем случае.
// Итоговая сложность: O((W * H) log (W * H)).
public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int endX = targetUnit.getxCoordinate();
        int endY = targetUnit.getyCoordinate();

        if (startX == endX && startY == endY) {
            return Collections.singletonList(new Edge(startX, startY));
        }

        Set<Edge> occupied = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                occupied.add(new Edge(unit.getxCoordinate(), unit.getyCoordinate()));
            }
        }

        Queue<PathNode> queue = new LinkedList<>();
        Map<Edge, Edge> cameFrom = new HashMap<>();
        Set<Edge> visited = new HashSet<>();

        Edge startEdge = new Edge(startX, startY);
        queue.add(new PathNode(startEdge, 0));
        visited.add(startEdge);
        cameFrom.put(startEdge, null);

        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        while (!queue.isEmpty()) {
            PathNode current = queue.poll();

            if (current.edge.getX() == endX && current.edge.getY() == endY) {
                return reconstructPath(cameFrom, current.edge);
            }

            for (int[] dir : directions) {
                int newX = current.edge.getX() + dir[0];
                int newY = current.edge.getY() + dir[1];

                if (newX < 0 || newX >= 27 || newY < 0 || newY >= 21) continue;

                Edge neighbor = new Edge(newX, newY);

                if (!occupied.contains(neighbor) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current.edge);
                    queue.add(new PathNode(neighbor, current.distance + 1));
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Edge> reconstructPath(Map<Edge, Edge> cameFrom, Edge end) {
        List<Edge> path = new ArrayList<>();
        Edge current = end;
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    private static class PathNode {
        Edge edge;
        int distance;

        PathNode(Edge edge, int distance) {
            this.edge = edge;
            this.distance = distance;
        }
    }
}
