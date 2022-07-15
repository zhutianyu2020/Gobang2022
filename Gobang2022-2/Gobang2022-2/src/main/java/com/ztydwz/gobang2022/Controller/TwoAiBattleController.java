package main.java.com.ztydwz.gobang2022.Controller;

import main.java.com.ztydwz.gobang2022.Model.Chess;
import main.java.com.ztydwz.gobang2022.Model.ChessType;
import main.java.com.ztydwz.gobang2022.Model.Pointer;
import main.java.com.ztydwz.gobang2022.Service.AI;
import main.java.com.ztydwz.gobang2022.Service.JudgeIfWin;
import main.java.com.ztydwz.gobang2022.View.Option;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static main.java.com.ztydwz.gobang2022.Model.Static.*;

public class TwoAiBattleController extends MouseAdapter {

    ChessController chessController = new ChessController();
    boolean ifFiveDa = false; //是否再进行五手N打
    boolean ifDRFiveDa = false;

    public TwoAiBattleController() {
        aiDesignatedStart();
        putChess = whoPutChess.aiPutChess;
        playerType = ChessType.BLACK;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameFlag) {
            return;
        }
        if (winFlag == -1)
            new JudgeIfWin();

        FiveDa();

        if (ifDRFiveDa) {
            chooseDRChess();
            ifDRFiveDa = false;
        }
        if (putChess == whoPutChess.aiPutChess) {
            whiteAiController();
            putChess = whoPutChess.playerPutChess;
        } else {
            blackAiController();
            putChess = whoPutChess.aiPutChess;
        }


    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameFlag) {
            return;
        }
        if (winFlag == -1)
            new JudgeIfWin();

    }


    public void FiveDa() {

        if (chessList.size() == 4 && !ifFiveDa) {
            new Option().createFiveDaNumber();
            AI ai = new AI();
            aiType = ChessType.BLACK;
            int[] arr = ai.fiveDa(Map, fiveDaNumber);
            for (int i = 0; i < arr.length; i += 2) {
                Pointer pointer = pointers[arr[i]][arr[i + 1]];
                dRFiveDaList.add(arr[i]);
                dRFiveDaList.add(arr[i + 1]);
                Chess chess = new Chess(pointer.getX(), pointer.getY(), aiType);
                sigleChessList.add(chess);
                putChess = whoPutChess.playerPutChess;
            }
            ifFiveDa = true;
            ifDRFiveDa = true;
            new Option().createFiveDaOption();
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

    public void blackAiController() {         //假定为玩家
        aiType = ChessType.BLACK;
        aiPutChess();
    }

    public void whiteAiController() {
        aiType = ChessType.White;
        aiPutChess();
    }

    public void chooseFiveDa() {
        sigleChessList = new ArrayList<>();
    }

    public void aiPutChess() {
        AI ai = new AI();
        if (aiType == ChessType.BLACK) {
            ai.setAiType(1);
        } else {
            ai.setAiType(2);
        }

        int[] arr = ai.getLocation();
        int i = arr[0];
        int j = arr[1];
        Pointer pointer = pointers[i][j];
        pointer.setHasChess(true);
        if (aiType == ChessType.BLACK) {
            Map[i][j] = 1;
        } else {
            Map[i][j] = 2;
        }
        Chess chess = new Chess(pointer.getX(), pointer.getY(), aiType);
        chessList.add(chess);
        if (aiType == ChessType.BLACK) {
            new AI().isForbiddenHand(i, j);
        }

    }


}
