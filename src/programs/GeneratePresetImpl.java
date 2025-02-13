package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

// 1. GeneratePresetImpl.generate
// Сложность: O(n * m), где n — количество типов юнитов, m — максимальное количество юнитов в армии.
// Обоснование:
// - Сортировка списка юнитов: O(n log n).
// - Цикл по каждому типу юнита: O(n).
// - Внутренний цикл для создания юнитов: O(m), где m — максимальное количество юнитов одного типа (11).
// Итоговая сложность: O(n log n + n * m). Поскольку n log n доминирует только при малых n, а m ограничено 11,
// итоговая сложность упрощается до O(n * m).
public class GeneratePresetImpl implements GeneratePreset {
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        List<Unit> sortedUnits = new ArrayList<>(unitList);
        sortedUnits.sort(Comparator.comparingDouble((Unit u) ->
                (u.getBaseAttack() / (double) u.getCost()) + (u.getHealth() / (double) u.getCost())
        ).reversed());

        Army army = new Army();
        int currentPoints = 0;
        int[] yCounters = new int[3]; // Счётчики для координат Y в рядах 24,25,26
        int totalUnitsCreated = 0; // Общий счётчик созданных юнитов

        for (Unit unitTemplate : sortedUnits) {
            int cost = unitTemplate.getCost();
            int maxPossible = Math.min(11, (maxPoints - currentPoints) / cost);

            if (maxPossible <= 0) continue;

            List<Unit> createdUnits = new ArrayList<>();
            for (int i = 1; i <= maxPossible; i++) {
                // Определяем координаты
                int rowIndex = totalUnitsCreated % 3;
                int x = 24 + rowIndex;
                int y = yCounters[rowIndex]++;

                // Создаём новый юнит с правильными параметрами
                Unit newUnit = new Unit(
                        unitTemplate.getUnitType() + " " + (i),
                        unitTemplate.getUnitType(),
                        unitTemplate.getHealth(),
                        unitTemplate.getBaseAttack(),
                        unitTemplate.getCost(),
                        unitTemplate.getAttackType(),
                        new HashMap<>(unitTemplate.getAttackBonuses()),
                        new HashMap<>(unitTemplate.getDefenceBonuses()),
                        x,
                        y
                );

                createdUnits.add(newUnit);
                currentPoints += cost;
                totalUnitsCreated++;

                if (currentPoints >= maxPoints) break;
            }

            army.getUnits().addAll(createdUnits);
            if (currentPoints >= maxPoints) break;
        }

        army.setPoints(currentPoints);
        return army;
    }
}