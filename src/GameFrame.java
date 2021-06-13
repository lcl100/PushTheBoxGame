import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * @author lcl100
 * @create 2021-06-10 16:37
 * 游戏面板类完成游戏的界面刷新显示，以及相应鼠标键盘的相关事件
 */
public class GameFrame extends JFrame implements
        ActionListener, MouseListener, KeyListener {// 实现动作事件监听器、鼠标事件监听器、键盘事件监听器

    // 当前的关卡数，默认为第一关，从1开始计数
    private int grade = 1;
    // row，column记载人的位置，分别表示二维数组中的行号和列号，即map[row][column]确定人的位置
    private int row = 7, column = 7;
    // leftX，leftY记载左上角图片的位置，避免图片从(0,0)坐标开始，因为是图片填充，从(0,0)开始不行
    private int leftX = 50, leftY = 50;
    // 记载地图的总共有多少行、多少列
    private int mapRow = 0, mapColumn = 0;
    // 记载屏幕窗口的宽度和高度
    private int width = 0, height = 0;
    private boolean acceptKey = true;
    // 程序所需要用到的图片
    private Image pics[] = null;// 图片数据
    private byte[][] map = null;// 地图数据
    private ArrayList list = new ArrayList();
    private SoundPlayerUtil soundPlayer;// 播放声音工具类

    /* 常量，即游戏中的资源 */
    private final static int WALL = 1;// 墙
    private final static int BOX = 2;// 箱子
    private final static int BOX_ON_END = 3;// 放到目的地的箱子
    private final static int END = 4;// 目的地
    private final static int MAN_DOWN = 5;// 向下的人
    private final static int MAN_LEFT = 6;// 向左的人
    private final static int MAN_RIGHT = 7;// 向右的人
    private final static int MAN_UP = 8;// 向上的人
    private final static int GRASS = 9;// 通道
    private final static int MAN_DOWN_ON_END = 10;// 站在目的地向下的人
    private final static int MAN_LEFT_ON_END = 11;// 站在目的地向左的人
    private final static int MAN_RIGHT_ON_END = 12;// 站在目的地向右的人
    private final static int MAN_UP_ON_END = 13;// 站在目的地向上的人
    private final static int MOVE_PIXEL = 30;// 表示每次移动30像素

    /**
     * 在构造方法GameFrame0中，调用initMap()法来初始化本关grade游戏地图，清空悔棋信
     * 息列表list,同时播放MIDI背景音乐。
     */
    public GameFrame() {
        // 游戏窗口的一些基本设置
        setTitle("推箱子游戏");
        setSize(600, 600);
        setVisible(true);
        setLocation(300, 20);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(Color.black);
        // 其他设置，初始化窗口的宽度和高度赋值给width和height
        this.width = getWidth();
        this.height = getHeight();
        // 初始化图片资源
        getPics();
        // 初始化地图数据
        initMap();
        // 注册事件监听器
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        // 播放音乐
        initSound();
    }

    /**
     * initMap()方法的作用是初始化本关grade 游戏地图，清空悔棋信息列表list。 调用
     * getMapSizeAndPosition(方法获取游戏区域大小及显示游戏的左上角位置( leftX, leftY )。
     */
    public void initMap() {
        // 获取当前关卡的地图数据
        map = MapFactory.getMap(grade);
        // 清除上一关保存的回退地图数据，即清空list集合的内容
        list.clear();
        // 初始化地图行列数和左上角起始坐标位置
        getMapSizeAndPosition();
        // 获取角色的坐标位置
        getManPosition();
    }

    /**
     * getManPosition()方法的作用是获取工人的当前位置(row,column)。
     */
    public void getManPosition() {
        // 即遍历地图数组map中存在那个值等于MANXXX（MAN_DOWN表示向下的人；MAN_UP表示向上的人）的情况，即表示该位置是人站立的位置，这个由地图数据扫描得出
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == MAN_DOWN || map[i][j] == MAN_DOWN_ON_END
                        || map[i][j] == MAN_UP || map[i][j] == MAN_UP_ON_END
                        || map[i][j] == MAN_LEFT || map[i][j] == MAN_LEFT_ON_END
                        || map[i][j] == MAN_RIGHT || map[i][j] == MAN_RIGHT) {
                    // 保存人的位置，i表示第几行，j表示第几列，而且是从0开始的
                    this.row = i;
                    this.column = j;
                    break;
                }
            }
        }
    }

    /**
     * getMapSizeAndPosition()方法用来获取游戏区域大小及显示游戏的左上角位置( lefX, leftY )。
     */
    private void getMapSizeAndPosition() {
        // 初始化mapRow和mapColumn，表示地图的行列数
        this.mapRow = map.length;
        this.mapColumn = map[0].length;
        // 初始化leftX和leftY，即计算左上角的位置，
        this.leftX = (width - map[0].length * MOVE_PIXEL) / 2;
        this.leftY = (height - map.length * MOVE_PIXEL) / 2;
    }

    /**
     * getPics()方法用来加载要显示的图片
     */
    public void getPics() {
        // 创建长度为13的数组，即有十三张图片
        pics = new Image[13];
        // 然后循环将每张图片读取保存到pics数组中
        for (int i = 0; i < 13; i++) {
            pics[i] = Toolkit.getDefaultToolkit().getImage("src\\images\\pic_" + (i + 1) + ".png");
        }
    }

    /**
     * 初始化播放的音乐
     */
    public void initSound() {
        // 调用SoundPlayerUtil类中的方法播放音乐
        soundPlayer = new SoundPlayerUtil();
        soundPlayer.loadSound("src\\sounds\\music.wav");
        soundPlayer.playSound(true);// 循环播放
    }

    /**
     * grassOrEnd()方法判断人所在的位置是通道GRASS还是目的地END
     *
     * @param man
     * @return
     */
    public byte grassOrEnd(byte man) {
        if (man == MAN_DOWN_ON_END || man == MAN_LEFT_ON_END || man == MAN_RIGHT_ON_END || man == MAN_UP_ON_END) {
            return END;
        }
        return GRASS;
    }

    /**
     * 人物向上移动
     */
    private void moveUp() {
        // 如果上一位是WALL，则不能移动
        if (map[row - 1][column] == WALL) {
            return;
        }
        // 如果上一位是BOX或BOX_ON_END，需要考虑上一位的上一位是什么情况
        if (map[row - 1][column] == BOX || map[row - 1][column] == BOX_ON_END) {
            // 那么就需要考虑上一位的上一位情况，若上上一位是END或GRASS，则向上一步，其他情况不用处理
            if (map[row - 2][column] == END || map[row - 2][column] == GRASS) {
                // 要保留当前信息，以便回退上一步
                Map currMap = new Map(row, column, map);
                list.add(currMap);
                byte boxTemp = (byte) ((byte) map[row - 2][column] == END ? BOX_ON_END : BOX);
                byte manTemp = (byte) (map[row - 1][column] == BOX ? MAN_UP : MAN_UP_ON_END);
                // 箱子变成temp，箱子往前移动一步
                map[row - 2][column] = boxTemp;
                // 人变成MAN_UP，往上走一步
                map[row - 1][column] = manTemp;
                // 将人刚才站的地方变成GRASS或者END
                map[row][column] = grassOrEnd(map[row][column]);
                // 人离开后修改人的坐标
                row--;
            }
        } else {
            // 上一位为GRASS或END，无需考虑上上一步，其他情况不用处理
            if (map[row - 1][column] == GRASS || map[row - 1][column] == END) {
                // 保留当前这一步的信息，以便回退上一步
                Map currMap = new Map(row, column, map);
                list.add(currMap);
                byte temp = (byte) (map[row - 1][column] == END ? MAN_UP_ON_END : MAN_UP);
                // 人变成temp，人往上走一步
                map[row - 1][column] = temp;
                // 人刚才站的地方变成GRASS或者END
                map[row][column] = grassOrEnd(map[row][column]);
                // 人离开后修改人的坐标
                row--;
            }
        }
    }

    /**
     * 人物向下移动，其中(row,column)是当前角色站的位置，而map[row,column]表示当前角色
     */
    private void moveDown() {
        // 如果下一位是WALL，则不能移动
        // 所以map[row+1][column]表示当前角色的下一步，也就是下一关图像块
        if (map[row + 1][column] == WALL) {
            return;
        }
        // 如果下一位是箱子BOX或放到目的地的箱子BOX_ON_END（即如果下一位是箱子，而不管是什么类型的箱子，都可以推动箱子），需要考虑下位的下一位是什么情况
        if (map[row + 1][column] == BOX || map[row + 1][column] == BOX_ON_END) {
            // 那么就需要考虑下一位的下一位情况（即箱子的下一位是什么，决定着箱子是否可以向前移动），若下下一位是目的地END或通道GRASS，则表示箱子可以向下移动一步，其他情况不用处理
            // map[row+2][column]表示箱子的下一步是什么
            if (map[row + 2][column] == END || map[row + 2][column] == GRASS) {
                // 下面的代码就是箱子向前移动一步，人移动原来箱子的位置
                // 要保留当前人和地图信息，以便回退下一步
                Map currMap = new Map(row, column, map);
                list.add(currMap);
                // 判断箱子的下一步是否是目的地，如果是目的地，那么箱子的下一步就应该变成BOX_ON_END（放在目的地的箱子），如果不是目的地那应该还只是普通箱子BOX
                byte boxTemp = (byte) ((byte) map[row + 2][column] == END ? BOX_ON_END : BOX);
                // 判断人的下一步是否是箱子
                byte manTemp = (byte) (map[row + 1][column] == BOX ? MAN_DOWN : MAN_DOWN_ON_END);
                // 箱子变成temp，箱子往下移动一步
                map[row + 2][column] = boxTemp;
                // 人变成MAN_UP，往下走一步
                map[row + 1][column] = manTemp;
                // 将人刚才站的地方变成GRASS或者END
                map[row][column] = grassOrEnd(map[row][column]);
                // 人离开后修改人的坐标
                row++;
            }
        } else {
            // 执行到这里，表示人的下一步不是箱子，那么要么是通道要么是终点
            // 下一位为GRASS或END，无需考虑下下一步，其他情况不用处理
            if (map[row + 1][column] == GRASS || map[row + 1][column] == END) {
                // 保留当前这一步的信息，以便回退下一步
                Map currMap = new Map(row, column, map);
                list.add(currMap);
                byte temp = (byte) (map[row + 1][column] == END ? MAN_DOWN_ON_END : MAN_DOWN);
                // 人变成temp，人往下走一步
                map[row + 1][column] = temp;
                // 人刚才站的地方变成GRASS或者END
                map[row][column] = grassOrEnd(map[row][column]);
                // 人离开后修改人的坐标
                row++;
            }
        }
    }

    /**
     * 人物向左移动
     */
    private void moveLeft() {
        // 如果左一位是WALL，则不能移动
        if (map[row][column - 1] == WALL) {
            return;
        }
        // 如果左一位是BOX或BOX_ON_END，需要考虑左一位的左一位是什么情况
        if (map[row][column - 1] == BOX || map[row][column - 1] == BOX_ON_END) {
            // 那么就需要考虑左一位的左一位情况，若左左一位是END或GRASS，则向左一步，其他情况不用处理
            if (map[row][column - 2] == END || map[row][column - 2] == GRASS) {
                // 要保留当前信息，以便回退左一步
                Map currMap = new Map(row, column, map);
                list.add(currMap);
                byte boxTemp = (byte) ((byte) map[row][column - 2] == END ? BOX_ON_END : BOX);
                byte manTemp = (byte) (map[row][column - 1] == BOX ? MAN_LEFT : MAN_LEFT_ON_END);
                // 箱子变成temp，箱子往前移动一步
                map[row][column - 2] = boxTemp;
                // 人变成MAN_UP，往左走一步
                map[row][column - 1] = manTemp;
                // 将人刚才站的地方变成GRASS或者END
                map[row][column] = grassOrEnd(map[row][column]);
                // 人离开后修改人的坐标
                column--;
            }
        } else {
            // 左一位为GRASS或END，无需考虑左左一步，其他情况不用处理
            if (map[row][column - 1] == GRASS || map[row][column - 1] == END) {
                // 保留当前这一步的信息，以便回退左一步
                Map currMap = new Map(row, column, map);
                list.add(currMap);
                byte temp = (byte) (map[row][column - 1] == END ? MAN_LEFT_ON_END : MAN_LEFT);
                // 人变成temp，人往左走一步
                map[row][column - 1] = temp;
                // 人刚才站的地方变成GRASS或者END
                map[row][column] = grassOrEnd(map[row][column]);
                // 人离开后修改人的坐标
                column--;
            }
        }
    }

    /**
     * 人物向右移动
     */
    private void moveRight() {
        // 如果右一位是WALL，则不能移动
        if (map[row][column + 1] == WALL) {
            return;
        }
        // 如果右一位是BOX或BOX_ON_END，需要考虑右位的右一位是什么情况
        if (map[row][column + 1] == BOX || map[row][column + 1] == BOX_ON_END) {
            // 那么就需要考虑右一位的右一位情况，若右右一位是END或GRASS，则向右一步，其他情况不用处理
            if (map[row][column + 2] == END || map[row][column + 2] == GRASS) {
                // 要保留当前信息，以便回退右一步
                Map currMap = new Map(row, column, map);
                list.add(currMap);
                byte boxTemp = (byte) ((byte) map[row][column + 2] == END ? BOX_ON_END : BOX);
                byte manTemp = (byte) (map[row][column + 1] == BOX ? MAN_RIGHT : MAN_RIGHT_ON_END);
                // 箱子变成temp，箱子往右移动一步
                map[row][column + 2] = boxTemp;
                // 人变成MAN_UP，往右走一步
                map[row][column + 1] = manTemp;
                // 将人刚才站的地方变成GRASS或者END
                map[row][column] = grassOrEnd(map[row][column]);
                // 人离开后修改人的坐标
                column++;
            }
        } else {
            // 右一位为GRASS或END，无需考虑右右一步，其他情况不用处理
            if (map[row][column + 1] == GRASS || map[row][column + 1] == END) {
                // 保留当前这一步的信息，以便回退右一步
                Map currMap = new Map(row, column, map);
                list.add(currMap);
                byte temp = (byte) (map[row][column + 1] == END ? MAN_RIGHT_ON_END : MAN_RIGHT);
                // 人变成temp，人往右走一步
                map[row][column + 1] = temp;
                // 人刚才站的地方变成GRASS或者END
                map[row][column] = grassOrEnd(map[row][column]);
                // 人离开后修改人的坐标
                column++;
            }
        }
    }

    /**
     * 验证玩家是否过关，如果有目的地END值或人直接站在目的地则没有成功
     *
     * @return 如果已经通关则返回true，否则返回false
     */
    public boolean isFinished() {
        for (int i = 0; i < mapRow; i++) {
            for (int j = 0; j < mapColumn; j++) {
                if (map[i][j] == END || map[i][j] == MAN_DOWN_ON_END || map[i][j] == MAN_UP_ON_END || map[i][j] == MAN_LEFT_ON_END || map[i][j] == MAN_RIGHT_ON_END) {
                    return false;
                }
            }
        }
        return true;
    }

    // 使用双缓冲技术解决动画闪烁问题
    private Image iBuffer;
    private Graphics gBuffer;

    /**
     * 重写绘制整个游戏区域的图形
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if (iBuffer == null) {
            iBuffer = createImage(width, height);
            gBuffer = iBuffer.getGraphics();
        }
        // 清空屏幕原来的绘画
        gBuffer.setColor(getBackground());
        gBuffer.fillRect(0, 0, width, height);
        for (int i = 0; i < mapRow; i++) {
            for (int j = 0; j < mapColumn; j++) {
                // 画出地图,i表示行数，j表示列数
                if (map[i][j] != 0) {
                    // 这里要减1是因为图片的名称序号不对应，应该从0开始，但是从1开始的
                    gBuffer.drawImage(pics[map[i][j] - 1], leftX + j * MOVE_PIXEL, leftY + i * MOVE_PIXEL, 30, 30, this);
                }
            }
        }
        gBuffer.setColor(Color.RED);
        gBuffer.setFont(new Font("楷体_2312", Font.BOLD, 30));
        gBuffer.drawString("现在是第", 150, 140);
        gBuffer.drawString(String.valueOf(grade), 310, 140);
        gBuffer.drawString("关", 360, 140);
        g.drawImage(iBuffer, 0, 0, this);
        /* 下面的代码是未使用双缓冲技术会导致动画闪烁的代码 */
        /*g.clearRect(0, 0, width, height);
        for (int i = 0; i < mapRow; i++) {
            for (int j = 0; j < mapColumn; j++) {
                // 画出地图,i表示行数，j表示列数
                if (map[i][j] != 0) {
                    // 这里要减1是因为图片的名称序号不对应，应该从0开始，但是从1开始的
                    g.drawImage(pics[map[i][j] - 1], leftX + j * MOVE_PIXEL, leftY + i * MOVE_PIXEL, 30, 30, this);
                }
            }
        }
        g.setColor(Color.RED);
        g.setFont(new Font("楷体_2312", Font.BOLD, 30));
        g.drawString("现在是第", 150, 140);
        g.drawString(String.valueOf(grade), 310, 140);
        g.drawString("关", 360, 140);*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 当按键盘上的按键时触发的事件
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:// 上方向键
                moveUp();// 向上移动
                break;
            case KeyEvent.VK_DOWN:// 下方向键
                moveDown();// 向下移动
                break;
            case KeyEvent.VK_LEFT:// 左方向键
                moveLeft();// 向左移动
                break;
            case KeyEvent.VK_RIGHT:// 右方向键
                moveRight();// 向右移动
                break;
        }
        // 然后重新绘制界面
        repaint();
        // 在移动完成后可能已经通关，所以需要判断是否通关
        if (isFinished()) {
            // 禁用按键
            acceptKey = false;
            // 判断是否是最后一关，如果是则直接提示，如果不是则询问是否要进入下一关
            if (grade == MapFactory.getCount()) {
                JOptionPane.showMessageDialog(this, "恭喜通过最后一关！");
            } else {
                // 提示进入下一关
                String msg = "恭喜通过第" + grade + "关！！！\n是否要进入下一关？";
                int choice = JOptionPane.showConfirmDialog(null, msg, "过关", JOptionPane.YES_NO_OPTION);
                if (choice == 1) {
                    System.exit(0);
                } else if (choice == 0) {
                    // 进入下一关
                    acceptKey = true;
                    nextGrade();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // MouseEvent.BUTTON3表示鼠标右键
        if (e.getButton() == MouseEvent.BUTTON3) {
            undo();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * 返回当前人的位置用getManX()方法和getManY()方法
     *
     * @return
     */
    public int getManX() {
        return row;
    }

    public int getManY() {
        return column;
    }

    /**
     * 返回当前关卡数
     *
     * @return
     */
    public int getGrade() {
        return grade;
    }

    /**
     * 返回当前关卡的地图信息
     *
     * @return
     */
    public byte[][] getMap() {
        return MapFactory.getMap(grade);
    }

    /**
     * 显示提示信息对话框
     *
     * @param str
     */
    public void displayToast(String str) {
        JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 撤销移动操作
     */
    public void undo() {
        if (acceptKey) {
            if (list.size() > 0) {
                // 如果要撤销，必须要走过
                // 考虑用栈更合适
                Map priorMap = (Map) list.get(list.size() - 1);
                this.map = priorMap.getMap();
                this.row = priorMap.getManX();
                this.column = priorMap.getManY();
                repaint();// 重新画图
                list.remove(list.size() - 1);
            } else {
                displayToast("不能再撤销了！");
            }
        } else {
            displayToast("此关已完成，不能撤销！");
        }
    }

    /**
     * 实现下一关的初始化，并且调用repaint()方法显示游戏界面
     */
    public void nextGrade() {
        // 初始化下一关的数据
        if (grade >= MapFactory.getCount()) {
            displayToast("恭喜你完成所有关卡！");
            acceptKey = false;
        } else {
            // 关卡数加1
            grade++;
            // 初始化下一关的地图数据
            initMap();
            // 重新绘制画面
            repaint();
            acceptKey = true;
        }
    }

    /**
     * 实现上一关初始化并且调用repaint()发显示游戏界面
     */
    public void priorGrade() {
        grade--;
        acceptKey = true;
        if (grade < 0) {
            grade = 0;
        }
        initMap();
        repaint();
    }
}
