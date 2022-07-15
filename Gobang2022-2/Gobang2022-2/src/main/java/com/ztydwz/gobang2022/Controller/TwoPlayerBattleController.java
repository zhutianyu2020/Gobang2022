package main.java.com.ztydwz.gobang2022.Controller;

import main.java.com.ztydwz.gobang2022.Model.ChessType;
import main.java.com.ztydwz.gobang2022.Service.JudgeIfWin;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static main.java.com.ztydwz.gobang2022.Model.Static.*;

public class TwoPlayerBattleController extends MouseAdapter {
    PointerController pointerController = new PointerController();
    ChessController chessController = new ChessController();

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameFlag) {
            return;
        }
        chessController.PlayerPutChess(e.getX(), e.getY());
        if (playerType == ChessType.BLACK) {
            playerType = ChessType.White;
        } else {
            playerType = ChessType.BLACK;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameFlag) {
            return;
        }
        if (winFlag == -1) {
            new JudgeIfWin();
        }
        pointerController.changePointerShow(e.getX(), e.getY());
    }
}
