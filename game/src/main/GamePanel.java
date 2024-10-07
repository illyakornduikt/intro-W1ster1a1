package main;

import javax.swing.JPanel;
import java.awt.*;
import entiti.Player;

public class GamePanel extends JPanel implements Runnable{
    final int originalTitleSize = 16;  // Размер одного тайла
    final int scale = 3;  // Масштаб
    public final int titleSize = originalTitleSize * scale;  // Итоговый размер тайла
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = titleSize * maxScreenCol;  // Ширина экрана
    final int screenHeight = titleSize * maxScreenRow; // Высота экрана

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);  // Создаем игрока

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);  // Для улучшения производительности рисования
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;  // Интервал между обновлениями
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null){
            update();  // Логика игры
            repaint();  // Перерисовка экрана

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;  // Переводим в миллисекунды

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();  // Обновление игрока
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        player.draw(g2);  // Отрисовка игрока
        g2.dispose();
    }
}
