package main.java.com.ztydwz.gobang2022.Controller;

import main.java.com.ztydwz.gobang2022.Model.Chess;
import main.java.com.ztydwz.gobang2022.Model.ChessType;
import main.java.com.ztydwz.gobang2022.Model.Pointer;
import main.java.com.ztydwz.gobang2022.Model.Static;
import main.java.com.ztydwz.gobang2022.Service.AI;
import main.java.com.ztydwz.gobang2022.Service.JudgeIfWin;
import main.java.com.ztydwz.gobang2022.View.Option;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static main.java.com.ztydwz.gobang2022.Model.Static.*;

public class DesignatedStartController extends MouseAdapter {
    boolean ifPlayerDesignatedStart = false;
    boolean ifFiveDa = false; //是否再进行五手N打
    boolean ifDRFiveDa = false;
    int num = fiveDaNumber;
    PointerController pointerController = new PointerController();
    ChessController chessController = new ChessController();

    public DesignatedStartController() {
        if (putChess == whoPutChess.aiPutChess) {
            aiDesignatedStart();
        } else {
            ifPlayerDesignatedStart = true;
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameFlag) {
            return;
        }

        if (ifPlayerDesignatedStart) {
            PlayerDesignatedStart(e);
        }


        if (ifFiveDa) {                              //选择要保留的棋子
            playerType = ChessType.BLACK;
            if (chessController.PlayerPutChess(e.getX(), e.getY())) {   //确保玩家棋子已经下了
                chooseFiveDa();
                putChess = whoPutChess.playerPutChess;
                playerType = ChessType.White;
                ifFiveDa = false;
            }
        }

        if (ifDRFiveDa) {                         //进行打点
            putChess = whoPutChess.playerPutChess;
            if (num > 0) {
                new Option().createOption("请再打" + (num - 1) + "个点");
                if (chessController.putFiveDaChess(e.getX(), e.getY())) {
                    num--;
                }
            }
        }

        if (putChess == whoPutChess.playerPutChess && !ifFiveDa && !ifDRFiveDa) {               //玩家回合
            if (chessController.PlayerPutChess(e.getX(), e.getY())) {   //确保玩家棋子已经下了
                putChess = whoPutChess.aiPutChess;
            }
        }


    }

    private void PlayerDesignatedStart(MouseEvent e) {


        if (chessList.size() < 3) {              //玩家先下三个棋子
            if (chessController.PlayerPutChess(e.getX(), e.getY())) {   //确保玩家棋子已经下了
                putChess = whoPutChess.playerPutChess;
                if (playerType == ChessType.BLACK) {
                    playerType = ChessType.White;
                } else {
                    playerType = ChessType.BLACK;
                }
            }
            if (chessList.size() == 3) {
                putChess = whoPutChess.aiPutChess;
                playerType = ChessType.BLACK;
                ifPlayerDesignatedStart = false;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameFlag) {
            return;
        }
        if (winFlag == -1)
            new JudgeIfWin();
        FiveDa();
        diRenFiveDa();
        pointerController.changePointerShow(e.getX(), e.getY());
        if (putChess == Static.whoPutChess.aiPutChess) {//ai回合
            chessController.AiPutChess();
            putChess = Static.whoPutChess.playerPutChess;
        }
        if (ifDRFiveDa && num == 0) {
            chooseDRChess();
        }
    }

    public void aiDesignatedStart() {
        gameFlag = true;
        Map[7][7] = 1;
        Map[6][7] = 2;
        Map[6][8] = 1;
        Pointer pointer1 = pointers[7][7];
        Pointer pointer2 = pointers[6][7];
        Pointer pointer3 = pointers[6][8];
        pointer1.setHasChess(true);
        pointer2.setHasChess(true);
        pointer3.setHasChess(true);

        Chess chess1 = new Chess(pointer1.getX(), pointer1.getY(), ChessType.BLACK);
        Chess chess2 = new Chess(pointer2.getX(), pointer2.getY(), ChessType.White);
        Chess chess3 = new Chess(pointer3.getX(), pointer3.getY(), ChessType.BLACK);
        chessList.add(chess1);
        chessList.add(chess2);
        chessList.add(chess3);
        putChess = whoPutChess.playerPutChess;
        exchange();
    }

    public void FiveDa() {
        if (chessList.size() == 4 && aiType == ChessType.BLACK && !ifFiveDa) {
            new Option().createFiveDaNumber();
            AI ai = new AI();
            int[] arr = ai.fiveDa(Map, fiveDaNumber);
            for (int i = 0; i < arr.length; i += 2) {
                Pointer pointer = pointers[arr[i]][arr[i + 1]];
                Chess chess = new Chess(pointer.getX(), pointer.getY(), aiType);
                sigleChessList.add(chess);
                putChess = whoPutChess.playerPutChess;

            }
            ifFiveDa = true;
            new Option().createFiveDaOption();
        }
    }

    public void diRenFiveDa() {
        if (chessList.size() == 4 && aiType == ChessType.White && !ifDRFiveDa) {
            new Option().createInputFiveDaNumber();
            num = fiveDaNumber;
            ifDRFiveDa = true;
            putChess = whoPutChess.playerPutChess;
        }

    }

    public void chooseDRChess() {
        int[] dRFiveDaChess = new int[fiveDaNumber * 2];
        for (int i = 0; i < dRFiveDaList.size(); i++) {
            dRFiveDaChess[i] = dRFiveDaList.get(i);
        }
        AI ai = new AI();
        int[] rtu = ai.drFiveDa(dRFiveDaChess);
        new Option().createOption("电脑选择留下坐标为" + (char) (rtu[1] + 1 + 64) + "," + (15 - rtu[0] + "的点"));
        chooseFiveDa();
        Pointer pointer = pointers[rtu[0]][rtu[1]];
        pointer.setHasChess(true);
        Map[rtu[0]][rtu[1]] = 1;
        Chess chess = new Chess(pointer.getX(), pointer.getY(), ChessType.BLACK);
        chessList.add(chess);
        ifDRFiveDa = false;
        putChess = whoPutChess.aiPutChess;
    }

    public void chooseFiveDa() {
        sigleChessList = new ArrayList<>();
    }

    public void exchange() {
        int option = new Option().createExchange();
        if (option == JOptionPane.YES_OPTION) {
            playerType = ChessType.BLACK;
            aiType = ChessType.White;
            putChess = whoPutChess.aiPutChess;
        }
    }

}
