package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.List;

import java.util.ArrayList;

// 3. SuitableForAttackUnitsFinderImpl.getSuitableUnits
// Сложность: O(n * m), где n — количество юнитов в ряду, m — количество рядов (фиксировано 3).
// Обоснование:
// - Цикл по каждому ряду: O(m).
// - Поиск минимального/максимального Y в ряду: O(n).
// - Добавление подходящих юнитов в список: O(n).
// Итоговая сложность: O(m * n). Поскольку m фиксировано (3), сложность упрощается до O(n).
public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        for (List<Unit> row : unitsByRow) {
            if (row.isEmpty()) continue;

            if (isLeftArmyTarget) {
                addUnitsWithMinY(row, suitableUnits);
            } else {
                addUnitsWithMaxY(row, suitableUnits);
            }
        }
        return suitableUnits;
    }

    private void addUnitsWithMinY(List<Unit> row, List<Unit> suitableUnits) {
        int minY = Integer.MAX_VALUE;
        for (Unit unit : row) {
            if (unit.isAlive() && unit.getyCoordinate() < minY) {
                minY = unit.getyCoordinate();
            }
        }
        for (Unit unit : row) {
            if (unit.isAlive() && unit.getyCoordinate() == minY) {
                suitableUnits.add(unit);
            }
        }
    }

    private void addUnitsWithMaxY(List<Unit> row, List<Unit> suitableUnits) {
        int maxY = Integer.MIN_VALUE;
        for (Unit unit : row) {
            if (unit.isAlive() && unit.getyCoordinate() > maxY) {
                maxY = unit.getyCoordinate();
            }
        }
        for (Unit unit : row) {
            if (unit.isAlive() && unit.getyCoordinate() == maxY) {
                suitableUnits.add(unit);
            }
        }
    }
}