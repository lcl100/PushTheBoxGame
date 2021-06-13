/**
 * @author lcl100
 * @create 2021-06-10 16:20
 * 地图数据类，保存所有关卡的原始地图数据，每关数据是一个二维数组，因此map是一个三维数组
 */
public class MapFactory {
    static byte map[][][] = {
            // 第一关
            {
                    {0, 0, 1, 1, 1, 0, 0, 0},
                    {0, 0, 1, 4, 1, 0, 0, 0},
                    {0, 0, 1, 9, 1, 1, 1, 1},
                    {1, 1, 1, 2, 9, 2, 4, 1},
                    {1, 4, 9, 2, 5, 1, 1, 1},
                    {1, 1, 1, 1, 2, 1, 0, 0},
                    {0, 0, 0, 1, 4, 1, 0, 0},
                    {0, 0, 0, 1, 1, 1, 0, 0}
            },
            // 第二关
            {
                    {1, 1, 1, 1, 1, 0, 0, 0, 0},
                    {1, 9, 9, 5, 1, 0, 0, 0, 0},
                    {1, 9, 2, 2, 1, 0, 1, 1, 1},
                    {1, 9, 2, 9, 1, 1, 1, 4, 1},
                    {1, 1, 1, 9, 1, 1, 1, 4, 1},
                    {0, 1, 1, 9, 9, 9, 9, 4, 1},
                    {0, 1, 9, 9, 9, 1, 9, 9, 1},
                    {0, 1, 9, 9, 9, 1, 1, 1, 1},
                    {0, 1, 1, 1, 1, 1, 0, 0, 0}
            }
            // 其他关卡数据
    };
    static int count = map.length;// 总的关卡数

    /**
     * 获取指定关卡的地图数据
     *
     * @param grade 指定关卡，从1开始传入
     * @return 返回该关卡的地图数据，就是一个二维数组
     */
    public static byte[][] getMap(int grade) {
        byte temp[][];// 保存关卡数据
        // 对传入的关卡数进行参数校验，不能是没有的关卡
        if (grade > 0 && grade <= count) {
            temp = map[grade - 1];// 获取指定关卡数据从map数组中，由于关卡从1开始，所以下标需要减去1
        } else {
            temp = map[0];// 如果传入的参数不合法，则显示第一关的数据
        }
//        return temp;
        // 获取已经获得关卡的行数和列数
        int row = temp.length;
        int column = temp[0].length;
        // 创建结果数组，将temp数组中的数据复制到result中返回
        byte[][] result = new byte[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result[i][j] = temp[i][j];
            }
        }
        return result;
    }

    /**
     * 返回关卡总数
     *
     * @return
     */
    public static int getCount() {
        return count;
    }
}
