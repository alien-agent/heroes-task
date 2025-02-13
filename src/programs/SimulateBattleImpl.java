package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 2. SimulateBattleImpl.simulate
// Сложность: O(k^2 log k), где k — общее количество юнитов в обеих армиях.
// Обоснование:
// - Получение живых юнитов: O(k).
// - Сортировка юнитов по атаке: O(k log k).
// - Цикл по каждому юниту: O(k).
// - Внутри цикла вызов метода attack: O(1) (по условию).
// Итоговая сложность: O(k log k + k * 1) = O(k log k). В худшем случае (много раундов) сложность становится O(k^2 log k).
public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        while (true) {
            List<Unit> playerUnits = getAliveUnits(playerArmy);
            List<Unit> computerUnits = getAliveUnits(computerArmy);

            if (playerUnits.isEmpty() && computerUnits.isEmpty()) break;
            if (playerUnits.isEmpty() || computerUnits.isEmpty()) break;

            List<Unit> allUnits = new ArrayList<>();
            allUnits.addAll(playerUnits);
            allUnits.addAll(computerUnits);
            allUnits.sort((u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack()));

            for (Unit unit : allUnits) {
                if (!unit.isAlive()) continue;

                Unit target = unit.getProgram().attack();
                if (target != null) {
                    this.printBattleLog.printBattleLog(unit, target);
                }

                if (!isArmyAlive(playerArmy) || !isArmyAlive(computerArmy)) {
                    break;
                }
            }

            if (!isArmyAlive(playerArmy) || !isArmyAlive(computerArmy)) {
                break;
            }
        }
    }

    private List<Unit> getAliveUnits(Army army) {
        return army.getUnits().stream()
                .filter(Unit::isAlive)
                .collect(Collectors.toList());
    }

    private boolean isArmyAlive(Army army) {
        return army.getUnits().stream().anyMatch(Unit::isAlive);
    }
}