/**
 * @author lcl100
 * @create 2021-06-10 16:29
 * 由于每移动一步,需要保存当前的游戏状态，所以此处定义此地图类,保存人的位置和游戏
 * 地图的当前状态。撤销移动时，恢复地图的操作通过此类获取需要的人的位置、地图的当前状态和关卡数来实现。
 */
public class Map {
    // 当前角色的x和y坐标
    int manX = 0, manY = 0;
    // 当前的地图数据
    byte[][] map;
    // 当前的关卡数
    int grade;

    /**
     * 此构造方法用于撤销操作，撤销操作只需要人的位置和地图的当前状态
     *
     * @param manX
     * @param manY
     * @param map
     */
    public Map(int manX, int manY, byte[][] map) {
        this.manX = manX;
        this.manY = manY;
//        this.map = map;// bug代码，不要使用
        // 将map数据直接复制到temp中，然后赋值给this.map
        int row = map.length;
        int column = map[0].length;
        byte[][] temp = new byte[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                temp[i][j] = map[i][j];
            }
        }
        this.map = temp;
    }

    /**
     * 此构造方法用于保存操作，恢复地图时需要人的位置、地图的当前状态和关卡数
     *
     * @param manX
     * @param manY
     * @param map
     * @param grade
     */
    public Map(int manX, int manY, byte[][] map, int grade) {
        this(manX, manY, map);
        this.grade = grade;
    }

    public int getManX() {
        return manX;
    }

    public void setManX(int manX) {
        this.manX = manX;
    }

    public int getManY() {
        return manY;
    }

    public void setManY(int manY) {
        this.manY = manY;
    }

    public byte[][] getMap() {
        return map;
    }

    public void setMap(byte[][] map) {
        this.map = map;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
