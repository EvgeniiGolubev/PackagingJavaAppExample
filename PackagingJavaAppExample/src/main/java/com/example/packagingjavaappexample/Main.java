package com.example.packagingjavaappexample;

import com.example.packagingjavaappexample.controller.Controller;
import com.example.packagingjavaappexample.model.Model;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame game = new JFrame();
        Model model = new Model();
        Controller controller = new Controller(model);

        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(460, 510);
        game.setResizable(false);
        game.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("icon.png")));
        game.add(controller.getView());
        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }
}
