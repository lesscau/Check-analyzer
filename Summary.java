import java.util.List;
import java.util.Map;

/**
 * Created by Руся on 28.09.2017.
 */
public class Summary {
    //Функция расчета кто сколько должен. Принимает количество участников n, список цен prices по порядку, как в чеке,
    // список из чека checklist с отметками участников заказавших каждую позицию.
    // Возвращает массив с суммами каждого участника стола
    public static float[] Calculate(int n, float[] prices, Map<Integer,List<Integer>> checklist){
        //Итоговый массив кто сколько должен
        float[] result = new float[n];
        //Проход по списку в чеке
        for(Integer i = 0; i < checklist.size(); i++){
            //Если никто не поставил галочку
            if(checklist.get(i).size() == 0) {
                for(int j = 0; j < n; j++){
                    result[j] += prices[i]/n;
                }
            }
            else
            {
                //Расчет для тех, кто поставил галочки
                for(Integer id_person : checklist.get(i)){
                    result[id_person] += prices[i]/checklist.get(i).size();
                }
            }
        }
        return result;
    }
}
