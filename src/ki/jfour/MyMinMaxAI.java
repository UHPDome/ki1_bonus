package ki.jfour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.lang.Integer.parseInt;

public class MyMinMaxAI extends AI{
    @Override
    public void start(Board b) {

        Player[][] current = b.getState();
        Player currentPlayer = b.getCurrentPlayer();
        int height = current.length;
        int width = current[0].length;

        boolean MinMax = Player(current, height, width);

        for (int i = 0; i < 100; i++) {
            Move best = new Move(minimax(b, i, -100000, 100000, MinMax, height, width, MinMax, currentPlayer, i));
            if(b.possibleMoves().contains(best)) {
                setBestMove(best);
            }else {
                setBestMove(b.possibleMoves().get(0));
            }
        }

    }
    @Override
    public String getDescription() {
        return "Dominik Petermann";
    }

    public int minimax(Board b, int depth, int alpha, int beta, boolean max, int height, int width, boolean playingAs, Player currentPLayer, int startDepth){
        Player[][] Board = b.getState();
        List<Move> possibleMoves = b.possibleMoves();
        int bestCol = 0;

        if(depth == 0){
            int heuristicEval = 0;
            if((playingAs && currentPLayer.equals(Player.RED)) || (!playingAs && currentPLayer.equals(Player.BLUE))) {
                heuristicEval = heuristicEval(Board, Player.RED, width, height, playingAs, currentPLayer) - heuristicEval(Board, Player.BLUE, width, height, playingAs, currentPLayer);
            }else{
                heuristicEval = heuristicEval(Board, Player.BLUE, width, height, playingAs, currentPLayer) - heuristicEval(Board, Player.RED, width, height, playingAs, currentPLayer);
            }
            return heuristicEval;
        }
        if (max){
            int maxEval = -1000000;
            for (Move i:possibleMoves) {
                Board boardIter = b;
                boardIter = boardIter.executeMove(i);
                Player Winner = boardIter.getWinner();
                int eval = 0;
                if(Winner.equals(Player.RED)){
                    eval = 100000;
                }else if(Winner.equals(Player.BLUE)){
                    eval = -100000;
                }
                else {
                    eval = minimax(boardIter, depth - 1, alpha, beta, false, height, width, playingAs, currentPLayer, startDepth);
                }
                if (maxEval < eval) {
                    maxEval = eval;
                    bestCol = i.column;
                }

                if (alpha < eval){
                    alpha = eval;
                }
                if(alpha >= beta){
                    break;
                }
            }
            if(depth == startDepth){
                return bestCol;
            }
            return maxEval;
        }
        else{
            int minEval = 1000000;
            for (Move i:possibleMoves) {
                Board boardIter = b;
                boardIter = boardIter.executeMove(i);

                Player Winner = boardIter.getWinner();
                int eval = 0;
                if(Winner.equals(Player.RED)){
                    eval = 100000;
                }else if(Winner.equals(Player.BLUE)){
                    eval = -100000;
                }
                else {
                    eval = minimax(boardIter, depth - 1, alpha, beta, true, height, width, playingAs, currentPLayer, startDepth);
                }
                if(minEval > eval) {
                    minEval = eval;
                    bestCol = i.column;
                }

                if (beta > eval){
                    beta = eval;
                }
                if(alpha >= beta){
                    break;
                }
            }
            if(depth == startDepth){
                return bestCol;
            }
            return minEval;
        }
    }
    
    public int heuristicEval(Player[][] Board, Player evalPlayer, int width, int height, boolean playingAs, Player currentPlayer){
        int heuristicEval = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int count_X = 0;
                //Vertikale
                while (j < width && Board[i][j].equals(evalPlayer)){
                    //Horizontal
                    int count_Y = 1;
                    while ((i + count_Y) < height && Board[i + count_Y][j].equals(evalPlayer)) {
                        count_Y++;
                    }
                    if (count_Y > 1) {
                        if(i > 0 && Board[i-1][j].equals(Player.NONE)){
                            heuristicEval++;
                            if (count_Y == 3) {
                                heuristicEval = heuristicEval + 9;
                            }
                        }
                    }
                    //Diagonale Unten
                    int count_Diag_Unten = 1;
                    while ((i + count_Diag_Unten) < height && (j + count_Diag_Unten) < width && Board[i + count_Diag_Unten][j + count_Diag_Unten].equals(evalPlayer)) {
                        count_Diag_Unten++;
                    }
                    if (count_Diag_Unten > 1) {
                        if((i > 0 && j > 0 && Board[i - 1][j - 1].equals(Player.NONE)) || (i + count_Diag_Unten < height && j + count_Diag_Unten < width && Board[i + count_Diag_Unten][j + count_Diag_Unten].equals(Player.NONE))) {
                            heuristicEval++;
                            if (count_Diag_Unten == 3) {
                                heuristicEval = heuristicEval + 9;
                            }
                        }
                    }
                        //}
                    //Diagonale Oben
                    int count_Diag_Oben = 1;
                    while ((i - count_Diag_Oben) > -1 && (j + count_Diag_Oben) < width && Board[i - count_Diag_Oben][j + count_Diag_Oben].equals(evalPlayer)) {
                        count_Diag_Oben++;
                    }
                    if (count_Diag_Oben > 1) {
                        if((i < height - 1 && j > 0 && Board[i + 1][j - 1].equals(Player.NONE)) || (i - count_Diag_Oben >= 0 && j + count_Diag_Oben < width && Board[i - count_Diag_Oben][j + count_Diag_Oben].equals(Player.NONE))) {
                            heuristicEval++;
                            if (count_Diag_Oben == 3) {
                                heuristicEval = heuristicEval + 9;
                            }
                        }
                    }
                    count_X++;
                    j++;
                }
                if(count_X > 1 ){
                    if((j - count_X > 0 && Board[i][j - count_X - 1].equals(Player.NONE)) || (j + 1 < width && Board[i][j + 1].equals(Player.NONE))) {
                        heuristicEval++;
                        if (count_X == 3) {
                            heuristicEval = heuristicEval + 9;
                        }
                    }
                }
            }
        }
        return heuristicEval;
    }

    public boolean[][] fillBool(int height, int width){
        boolean[][] fillBool = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                fillBool[i][j] = false;
            }
        }
        return fillBool;
    }

    public boolean Player(Player[][] current, int height, int width){
        int countRed = 0;
        int countBlue = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(current[i][j].equals(Player.RED)){
                    countRed++;
                } else if(current[i][j].equals(Player.BLUE)){
                    countBlue++;
                }
            }
        }
        return countRed == countBlue;
    }


    public static void main(String[] args) throws IOException {
        MyMinMaxAI ai = new MyMinMaxAI();
        Board b = new Board(10, 10);

        for (int i = 0; i < 100; i++) {


            // /*
            System.out.println(b.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter String");
            String exec = br.readLine();
            int execute = parseInt(exec);
            b = b.executeMove(new Move(execute - 1));
            // */ b = b.executeMove(new Move(0));

            ai.start(b);
            b = b.executeMove(ai.getBestMove().get());

        }
    }
}
//  if (count_Diag_Unten == 4 && !Board[i][j].equals(currentPlayer)) {
//    heuristicEval = heuristicEval + 1000;
//} else if (count_Diag_Unten == 4) {
//  heuristicEval = heuristicEval + 1000;
//}