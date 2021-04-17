package com.github.pangju666.utils.security;

import com.github.pangju666.utils.sys.RandomUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 验证码图像生成器
 *
 * @author 胖橘
 * @version 1.0 2020-3-18
 * @version 1.1 2020-3-31 增加验证图像尺寸参数选择
 * @since 1.0
 */
public final class VerificationImageUtils {
    public VerificationImageUtils() {}

    /**
     * 生成验证码图像
     *
     * @param code 验证码
     *
     * @since 1.0
     */
    public BufferedImage generate(final  String code) {
        return generate(code, 92, 25);
    }

    /**
     * 生成验证码图像
     *
     * @param code 验证码
     *
     * @since 1.0
     */
    public BufferedImage generate(final String code, final int width, final int height) {
        // 初始化缓存区图像为8位图像
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        // 获取图形上下文
        Graphics graphics = image.getGraphics();
        // 填充画布
        graphics.fillRect(0, 0, width, height);
        // 绘制干扰线
        drawLine(graphics, width, height);
        // 绘制文字
        drawText(code, graphics);
        // 关闭画布
        graphics.dispose();
        return image;
    }

    /**
     * 获取字体
     *
     * @since 1.0
     */
    private Font getFont() {
        // 字体属性为Tahoma字体, 样式为斜体, 字体尺寸为18
        return new Font("Tahoma", Font.ITALIC, 18);
    }

    /**
     * 获取颜色
     *
     * @since 1.0
     */
    private Color getColor() {
        // 随机生成RGB三色通道值
        int rColor = RandomUtils.nextInt(0, 255);
        int gColor = RandomUtils.nextInt(0, 255);
        int bColor = RandomUtils.nextInt(0, 255);
        return new Color(rColor, gColor, bColor);
    }

    /**
     * 绘制文字
     *
     * @param text 待绘制文字
     *
     * @since 1.0
     */
    private void drawText(final String text, final Graphics graphics) {
        // 获取字体
        graphics.setFont(getFont());
        // 逐个绘制文字
        for (int i = 0; i < text.length(); i++) {
            // 随机获取文字颜色
            graphics.setColor(getColor());
            // 随机生成X轴偏移量，取值范围0-9
            int translateX = RandomUtils.nextInt(10);
            // 随机生成Y轴偏移量，取值范围0-3
            int translateY = RandomUtils.nextInt(3);
            // 执行偏移操作
            graphics.translate(translateX, translateY);
            // 绘制文字，文字间距离相差15
            graphics.drawString(String.valueOf(text.charAt(i)), i * 15, 16);
        }
    }

    /**
     * 绘制干扰线
     *
     * @since 1.0
     */
    private void drawLine(final Graphics graphics, final int width, final int height) {
        // 随机获取干扰线数量，取值范围为20-40
         int lineSize = RandomUtils.nextInt(20, 40);
         // 绘制干扰线
         for (int i = 0; i <= lineSize; i++) {
             // 计算起始x轴坐标
             int x1 = RandomUtils.nextInt(width);
             // 计算起始y轴坐标
             int y1 = RandomUtils.nextInt(height);
             // 计算终止x轴坐标
             int x2 = x1 + RandomUtils.nextInt(13);
             // 计算终止y轴坐标
             int y2 = y1 + RandomUtils.nextInt(15);
             // 设置干扰线颜色
             graphics.setColor(getColor());
             // 绘制干扰线
             graphics.drawLine(x1, y1, x2, y2);
        }
    }
}